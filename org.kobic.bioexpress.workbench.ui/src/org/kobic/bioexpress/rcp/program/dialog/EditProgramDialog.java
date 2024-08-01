package org.kobic.bioexpress.rcp.program.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.Constant;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.parameter.ParameterModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.dialog.ViewCategoryDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent1;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent2;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent3;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class EditProgramDialog extends TitleAreaDialog {

	private ProgramModel programModel;

	private CTabFolder tabFolder;

	private Text programNameText;

	private ProgramWizardComponent1 programComponent1;
	private ProgramWizardComponent2 programComponent2;
	private ProgramWizardComponent3 programComponent3;

	private List<String> programName;
	private List<ProgramDataModel> programs;

	private List<String> paramNames;
	private List<ParameterDataModel> paramList;

	private Map<String, String> paramMap;

	private TableItem items[];

	private String programNameTextValue;
	private String categoryID;
	private String memberID;

	private boolean isExist;

	private ProgramClient programClient;

	public EditProgramDialog(Shell parent, ProgramModel programModel) {
		super(parent);
		this.programModel = programModel;
	}

	@Override
	public void create() {

		super.create();

		setTitle("Edit Program");
		setMessage(Constants.EDIT_PROGRAM_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_EDIT_DIALOG_TITLE_ICON));

		this.getShell().setText("Edit Program Information");
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
		programNameLabel.setText("Program Name:");

		programNameText = new Text(container, SWT.BORDER);
		programNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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

		CTabItem executeTabItem = new CTabItem(tabFolder, SWT.NONE);
		executeTabItem.setText("Execute");
		executeTabItem
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_EXCUTE_TAB_ICON));

		programComponent2 = new ProgramWizardComponent2();
		Composite executeComposite = programComponent2.getProgramWizardComponent2(tabFolder);
		executeTabItem.setControl(executeComposite);

		programComponent2.scriptPathText.setEditable(false);
		programComponent2.scriptTypeCombo.setEnabled(false);
		programComponent2.selectButton.dispose();

		CTabItem parameterTabItem = new CTabItem(tabFolder, SWT.NONE);
		parameterTabItem.setText("Parameter");
		parameterTabItem.setImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_PARAMETER_TAB_ICON));

		programComponent3 = new ProgramWizardComponent3();
		Composite parameterComposite = programComponent3.getProgramWizardComponent3(tabFolder);
		parameterTabItem.setControl(parameterComposite);

		tabFolder.setFocus();

		return area;
	}

	public void init() {

		categoryID = programModel.getProgramData().getSubCategoryID();
		memberID = Activator.getMember().getMemberId();

		programClient = new ProgramClientImpl();
		programs = programClient.getProgramDataList(categoryID, memberID);

		programName = new ArrayList<String>();

		programs.forEach(program -> programName.add(program.getProgramName()));

		bind();
		event();
	}

	public void bind() {

		programNameText.setText(programModel.getProgramData().getProgramName());

		/**
		 * 프로그램 위자드 컴포넌트 1 바인드
		 */
		programComponent1.subCategoryIdText.setText(programModel.getProgramData().getSubCategoryID());
		programComponent1.subCategoryNameText.setText(programModel.getProgramData().getSubCategoryName());
		programComponent1.keywordText.setText(programModel.getProgramData().getKeyword());
		programComponent1.urlText.setText(programModel.getProgramData().getUrl());
		programComponent1.versionText.setText(programModel.getProgramData().getVersion());
		programComponent1.descriptionText.setText(programModel.getProgramData().getProgramDesc());

		/**
		 * 프로그램 위자드 컴포넌트 2 바인드
		 */
		switch (programModel.getProgramData().getScriptType()) {
		case "Python":
			programComponent2.scriptTypeCombo.select(0);
			break;
		case "Bash":
			programComponent2.scriptTypeCombo.select(1);
			break;
		case "Java":
			programComponent2.scriptTypeCombo.select(2);
			break;
		default:
			break;
		}

		programComponent2.scriptPathText.setText(programModel.getProgramData().getScriptPath());

		boolean isMulti = programModel.getProgramData().isMultiCore;

		if (isMulti) {
			programComponent2.multiCoreRadio.setSelection(true);
			programComponent2.singleCoreRadio.setSelection(false);
		} else {
			programComponent2.singleCoreRadio.setSelection(true);
			programComponent2.multiCoreRadio.setSelection(false);
		}

		if (programModel.getProgramData().getEnv().equals(Constant.CLUSTER_ENV)) {
			programComponent2.hpcRadio.setSelection(true);
			programComponent2.gpuRadio.setSelection(false);
			programComponent2.bigRadio.setSelection(false);
		} else if (programModel.getProgramData().getEnv().equals(Constant.GPU_ENV)) {
			programComponent2.hpcRadio.setSelection(false);
			programComponent2.gpuRadio.setSelection(true);
			programComponent2.bigRadio.setSelection(false);
		} else if (programModel.getProgramData().getEnv().equals(Constant.BIGMEM_ENV)) {
			programComponent2.hpcRadio.setSelection(false);
			programComponent2.gpuRadio.setSelection(false);
			programComponent2.bigRadio.setSelection(true);
		}

		List<ProgramDataModel> relationList = programModel.getRelationProgram();

		programComponent2.treeViewer.setInput(relationList);

		/**
		 * 프로그램 위자드 컴포넌트 3 바인드
		 */

		List<ParameterDataModel> inputParamList = programModel.getParameter().getParameterInput();
		List<ParameterDataModel> outputParamList = programModel.getParameter().getParameterOutput();
		List<ParameterDataModel> optionParamList = programModel.getParameter().getParameterOption();

		programComponent3.inputTableViewer.setInput(inputParamList);
		programComponent3.outputTableViewer.setInput(outputParamList);
		programComponent3.optionTableViewer.setInput(optionParamList);

	}

	public void event() {

		programNameText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				programNameTextValue = programNameText.getText();

				System.out.println(programNameTextValue);

				if (programName.contains(programNameTextValue)) {
					setErrorMessage("The program name already exists.");
					isExist = true;
				} else {
					setErrorMessage(null);
					setMessage("The program name is available.");
					isExist = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});

		/**
		 * 프로그램 위자드 컴포넌트 1 이벤트
		 */
		programComponent1.selectButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ViewCategoryDialog dialog = new ViewCategoryDialog(getShell(), Constants.PROGRAM_LABEL);

				if (dialog.open() == Window.OK) {
					programComponent1.subCategoryNameText.setText(dialog.getCategoryName());
					programComponent1.subCategoryIdText.setText(dialog.getCategoryID());

					categoryID = dialog.getCategoryID();

					programClient = new ProgramClientImpl();
					programs = programClient.getProgramDataList(categoryID, memberID);

					programName = new ArrayList<String>();

					programs.forEach(program -> programName.add(program.getProgramName()));
				}
			}
		});

		/**
		 * 프로그램 위자드 컴포넌트 3 이벤트
		 */
		programComponent3.addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tab = programComponent3.tabFolder.getSelectionIndex();

				paramNames = new ArrayList<String>();
				paramList = new ArrayList<ParameterDataModel>();

				switch (tab) {
				case 0:

					items = programComponent3.inputTableViewer.getTable().getItems();

					for (TableItem item : items) {
						ParameterDataModel parameterData = (ParameterDataModel) item.getData();
						paramList.add(parameterData);
					}

					if (paramList != null)
						paramList.forEach(p -> paramNames.add(p.getParameterName()));

					break;
				case 1:

					items = programComponent3.outputTableViewer.getTable().getItems();

					for (TableItem item : items) {
						ParameterDataModel parameterData = (ParameterDataModel) item.getData();
						paramList.add(parameterData);
					}

					if (paramList != null)
						paramList.forEach(p -> paramNames.add(p.getParameterName()));

					break;
				case 2:

					items = programComponent3.optionTableViewer.getTable().getItems();

					for (TableItem item : items) {
						ParameterDataModel parameterData = (ParameterDataModel) item.getData();
						paramList.add(parameterData);
					}

					if (paramList != null)
						paramList.forEach(p -> paramNames.add(p.getParameterName()));

					break;
				default:
					break;
				}

				NewParameterDialog dialog = new NewParameterDialog(getShell(), tab, paramNames);

				if (dialog.open() == Window.OK) {

					ParameterDataModel dataModel = dialog.getParameterData();

					switch (tab) {
					case 0:
						programComponent3.inputTableViewer.add(dataModel);
						break;
					case 1:
						programComponent3.outputTableViewer.add(dataModel);
						break;
					case 2:
						programComponent3.optionTableViewer.add(dataModel);
						break;
					default:
						break;
					}
				}
			}
		});

		programComponent3.editButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int tab = programComponent3.tabFolder.getSelectionIndex();

				if (isSelectionItem(tab)) {

					ParameterDataModel parameterDataModel = new ParameterDataModel();

					paramMap = new HashMap<String, String>();
					paramList = new ArrayList<ParameterDataModel>();
					items = null;

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

		programComponent3.removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tab = programComponent3.tabFolder.getSelectionIndex();

				if (isSelectionItem(tab)) {
					Object obj = null;

					switch (programComponent3.tabFolder.getSelectionIndex()) {
					case 0:
						obj = programComponent3.inputTableViewer.getStructuredSelection().getFirstElement();
						programComponent3.inputTableViewer.remove((ParameterDataModel) obj);
						programComponent3.inputTableViewer.setSelection(StructuredSelection.EMPTY);
						break;
					case 1:
						obj = programComponent3.outputTableViewer.getStructuredSelection().getFirstElement();
						programComponent3.outputTableViewer.remove((ParameterDataModel) obj);
						programComponent3.outputTableViewer.setSelection(StructuredSelection.EMPTY);
						break;
					case 2:
						obj = programComponent3.optionTableViewer.getStructuredSelection().getFirstElement();
						programComponent3.optionTableViewer.remove((ParameterDataModel) obj);
						programComponent3.optionTableViewer.setSelection(StructuredSelection.EMPTY);
						break;
					default:
						break;
					}
				} else {
					MessageDialog.openWarning(getShell(), "Select Warning", "Please select a parameter to delete.");
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
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	@Override
	protected void okPressed() {

		if (isExist) {

			setErrorMessage("The program name already exists.");
			programNameText.setFocus();

		} else if (programNameText.getText().length() == 0) {

			setErrorMessage("Please enter a program name.");
			programNameText.setFocus();

		} else if (!ValidationUtils.getInstance().isNameVaildation(programNameText.getText())) {
			setErrorMessage(Constants.NAMING_RULE);
			programNameText.setFocus();

		} else if (programComponent1.subCategoryNameText.getText().length() == 0) {

			setErrorMessage("Please enter a category.");
			programComponent1.subCategoryNameText.setFocus();

		} else if (programComponent1.versionText.getText().length() == 0) {

			setErrorMessage("Please enter a version.");
			programComponent1.versionText.setFocus();

		} else if (programComponent1.keywordText.getText().length() == 0) {

			setErrorMessage("Please enter a keyword.");
			programComponent1.keywordText.setFocus();

		} else if (programComponent1.urlText.getText().length() == 0) {

			setErrorMessage("Please enter a URL.");
			programComponent1.urlText.setFocus();

		} else if (programComponent1.descriptionText.getText().length() == 0) {

			setErrorMessage("Please enter a description.");
			programComponent1.descriptionText.setFocus();

		} else {

			String programName = programNameText.getText();
			String subCategoryID = programComponent1.subCategoryIdText.getText();
			String subCategoryName = programComponent1.subCategoryNameText.getText();
			String version = programComponent1.versionText.getText();
			String keyword = programComponent1.keywordText.getText();
			String url = programComponent1.urlText.getText();
			String description = programComponent1.descriptionText.getText();
			String memberID = Activator.getMember().getMemberId();

			boolean isMultiCore = programComponent2.singleCoreRadio.getSelection() ? false : true;

			String env = null;

			if (programComponent2.hpcRadio.getSelection()) {
				env = Constant.CLUSTER_ENV;
			} else if (programComponent2.gpuRadio.getSelection()) {
				env = Constant.GPU_ENV;
			} else if (programComponent2.bigRadio.getSelection()) {
				env = Constant.BIGMEM_ENV;
			}

			CategoryClient categoryClient = new CategoryClientImpl();
			CategoryModel suCategoryModel = categoryClient.getCategoryWithID(subCategoryID);
			CategoryModel rootCategoryModel = categoryClient.getCategoryWithID(suCategoryModel.getParentID());

			ProgramDataModel programDataModel = programModel.getProgramData();
			programDataModel.setProgramName(programName);
			programDataModel.setProgramDesc(description);
			programDataModel.setUrl(url);
			programDataModel.setVersion(version);
			programDataModel.setRootCategoryID(rootCategoryModel.getCategoryID());
			programDataModel.setRootCategoryName(rootCategoryModel.getCategoryName());
			programDataModel.setSubCategoryID(subCategoryID);
			programDataModel.setSubCategoryName(subCategoryName);
			programDataModel.setKeyword(Utils.getInstance().getKeywordFormat(keyword));
			programDataModel.setMemberID(memberID);
			programDataModel.setIsMultiCore(isMultiCore);
			programDataModel.setEnv(env);

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

			List<ProgramDataModel> relationProgram = new ArrayList<ProgramDataModel>();

			if (programComponent2.treeViewer.getTree().getSelectionCount() > 0) {

				TreeItem[] items = programComponent2.treeViewer.getTree().getSelection();

				for (TreeItem item : items) {

					ProgramDataModel programDataMoel = (ProgramDataModel) item.getData();
					relationProgram.add(programDataMoel);
				}
			}

			ParameterModel parameterModel = programModel.getParameter();
			parameterModel.setParameterInput(parameterInput);
			parameterModel.setParameterOutput(parameterOutput);
			parameterModel.setParameterOption(parameterOption);

			programModel.setProgramData(programDataModel);
			programModel.setParameter(parameterModel);
			programModel.setRelationProgram(relationProgram);

			String rawID = programModel.getRawID();

			ProgramClient programClient = new ProgramClientImpl();
			int res = programClient.updateProgram(rawID, programModel);

			if (res == 0) {
				super.okPressed();
			} else {
				MessageDialog.openWarning(getShell(), "Program Warning", "An error occurred while updating.");
			}
		}
	}

	public void openParameterEditDialog(ParameterDataModel parameterDataModel, Map<String, String> paramMap) {

		int tab = programComponent3.tabFolder.getSelectionIndex();

		EditParameterDialog dialog = new EditParameterDialog(getShell(), parameterDataModel, tab,
				Constants.EDIT_PROGRAM_PARAMETER, paramMap);

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
}
