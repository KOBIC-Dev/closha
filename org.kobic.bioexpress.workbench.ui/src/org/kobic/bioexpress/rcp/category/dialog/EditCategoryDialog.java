package org.kobic.bioexpress.rcp.category.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.component.CategoryDialogComponent;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

public class EditCategoryDialog extends TitleAreaDialog {

	private CategoryDialogComponent categoryDialogComponent;
	private CategoryModel categoryModel;

	int result = 0;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public EditCategoryDialog(Shell parent, String categoryType, String parentID, CategoryModel categoryModel) {
		super(parent);
		this.categoryModel = categoryModel;
	}

	@Override
	public void create() {
		super.create();

		setTitle("Edit Category");
		setMessage(Constants.EDIT_CATEGORY_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.CATEGORY_EDIT_DIALOG_TITLE_ICON));
		
		this.getShell().setText("Edit Category");
		this.getShell().setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		categoryDialogComponent = new CategoryDialogComponent(composite, this, Activator.isAdmin());

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	private void init() {
		event();
		bind();
	}

	private void event() {

	}

	private void bind() {

		if (categoryModel != null) {
			categoryDialogComponent.categoryNameText.setText(categoryModel.getCategoryName());
			categoryDialogComponent.categoryDescriptionText.setText(categoryModel.getCategoryDesc());
			categoryDialogComponent.publicCheck.setSelection(categoryModel.isIsPublic());
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 314);
	}

	@Override
	protected void okPressed() {

		String name = categoryDialogComponent.categoryNameText.getText();
		String desc = categoryDialogComponent.categoryDescriptionText.getText();
		boolean isPublic = categoryDialogComponent.publicCheck.getSelection();

		if (name.length() == 0) {
			setErrorMessage("Please enter a category name.");
			categoryDialogComponent.categoryNameText.setFocus();
			
		} else if(!ValidationUtils.getInstance().isNameVaildation(name)) {
			setErrorMessage(Constants.NAMING_RULE);
			categoryDialogComponent.categoryNameText.setFocus();
			
		} else if (desc.length() == 0) {
			setErrorMessage("Please enter a category description.");
			categoryDialogComponent.categoryDescriptionText.setFocus();
			
		} else {

			CategoryClient categoryClient = new CategoryClientImpl();

			int res = categoryClient.updateCategory(this.categoryModel.getRawID(), name, desc, isPublic);

			setResult(res);

			super.okPressed();
		}
	}
}
