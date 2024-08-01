package org.kobic.bioexpress.rcp.script.handler;

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
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClient;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClientImpl;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.client.script.ScriptClient;
import org.kobic.bioexpress.channel.client.script.ScriptClientImpl;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.script.ScriptModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.script.dialog.NewScriptDialog;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

public class ScriptNewFileHandler {

	final static Logger logger = Logger.getLogger(ScriptNewFolderHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	private String ext = null;

	@Execute
	public void execute(MMenuItem menuItem, IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof FileModel) {

			String language = menuItem.getLabel().toLowerCase();

			switch (language) {
			case Constants.PYTHON:
				ext = Constants.PYTHON_EXT;
				break;
			case Constants.BASH:
				ext = Constants.BASH_EXT;
				break;
			case Constants.R:
				ext = Constants.R_EXT;
				break;
			default:
				break;
			}

			FileModel file = (FileModel) object;

			String path = file.getPath();

			FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
			List<FileModel> sub = fileUtilsClient.getFiles(path);

			List<FileModel> subDir = new ArrayList<FileModel>();

			for (FileModel f : sub) {
				if (f.isIsFile())
					subDir.add(f);
			}

			IInputValidator validator = new IInputValidator() {

				@Override
				public String isValid(String newText) {

					if (newText.isEmpty()) {
						return "Please enter new script file name.";
					} else {

						if (!newText.endsWith(ext)) {
							return "Please check the file extension.";

						} else if (!ValidationUtils.getInstance().isFileNameValidation(newText)) {
							return Constants.NEW_NAMING_RULE;
						}

						for (FileModel file : subDir) {
							if (newText.toLowerCase().equals(file.getName().toLowerCase())) {
								return "The script name already exists.";
							}
						}

					}

					return null;
				}
			};

//			NewScriptDialog dialog = new NewScriptDialog(Display.getCurrent().getActiveShell(), "New Script",
//					"New script name:", ext, validator);

			NewScriptDialog dialog = new NewScriptDialog(Display.getCurrent().getActiveShell(), "New Script",
					"New script name:", Constants.DEFAULT_NULL_VALUE, validator);

			if (dialog.open() == Window.OK) {

				System.out.println(path + "::" + dialog.getName().trim());

				Utils utils = Utils.getInstance();

				String memberID = Activator.getMember().getMemberId();
				String name = dialog.getName().trim();
				String sPath = String.join("/", path, name);

				String podmanRawID = dialog.getPodmanRawID();

				PodmanAPIClient podmanAPI = new PodmanAPIClientImpl();
				PodmanModel podmanModel = podmanAPI.getSelectPodman(podmanRawID);

				if (podmanModel != null) {

					ScriptModel model = new ScriptModel();
					model.setRawID(utils.getNewRawID());
					model.setScriptID(utils.getNewRawID());
					model.setName(name);
					model.setScriptType(language.toUpperCase());
					model.setSavePath(sPath);
					model.setParentPath(path);
					model.setCreateDate(utils.getDate());
					model.setModifyDate(utils.getDate());
					model.setMemberID(memberID);
					model.setPodmanID(podmanModel.getRawID());
					model.setPodmanImgID(podmanModel.getImageID());
					model.setPodmanName(podmanModel.getName());
					model.setPodmanRepo(podmanModel.getRepo());
					model.setPodmanTag(podmanModel.getTag());
					model.setTimeStamp(utils.getCurruntTime());
					model.setIsDelete(false);

					ScriptClient client = new ScriptClientImpl();
					client.insertScript(model);

					fileUtilsClient.createFile(String.join("/", path, dialog.getName()));

					iEventBroker.send(Constants.SCRIPT_REFRESH_EVENT_BUS_NAME, dialog.getName());
				} else {
					MessageDialog.openError(shell, "Script Error",
							"Error while creating script file. Please try again.");
				}
			}

		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof FileModel) {

			FileModel file = (FileModel) object;

			if (file.isIsDir()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}
