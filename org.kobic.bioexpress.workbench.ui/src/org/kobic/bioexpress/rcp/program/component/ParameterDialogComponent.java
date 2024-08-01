package org.kobic.bioexpress.rcp.program.component;

import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ParameterDialogComponent {

	public Text parameterNameText;
	public Text parameterDescriptionText;
	public Button okButton;
	public TableCombo parameterValueTypeCombo;
	public Button isRequiredCheck;
	public Button selectBtn;
	public Text parameterValueText;

	public ParameterDialogComponent(Composite composite) {

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		Label parameterTypeLabel = new Label(container, SWT.NONE);
		parameterTypeLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		parameterTypeLabel.setText("Type:");

		parameterValueTypeCombo = new TableCombo(container, SWT.BORDER);
		parameterValueTypeCombo.setEditable(false);
		parameterValueTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label ParameterNameLabel = new Label(container, SWT.NONE);
		ParameterNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		ParameterNameLabel.setText("Name:");

		parameterNameText = new Text(container, SWT.BORDER);
		parameterNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label ParameterValueLabel = new Label(container, SWT.NONE);
		ParameterValueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		ParameterValueLabel.setText("Value:");

		parameterValueText = new Text(container, SWT.BORDER);
		parameterValueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		selectBtn = new Button(container, SWT.NONE);
		GridData gd_selectBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_selectBtn.heightHint = 23;
		selectBtn.setLayoutData(gd_selectBtn);
		selectBtn.setText("Browser...");
		selectBtn.setVisible(true);

		Label DescriptopnLabel = new Label(container, SWT.NONE);
		DescriptopnLabel.setText("Descriptopn:");
		new Label(container, SWT.NONE);

		parameterDescriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData parameterDescriptionTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		parameterDescriptionTextGridData.grabExcessHorizontalSpace = true;
		parameterDescriptionTextGridData.grabExcessVerticalSpace = true;
		parameterDescriptionText.setLayoutData(parameterDescriptionTextGridData);

		isRequiredCheck = new Button(container, SWT.CHECK);
		isRequiredCheck.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		isRequiredCheck.setText("Required arguments");

		comboBind();
	}

	public void comboBind() {

		TableItem stringItem = new TableItem(parameterValueTypeCombo.getTable(), SWT.BORDER);
		stringItem.setText(Constants.PARAMETER_VALUE_TYPE_STRING);
		stringItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_STRING_ICON));

		TableItem integerItem = new TableItem(parameterValueTypeCombo.getTable(), SWT.BORDER);
		integerItem.setText(Constants.PARAMETER_VALUE_TYPE_INTEGER);
		integerItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_INTEGER_ICON));

		TableItem floatItem = new TableItem(parameterValueTypeCombo.getTable(), SWT.BORDER);
		floatItem.setText(Constants.PARAMETER_VALUE_TYPE_FLOAT);
		floatItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_FLOAT_ICON));
		
		TableItem boolItem = new TableItem(parameterValueTypeCombo.getTable(), SWT.BORDER);
		boolItem.setText(Constants.PARAMETER_VALUE_TYPE_BOOLEAN);
		boolItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_BOOLEAN_ICON));

		TableItem filelItem = new TableItem(parameterValueTypeCombo.getTable(), SWT.BORDER);
		filelItem.setText(Constants.PARAMETER_VALUE_TYPE_FILE);
		filelItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_FILE_ICON));

		TableItem folderlItem = new TableItem(parameterValueTypeCombo.getTable(), SWT.BORDER);
		folderlItem.setText(Constants.PARAMETER_VALUE_TYPE_FOLDER);
		folderlItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_DIALOG_FOLDER_ICON));
		
		parameterValueTypeCombo.select(0);

	}
}