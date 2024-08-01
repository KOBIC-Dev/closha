package org.kobic.bioexpress.rcp.common.dialog;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;

public class CommonInputDialog extends InputDialog {

	private String name = null;

	public CommonInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
			IInputValidator validator) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite body = (Composite) super.createDialogArea(parent);
		return body;
	}

	@Override
	protected void okPressed() {
		name = this.getText().getText();
		super.okPressed();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
