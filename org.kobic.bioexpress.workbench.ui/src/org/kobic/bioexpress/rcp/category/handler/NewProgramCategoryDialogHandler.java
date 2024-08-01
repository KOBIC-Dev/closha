package org.kobic.bioexpress.rcp.category.handler;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

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
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.dialog.NewCategoryDialog;
import org.kobic.bioexpress.rcp.constant.Constants;

public class NewProgramCategoryDialogHandler {

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) CategoryModel categoryModel,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart, @Named(IServiceConstants.ACTIVE_SHELL) Shell s) {

		String categoryType = null;
		String parentID = null;

		String memberID = Activator.getMember().getMemberId();

		List<CategoryModel> categorys = new ArrayList<CategoryModel>();
		List<String> categoryName = new ArrayList<String>();

		CategoryClient categoryClient = new CategoryClientImpl();

		if (activePart.getLabel().toLowerCase().equals(Constants.PROGRAM_LABEL)) {

			categoryType = Constants.PROGRAM_LABEL;

			if (categoryModel == null) {
				parentID = Constant.ROOT_CATEGORY_DEFAULT_ID;
			} else {
				if (categoryModel.isIsProgram()) {
					parentID = categoryModel.getCategoryID();
				} else {
					parentID = Constant.ROOT_CATEGORY_DEFAULT_ID;
				}
			}

			if (parentID.equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
				categorys = categoryClient.getProgramRootCategory(memberID);
				categorys.forEach(category -> categoryName.add(category.getCategoryName()));
			} else {
				categorys = categoryClient.getProgramSubCategory(categoryModel.getCategoryID(), memberID);
				categorys.forEach(category -> categoryName.add(category.getCategoryName()));
			}

		}

		System.out.println("-->" + categoryName);

		System.out.println(activePart.getElementId() + "\t" + activePart.getLabel() + "\t" + parentID);

		NewCategoryDialog dialog = new NewCategoryDialog(shell, categoryType, parentID, categoryName);

		if (dialog.open() == Window.OK) {

			if (dialog.getResult() == 1) {
				if (categoryType.equals(Constants.PROGRAM_LABEL)) {
					iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
				} else {
					MessageDialog.openError(shell.getShell(), "Category Error",
							"An error occurred while creating the category.");
				}
			} else {
				MessageDialog.openError(shell.getShell(), "Category Error", "An error occurred while creating the category.");
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

					return true;

				} else {

					return false;
				}
			}

			return true;

		} else {

			return false;
		}
	}
}
