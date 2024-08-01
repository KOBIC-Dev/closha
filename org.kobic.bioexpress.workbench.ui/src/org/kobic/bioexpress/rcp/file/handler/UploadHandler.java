package org.kobic.bioexpress.rcp.file.handler;

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
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.constant.Constants;

public class UploadHandler {

	final static Logger logger = Logger.getLogger(UploadHandler.class);

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		logger.info(activePart.getElementId() + "\t" + activePart.getLabel() + "\t" + object);
		
		if (activePart.getElementId().equals(Constants.FILE_TABLE_VIEW_ID)) {
			iEventBroker.send(Constants.FILE_TABLE_UPLOAD_EVENT_BUS_NAME, new UID().toString());
		}

	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		return true;
	}
}
