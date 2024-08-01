package org.kobic.bioexpress.rcp.workspace.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClient;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClientImpl;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;
import org.kobic.bioexpress.rcp.workspace.component.WorkspaceDialogComponent;

public class NewWorkspaceDialog extends TitleAreaDialog {

	final static Logger logger = Logger.getLogger(EditWorkspaceDialog.class);

	private WorkspaceDialogComponent workspaceDialogComponent;

	@SuppressWarnings("unused")
	private Shell parent;

	private List<String> workspaceName;

	private String workspaceNameText;

	private boolean isExist;

	public NewWorkspaceDialog(Shell parent) {
		super(parent);
		this.parent = parent;
	}

	@Override
	public void create() {
		super.create();

		setTitle("New Workspace");
		setMessage(Constants.NEW_WORKSPACE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.WORKSPACE_NEW_DIALOG_TITLE_ICON));
		this.getShell().setText("New Workspace");
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
				//대소문자 구분없이 체크.
				if (workspaceName.contains(workspaceNameText.toLowerCase())) {
					setErrorMessage("The workspace name already exists.");
					isExist = true;
				} else {
					setErrorMessage(null);
					setMessage("Create a new workspace.");
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
		return new Point(429, 385);
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	@Override
	protected void okPressed() {

		String name = workspaceDialogComponent.workspaceNameText.getText();
		String desc = workspaceDialogComponent.workspaceDescriptionText.getText();
		String keyword = workspaceDialogComponent.workspaceKeywordText.getText();

		if (name.length() == 0) {
			setErrorMessage("Please enter a workspace name.");
			workspaceDialogComponent.workspaceNameText.setFocus();
			
		} else if(!ValidationUtils.getInstance().isNameVaildation(name)) {
			setErrorMessage(Constants.NAMING_RULE);
			workspaceDialogComponent.workspaceNameText.setFocus();
			
		} else if (keyword.length() == 0) {
			setErrorMessage("Please enter a keyword.");
			workspaceDialogComponent.workspaceKeywordText.setFocus();
			
		} else if (desc.length() == 0) {
			setErrorMessage("Please enter a description.");
			workspaceDialogComponent.workspaceDescriptionText.setFocus();
			
		} else if (isExist) {
			setErrorMessage("The workspace name already exists.");
			workspaceDialogComponent.workspaceNameText.setFocus();
			
		} else {

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
			
			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starting to create workspace.", IProgressMonitor.UNKNOWN);

						monitor.subTask("Connecting to database.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Creating a workspace.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Entering information into the workspace.");

						String memberID = Activator.getMember().getMemberId();

						WorkspaceModel workspaceModel = new WorkspaceModel();
						workspaceModel.setWorkspaceName(name);
						workspaceModel.setDescription(desc);
						workspaceModel.setKeyword(Utils.getInstance().getKeywordFormat(keyword));
						workspaceModel.setMemberID(memberID);

						WorkspaceClient workspaceClient = new WorkspaceClientImpl();
						workspaceClient.insertWorkspace(workspaceModel);

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

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