package org.kobic.bioexpress.rcp.workspace.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClient;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClientImpl;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.keyword.dialog.KeywordDialog;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;
import org.kobic.bioexpress.rcp.workspace.component.WorkspaceDialogComponent;

import com.strikewire.snl.apc.ErrorMessageDialog;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

@SuppressWarnings("unused")
public class EditWorkspaceDialog extends TitleAreaDialog {

	final static Logger logger = Logger.getLogger(EditWorkspaceDialog.class);

	private WorkspaceDialogComponent workspaceDialogComponent;

	private WorkspaceModel workspaceModel;

	private Shell parent;

	private List<String> workspaceName;

	private String workspaceNameText;

	private boolean isExist;

	public EditWorkspaceDialog(Shell parent, WorkspaceModel workspaceModel) {
		super(parent);
		this.workspaceModel = workspaceModel;
		this.parent = parent;
	}

	@Override
	public void create() {
		super.create();
		
		setTitle("Edit Workspace");
		setMessage(Constants.EDIT_WORKSPACE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.WORKSPACE_EDIT_DIALOG_TITLE_ICON));

		this.getShell().setText("Edit Workspace");
		this.getShell().setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		workspaceDialogComponent = new WorkspaceDialogComponent(composite, this);

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	private void init() {

		String memberID = Activator.getMember().getMemberId();

		WorkspaceClient workspaceClient = new WorkspaceClientImpl();
		List<WorkspaceModel> workspaces = workspaceClient.getUserWorkspace(memberID);

		workspaceName = new ArrayList<String>();

		workspaces.forEach(workspace -> workspaceName.add(workspace.getWorkspaceName().toLowerCase()));

		bind();
		event();
	}

	private void event() {

		workspaceDialogComponent.workspaceNameText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				workspaceNameText = workspaceDialogComponent.workspaceNameText.getText();

				System.out.println(workspaceNameText);
				//대소문자 구분없이 체크..
				if (workspaceName.contains(workspaceNameText.toLowerCase())) {
					setErrorMessage("The workspace name already exists.");
					isExist = true;
				} else {
					setErrorMessage(null);
					setMessage("Rename workspace.");
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
		if (workspaceModel != null) {
			workspaceDialogComponent.workspaceNameText.setText(workspaceModel.getWorkspaceName());
			workspaceDialogComponent.workspaceDescriptionText.setText(workspaceModel.getDescription());
			workspaceDialogComponent.workspaceKeywordText.setText(workspaceModel.getKeyword());
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(429, 385);
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	@Override
	protected void okPressed() {

		String name = workspaceDialogComponent.workspaceNameText.getText();
		String keyword = workspaceDialogComponent.workspaceKeywordText.getText();
		String desc = workspaceDialogComponent.workspaceDescriptionText.getText();

		if (name.length() == 0) {
			setErrorMessage("Please enter a workspace name.");
			workspaceDialogComponent.workspaceNameText.setFocus();

		} else if (!ValidationUtils.getInstance().isNameVaildation(name)) {
			setErrorMessage(Constants.NAMING_RULE);
			workspaceDialogComponent.workspaceNameText.setFocus();

		} else if (keyword.length() == 0) {
			setErrorMessage("Please select a keyword.");
			workspaceDialogComponent.workspaceKeywordText.setFocus();

		} else if (desc.length() == 0) {
			setErrorMessage("Please enter a description.");
			workspaceDialogComponent.workspaceDescriptionText.setFocus();

		} else if (isExist) {
			setErrorMessage("The workspace name already exists.");
			workspaceDialogComponent.workspaceNameText.setFocus();

		} else {
			//
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starting to modify a workspace information.", IProgressMonitor.UNKNOWN);

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Modifying workspace information.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						String keywords = Utils.getInstance().getKeywordFormat(keyword);
						WorkspaceClient workspaceClient = new WorkspaceClientImpl();
						workspaceClient.updateWorkspaceData(workspaceModel.getRawID(), name, keywords, desc);

						if (monitor.isCanceled()) {
							monitor.done();
							return;
						}

						monitor.done();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.okPressed();
		}
	}
}
