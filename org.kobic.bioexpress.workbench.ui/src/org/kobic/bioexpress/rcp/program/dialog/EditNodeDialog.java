package org.kobic.bioexpress.rcp.program.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.Constant;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.parameter.ParameterModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent1;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent2;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent3;
import org.kobic.bioexpress.rcp.utils.CanvasUtil;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

public class EditNodeDialog extends TitleAreaDialog {

	private NodeModel nodeModel;
	private PipelineModel pipelineModel;

	private CTabFolder tabFolder;

	private Text programNameText;
	private Button editcodeButton;

	private ProgramWizardComponent1 programComponent1;
	private ProgramWizardComponent2 programComponent2;
	private ProgramWizardComponent3 programComponent3;

	private List<ParameterDataModel> paramList;
	private Map<String, String> paramMap;

	private TableItem items[];

	@Inject
	@Optional
	private EPartService ePartService;

	public EditNodeDialog(Shell parent, NodeModel nodeModel, PipelineModel pipelineModel) {

		super(parent);
		this.nodeModel = nodeModel;
		PipelineClient pipelineClient = new PipelineClientImpl();
		try {
			this.pipelineModel = pipelineClient.getPipeline(pipelineModel.getRawID());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void create() {

		super.create();

		setTitle("Edit Node Parameter value");
		setMessage(Constants.EDIT_PROGRAM_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_EDIT_DIALOG_TITLE_ICON));

		this.getShell().setText("Edit Node Information");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(2, false));

		Label programNameLabel = new Label(container, SWT.NONE);
		programNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		/// Change NINESOFT..
		programNameLabel.setText("Node Name:");

		programNameText = new Text(container, SWT.BORDER);
		programNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		programNameText.setEditable(false);

		Label separatorLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separatorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem basicTabItem = new CTabItem(tabFolder, SWT.NONE);
		basicTabItem.setText("Basic");
		basicTabItem
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_BASIC_TAB_ICON));

		programComponent1 = new ProgramWizardComponent1();
		Composite basicComposite = programComponent1.getProgramWizardComponent1(tabFolder);
		basicTabItem.setControl(basicComposite);
		programComponent1.selectButton.dispose();
		programComponent1.subCategoryNameText.setEditable(false);
		programComponent1.keywordText.setEditable(false);
		programComponent1.versionText.setEditable(false);
		programComponent1.descriptionText.setEditable(false);
		programComponent1.urlText.setEditable(false);

		CTabItem executeTabItem = new CTabItem(tabFolder, SWT.NONE);
		executeTabItem.setText("Execute");
		executeTabItem
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_EXCUTE_TAB_ICON));

		programComponent2 = new ProgramWizardComponent2();
		Composite executeComposite = programComponent2.getProgramWizardComponent2(tabFolder);
		executeTabItem.setControl(executeComposite);

		programComponent2.scriptPathText.setEditable(false);
		programComponent2.scriptTypeCombo.setEnabled(false);
		programComponent2.singleCoreRadio.setEnabled(false);
		programComponent2.multiCoreRadio.setEnabled(false);
		programComponent2.hpcRadio.setEnabled(false);
		programComponent2.gpuRadio.setEnabled(false);
		programComponent2.bigRadio.setEnabled(false);
		programComponent2.selectButton.dispose();
		programComponent2.relationGroup.dispose();

		CTabItem parameterTabItem = new CTabItem(tabFolder, SWT.NONE);
		parameterTabItem.setText("Parameter");
		parameterTabItem.setImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_PARAMETER_TAB_ICON));

		programComponent3 = new ProgramWizardComponent3();
		Composite parameterComposite = programComponent3.getProgramWizardComponent3(tabFolder);
		parameterTabItem.setControl(parameterComposite);

		programComponent3.addButton.dispose();
		programComponent3.removeButton.dispose();

		tabFolder.setSelection(parameterTabItem);
		tabFolder.setFocus();

		return area;
	}

	public void init() {
		bind();
		event();
	}

	public void bind() {

		programNameText.setText(nodeModel.getNodeData().getNodeName());

		/**
		 * 프로그램 컴포넌트 1 바인드
		 */
		programComponent1.subCategoryIdText.setText(nodeModel.getNodeData().getCategoryID());
		programComponent1.subCategoryNameText.setText(nodeModel.getNodeData().getCategoryName());
		programComponent1.keywordText.setText(nodeModel.getNodeData().getKeyword());
		programComponent1.urlText.setText(nodeModel.getNodeData().getUrl());
		programComponent1.versionText.setText(nodeModel.getNodeData().getVersion());
		programComponent1.descriptionText.setText(nodeModel.getNodeData().getProgramDesc());

		/**
		 * 프로그램 컴포넌트 2 바인드
		 */
		System.out.println(nodeModel.getNodeData().getScriptType());

		switch (nodeModel.getNodeData().getScriptType().toLowerCase()) {
		case Constants.PYTHON:
			programComponent2.scriptTypeCombo.select(0);
			break;
		case Constants.BASH:
			programComponent2.scriptTypeCombo.select(1);
			break;
		case Constants.R:
			programComponent2.scriptTypeCombo.select(2);
			break;
		}

		programComponent2.scriptPathText.setText(nodeModel.getNodeData().getScriptPath());

		boolean isMulti = nodeModel.getNodeData().MultiCore;

		if (isMulti) {
			programComponent2.multiCoreRadio.setSelection(true);
			programComponent2.singleCoreRadio.setSelection(false);
		} else {
			programComponent2.singleCoreRadio.setSelection(true);
			programComponent2.multiCoreRadio.setSelection(false);
		}

		if (nodeModel.getNodeData().getEnv().equals(Constant.CLUSTER_ENV)) {
			programComponent2.hpcRadio.setSelection(true);
			programComponent2.gpuRadio.setSelection(false);
			programComponent2.bigRadio.setSelection(false);
		} else if (nodeModel.getNodeData().getEnv().equals(Constant.GPU_ENV)) {
			programComponent2.hpcRadio.setSelection(false);
			programComponent2.gpuRadio.setSelection(true);
			programComponent2.bigRadio.setSelection(false);
		} else if (nodeModel.getNodeData().getEnv().equals(Constant.BIGMEM_ENV)) {
			programComponent2.hpcRadio.setSelection(false);
			programComponent2.gpuRadio.setSelection(false);
			programComponent2.bigRadio.setSelection(true);
		}

		/**
		 * 프로그램 컴포넌트 3 바인드
		 */

		List<ParameterDataModel> inputParamList = nodeModel.getParameter().getParameterInput();
		List<ParameterDataModel> outputParamList = nodeModel.getParameter().getParameterOutput();
		List<ParameterDataModel> optionParamList = nodeModel.getParameter().getParameterOption();

		programComponent3.inputTableViewer.setInput(inputParamList);
		programComponent3.outputTableViewer.setInput(outputParamList);
		programComponent3.optionTableViewer.setInput(optionParamList);

	}

	public void event() {

		/**
		 * 프로그램 위자드 컴포넌트 3 이벤트
		 */
		programComponent3.editButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tab = programComponent3.tabFolder.getSelectionIndex();

				if (isSelectionItem(tab)) {

					ParameterDataModel parameterDataModel = new ParameterDataModel();

					paramMap = new HashMap<String, String>();
					paramList = new ArrayList<ParameterDataModel>();

					switch (tab) {
					case 0:
						parameterDataModel = (ParameterDataModel) programComponent3.inputTableViewer
								.getStructuredSelection().getFirstElement();

						items = programComponent3.inputTableViewer.getTable().getItems();

						for (TableItem item : items) {
							ParameterDataModel parameterData = (ParameterDataModel) item.getData();
							paramList.add(parameterData);
						}

						if (paramList != null)
							paramList.forEach(p -> paramMap.put(p.getParameterID(), p.getParameterName()));

						break;
					case 1:
						parameterDataModel = (ParameterDataModel) programComponent3.outputTableViewer
								.getStructuredSelection().getFirstElement();

						items = programComponent3.outputTableViewer.getTable().getItems();

						for (TableItem item : items) {
							ParameterDataModel parameterData = (ParameterDataModel) item.getData();
							paramList.add(parameterData);
						}

						if (paramList != null)
							paramList.forEach(p -> paramMap.put(p.getParameterID(), p.getParameterName()));

						break;
					case 2:
						parameterDataModel = (ParameterDataModel) programComponent3.optionTableViewer
								.getStructuredSelection().getFirstElement();

						items = programComponent3.optionTableViewer.getTable().getItems();

						for (TableItem item : items) {
							ParameterDataModel parameterData = (ParameterDataModel) item.getData();
							paramList.add(parameterData);
						}

						if (paramList != null)
							paramList.forEach(p -> paramMap.put(p.getParameterID(), p.getParameterName()));

						break;
					default:
						break;
					}

					openParameterEditDialog(parameterDataModel, paramMap);
				} else {
					MessageDialog.openWarning(getShell(), "Select Warning", "Please select a parameter to change.");
				}
			}
		});

		programComponent3.inputTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				ParameterDataModel parameterDataModel = (ParameterDataModel) programComponent3.inputTableViewer
						.getStructuredSelection().getFirstElement();

				paramMap = new HashMap<String, String>();
				paramList = new ArrayList<ParameterDataModel>();

				items = programComponent3.inputTableViewer.getTable().getItems();

				for (TableItem item : items) {
					ParameterDataModel parameterData = (ParameterDataModel) item.getData();
					paramList.add(parameterData);
				}

				if (paramList != null)
					paramList.forEach(p -> paramMap.put(p.getParameterID(), p.getParameterName()));

				openParameterEditDialog(parameterDataModel, paramMap);
			}
		});

		programComponent3.outputTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				ParameterDataModel parameterDataModel = (ParameterDataModel) programComponent3.outputTableViewer
						.getStructuredSelection().getFirstElement();

				paramMap = new HashMap<String, String>();
				paramList = new ArrayList<ParameterDataModel>();

				items = programComponent3.outputTableViewer.getTable().getItems();

				for (TableItem item : items) {
					ParameterDataModel parameterData = (ParameterDataModel) item.getData();
					paramList.add(parameterData);
				}

				if (paramList != null)
					paramList.forEach(p -> paramMap.put(p.getParameterID(), p.getParameterName()));

				openParameterEditDialog(parameterDataModel, paramMap);

			}
		});

		programComponent3.optionTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				ParameterDataModel parameterDataModel = (ParameterDataModel) programComponent3.optionTableViewer
						.getStructuredSelection().getFirstElement();

				paramMap = new HashMap<String, String>();
				paramList = new ArrayList<ParameterDataModel>();

				items = programComponent3.optionTableViewer.getTable().getItems();

				for (TableItem item : items) {
					ParameterDataModel parameterData = (ParameterDataModel) item.getData();
					paramList.add(parameterData);
				}

				if (paramList != null)
					paramList.forEach(p -> paramMap.put(p.getParameterID(), p.getParameterName()));

				openParameterEditDialog(parameterDataModel, paramMap);
			}
		});

		/**
		 * editcodeButton
		 */

		editcodeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				System.out.println("editcodeButton pressed");

				// open editor...
				String path = nodeModel.getNodeData().getScriptPath();
				System.out.println(path);
				FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
				FileModel fileModel = fileUtilsClient.getFile(path);
				CanvasUtil.openEditor(fileModel);

				getShell().close();

			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		editcodeButton = createButton(parent, Constants.EDTICODE_BUTTON_ID, "Edit Code", false);
		if (nodeModel.getNodeData().getStatus().equals(Constants.STATUS_RUN)
				|| pipelineModel.getPipelineData().getStatus().equals(Constants.STATUS_RUN)
				|| pipelineModel.getPipelineData().getStatus().equals(Constants.STATUS_EXEC)
				|| (nodeModel.getNodeData().isPublic() && !Activator.isAdmin())) {
			editcodeButton.setEnabled(false);
		} else {
			editcodeButton.setEnabled(true);
		}
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	@Override
	protected void okPressed() {

		int count = 0;

		List<ParameterDataModel> parameterInput = new ArrayList<ParameterDataModel>();

		count = programComponent3.getInputTableViewer().getTable().getItemCount();

		if (count != 0) {
			for (int i = 0; i < count; i++) {
				ParameterDataModel inputParam = (ParameterDataModel) programComponent3.getInputTableViewer()
						.getElementAt(i);
				parameterInput.add(inputParam);
			}
		}

		List<ParameterDataModel> parameterOutput = new ArrayList<ParameterDataModel>();

		count = programComponent3.getOutputTableViewer().getTable().getItemCount();

		if (count != 0) {
			for (int i = 0; i < count; i++) {
				ParameterDataModel inputParam = (ParameterDataModel) programComponent3.getOutputTableViewer()
						.getElementAt(i);
				parameterOutput.add(inputParam);
			}
		}

		List<ParameterDataModel> parameterOption = new ArrayList<ParameterDataModel>();

		count = programComponent3.getOptionTableViewer().getTable().getItemCount();

		if (count != 0) {
			for (int i = 0; i < count; i++) {
				ParameterDataModel inputParam = (ParameterDataModel) programComponent3.getOptionTableViewer()
						.getElementAt(i);
				parameterOption.add(inputParam);
			}
		}

		ParameterModel parameterModel = nodeModel.getParameter();
		parameterModel.setParameterInput(parameterInput);
		parameterModel.setParameterOutput(parameterOutput);
		parameterModel.setParameterOption(parameterOption);

		nodeModel.setParameter(parameterModel);

		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(700, 630);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	public void openParameterEditDialog(ParameterDataModel parameterDataModel, Map<String, String> paramMap) {

		int tab = programComponent3.tabFolder.getSelectionIndex();

		EditParameterDialog dialog = new EditParameterDialog(getShell(), parameterDataModel, tab,
				Constants.EDIT_NODE_PARAMETER, paramMap);

		if (dialog.open() == Window.OK) {

			ParameterDataModel model = dialog.getParameterData();

			switch (tab) {
			case 0:

				programComponent3.inputTableViewer.replace(model,
						programComponent3.inputTableViewer.getTable().getSelectionIndex());

				break;
			case 1:

				programComponent3.outputTableViewer.replace(model,
						programComponent3.outputTableViewer.getTable().getSelectionIndex());

				break;
			case 2:

				programComponent3.optionTableViewer.replace(model,
						programComponent3.optionTableViewer.getTable().getSelectionIndex());

				break;
			default:
				break;
			}
		}
	}

	public boolean isSelectionItem(int tab) {

		boolean isSelect = true;

		switch (tab) {
		case 0:
			if (programComponent3.inputTableViewer.getStructuredSelection().isEmpty()) {
				isSelect = false;
			}
			break;
		case 1:
			if (programComponent3.outputTableViewer.getStructuredSelection().isEmpty()) {
				isSelect = false;
			}
			break;
		case 2:
			if (programComponent3.optionTableViewer.getStructuredSelection().isEmpty()) {
				isSelect = false;
			}
			break;
		default:
			break;
		}

		return isSelect;
	}

	public NodeModel getNodeModel() {
		return nodeModel;
	}

}
