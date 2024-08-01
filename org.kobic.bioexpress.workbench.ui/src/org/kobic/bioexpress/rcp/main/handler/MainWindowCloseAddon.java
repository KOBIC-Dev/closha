package org.kobic.bioexpress.rcp.main.handler;

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

public class MainWindowCloseAddon implements IWindowCloseHandler {

	@Inject
	@Optional
	IWorkbench workbench;

	@Inject
	@Optional
	public void startupComplete(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) MApplication application,
			EModelService modelService) {
		//TODO : BIOEXPESS
		MWindow window = (MWindow) modelService.find(Constants.MAIN_M_WINDOW_ID, application);
		window.getContext().set(IWindowCloseHandler.class, this);
	}

	@Override
	public boolean close(MWindow window) {
		// Your code goes here
		System.out.println("Close Closha : " + workbench.close());
		return true;
	}
}