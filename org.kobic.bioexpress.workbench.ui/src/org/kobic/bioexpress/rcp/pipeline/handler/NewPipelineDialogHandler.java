package org.kobic.bioexpress.rcp.pipeline.handler;

import java.rmi.server.UID;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.dialog.NewPipelineDialog;

public class NewPipelineDialogHandler {

	@Inject
	@Named(IServiceConstants.ACTIVE_SELECTION)
	private Object object;

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void newPipeline(IWorkbench iWorkbench, Shell shell) {

		if (object instanceof WorkspaceModel) {

			WorkspaceModel workspaceModel = (WorkspaceModel) object;

			NewPipelineDialog dialog = new NewPipelineDialog(shell, workspaceModel);

			if (dialog.open() == Window.OK) {
				iEventBroker.send(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME, new UID().toString());
			}
		}
	}

	@CanExecute
	public boolean canExecute() {

		if (object instanceof WorkspaceModel) {
			return true;
		} else {
			return false;
		}
	}
}
