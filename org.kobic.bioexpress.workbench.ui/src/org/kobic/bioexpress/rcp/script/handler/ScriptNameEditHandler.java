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
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.client.script.ScriptClient;
import org.kobic.bioexpress.channel.client.script.ScriptClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.script.ScriptModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.common.dialog.CommonInputDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

public class ScriptNameEditHandler {

	final static Logger logger = Logger.getLogger(ScriptNameEditHandler.class);

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

			String path = file.getPath();
			String name = file.getName();
			String parent = file.getParentPath() + "/";
			boolean isDir = file.isIsDir();

			String memberID = Activator.getMember().getMemberId();

			FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
			List<FileModel> sub = fileUtilsClient.getFiles(path);

			List<FileModel> subDir = new ArrayList<FileModel>();

			if (isDir) {
				for (FileModel f : sub) {
					if (f.isIsDir())
						subDir.add(f);
				}
			} else {
				for (FileModel f : sub) {
					if (f.isIsFile())
						subDir.add(f);
				}
			}

			IInputValidator validator = new IInputValidator() {

				@Override
				public String isValid(String newText) {

					if (newText.isEmpty()) {
						return "Please enter new edit name.";
					} else {
						if (newText.equals(file.getName())) {

							return "The script name already exists.";

						} else if (!ValidationUtils.getInstance().isFileNameValidation(newText)) {
							return Constants.NEW_NAMING_RULE;
						}

					}
					return null;
				}
			};

			CommonInputDialog dialog = new CommonInputDialog(Display.getCurrent().getActiveShell(), "Rename",
					"Edit name:", name, validator);

			if (dialog.open() == Window.OK) {

				System.out.println(path);
				System.out.println(dialog.getName());

				boolean isEidt = false;

				if (isDir) {
					isEidt = fileUtilsClient.renameDir(path, parent + dialog.getName());
				} else {

					/**
					 * 스크립트 파일 데이터베이스 정보 업데이트
					 */
					ScriptClient scriptClient = new ScriptClientImpl();

					ScriptModel scriptModel = scriptClient.getSelectScript(memberID, path, name);
					scriptModel.setSavePath(parent + dialog.getName());
					scriptModel.setName(dialog.getName());

					String rawID = scriptModel.getRawID();

					int res = scriptClient.updateScript(rawID, scriptModel);

					if (res == 1) {
						isEidt = fileUtilsClient.renameFile(path, parent + dialog.getName());
					} else {
						MessageDialog.openError(shell, "Script Error",
								"Error while editing script file. Please try again.");
					}
				}

				if (isEidt) {
					iEventBroker.send(Constants.SCRIPT_REFRESH_EVENT_BUS_NAME, dialog.getName());
				}
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof FileModel) {
			return true;
		}

		return false;
	}

}
