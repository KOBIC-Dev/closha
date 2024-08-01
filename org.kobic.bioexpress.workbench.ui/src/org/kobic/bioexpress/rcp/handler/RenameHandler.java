package org.kobic.bioexpress.rcp.handler;

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
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class RenameHandler {

	final static Logger logger = Logger.getLogger(RenameHandler.class);

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

		if (object instanceof FileModel) {

			FileModel fileModel = (FileModel) object;

			if (activePart.getElementId().equals(Constants.FILE_TABLE_VIEW_ID)) {

				iEventBroker.send(Constants.FILE_TABLE_RENAME_EVENT_BUS_NAME, fileModel);

			} else if (activePart.getElementId().equals(Constants.GBOX_TABLE_VIEW_ID)) {

				iEventBroker.send(Constants.GBOX_RENAME_EVENT_BUS_NAME, fileModel);
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		return true;
	}
}
