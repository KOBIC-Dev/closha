package org.kobic.bioexpress.rcp.login.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.kobic.bioexpress.rcp.constant.Constants;

public class LoginDialogCloseAddon implements IWindowCloseHandler {

	@Inject
	@Optional
	private IWorkbench workbench;

	@Inject
	@Optional
	public void startupComplete(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) MApplication application,
			EModelService modelService) {

		MWindow window = (MWindow) modelService.find(Constants.LOGIN_M_WINDOW_ID, application);
		window.getContext().set(IWindowCloseHandler.class, this);
	}

	@Override
	public boolean close(MWindow window) {
		// Your code goes here
		System.out.println("Close login : " + workbench.close());
		return true;
	}
}