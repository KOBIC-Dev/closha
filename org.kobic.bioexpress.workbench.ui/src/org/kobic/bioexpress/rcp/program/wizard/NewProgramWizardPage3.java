package org.kobic.bioexpress.rcp.program.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent3;
import org.kobic.bioexpress.rcp.program.dialog.EditParameterDialog;
import org.kobic.bioexpress.rcp.program.dialog.NewParameterDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewProgramWizardPage3 extends WizardPage {

	private List<String> paramNames;
	private List<ParameterDataModel> paramList;
	private Map<String, String> paramMap;

	private ProgramWizardComponent3 programComponent3;

	private TableItem items[];

	public NewProgramWizardPage3() {

		super("wizardPage");

		setTitle("Create New Analysis Program");
		setDescription("Setting Program Parameter Information");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Constants.SYMBOLIC_NAME, Constants.PROGRAM_NEW_DIALOG_TITLE_ICON));
		
	}

	@Override
	public void createControl(Composite parent) {

		programComponent3 = new ProgramWizardComponent3();

		Composite parameterCompostie = programComponent3.getProgramWizardComponent3(parent);
		setControl(parameterCompostie);

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {

	}

	private void event() {

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

//				String env = page2.getProgramComponent2().clusterRadio.getSelection() ? Constant.CLUSTER_ENV : Constant.BIGDATA_ENV;
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

	public TableViewer getInputTableViewer() {
		return programComponent3.inputTableViewer;
	}

	public void setInputTableViewer(TableViewer inputTableViewer) {
		this.programComponent3.inputTableViewer = inputTableViewer;
	}

	public TableViewer getOutputTableViewer() {
		return programComponent3.outputTableViewer;
	}

	public void setOutputTableViewer(TableViewer outputTableViewer) {
		this.programComponent3.outputTableViewer = outputTableViewer;
	}

	public TableViewer getOptionTableViewer() {
		return programComponent3.optionTableViewer;
	}

	public void setOptionTableViewer(TableViewer optionTableViewer) {
		this.programComponent3.optionTableViewer = optionTableViewer;
	}

}