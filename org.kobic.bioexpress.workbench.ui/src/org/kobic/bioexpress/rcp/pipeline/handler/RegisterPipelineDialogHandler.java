package org.kobic.bioexpress.rcp.pipeline.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.rcp.pipeline.dialog.RegisterPipelineDialog;

public class RegisterPipelineDialogHandler {

	final static Logger logger = Logger.getLogger(RegisterPipelineDialogHandler.class);

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof PipelineDataModel) {

			PipelineDataModel pipelineData = (PipelineDataModel) object;

			PipelineClient pipelineClient = new PipelineClientImpl();
			PipelineModel pipelineModel = null;

			try {
				pipelineModel = pipelineClient.getPipeline(pipelineData.getRawID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (pipelineModel == null) {
				MessageDialog.openError(shell, "Regist Pipeline Error",
						"An error occurred while requesting to register the pipeline. Check the pipeline again.");
				return;
			}

			List<String> nodeName = new ArrayList<String>();

			for (NodeModel node : pipelineModel.getNode()) {
				if (!node.getNodeData().isPublic()) {
					nodeName.add(node.getNodeData().getNodeName());
				}
			}

			if (nodeName.size() > 0) {
				MessageDialog.openError(shell, "Regist Pipeline Error",
						"The program has not been registered. [" + String.join(",", nodeName) + "]");
				return;
			}

			RegisterPipelineDialog dialog = new RegisterPipelineDialog(shell, pipelineModel);

			if (dialog.open() == Window.OK) {

				if (dialog.isRegistComplete()) {
					MessageDialog.openInformation(shell, "Request Regist pipeline",
							"The pipeline registration request has been processed normally, and it can be used after administrator approval.");
				} else {
					MessageDialog.openError(shell, "Regist Pipeline Error",
							"An error occurred while requesting to register the pipeline. Please try again in a few minutes.");
				}

			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof PipelineDataModel) {

			PipelineDataModel pipelineData = (PipelineDataModel) object;

			if (pipelineData.isIsPublic()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
