package org.kobic.bioexpress.rcp;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.login.dialog.WorkbenchLoginDialog;
import org.kobic.bioexpress.rcp.login.view.LoginDialog;
import org.osgi.service.event.EventHandler;

/**
 * This is a stub implementation containing e4 LifeCycle annotated
 * methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/

public class E4LifeCycle {

	@Inject
	IEventBroker eventBroker;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private EPartService ePartService;

	public EventHandler startupHandler = new EventHandler() {
		@Override
		public void handleEvent(org.osgi.service.event.Event event) {
			
			System.out.println("startup handler started");

			if (Activator.getService().equals(Constants.GBox)) {
				MWindow window = (MWindow) eModelService.find("org.eclipse.e4.window.main", mApplication);
				List<MPerspectiveStack> pStacks = eModelService.findElements(window, null, MPerspectiveStack.class);
				if (pStacks.size() == 1) {
					List<MPerspective> pers = pStacks.get(0).getChildren();
					for (MPerspective per : pers) {
						if (per.getElementId().contains(Constants.GBOX_PERSPECTIVE_ID)) {
							ePartService.switchPerspective(per);
							break;
						}
					}
				}
			}
		}
	};

	private EventHandler shutdownHandler = new EventHandler() {
		@Override
		public void handleEvent(org.osgi.service.event.Event event) {
			System.out.println("shutdown handler started");
		}
	};

	@PostContextCreate
	public void postContextCreate(IApplicationContext appContext, Display display, IEclipseContext context) {
		System.out.println("1111-postContextCreate");
		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, startupHandler);
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
		System.out.println("preSave");
		eventBroker.unsubscribe(startupHandler);
	}

	@ProcessAdditions
	public void processAdditions(IApplicationContext appContext, final MApplication app,
			final EModelService modelService, EPartService partService) {

		final Shell shell = new Shell(SWT.SHELL_TRIM);

		// WorkbenchLoginDialog dialog = new WorkbenchLoginDialog(shell, SWT.BORDER);
		// dialog.open();
		appContext.applicationRunning();
		setLocation(shell, app, modelService, partService);
		System.out.println("processAdditions");

	}

	private void setLocation(Shell shell, MApplication app, EModelService modelService, EPartService partService) {
		MWindow window = (MWindow) modelService.find("org.eclipse.e4.window.main", app);

		Monitor monitor = Display.getCurrent().getPrimaryMonitor();

		Rectangle monitorClientArea = monitor.getClientArea();

		System.out.println("==>" + monitorClientArea.x + " " + monitorClientArea.y + " " + monitorClientArea.width + " "
				+ monitorClientArea.height);

		Rectangle monitorRect = monitor.getBounds();
//		int x = (monitorRect.width / 2) - (window.getWidth() / 2);// - window.width) + 100;
//		int y = (monitorRect.height / 2) - (window.getHeight() / 2);// - shellRect.height) + 100;

		System.out.println(monitorRect.width + "\t" + monitorRect.height);
		System.out.println(window.getWidth() + "\t" + window.getHeight());

		int x = (monitorRect.width - window.getWidth()) / 2;
		int y = (monitorRect.height - window.getHeight()) / 2;

		window.setX(x);
		window.setY(y);

		System.out.println(x + " " + y + " " + window.getWidth() + " " + window.getHeight());

		window.setWidth(monitorClientArea.width);
		window.setHeight(monitorClientArea.height);
	}

	@SuppressWarnings("unused")
	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext, final MApplication app, final EModelService modelService,
			EPartService partService) {

		MWindow window = (MWindow) modelService.find("org.eclipse.e4.window.main", app);

		Monitor monitor = Display.getCurrent().getPrimaryMonitor();

		Rectangle monitorClientArea = monitor.getClientArea();

		System.out.println("processRemovals");
	}
}