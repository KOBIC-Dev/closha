package org.kobic.bioexpress.rcp.podman.handler;

import java.rmi.server.UID;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.podman.dialog.RegistPodmanImageDialog;

public class PodmanHandler {

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void showPodmanImageDialog(IWorkbench iWorkbench, Shell shell, MWindow window) {

		RegistPodmanImageDialog dialog = new RegistPodmanImageDialog(shell);

		if (dialog.open() == Window.OK) {
			iEventBroker.send(Constants.PODMAN_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

			MPart mPart = ePartService.findPart(Constants.PODMAN_VIEW_ID);
			ePartService.showPart(mPart, PartState.ACTIVATE);
		}
	}

	@CanExecute
	public boolean enable() {

		if (Activator.isAdmin()) {
			return true;
		} else {
			return false;
		}
	}
}
