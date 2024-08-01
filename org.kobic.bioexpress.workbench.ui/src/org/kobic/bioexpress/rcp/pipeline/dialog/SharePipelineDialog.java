package org.kobic.bioexpress.rcp.pipeline.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.pipeline.ShareModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.member.dialog.SearchMemberDialog;
import org.kobic.bioexpress.rcp.pipeline.component.SharedPipelineDialogComponent;

public class SharePipelineDialog extends TitleAreaDialog {

	private SharedPipelineDialogComponent sharePipelineComponent;

	private PipelineClient pipelineClient;

	private PipelineModel pipeline;

	private String rawID;

	private List<ShareModel> share;

	public PipelineModel getPipeline() {
		return pipeline;
	}

	public void setPipeline(PipelineModel pipeline) {
		this.pipeline = pipeline;
	}

	public List<ShareModel> getShare() {
		return share;
	}

	public SharePipelineDialog(Shell parentShell, String rawID) {
		super(parentShell);
		this.rawID = rawID;
		this.pipelineClient = new PipelineClientImpl();
	}

	@Override
	public void create() {

		super.create();

		setTitle("Share Pipeline");
		setMessage(Constants.SHARE_PIPELINE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_SHARE_DIALOG_TITLE_ICON));

		this.getShell().setText("Share Pipeline");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		sharePipelineComponent = new SharedPipelineDialogComponent(composite);
		sharePipelineComponent.tabFolder.setFocus();

		init();

		return composite;
	}

	public void init() {

		try {
			this.pipeline = pipelineClient.getPipeline(rawID);
			setPipeline(pipeline);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bind();
		event();
	}

	public void bind() {

		List<ShareModel> shared = pipeline.getShare();

		if (shared != null) {
			sharePipelineComponent.shareTableViewer.setInput(shared);
		}

		setTableItemChecked();

		PipelineDataModel pipelineDataModel = this.pipeline.getPipelineData();

		if (pipelineDataModel != null) {
			sharePipelineComponent.workspaceNameText.setText(pipelineDataModel.getWorkspaceName());
			sharePipelineComponent.workspaceIDText.setText(pipelineDataModel.getWorkspaceID());
			sharePipelineComponent.pipelineNameText.setText(pipelineDataModel.getPipelineName());
			sharePipelineComponent.keywordText.setText(pipelineDataModel.getKeyword());
			sharePipelineComponent.versionText.setText(pipelineDataModel.getVersion());
			sharePipelineComponent.referenceText.setText(pipelineDataModel.getReference());
			sharePipelineComponent.descriptionText.setText(pipelineDataModel.getPipelineDesc());
		}
	}

	public void event() {

		sharePipelineComponent.addMemberBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean isContains = false;

				SearchMemberDialog dialog = new SearchMemberDialog(getShell());

				if (dialog.open() == Window.OK) {

					TableItem[] items = sharePipelineComponent.shareTableViewer.getTable().getItems();

					for (Member m1 : dialog.getShareMembers()) {

						isContains = false;

						for (TableItem item : items) {

							Member m2 = (Member) item.getData();

							if (m1.getMemberNo().equals(m2.getMemberNo())) {
								isContains = true;
								break;
							}
						}

						if (!isContains) {
							sharePipelineComponent.shareTableViewer.add(m1);
						}
					}
					setTableItemChecked();
				}
			}
		});

		sharePipelineComponent.removeMemberBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sharePipelineComponent.shareTableViewer
						.remove(sharePipelineComponent.shareTableViewer.getTable().getSelection());
				sharePipelineComponent.shareTableViewer.refresh(true);
			}
		});
	}

	private void setTableItemChecked() {

		TableItem[] items = sharePipelineComponent.shareTableViewer.getTable().getItems();

		for (TableItem item : items) {
			if (!item.getChecked()) {
				item.setChecked(true);
			}
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
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
	protected void okPressed() {

		int shareCount = sharePipelineComponent.shareTableViewer.getTable().getItemCount();

		if (shareCount == 0) {

			setErrorMessage("Please select a member to share.");
			sharePipelineComponent.tabFolder.setSelection(1);

		} else {

			share = new ArrayList<ShareModel>();

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {

				progressDialog.run(false, false, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starting to create workspace.", IProgressMonitor.UNKNOWN);

						monitor.subTask("Attempts to connect to the database.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Preparing to update the shared pipeline information.");

						TableItem[] items = sharePipelineComponent.shareTableViewer.getTable().getItems();

						for (TableItem item : items) {
							Member member = (Member) item.getData();

							ShareModel shareModel = new ShareModel();
							shareModel.setMemberID(member.getMemberId());
							shareModel.setMemberName(member.getMemberNm());
							shareModel.setEmail(member.getMemberEmail());

							share.add(shareModel);
						}

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						pipeline.getPipelineData().setIsShared(true);
						pipeline.setShare(share);

						monitor.subTask("Updating shared pipeline information.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

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
