package org.kobic.bioexpress.rcp.program.handler;

import java.util.Properties;

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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.wizard.NewProgramWizard;

public class NewProgramWizardHandler {

	final static Logger logger = Logger.getLogger(NewProgramWizardHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@SuppressWarnings("unused")
	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_MESSAGE_PROPERTIES);

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof CategoryModel) {

			CategoryModel category = (CategoryModel) object;

			if (!category.getParentID().equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {

				WizardDialog registerProgramDialog = new WizardDialog(shell,
						new NewProgramWizard(iEventBroker, category));
				WizardDialog.setDefaultImage(
						ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
				registerProgramDialog.setPageSize(600, 500);
				registerProgramDialog.open();

			} else {
				MessageDialog.openWarning(shell.getShell(), "Category Warning", "Please select a sub-category.");
			}
		}

	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Optional @Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (activePart != null && activePart.getLabel().toLowerCase().equals(Constants.PROGRAM_LABEL)) {

			if (object instanceof CategoryModel) {

				CategoryModel category = (CategoryModel) object;

				if (category.getParentID().equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
					return false;
				} else {
					return true;
				}

			} else {
				return false;
			}
			
		} else {

			return false;
		}
	}
}
