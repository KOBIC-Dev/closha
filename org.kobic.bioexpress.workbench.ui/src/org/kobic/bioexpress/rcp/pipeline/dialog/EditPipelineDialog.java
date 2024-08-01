package org.kobic.bioexpress.rcp.pipeline.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.component.EditPipelineDialogComponent;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;
import org.kobic.bioexpress.rcp.workspace.dialog.ViewWorkspaceDialog;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EditPipelineDialog extends TitleAreaDialog {

	private EditPipelineDialogComponent pipelineDialogComponent;

	private PipelineDataModel pipelineDataModel;

	private Shell parent;

	private List<String> pipelineName;

	private String pipelineNameText;

	private boolean isExist;

	public EditPipelineDialog(Shell parentShell) {
		super(parentShell);
		this.parent = parentShell;
	}

	public EditPipelineDialog(Shell parentShell, PipelineDataModel pipelineDataModel) {
		super(parentShell);
		this.pipelineDataModel = pipelineDataModel;
	}

	@Override
	public void create() {

		super.create();

		setTitle("Edit Pipeline");
		setMessage(Constants.EDIT_PIPELINE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_EDIT_DIALOG_TITLE_ICON));
		
		this.getShell().setText("Edit Pipeline");
		this.getShell().setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		pipelineDialogComponent = new EditPipelineDialogComponent(composite);
		pipelineDialogComponent.workspaceNameText.setEnabled(false);
		pipelineDialogComponent.pipelineNameText.setEditable(false);

		return composite;
	}

	private void init() {

		String memberID = Activator.getMember().getMemberId();
		String workspaceID = pipelineDataModel.getWorkspaceID();

		PipelineClient pipelineClient = new PipelineClientImpl();
		List<PipelineDataModel> pipelines = pipelineClient.getPipelineDataOfWorkspace(memberID, workspaceID);

		pipelineName = new ArrayList<String>();

		pipelines.forEach(pipeline -> pipelineName.add(pipeline.getPipelineName().toLowerCase()));

		bind();
		event();
	}

	private void bind() {

		if (pipelineDataModel != null) {
			pipelineDialogComponent.workspaceNameText.setText(pipelineDataModel.getWorkspaceName());
			pipelineDialogComponent.workspaceIDText.setText(pipelineDataModel.getWorkspaceID());
			pipelineDialogComponent.pipelineNameText.setText(pipelineDataModel.getPipelineName());
			pipelineDialogComponent.keywordText.setText(pipelineDataModel.getKeyword());
			pipelineDialogComponent.versionText.setText(pipelineDataModel.getVersion());
			pipelineDialogComponent.referenceText.setText(pipelineDataModel.getReference());
			pipelineDialogComponent.descriptionText.setText(pipelineDataModel.getPipelineDesc());
		}
	}

	private void event() {

		pipelineDialogComponent.workspaceSelectBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ViewWorkspaceDialog dialog = new ViewWorkspaceDialog(getShell());
				if (dialog.open() == Window.OK) {

					WorkspaceModel workspace = dialog.getWorkspaceModel();
					pipelineDialogComponent.workspaceNameText.setText(workspace.getWorkspaceName());
					pipelineDialogComponent.workspaceIDText.setText(workspace.getWorkspaceID());
				}
			}
		});

		pipelineDialogComponent.pipelineNameText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				pipelineNameText = pipelineDialogComponent.pipelineNameText.getText();

				System.out.println(pipelineNameText);
				//대소문자 구분없이 이름 체크.
				if (pipelineName.contains(pipelineNameText.toLowerCase())) {
					setErrorMessage("The pipeline name already exists.");
					isExist = true;
				} else {
					setErrorMessage(null);
					setMessage("Rename pipeline");
					
					isExist = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(478, 600);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	@Override
	protected void okPressed() {

		String pipelineName = pipelineDialogComponent.pipelineNameText.getText();
		String pipelineDesc = pipelineDialogComponent.descriptionText.getText();
		String version = pipelineDialogComponent.versionText.getText();
		String reference = pipelineDialogComponent.referenceText.getText();
		String keyword = pipelineDialogComponent.keywordText.getText();
		String workspaceName = pipelineDialogComponent.workspaceNameText.getText();
		String workspaceID = pipelineDialogComponent.workspaceIDText.getText();

		if (pipelineName.length() == 0) {
			setErrorMessage("Please enter a pipeline name.");
			pipelineDialogComponent.pipelineNameText.setFocus();
		} else if(!ValidationUtils.getInstance().isNameVaildation(pipelineName)) {
			setErrorMessage(Constants.NAMING_RULE);
			pipelineDialogComponent.pipelineNameText.setFocus();
		} else if (version.length() == 0) {
			setErrorMessage("Please enter a version.");
			pipelineDialogComponent.versionText.setFocus();
		} else if (reference.length() == 0) {
			setErrorMessage("Please enter a referenece.");
			pipelineDialogComponent.referenceText.setFocus();
		} else if (keyword.length() == 0) {
			setErrorMessage("Please enter a keyword.");
			pipelineDialogComponent.descriptionText.setFocus();
		} else if (pipelineDesc.length() == 0) {
			setErrorMessage("Please enter a description.");
			pipelineDialogComponent.keywordText.setFocus();
		} else if (isExist) {
			setErrorMessage("Pipeline name already exists.");
			pipelineDialogComponent.pipelineNameText.setFocus();
		} else {

			pipelineDataModel.setPipelineName(pipelineName);
			pipelineDataModel.setPipelineDesc(pipelineDesc);
			pipelineDataModel.setKeyword(Utils.getInstance().getKeywordFormat(keyword));
			pipelineDataModel.setVersion(version);
			pipelineDataModel.setWorkspaceName(workspaceName);
			pipelineDataModel.setWorkspaceID(workspaceID);
			pipelineDataModel.setReference(reference);

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starts modifying pipeline information.", IProgressMonitor.UNKNOWN);

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Connecting to database");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Modifying pipeline information.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						String rawID = pipelineDataModel.getRawID();

						try {
							PipelineClient pipelineClient = new PipelineClientImpl();
							int res = pipelineClient.updatePipelineData(rawID, pipelineDataModel);

							if (res != 0) {
								MessageDialog.openError(parent.getShell(), "Pipeline Error", "An error occurred while modifying the pipeline.");
							} else {
								if (monitor.isCanceled()) {
									monitor.done();
									return;
								}

								monitor.done();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.okPressed();
		}
	}
}
