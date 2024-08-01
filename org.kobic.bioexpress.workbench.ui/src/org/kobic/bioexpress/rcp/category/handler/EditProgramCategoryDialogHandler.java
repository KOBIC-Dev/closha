package org.kobic.bioexpress.rcp.category.handler;

import java.rmi.server.UID;

import javax.inject.Inject;
import javax.inject.Named;

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
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.category.dialog.EditCategoryDialog;
import org.kobic.bioexpress.rcp.constant.Constants;

public class EditProgramCategoryDialogHandler {

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) CategoryModel category,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		String categoryType = null;
		String parentID = null;

		if (activePart.getLabel().toLowerCase().equals(Constants.PROGRAM_LABEL)) {
			categoryType = Constants.PROGRAM_LABEL;
		}

		if (category != null) {
			parentID = category.getCategoryID();
		} else {
			parentID = Constant.ROOT_CATEGORY_DEFAULT_ID;
		}

		if (category.getObjectCount() != 0) {

			MessageDialog.openWarning(shell, "Category Warning","'"+
					category.getCategoryName()+"' " + "\n\nIt cannot be edit because there is an item in the category.");

		} else {

			EditCategoryDialog dialog = new EditCategoryDialog(shell, categoryType, parentID, category);

			if (dialog.open() == Window.OK) {

				if (dialog.getResult() == 1) {
					if (categoryType.equals(Constants.PROGRAM_LABEL)) {
						iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
					} else {
						MessageDialog.openError(shell.getShell(), "Category Error",
								"An error occurred while editing the category.");
					}
				} else {
					MessageDialog.openError(shell.getShell(), "Category Error",
							"An error occurred while editing the category.");
				}
			}

		}

	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof CategoryModel) {

			CategoryModel category = (CategoryModel) object;

			if (category.isIsProgram() && activePart.getLabel().toLowerCase().equals(Constants.PROGRAM_LABEL)) {
				return true;
			} else {
				return false;
			}
		}

		return false;

	}
}
