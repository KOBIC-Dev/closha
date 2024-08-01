package org.kobic.bioexpress.rcp.workspace.handler;

import java.rmi.server.UID;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.workspace.dialog.NewWorkspaceDialog;

public class NewWorkspaceDialogHandler {

	final static Logger logger = Logger.getLogger(NewWorkspaceDialogHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell) {

		NewWorkspaceDialog dialog = new NewWorkspaceDialog(shell);

		if (dialog.open() == Window.OK) {
			iEventBroker.send(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME, new UID().toString());

		}
	}

	@CanExecute
	public boolean enable() {

		// login 이후 활성화
		if (Activator.getMember() != null) {
			return true;
		} else {
			return false;
		}
	}
}
