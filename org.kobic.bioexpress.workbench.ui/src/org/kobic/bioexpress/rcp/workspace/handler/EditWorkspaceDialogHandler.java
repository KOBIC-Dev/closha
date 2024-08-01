package org.kobic.bioexpress.rcp.workspace.handler;

import java.rmi.server.UID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.workspace.dialog.EditWorkspaceDialog;

public class EditWorkspaceDialogHandler {

	final static Logger logger = Logger.getLogger(EditWorkspaceDialogHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof WorkspaceModel) {

			WorkspaceModel workspaceModel = (WorkspaceModel) object;

			EditWorkspaceDialog dialog = new EditWorkspaceDialog(shell, workspaceModel);

			if (dialog.open() == Window.OK) {

				iEventBroker.send(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME, new UID().toString());
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof WorkspaceModel) {
			return true;
		} else {
			return false;
		}
	}
}
