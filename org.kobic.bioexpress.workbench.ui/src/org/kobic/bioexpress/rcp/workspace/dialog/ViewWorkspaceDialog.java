package org.kobic.bioexpress.rcp.workspace.dialog;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
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
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClient;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClientImpl;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;
import org.eclipse.swt.widgets.Label;

public class ViewWorkspaceDialog extends Dialog {

	final static Logger logger = Logger.getLogger(ViewWorkspaceDialog.class);

	private TreeViewer treeViewer;

	private WorkspaceModel workspaceModel;

	@Optional
	private EPartService ePartService;

	public WorkspaceModel getWorkspaceModel() {
		return workspaceModel;
	}

	public void setWorkspaceModel(WorkspaceModel workspaceModel) {
		this.workspaceModel = workspaceModel;
	}

	public ViewWorkspaceDialog(Shell shell) {
		super(shell);

	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub

		super.configureShell(newShell);

		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
		newShell.setText("Select Workspace");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Label messageLabel = new Label(container, SWT.NONE);
		messageLabel.setText("Please select workspace");

		treeViewer = new TreeViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		treeViewer.setContentProvider(new ITreeContentProvider() {
			@Override
			public boolean hasChildren(Object element) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return ArrayContentProvider.getInstance().getElements(inputElement);
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				return null;
			}
		});

		treeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {

				WorkspaceModel t1 = (WorkspaceModel) e1;
				WorkspaceModel t2 = (WorkspaceModel) e2;

				TreeViewer tableViewer = (TreeViewer) viewer;

				TreeColumn tableColumn = tableViewer.getTree().getSortColumn();

				String columnText = null;

				if (tableColumn == null) {
					columnText = "Workspace Name";
				} else {
					columnText = tableColumn.getText();
				}

				if (columnText.equals("Workspace Name")) {
					if (tableViewer.getTree().getSortDirection() == SWT.DOWN) {
						return t2.getWorkspaceName().compareTo(t1.getWorkspaceName());
					} else {
						return t1.getWorkspaceName().compareTo(t2.getWorkspaceName());
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

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(200);
		nameCol.getColumn().setText("Workspace Name");
		nameCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		nameCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof WorkspaceModel) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
							Constants.WORKSPACE_VIEW_WORKSPACE_ICON);
				} else {
					return null;
				}
			}

			@Override
			public String getText(final Object element) {
				if (element instanceof WorkspaceModel) {
					WorkspaceModel workspace = (WorkspaceModel) element;
					return workspace.getWorkspaceName();
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
				if (element instanceof WorkspaceModel) {
					WorkspaceModel workspace = (WorkspaceModel) element;
					return workspace.getCreateDate();
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

		if (treeViewer.getStructuredSelection().isEmpty()) {

		} else {
			WorkspaceModel workspacd = (WorkspaceModel) treeViewer.getStructuredSelection().getFirstElement();

			setWorkspaceModel(workspacd);

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

		WorkspaceClient workspaceClient = new WorkspaceClientImpl();
		List<WorkspaceModel> list = workspaceClient.getUserWorkspace(memberID);
		treeViewer.setInput(list);
	}
}