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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.common.dialog.CommonInputDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

public class ScriptNewFolderHandler {

	final static Logger logger = Logger.getLogger(ScriptNewFolderHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		String path = null;

		if (object instanceof FileModel) {

			FileModel file = (FileModel) object;
			path = file.getPath();

		} else {

			String memberID = Activator.getMember().getMemberId();
			path = String.format(Constants.SCRIPT_ROOT_PATH, memberID);
		}

		FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
		List<FileModel> sub = fileUtilsClient.getFiles(path);

		List<FileModel> subDir = new ArrayList<FileModel>();

		for (FileModel f : sub) {
			if (f.isIsDir())
				subDir.add(f);
		}

		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {

				if (newText.isEmpty()) {
					return "please enter new folder name.";
				} else {
					for (FileModel file : subDir) {
						if (newText.equals(file.getName())) {
							return "The folder name already exists.";
						}else if(!ValidationUtils.getInstance().isFileNameValidation(newText)){
							return Constants.NEW_NAMING_RULE;
						}
					}
				}
				return null;
			}
		};

		CommonInputDialog dialog = new CommonInputDialog(Display.getCurrent().getActiveShell(), "New Folder",
				"New folder name:", Constants.DEFAULT_NULL_VALUE, validator);

		if (dialog.open() == Window.OK) {

			System.out.println(path);
			System.out.println(dialog.getName());
			fileUtilsClient.makeDir(path, dialog.getName());
		}

		iEventBroker.send(Constants.SCRIPT_REFRESH_EVENT_BUS_NAME, dialog.getName());
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		return true;
	}
}
