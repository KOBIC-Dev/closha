package org.kobic.bioexpress.rcp.pipeline.handler;

import java.io.IOException;
import java.rmi.server.UID;

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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.dialog.EditPublicPipelineDialog;

public class EditPublicPipelineDialogHandler {

	final static Logger logger = Logger.getLogger(EditPublicPipelineDialogHandler.class);

	@Inject
	@Named(IServiceConstants.ACTIVE_SELECTION)

	private Object object;

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof PipelineDataModel) {

			PipelineDataModel pipelineData = (PipelineDataModel) object;

			try {
				PipelineClient pipelineClient = new PipelineClientImpl();
				PipelineModel pipeline = pipelineClient.getPipeline(pipelineData.getRawID());

				EditPublicPipelineDialog dialog = new EditPublicPipelineDialog(shell, pipeline.getPipelineData());

				if (dialog.open() == Window.OK) {
					iEventBroker.send(Constants.PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof PipelineDataModel
				&& activePart.getLabel().toLowerCase().equals(Constants.PIPELINE_LABEL)) {

			PipelineDataModel pipelineData = (PipelineDataModel) object;

			if (Activator.isAdmin() && pipelineData.isIsPublic()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
