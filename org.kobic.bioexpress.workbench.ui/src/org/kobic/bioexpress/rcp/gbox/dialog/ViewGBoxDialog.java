package org.kobic.bioexpress.rcp.gbox.dialog;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.gbox.provider.GBoxTreeViewContentProvider;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;

public class ViewGBoxDialog extends Dialog {

	final static Logger logger = Logger.getLogger(ViewGBoxDialog.class);

	private CheckboxTreeViewer treeViewer;
	private Shell shell;

	private String path;
	private String valueType;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ViewGBoxDialog(Shell shell, String valueType) {
		super(shell);
		this.valueType = valueType;
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub

		super.configureShell(newShell);

		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
		newShell.setText("Select Analysis GBox Data");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);

		Label categoryMsgLabel = new Label(container, SWT.NONE);
		categoryMsgLabel.setText("Please select GBox item.");

		treeViewer = new CheckboxTreeViewer(container,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.CHECK);
		treeViewer.setContentProvider(new GBoxTreeViewContentProvider());
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {

				FileModel t1 = (FileModel) e1;
				FileModel t2 = (FileModel) e2;

				TreeViewer tableViewer = (TreeViewer) viewer;

				TreeColumn tableColumn = tableViewer.getTree().getSortColumn();

				String columnText = null;

				if (tableColumn == null) {
					columnText = "File Name";
				} else {
					columnText = tableColumn.getText();
				}

				if (columnText.equals("File Name")) {
					if (tableViewer.getTree().getSortDirection() == SWT.DOWN) {
						return t2.getName().compareTo(t1.getName());
					} else {
						return t1.getName().compareTo(t2.getName());
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

		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(300);
		nameCol.getColumn().setText("File Name");
		nameCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		nameCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object object) {
				if (object instanceof FileModel) {
					FileModel fileModel = (FileModel) object;
					return fileModel.getName();
				}
				return null;
			}

			@Override
			public Image getImage(Object object) {
				if (object instanceof FileModel) {
					FileModel fileModel = (FileModel) object;
					if (fileModel.isIsDir()) {
						if (fileModel.isSymbol) {
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
									Constants.GBOX_VIEW_SYMBOLIC_FOLDER_ICON);
						} else {
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
									Constants.FILE_VIEW_FOLDER_ICON);
						}
					} else {
						String extIcon = Constants.EXTENSION_MAP.get(fileModel.getExtension().toLowerCase());
						if (extIcon != null && extIcon.length() != 0) {
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, extIcon);
						} else {
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
									Constants.EXTENSION_MAP.get(Constants.FILE_EXTENSION_BASIC));
						}
					}
				}
				return null;
			}
		});

		TreeViewerColumn dateCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		dateCol.getColumn().setWidth(200);
		dateCol.getColumn().setText("Create Date");
		dateCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		dateCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof FileModel) {
					FileModel fileModel = (FileModel) element;
					return fileModel.getCreateDate();
				}
				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
		setTreeDataBind();
	}

	private void event() {

		treeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				TreeItem item = (TreeItem) e.item;

				FileModel selectItem = (FileModel) item.getData();

				if (e.detail == SWT.CHECK) {

					if (item.getChecked()) {

						Object obj[] = treeViewer.getCheckedElements();

						for (int i = 0; i < obj.length; i++) {

							FileModel fileItem = (FileModel) obj[i];

							if (!selectItem.getPath().equals(fileItem.getPath())) {

								if (treeViewer.getChecked(obj[i])) {
									treeViewer.setChecked(obj[i], false);
								}
							}
						}
					}
				}
			}
		});
	}

	@Override
	protected void okPressed() {

		Object[] object = treeViewer.getCheckedElements();

		if (object.length != 0) {

			FileModel fileModel = (FileModel) object[0];

			setPath(fileModel.getPath().replace(Constants.GBOX_RAPIDANT_ROOT, ""));

			if (valueType.equals(Constants.FILE_VALUE_TYPE)) {
				if (fileModel.isIsFile()) {
					super.okPressed();
				} else {
					MessageDialog.openError(shell, "Parameter Error", "Please set the input value to file type.");
				}
			} else if (valueType.equals(Constants.FOLDER_VALUE_TYPE)) {
				if (fileModel.isIsDir()) {
					super.okPressed();
				} else {
					MessageDialog.openError(shell, "Parameter Error", "Please set the input value to folder type.");
				}
			}

		} else {
			MessageDialog.openWarning(this.shell.getShell(), "GBox Warning",
					"An error occurred while setting the GBox data.");
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(556, 446);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void setTreeDataBind() {

		String memberID = Activator.getMember().getMemberId();

		/**
		 * 레피던트 확인 후 원복하기.
		 */

		List<FileModel> root = Activator.getRapidantService().getRoot(memberID);

//		FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
//		List<FileModel> root = fileUtilsClient.getFiles(Constants.GBOX_RAPIDANT_ROOT + "/" + memberID);

		treeViewer.setInput(root);
	}

}
