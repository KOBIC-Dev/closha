package org.kobic.bioexpress.rcp.handler;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.constant.Constants;

public class QuitHandler {

	final static Logger logger = Logger.getLogger(QuitHandler.class);

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell) {
		if (MessageDialog.openConfirm(shell, Constants.CONFIRM_DIALOG_TITLE,
				Constants.CLOSHA_EXIST_MSG)) {
			iWorkbench.close();
		}
	}

}
