package org.kobic.bioexpress.rcp.category.dialog;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

public class ViewCategoryDialog extends Dialog {

	final static Logger logger = Logger.getLogger(ViewCategoryDialog.class);

	private TreeViewer treeViewer;
	private String type;
	private Shell shell;

	private String categoryID;
	private String categoryName;

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ViewCategoryDialog(Shell shell, String type) {
		super(shell);

		this.type = type;
		this.shell = shell;
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub

		super.configureShell(newShell);

		newShell.setText("Select Category");
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);

		Label categoryMsgLabel = new Label(container, SWT.NONE);
		categoryMsgLabel.setText("Please select a sub-category.");

		treeViewer = new TreeViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);

		treeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {

				CategoryModel t1 = (CategoryModel) e1;
				CategoryModel t2 = (CategoryModel) e2;

				TreeViewer tableViewer = (TreeViewer) viewer;

				TreeColumn tableColumn = tableViewer.getTree().getSortColumn();

				String columnText = null;

				if (tableColumn == null) {
					columnText = "Category Name";
				} else {
					columnText = tableColumn.getText();
				}
				if (columnText.equals("Category Name")) {
					if (tableViewer.getTree().getSortDirection() == SWT.DOWN) {
						return t2.getCategoryName().compareTo(t1.getCategoryName());
					} else {
						return t1.getCategoryName().compareTo(t2.getCategoryName());
					}
				} else if (columnText.equals("Create Date")) {
					if (tableViewer.getTree().getSortDirection() == SWT.DOWN) {
						return t2.getCreateDate().compareTo(t1.getCreateDate());
					} else {
						return t1.getCreateDate().compareTo(t2.getCreateDate());
					}
				} else {
					return 0;
				}
			};
		});

		if (type.toLowerCase().equals(Constants.PIPELINE_LABEL)) {

			treeViewer.setContentProvider(new ITreeContentProvider() {
				@Override
				public boolean hasChildren(Object element) {
					if (element instanceof CategoryModel) {
						CategoryModel categoryModel = (CategoryModel) element;
						if (categoryModel.isIsRoot()) {
							return true;
						} else {
							return false;
						}
					}
					return false;
				}

				@Override
				public Object getParent(Object element) {
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					return ArrayContentProvider.getInstance().getElements(inputElement);
				}

				@Override
				public Object[] getChildren(Object parentElement) {

					if (parentElement instanceof CategoryModel) {
						CategoryModel categoryModel = (CategoryModel) parentElement;
						String categoryID = categoryModel.getCategoryID();
						String memberID = Activator.getMember().getMemberId();
						Object[] list = null;
						if (categoryModel.getParentID().equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
							CategoryClient categoryClient = new CategoryClientImpl();
							list = categoryClient.getPipelineSubCategory(categoryID, memberID).toArray();
						}
						return list;
					}

					return null;
				}
			});

		} else if (type.toLowerCase().equals(Constants.PROGRAM_LABEL)) {

			treeViewer.setContentProvider(new ITreeContentProvider() {
				@Override
				public boolean hasChildren(Object element) {
					if (element instanceof CategoryModel) {
						CategoryModel categoryModel = (CategoryModel) element;
						if (categoryModel.isIsRoot()) {
							return true;
						} else {
							return false;
						}
					}
					return false;
				}

				@Override
				public Object getParent(Object element) {
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					return ArrayContentProvider.getInstance().getElements(inputElement);
				}

				@Override
				public Object[] getChildren(Object parentElement) {
					if (parentElement instanceof CategoryModel) {
						CategoryModel categoryModel = (CategoryModel) parentElement;
						String categoryID = categoryModel.getCategoryID();
						String memberID = Activator.getMember().getMemberId();
						Object[] list = null;
						if (categoryModel.getParentID().equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
							CategoryClient categoryClient = new CategoryClientImpl();
							list = categoryClient.getProgramSubCategory(categoryID, memberID).toArray();
						}
						return list;
					}
					return null;
				}
			});
		}

		Tree categoryTree = treeViewer.getTree();
		categoryTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(200);
		nameCol.getColumn().setText("Category Name");
		nameCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		nameCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object object) {
				if (object instanceof CategoryModel) {
					CategoryModel categoryModel = (CategoryModel) object;
					if (type.toLowerCase().equals(Constants.PIPELINE_LABEL)) {
						if (categoryModel.isRoot) {
							if (categoryModel.isPublic) {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PIPELINE_VIEW_PUBLIC_ROOT_CATEGORY_ICON);
							} else {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PIPELINE_VIEW_ROOT_CATEGORY_ICON);
							}
						} else {
							if (categoryModel.isPublic) {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PIPELINE_VIEW_PUBLIC_SUB_CATEGORY_ICON);
							} else {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PIPELINE_VIEW_SUB_CATEGORY_ICON);
							}
						}
					} else if (type.toLowerCase().equals(Constants.PROGRAM_LABEL)) {
						if (categoryModel.isRoot) {
							if (categoryModel.isPublic) {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PROGRAM_VIEW_PUBLIC_ROOT_CATEGORY_ICON);
							} else {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PROGRAM_VIEW_ROOT_CATEGORY_ICON);
							}
						} else {
							if (categoryModel.isPublic) {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PROGRAM_VIEW_PUBLIC_SUB_CATEGORY_ICON);
							} else {
								return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
										Constants.PROGRAM_VIEW_SUB_CATEGORY_ICON);
							}
						}
					}
				}
				return null;
			}

			@Override
			public String getText(final Object element) {
				if (element instanceof CategoryModel) {
					CategoryModel category = (CategoryModel) element;
					return category.getCategoryName();
				}
				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn dateCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		dateCol.getColumn().setWidth(200);
		dateCol.getColumn().setText("Create Date");
		dateCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		dateCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof CategoryModel) {
					CategoryModel category = (CategoryModel) element;
					return category.getCreateDate();
				}
				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		return container;
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
		setTreeDataBind();
	}

	private void event() {
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	@Override
	protected void okPressed() {
		if (selectEvent())
			super.okPressed();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(426, 446);
	}

	private void setTreeDataBind() {

		String memberID = Activator.getMember().getMemberId();

		CategoryClient categoryClient = new CategoryClientImpl();

		List<CategoryModel> list = null;

		if (type.equals(Constants.PROGRAM_LABEL)) {
			list = categoryClient.getProgramRootCategory(memberID);
		} else {
			list = categoryClient.getPipelineRootCategory(memberID);
		}

		treeViewer.setInput(list);
	}

	private boolean selectEvent() {

		if (!treeViewer.getSelection().isEmpty()) {

			CategoryModel category = (CategoryModel) treeViewer.getStructuredSelection().getFirstElement();

			if (!category.isIsRoot()) {
				setCategoryID(category.getCategoryID());
				setCategoryName(category.getCategoryName());
				return true;
			} else {
				MessageDialog.openWarning(shell.getShell(), "Category Warning",
						"Cannot be registered to a root-category. Please select a sub-category.");
			}
		} else {
			MessageDialog.openWarning(shell.getShell(), "Category Warning", "Please select a category to register.");
		}

		return false;
	}
}