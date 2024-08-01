package org.kobic.bioexpress.rcp.script.dialog;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.closha.CloshaClient;
import org.kobic.bioexpress.channel.client.closha.CloshaClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.script.provider.ScriptTreeViewContentProvider;
import org.kobic.bioexpress.rcp.swt.component.BorderLayout;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;

public class ViewScriptDialog extends Dialog {

	final static Logger logger = Logger.getLogger(ViewScriptDialog.class);

	private TreeViewer treeViewer;

	private String scriptPath;

	@SuppressWarnings("unused")
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public ViewScriptDialog(Shell shell) {
		super(shell);

	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub

		super.configureShell(newShell);

		newShell.setText("Select Script");
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(container, SWT.NONE | SWT.BORDER);
		composite.setLayout(new BorderLayout());

		GridData compositeGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		compositeGridData.grabExcessHorizontalSpace = true;
		compositeGridData.grabExcessVerticalSpace = true;
		composite.setLayoutData(compositeGridData);

		treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.setContentProvider(new ScriptTreeViewContentProvider());
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
					columnText = "Script Name";
				} else {
					columnText = tableColumn.getText();
				}

				if (columnText.equals("Script Name")) {
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

		Tree scriptTree = treeViewer.getTree();
		scriptTree.setLayoutData(new BorderLayout.BorderData(BorderLayout.CENTER));

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(200);
		nameCol.getColumn().setText("Script Name");
		nameCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		nameCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof FileModel) {
					FileModel fileModel = (FileModel) element;
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

			@Override
			public String getText(final Object element) {
				if (element instanceof FileModel) {
					FileModel fileModel = (FileModel) element;
					return fileModel.getName();
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
				if (element instanceof FileModel) {
					FileModel fileModel = (FileModel) element;
					return fileModel.getCreateDate();
				}
				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		init();

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
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
	protected void okPressed() {

		FileModel file = (FileModel) treeViewer.getStructuredSelection().getFirstElement();

		if (file.isIsDir()) {
			MessageDialog.openError(getShell(), "Select Error", "Folder not available. Please select a file.");
		} else {
			setScriptPath(file.getPath());
			super.okPressed();
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(426, 446);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void setTreeDataBind() {

		String memberID = Activator.getMember().getMemberId();

		CloshaClient closhaClient = new CloshaClientImpl();
		List<FileModel> files = closhaClient.getUserRootScriptFiles(memberID);

		treeViewer.setInput(files);
	}

}
