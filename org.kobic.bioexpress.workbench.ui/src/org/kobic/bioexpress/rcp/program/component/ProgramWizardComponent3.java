package org.kobic.bioexpress.rcp.program.component;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.provider.ParameterTableViewLabelProvider;

public class ProgramWizardComponent3 {

	public TabFolder tabFolder;

	public TableViewer inputTableViewer;
	public TableViewer outputTableViewer;
	public TableViewer optionTableViewer;

	public Button addButton;
	public Button editButton;
	public Button removeButton;

	public Composite getProgramWizardComponent3(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		
		composite.setLayout(new GridLayout(2, false));

		tabFolder = new TabFolder(composite, SWT.NONE);
		GridData tabFolderGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		tabFolderGridData.grabExcessHorizontalSpace = true;
		tabFolderGridData.grabExcessVerticalSpace = true;
		tabFolder.setLayoutData(tabFolderGridData);

		ParameterTableComponent parameterTableComponent = new ParameterTableComponent();

		TabItem inputTab = new TabItem(tabFolder, SWT.NONE);
		inputTab.setText("Input");
		inputTab.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_INPUT_TAB_ICON));

		Composite inputComposite = new Composite(tabFolder, SWT.NONE);
		inputTab.setControl(inputComposite);
		inputComposite.setLayout(new GridLayout(1, false));

		inputTableViewer = new TableViewer(inputComposite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		inputTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		inputTableViewer.setLabelProvider(new ParameterTableViewLabelProvider());
		inputTableViewer.applyEditorValue();
//		inputTableViewer.setComparator(new ViewerComparator() {
//			public int compare(Viewer viewer, Object e1, Object e2) {
//				ParameterDataModel t1 = (ParameterDataModel) e1;
//				ParameterDataModel t2 = (ParameterDataModel) e2;
//				return t1.getParameterName().compareTo(t2.getParameterName());
//			};
//		});

		parameterTableComponent.getParameterTableComponent(inputTableViewer);

		TabItem outputTab = new TabItem(tabFolder, SWT.NONE);
		outputTab.setText("Output");
		outputTab.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_OUTPUT_TAB_ICON));

		Composite ouputComposite = new Composite(tabFolder, SWT.NONE);
		outputTab.setControl(ouputComposite);
		ouputComposite.setLayout(new GridLayout(1, false));

		outputTableViewer = new TableViewer(ouputComposite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		outputTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		outputTableViewer.setLabelProvider(new ParameterTableViewLabelProvider());
		outputTableViewer.applyEditorValue();
//		outputTableViewer.setComparator(new ViewerComparator() {
//			public int compare(Viewer viewer, Object e1, Object e2) {
//				ParameterDataModel t1 = (ParameterDataModel) e1;
//				ParameterDataModel t2 = (ParameterDataModel) e2;
//				return t1.getParameterName().compareTo(t2.getParameterName());
//			};
//		});

		parameterTableComponent.getParameterTableComponent(outputTableViewer);

		TabItem optionTab = new TabItem(tabFolder, SWT.NONE);
		optionTab.setText("Option");
		optionTab.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_OPTION_TAB_ICON));

		Composite optionComposite = new Composite(tabFolder, SWT.NONE);
		optionTab.setControl(optionComposite);
		optionComposite.setLayout(new GridLayout(1, false));

		optionTableViewer = new TableViewer(optionComposite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		optionTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		optionTableViewer.setLabelProvider(new ParameterTableViewLabelProvider());
		optionTableViewer.applyEditorValue();
//		optionTableViewer.setComparator(new ViewerComparator() {
//			public int compare(Viewer viewer, Object e1, Object e2) {
//				ParameterDataModel t1 = (ParameterDataModel) e1;
//				ParameterDataModel t2 = (ParameterDataModel) e2;
//				return t1.getParameterName().compareTo(t2.getParameterName());
//			};
//		});

		parameterTableComponent.getParameterTableComponent(optionTableViewer);
		new Label(composite, SWT.NONE);

		Composite buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		buttonComposite.setLayout(new GridLayout(1, false));

		addButton = new Button(buttonComposite, SWT.NONE);
		addButton.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_ADD_ICON));
		addButton.setToolTipText("Add Parameter");

		editButton = new Button(buttonComposite, SWT.NONE);
		editButton.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_EDIT_ICON));
		editButton.setToolTipText("Edit Parameter");

		removeButton = new Button(buttonComposite, SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		removeButton.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_REMOVE_ICON));
		removeButton.setToolTipText("Remove Parameter");

		return composite;
	}

	public TableViewer getInputTableViewer() {
		return inputTableViewer;
	}

	public TableViewer getOutputTableViewer() {
		return outputTableViewer;
	}

	public TableViewer getOptionTableViewer() {
		return optionTableViewer;
	}

	public Button getEditButton() {
		return editButton;
	}

	public void setEditButton(Button editButton) {
		this.editButton = editButton;
	}

	public Button getRemoveButton() {
		return removeButton;
	}

	public void setRemoveButton(Button removeButton) {
		this.removeButton = removeButton;
	}

}
