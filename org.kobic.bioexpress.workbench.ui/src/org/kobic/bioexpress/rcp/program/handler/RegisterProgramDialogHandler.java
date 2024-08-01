package org.kobic.bioexpress.rcp.program.handler;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.dialog.RegisterProgramDialog;
import org.kobic.bioexpress.rcp.program.wizard.NewProgramWizard;

@SuppressWarnings("unused")
public class RegisterProgramDialogHandler {

	final static Logger logger = Logger.getLogger(RegisterProgramDialogHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof ProgramDataModel) {

			ProgramDataModel programData = (ProgramDataModel) object;

			RegisterProgramDialog dialog = new RegisterProgramDialog(shell, programData);
			if (dialog.open() == Window.OK) {

//				iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());

				if (dialog.isRegistComplete()) {
					MessageDialog.openInformation(shell, "Regist Program Information",
							"The program registration request has been processed normally, and it can be used after administrator approval.");
				} else {
					MessageDialog.openError(shell, "Regist Program error",
							"An error occurred while requesting to register the program. Please try again in a few minutes.");
				}
			}
		}

	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof ProgramDataModel) {

			ProgramDataModel programData = (ProgramDataModel) object;

			if (programData.isIsPublic()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

}
