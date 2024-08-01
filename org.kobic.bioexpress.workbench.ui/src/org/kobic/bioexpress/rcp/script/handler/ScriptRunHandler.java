package org.kobic.bioexpress.rcp.script.handler;

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
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.kobic.bioexpress.channel.client.script.ScriptClient;
import org.kobic.bioexpress.channel.client.script.ScriptClientImpl;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.script.ScriptModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ScriptRunHandler {

	final static Logger logger = Logger.getLogger(ScriptRunHandler.class);

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof FileModel) {

			FileModel file = (FileModel) object;

			String userID = Activator.getMember().getMemberId();
			String script = file.getPath();

			String repo = null;
			String tag = null;
			String env = null;

			ScriptClient scriptClient = new ScriptClientImpl();

			boolean isExist = scriptClient.isExistScript(userID, script, file.getName());
			
			System.out.println(String.join("\t", String.valueOf(isExist), "====>", userID, script, file.getName()));

			if (isExist) {

				ScriptModel scriptModel = scriptClient.getSelectScript(userID, script, file.getName());

				System.out.println(scriptModel.toString());

				repo = scriptModel.getPodmanRepo();
				tag = scriptModel.getPodmanTag();
				env = scriptModel.getPodmanName();
			} else {
				repo = Constants.DEFAULT_VALUE;
				tag = Constants.DEFAULT_VALUE;
				env = Constants.PODMAN_BASE_IMAGE_NAME;
			}
			//check script file save status..
			boolean cc = false;
			IWorkbenchPage[] pages =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages();
			for(IWorkbenchPage page:pages) {
				IEditorReference[] refs = page.getEditorReferences();
				for(IEditorReference ref:refs) {
					if(ref.isDirty()) {
						//ref.getEditor(true).doSave(null);
						cc = true;
						break;
					}
				}
			}
			if(cc) {
				boolean res = MessageDialog.openConfirm(shell, "Save",
						"An unsaved script file exists. Do you want to save it?");
				if(res) {
					for(IWorkbenchPage page:pages) {
						IEditorReference[] refs = page.getEditorReferences();
						for(IEditorReference ref:refs) {
							if(ref.isDirty()) {
								ref.getEditor(true).doSave(null);
							}
						}
					}
				}else {
					return;
				}
			}
			//
			boolean res = MessageDialog.openConfirm(shell, "Execute",
					"Do you want to run a " + file.getName() + "[" + env + "] script file?");

			if (res) {

				System.out.println(String.join("\t", userID, script, file.getName()));

				TaskClient taskClient = new TaskClientImpl();
				taskClient.executeScriptTask(userID, script, repo, tag, env);

				MPart mPart = ePartService.findPart(Constants.SCRIPT_TASK_HISTORY_VIEW_ID);
				ePartService.showPart(mPart, PartState.ACTIVATE);
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof FileModel) {
			
			FileModel file = (FileModel) object;

			if (file.isIsFile()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}
