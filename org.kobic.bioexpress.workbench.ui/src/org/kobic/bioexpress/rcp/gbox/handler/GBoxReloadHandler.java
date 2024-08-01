package org.kobic.bioexpress.rcp.gbox.handler;

import java.rmi.server.UID;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.constant.Constants;

public class GBoxReloadHandler {

	final static Logger logger = Logger.getLogger(GBoxReloadHandler.class);

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
	public void gboxTransferMonitorEvent(IWorkbench iWorkbench, Shell shell) {
		iEventBroker.send(Constants.GBOX_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());
	}

}