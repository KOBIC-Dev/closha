package org.kobic.bioexpress.rcp.gbox.handler;

import java.rmi.server.UID;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.constant.Constants;

public class GBoxHomeHandler {

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void goHome(IWorkbench iWorkbench, Shell shell, @Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (activePart.getElementId().equals(Constants.GBOX_TABLE_VIEW_ID)) {

			iEventBroker.send(Constants.GBOX_REMOTE_EXPLORER_HOME_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.FILE_TABLE_VIEW_ID)) {

			iEventBroker.send(Constants.GBOX_LOCAL_EXPLORER_HOME_EVENT_BUS_NAME, new UID().toString());

		}
	}
}
