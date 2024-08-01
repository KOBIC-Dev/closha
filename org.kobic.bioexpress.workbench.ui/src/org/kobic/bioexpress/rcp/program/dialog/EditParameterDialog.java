package org.kobic.bioexpress.rcp.program.dialog;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.gbox.dialog.ViewGBoxDialog;
import org.kobic.bioexpress.rcp.program.component.ParameterDialogComponent;

public class EditParameterDialog extends Dialog {

	final static Logger logger = Logger.getLogger(EditParameterDialog.class);

	public ParameterDialogComponent parameterDialogComponent;

	private ParameterDataModel parameterData;

	private String type = null;
	private String name = null;
	private String value = null;
	private String desc = null;
	private String valueType = null;
	private String editType = null;

	boolean isRequire = false;
	boolean isExist = false;

	private String paramID;

	private Map<String, String> paramsMap;

	public EditParameterDialog(Shell parent, ParameterDataModel parameterDataModel, int tab, String editType,
			Map<String, String> paramsMap) {

		super(parent);

		this.parameterData = parameterDataModel;
		this.editType = editType;
		this.paramsMap = paramsMap;

		this.paramID = parameterDataModel.getParameterID();

		switch (tab) {
		case 0:
			this.type = "Input";
			break;
		case 1:
			this.type = "Output";
			break;
		case 2:
			this.type = "Option";
			break;
		default:
			break;
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub

		super.configureShell(newShell);

		newShell.setText("Edit Parameter");
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		parameterDialogComponent = new ParameterDialogComponent(composite);

		if (editType == Constants.EDIT_NODE_PARAMETER) {
			parameterDialogComponent.parameterNameText.setEditable(false);
			parameterDialogComponent.parameterValueTypeCombo.setEnabled(false);
			parameterDialogComponent.parameterDescriptionText.setEditable(false);
			parameterDialogComponent.isRequiredCheck.setVisible(false);
		}

		return composite;
	}

	public void init() {
		event();
		bind();
	}

	public void bind() {

		if (parameterData != null) {

			parameterDialogComponent.parameterValueTypeCombo.setText(parameterData.getParameterValueType());
			parameterDialogComponent.parameterNameText.setText(parameterData.getParameterName());
			parameterDialogComponent.parameterDescriptionText.setText(parameterData.getParameterDesc());
			parameterDialogComponent.isRequiredCheck.setSelection(parameterData.isIsRequire());
			parameterDialogComponent.parameterValueText.setText(parameterData.getParameterValue());
		}

		if (parameterDialogComponent.parameterValueTypeCombo.getText().equals(Constants.PARAMETER_VALUE_TYPE_FILE)
				|| parameterDialogComponent.parameterValueTypeCombo.getText()
						.equals(Constants.PARAMETER_VALUE_TYPE_FOLDER)) {
			parameterDialogComponent.selectBtn.setEnabled(true);
			parameterDialogComponent.parameterValueText.setEditable(false);
		} else {
			parameterDialogComponent.selectBtn.setEnabled(false);
			parameterDialogComponent.parameterValueText.setEditable(true);
		}

	}

	public void event() {

		parameterDialogComponent.selectBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				valueType = parameterDialogComponent.parameterValueTypeCombo.getText();

				ViewGBoxDialog dialog = new ViewGBoxDialog(getShell(), valueType);

				if (dialog.open() == Window.OK) {
					parameterDialogComponent.parameterValueText.setText(dialog.getPath());
				}
			}
		});

		parameterDialogComponent.parameterValueTypeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (parameterDialogComponent.parameterValueTypeCombo.getText()
						.equals(Constants.PARAMETER_VALUE_TYPE_FILE)
						|| parameterDialogComponent.parameterValueTypeCombo.getText()
								.equals(Constants.PARAMETER_VALUE_TYPE_FOLDER)) {
					parameterDialogComponent.selectBtn.setEnabled(true);
					parameterDialogComponent.parameterValueText.setEditable(false);
					parameterDialogComponent.parameterValueText.setText(Constants.DEFAULT_NULL_VALUE);

				} else {
					parameterDialogComponent.selectBtn.setEnabled(false);
					parameterDialogComponent.parameterValueText.setEditable(true);
					parameterDialogComponent.parameterValueText.setText(Constants.DEFAULT_NULL_VALUE);
				}
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

		name = parameterDialogComponent.parameterNameText.getText();
		desc = parameterDialogComponent.parameterDescriptionText.getText();
		valueType = parameterDialogComponent.parameterValueTypeCombo.getText();
		isRequire = parameterDialogComponent.isRequiredCheck.getSelection();
		value = parameterDialogComponent.parameterValueText.getText();

		if (type.length() == 0) {

			MessageDialog.openWarning(getShell(), "Parameter Warning", "Please select a type.");

		} else if (name.length() == 0) {

			MessageDialog.openWarning(getShell(), "Parameter Warning", "Please enter a name.");

		} else if (value.length() == 0) {

			MessageDialog.openWarning(getShell(), "Parameter Warning", "Please enter a value.");

		} else if (desc.length() == 0) {

			MessageDialog.openWarning(getShell(), "Parameter Warning", "Please enter a description.");

		} else if (!paramsMap.get(paramID).equals(name)) {

			isExist = false;

			for (Map.Entry<String, String> elem : paramsMap.entrySet()) {
				if (!elem.getKey().equals(paramID)) {
					if (elem.getValue().equals(name)) {
						isExist = true;
						break;
					}
				}
			}

			if (isExist) {
				MessageDialog.openWarning(getShell(), "Parameter Warning", "The parameter name already exists.");
			} else {
				submit();
			}

		} else {
			submit();
		}

	}

	@Override
	protected Point getInitialSize() {
		return new Point(491, 401);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public ParameterDataModel getParameterData() {
		return parameterData;
	}

	public void setParameterData(ParameterDataModel parameterDada) {
		this.parameterData = parameterDada;
	}

	public void submit() {
		parameterData.setParameterType(type);
		parameterData.setParameterName(name);
		parameterData.setParameterDesc(desc);
		parameterData.setParameterValue(value);
		parameterData.setParameterValueType(valueType);
		parameterData.setIsRequire(isRequire);

		System.out.println(valueType + "\t" + value);

		setParameterData(parameterData);

		super.okPressed();
	}

}
