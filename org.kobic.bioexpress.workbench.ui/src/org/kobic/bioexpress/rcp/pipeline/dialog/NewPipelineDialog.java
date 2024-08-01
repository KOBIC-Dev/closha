package org.kobic.bioexpress.rcp.pipeline.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.parameter.ParameterModel;
import org.kobic.bioexpress.model.pipeline.LinkModel;
import org.kobic.bioexpress.model.pipeline.NodeDataModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.component.NewPipelineDialogComponent;
import org.kobic.bioexpress.rcp.utils.PipelineUtil;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;
import org.kobic.bioexpress.rcp.workspace.dialog.ViewWorkspaceDialog;

public class NewPipelineDialog extends TitleAreaDialog {

	final static Logger logger = Logger.getLogger(NewPipelineDialog.class);

	private NewPipelineDialogComponent pipelineDialogComponent;

	private WorkspaceModel workspaceModel;

	@SuppressWarnings("unused")
	private Shell parent;

	private List<String> pipelineName;

	private String pipelineNameText;

	private boolean isExist;

	private PipelineDataModel pipelineDataModel;

	public NewPipelineDialog(Shell parent, WorkspaceModel workspaceModel) {
		super(parent);
		this.workspaceModel = workspaceModel;
		this.parent = parent;
	}

	@Override
	public void create() {

		super.create();

		setTitle("New Pipeline");
		setMessage(Constants.NEW_PIPELINE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_NEW_DIALOG_TITLE_ICON));

		this.getShell().setText("New Pipeline");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		pipelineDialogComponent = new NewPipelineDialogComponent(composite);

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
		String workspaceID = workspaceModel.getWorkspaceID();

		PipelineClient pipelineClient = new PipelineClientImpl();
		List<PipelineDataModel> pipelines = pipelineClient.getPipelineDataOfWorkspace(memberID, workspaceID);

		pipelineName = new ArrayList<String>();

		pipelines.forEach(pipeline -> pipelineName.add(pipeline.getPipelineName().toLowerCase()));

		bind();
		event();
	}

	private void bind() {

		PipelineClient pipelineClient = new PipelineClientImpl();
		List<PipelineDataModel> list = pipelineClient.getPublicPipelineData();

		pipelineDialogComponent.instanceComboTableView.setInput(list);
		pipelineDialogComponent.workspaceNameText.setText(workspaceModel.getWorkspaceName());
		pipelineDialogComponent.workspaceIDText.setText(workspaceModel.getWorkspaceID());

	}

	private void event() {

		pipelineDialogComponent.instanceRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pipelineDialogComponent.instanceComboTable.setEnabled(true);
			}
		});

		pipelineDialogComponent.developRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pipelineDialogComponent.instanceComboTable.setEnabled(false);
			}
		});

		pipelineDialogComponent.workspaceSelectBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ViewWorkspaceDialog dialog = new ViewWorkspaceDialog(getShell());
				if (dialog.open() == Window.OK) {
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
					setMessage("Create a pipeline in the workspace.");
					isExist = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		pipelineDialogComponent.instanceComboTableView.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				if (!event.getSelection().isEmpty()) {
					PipelineDataModel pipelineDataModel = (PipelineDataModel) event.getStructuredSelection()
							.getFirstElement();
					logger.debug(pipelineDataModel.getPipelineName() + "\t" + pipelineDataModel.getPipelineDesc());

					pipelineDialogComponent.versionText.setText(pipelineDataModel.getVersion());
					pipelineDialogComponent.referenceText.setText(pipelineDataModel.getReference());
					pipelineDialogComponent.keywordText.setText(pipelineDataModel.getKeyword());
					pipelineDialogComponent.descriptionText.setText(pipelineDataModel.getPipelineDesc());
				}

			}
		});
	}

	@Override
	protected void okPressed() {

		String pipelineName = pipelineDialogComponent.pipelineNameText.getText();
		String pipelineDesc = pipelineDialogComponent.descriptionText.getText();
		String version = pipelineDialogComponent.versionText.getText();
		String reference = pipelineDialogComponent.referenceText.getText();
		String keyword = pipelineDialogComponent.keywordText.getText();
		String memberID = Activator.getMember().getMemberId();

		boolean isInstance = pipelineDialogComponent.instanceRadio.getSelection();

		if (pipelineName.length() == 0) {
			setErrorMessage("Please enter a pipeline name.");
			pipelineDialogComponent.pipelineNameText.setFocus();
		} else if (!ValidationUtils.getInstance().isNameVaildation(pipelineName)) {
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
			setErrorMessage("The pipeline name already exists.");
			pipelineDialogComponent.pipelineNameText.setFocus();

		} else {

			if (isInstance) {
				int idx = pipelineDialogComponent.instanceComboTable.getSelectionIndex();
				pipelineDataModel = (PipelineDataModel) pipelineDialogComponent.instanceComboTable.getTable()
						.getItem(idx).getData();
			}

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starting to create a pipeline.", IProgressMonitor.UNKNOWN);

						monitor.subTask("Attempts to connect to the database.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Creating a pipeline.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Entering pipeline information.");

						PipelineClient pipelineClient = new PipelineClientImpl();
						PipelineModel pipeline = null;

						if (isInstance) {

							String rawID = pipelineDataModel.getRawID();

							try {
								pipeline = pipelineClient.getPipeline(rawID);

								pipeline.getPipelineData().setWorkspaceID(workspaceModel.getWorkspaceID());
								pipeline.getPipelineData().setWorkspaceName(workspaceModel.getWorkspaceName());
								pipeline.getPipelineData().setCategoryID(Constants.DEFAULT_VALUE);
								pipeline.getPipelineData().setCategoryName(Constants.DEFAULT_VALUE);
								pipeline.getPipelineData().setPipelineName(pipelineName);
								pipeline.getPipelineData().setPipelineDesc(pipelineDesc);
								pipeline.getPipelineData().setVersion(version);
								pipeline.getPipelineData().setReference(reference);
								pipeline.getPipelineData().setOwner(memberID);
								pipeline.getPipelineData().setStatus(Constants.STATUS_WAIT);
								pipeline.getPipelineData().setIsPublic(false);
								pipeline.getPipelineData().setMessage(Constants.DEFAULT_VALUE);
								pipeline.getPipelineData().setExeCount(String.valueOf(Constants.DEFAULT_COUNT));
								pipeline.getPipelineData().setRegistCode(Constants.PIPELINE_STATUS_REGISTER_READY);
								pipeline.getPipelineData().setIsInstance(true);

								pipeline = PipelineUtil.getInstance().instancePipeline(pipeline);

							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {

							PipelineDataModel pipelineData = new PipelineDataModel();
							pipelineData.setPipelineName(pipelineName);
							pipelineData.setPipelineDesc(pipelineDesc);
							pipelineData.setVersion(version);
							pipelineData.setReference(reference);
							pipelineData.setOwner(memberID);
							pipelineData.setRegistrant(memberID);
							pipelineData.setKeyword(Utils.getInstance().getKeywordFormat(keyword));
							pipelineData.setWorkspaceID(workspaceModel.getWorkspaceID());
							pipelineData.setWorkspaceName(workspaceModel.getWorkspaceName());
							pipelineData.setPipelineTemplate(Activator.getPipelineTemplate());
							pipelineData.setStatus(Constant.INIT_STATUS);
							pipelineData.setIsInstance(false);

							List<ParameterDataModel> input = new ArrayList<ParameterDataModel>();
							List<ParameterDataModel> output = new ArrayList<ParameterDataModel>();
							List<ParameterDataModel> option = new ArrayList<ParameterDataModel>();

							ParameterModel parameter = new ParameterModel();
							parameter.setParameterInput(input);
							parameter.setParameterOutput(output);
							parameter.setParameterOption(option);

							NodeDataModel nodeData = new NodeDataModel();

							NodeModel node = new NodeModel();
							node.setNodeData(nodeData);
							node.setParameter(parameter);

							List<NodeModel> nodes = new ArrayList<NodeModel>();
							nodes.add(node);

							LinkModel link = new LinkModel();

							List<LinkModel> links = new ArrayList<LinkModel>();
							links.add(link);

							pipeline = new PipelineModel();
							pipeline.setPipelineData(pipelineData);
						}

						try {
							String rawID = pipelineClient.insertPipeline(pipeline);
							logger.info("create new rawID: " + rawID);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

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
	protected Point getInitialSize() {
		return new Point(478, 623);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

}