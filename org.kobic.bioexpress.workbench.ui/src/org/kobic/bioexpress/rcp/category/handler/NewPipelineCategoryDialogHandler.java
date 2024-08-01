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

public class NewPipelineCategoryDialogHandler {

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

		if (activePart.getLabel().toLowerCase().equals(Constants.PIPELINE_LABEL)) {

			categoryType = Constants.PIPELINE_LABEL;

			if (categoryModel == null) {
				parentID = Constant.ROOT_CATEGORY_DEFAULT_ID;
			} else {
				if (categoryModel.isIsPipeline()) {
					parentID = categoryModel.getCategoryID();
				} else {
					parentID = Constant.ROOT_CATEGORY_DEFAULT_ID;
				}
			}

			if (parentID.equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
				categorys = categoryClient.getPipelineRootCategory(memberID);
				categorys.forEach(category -> categoryName.add(category.getCategoryName()));
			} else {
				categorys = categoryClient.getPipelineSubCategory(categoryModel.getCategoryID(), memberID);
				categorys.forEach(category -> categoryName.add(category.getCategoryName()));
			}

		}

		System.out.println("-->" + categoryName);

		System.out.println(activePart.getElementId() + "\t" + activePart.getLabel() + "\t" + parentID);

		NewCategoryDialog dialog = new NewCategoryDialog(shell, categoryType, parentID, categoryName);

		if (dialog.open() == Window.OK) {

			if (dialog.getResult() == 1) {
				if (categoryType.equals(Constants.PIPELINE_LABEL)) {
					iEventBroker.send(Constants.PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
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

		//파이프라인 뷰 선택 여부, 관리자 여부
		if (activePart != null && activePart.getLabel().toLowerCase().equals(Constants.PIPELINE_LABEL)
				&& Activator.isAdmin()) {
			//선택한 카데고리가 루트일 경우 서브카데고리 생성
			if (object instanceof CategoryModel) {
				CategoryModel category = (CategoryModel) object;
				if (category.getParentID().equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
					return true;
				}else {
					//선택된 카데고리가 서브일 경우 새로운 카테고리 생성 못함
					return false;
				}
			}
			//루트카테고리 생성
			return true;
			
		} else {

			//파이프라인뷰가 아니고 관리자가 아니면 새로운 카데고리 생성 못함
			return false;
		}
	}
}
