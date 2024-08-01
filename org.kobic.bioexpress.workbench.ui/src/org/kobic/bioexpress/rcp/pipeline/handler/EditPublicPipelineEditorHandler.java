package org.kobic.bioexpress.rcp.pipeline.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.CanvasUtil;
import org.kobic.bioexpress.rcp.utils.InfoUtils;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;

public class EditPublicPipelineEditorHandler {

	final static Logger logger = Logger.getLogger(EditPublicPipelineEditorHandler.class);

	@Inject
	@Named(IServiceConstants.ACTIVE_SELECTION)
	private Object object;

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	@Optional
	private MPartStack mPartStack;

	@Inject
	@Optional
	private IWorkbench iworkbench;

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (activePart.getElementId().equals(Constants.PIPELINE_VIEW_ID) && object instanceof PipelineDataModel) {

			PipelineDataModel pipelineData = (PipelineDataModel) object;

			MPart mPart1 = ePartService.findPart(Constants.DETAIL_DATA_VIEW_ID);

			ePartService.showPart(mPart1, PartState.ACTIVATE);

			Map<String, Object> map = InfoUtils.getInstance().converDataToMap(pipelineData);

			iEventBroker.send("_TEST_011", map);

			MPart mPart2 = ePartService.findPart(Constants.PIPELINE_DETAIL_VIEW_ID);

			ePartService.showPart(mPart2, PartState.ACTIVATE);

			iEventBroker.send("_TEST_012", pipelineData);

			PipelineModel pipeline = null;

			PipelineClient pipelineClient = new PipelineClientImpl();
			try {
				pipeline = pipelineClient.getPipeline(pipelineData.getRawID());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				String emfData = pipeline.getPipelineData().getPipelineTemplate();
				pipelineData = pipeline.getPipelineData();
				String fname = pipelineData.getPipelineName();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = root.getProject("temp");

				String path = pipeline.getPipelineData().getWorkspaceName();
				IFolder parentF = CanvasUtil.createFolders(path, project);
				IFile file = parentF.getFile(fname + ".iwf");

				String status = pipelineData.getStatus();
				String pattern = "pstatus=\"(?:.*?)\"";
				Pattern pat = Pattern.compile(pattern);
				Matcher m = pat.matcher(emfData);
				emfData = m.replaceFirst("pstatus=\"" + status + "\"");

				if (file.exists()) {
					try {
						ByteArrayInputStream inputStream = new ByteArrayInputStream(emfData.getBytes());
						file.setContents(inputStream, true, true, null);
						inputStream.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IEditorPart part = IDE.openEditor(page, file, false);
					WorkflowDiagramEditor editor = (WorkflowDiagramEditor) part;
					editor.setPipeline(pipeline);
				} else {
					try {
						ByteArrayInputStream inputStream = new ByteArrayInputStream(emfData.getBytes());
						file.create(inputStream, true, null);
						inputStream.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IEditorPart part = IDE.openEditor(page, file, false);
					WorkflowDiagramEditor editor = (WorkflowDiagramEditor) part;
					editor.setPipeline(pipeline);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(shell, "Creating Workflow Error", "Creating workflow error message: " + e.getMessage());
			}

		} else {
			MessageDialog.openError(shell, "Creating Workflow Error", "Error creating workflow: [");
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
