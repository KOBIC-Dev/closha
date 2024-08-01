package org.kobic.bioexpress.rcp.pipeline.dialog;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.regist.RegistClient;
import org.kobic.bioexpress.channel.client.regist.RegistClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.dialog.ViewPublicCategoryDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.component.RegisterPipelineDialogComponent;

public class RegisterPipelineDialog extends TitleAreaDialog {

	private RegisterPipelineDialogComponent registerPipelineComponent;

	private PipelineModel pipelineModel;

	private boolean isRegistComplete;

	@SuppressWarnings("unused")
	private Shell parent;

	public boolean isRegistComplete() {
		return isRegistComplete;
	}

	public RegisterPipelineDialog(Shell parent, PipelineModel pipelineModel) {
		super(parent);
		this.pipelineModel = pipelineModel;
	}

	@Override
	public void create() {

		super.create();

		setTitle("Register Pipeline");
		setMessage(Constants.REGISTER_PIPELINE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_REGISTER_DIALOG_TITLE_ICON));

		this.getShell().setText("Register Pipeline");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		registerPipelineComponent = new RegisterPipelineDialogComponent(composite);

		init();

		return composite;
	}

	public void init() {
		bind();
		event();
	}

	public void bind() {
		registerPipelineComponent.workspaceNameText.setText(this.pipelineModel.getPipelineData().getWorkspaceName());
		registerPipelineComponent.workspaceIDText.setText(this.pipelineModel.getPipelineData().getWorkspaceID());
		registerPipelineComponent.pipelineNameText.setText(this.pipelineModel.getPipelineData().getPipelineName());
		registerPipelineComponent.keywordText.setText(this.pipelineModel.getPipelineData().getKeyword());
		registerPipelineComponent.versionText.setText(this.pipelineModel.getPipelineData().getVersion());
		registerPipelineComponent.referenceText.setText(this.pipelineModel.getPipelineData().getReference());
		registerPipelineComponent.descriptionText.setText(this.pipelineModel.getPipelineData().getPipelineDesc());
	}

	public void event() {

		registerPipelineComponent.categorySelectBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ViewPublicCategoryDialog dialog = new ViewPublicCategoryDialog(getShell(), Constants.PIPELINE_LABEL);

				if (dialog.open() == Window.OK) {
					registerPipelineComponent.categoryNameText.setText(dialog.getCategoryName());
					registerPipelineComponent.categoryIDText.setText(dialog.getCategoryID());
				}
			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Register", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(478, 699);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {

		String pipelineName = registerPipelineComponent.pipelineNameText.getText();
		String categoryID = registerPipelineComponent.categoryIDText.getText();
		String categoryName = registerPipelineComponent.categoryNameText.getText();
		String message = registerPipelineComponent.requestMsgText.getText();
		String registrant = Activator.getMember().getMemberId();
		String rawID = pipelineModel.getRawID();
		String description = registerPipelineComponent.descriptionText.getText();

		if (pipelineName.length() == 0) {

			setErrorMessage("Please enter a pipeline name.");
			registerPipelineComponent.pipelineNameText.setFocus();

		} else if (categoryName.length() == 0 && categoryID.length() == 0) {

			setErrorMessage("Please select a category.");

		} else if (description.length() == 0) {

			setErrorMessage("Please enter a description.");
			registerPipelineComponent.descriptionText.setFocus();

		} else if (message.length() == 0) {

			setErrorMessage("Please write a pipeline registration request message.");
			registerPipelineComponent.requestMsgText.setFocus();

		} else {

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starting to request regist pipeline.", IProgressMonitor.UNKNOWN);

						monitor.subTask("Attempts to connect to the database.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Requesting pipeline registration.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						RegistClient registClient = new RegistClientImpl();
						isRegistComplete = registClient.requestRegistPipeline(categoryID, categoryName, registrant,
								message, rawID, pipelineName, description);

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

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}
}