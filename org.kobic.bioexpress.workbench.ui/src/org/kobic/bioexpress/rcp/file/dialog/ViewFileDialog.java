package org.kobic.bioexpress.rcp.file.dialog;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.file.provider.FileTreeViewContentProvider;
import org.kobic.bioexpress.rcp.file.provider.FileTreeViewLabelProvider;

public class ViewFileDialog extends Dialog {

	final static Logger logger = Logger.getLogger(ViewFileDialog.class);

	private CheckboxTreeViewer treeViewer;
	private Shell shell;

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ViewFileDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		
		super.configureShell(newShell);
		
		newShell.setText("Select File");
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);

		Label categoryMsgLabel = new Label(container, SWT.NONE);
		categoryMsgLabel.setText("Please select a file.");

		treeViewer = new CheckboxTreeViewer(container,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.CHECK);
		treeViewer.setLabelProvider(new FileTreeViewLabelProvider());
		treeViewer.setContentProvider(new FileTreeViewContentProvider());
		treeViewer.getTree().setHeaderVisible(false);
		treeViewer.getTree().setLinesVisible(true);

		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

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

			setPath(fileModel.getPath());

			super.okPressed();
		} else {
			MessageDialog.openWarning(this.shell.getShell(), "Select Waring", "Please select a file.");
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(313, 446);
	}

	private void setTreeDataBind() {

		List<FileModel> root = Activator.getFileService().getRoot();
		treeViewer.setInput(root);
	}

}
