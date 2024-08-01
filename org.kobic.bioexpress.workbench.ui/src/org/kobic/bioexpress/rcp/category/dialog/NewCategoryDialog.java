package org.kobic.bioexpress.rcp.category.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.component.CategoryDialogComponent;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

public class NewCategoryDialog extends TitleAreaDialog {

	private List<String> categoryName;

	private String parentID;
	private String categoryType;

	private String categoryNameText;

	private boolean isExist;

	private CategoryDialogComponent categoryDialogComponent;

	int result = 0;

	private String memberID;
	private boolean isAdmin;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public NewCategoryDialog(Shell parent, String categoryType, String parentID, List<String> categoryName) {

		super(parent);

		this.parentID = parentID;
		this.categoryType = categoryType;
		this.categoryName = categoryName;

		this.memberID = Activator.getMember().getMemberId();
		this.isAdmin = Activator.isAdmin();
	}

	@Override
	public void create() {
		super.create();

		setTitle("New Category");
		setMessage(Constants.NEW_CATEGORY_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.CATEGORY_NEW_DIALOG_TITLE_ICON));
		
		this.getShell().setText("New Category");
		this.getShell().setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		categoryDialogComponent = new CategoryDialogComponent(composite, this, isAdmin);

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

		categoryDialogComponent.categoryNameText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				categoryNameText = categoryDialogComponent.categoryNameText.getText();

				System.out.println(categoryNameText);

				if (categoryName.contains(categoryNameText)) {
					setErrorMessage("The category name already exists.");
					isExist = true;
				} else {
					setErrorMessage(null);
					setMessage(String.format("Create a new %s category.", categoryType));
					isExist = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void bind() {

	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 314);
	}

	@Override
	protected void okPressed() {

		String name = categoryDialogComponent.categoryNameText.getText();
		String desc = categoryDialogComponent.categoryDescriptionText.getText();

		if (name.length() == 0) {
			setErrorMessage("Please enter a category name.");
			categoryDialogComponent.categoryNameText.setFocus();
			
		} else if(!ValidationUtils.getInstance().isNameVaildation(name)) {
			setErrorMessage(Constants.NAMING_RULE);
			categoryDialogComponent.categoryNameText.setFocus();
			
		} else if (desc.length() == 0) {
			setErrorMessage("Please enter a category description.");
			categoryDialogComponent.categoryDescriptionText.setFocus();
		} else if (isExist) {
			setErrorMessage("The category name already exists.");
			categoryDialogComponent.categoryNameText.setFocus();
		} else {

			CategoryModel categoryModel = new CategoryModel();
			categoryModel.setCategoryName(name);
			categoryModel.setCategoryDesc(desc);
			categoryModel.setIsPipeline(categoryType.equals(Constants.PIPELINE_LABEL));
			categoryModel.setIsProgram(categoryType.equals(Constants.PROGRAM_LABEL));
			categoryModel.setMemberID(memberID);
			categoryModel.setParentID(parentID);
			categoryModel.setIsRoot(parentID.equals(Constant.ROOT_CATEGORY_DEFAULT_ID));
			categoryModel.setIsAdmin(isAdmin);

			if (isAdmin) {
				if (categoryDialogComponent.publicCheck.getSelection()) {
					categoryModel.setIsPublic(true);
				} else {
					categoryModel.setIsPublic(false);
				}
			} else {
				categoryModel.setIsPublic(false);
			}

			CategoryClient categoryClient = new CategoryClientImpl();

			int res = categoryClient.insertCategory(categoryModel);

			setResult(res);

			super.okPressed();
		}
	}
}
