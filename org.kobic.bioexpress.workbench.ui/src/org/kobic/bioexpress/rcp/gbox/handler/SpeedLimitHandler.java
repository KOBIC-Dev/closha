
package org.kobic.bioexpress.rcp.gbox.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.gbox.dialog.SpeedLimitDialog;

public class SpeedLimitHandler {

	@SuppressWarnings("unused")
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

		SpeedLimitDialog dialog = new SpeedLimitDialog(shell);

		if (dialog.open() == Window.OK) {
			MessageDialog.openInformation(shell, "Setting Complete", "GBox setup is complete.");
		}
	}

	@CanExecute
	public boolean enable() {
		return true;
	}
}