package org.kobic.bioexpress.rcp.script.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.common.dialog.CommonInputDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.swt.component.CreateImageDescriptor;

public class ScriptNewFolderAction extends Action {

	@SuppressWarnings("unused")
	private IEventBroker iEventBroker;
	private Composite parent;
	private TreeViewer viewer;

	public ScriptNewFolderAction(IEventBroker iEventBroker, Composite parent, TreeViewer viewer) {

		this.iEventBroker = iEventBroker;
		this.parent = parent;
		this.viewer = viewer;

		setText("New Folder");
		setToolTipText("Create New Folder");
		setImageDescriptor(CreateImageDescriptor.getInstance().getImageDescriptor(Constants.PARAMETER_DIALOG_ADD_ICON));
	}

	private String path = null;

	@Override
	public void run() {

		if (viewer.getStructuredSelection().isEmpty()) {
			MessageDialog.openWarning(parent.getShell(), "Select Warning", "Select a folder first and create it.");
		} else {

			FileModel fileModel = (FileModel) viewer.getStructuredSelection().getFirstElement();

			if (fileModel.isIsFile()) {
				MessageDialog.openWarning(parent.getShell(), "Select Warning", "No file. Please select a folder.");
			} else {

				path = fileModel.getPath();

				FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
				List<FileModel> sub = fileUtilsClient.getFiles(path);

				List<FileModel> subDir = new ArrayList<FileModel>();

				for (FileModel file : sub) {
					if (file.isIsDir())
						subDir.add(file);
				}

				IInputValidator validator = new IInputValidator() {

					@Override
					public String isValid(String newText) {

						if (newText.isEmpty()) {
							return "'Please enter new folder name.";
						} else {
							for (FileModel file : subDir) {

								if (newText.equals(file.getName())) {
									return "Folder name already exists.";
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

//					ChannelClient channelClient = new ChannelClientImpl();
//					channelClient.makeDir(path, name);
				}

			}
		}

	}
}