package org.kobic.bioexpress.rcp.script.handler;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.task.ScriptTaskModel;
import org.kobic.bioexpress.model.task.ScriptTaskResponseModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.handler.RenameHandler;

public class ScriptStopHandler {

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

		if (activePart.getElementId().equals(Constants.SCRIPT_TASK_HISTORY_VIEW_ID)) {

			if (object instanceof ScriptTaskModel) {

				ScriptTaskModel scriptTaskModel = (ScriptTaskModel) object;

				boolean res = MessageDialog.openConfirm(shell, "Stop script execution",
						String.format("%s [job number: %s] Stop running a script?", scriptTaskModel.getScriptName(),
								scriptTaskModel.getJobID()));

				if (res) {
					TaskClient client = new TaskClientImpl();
					ScriptTaskResponseModel response = client.stopScriptTask(scriptTaskModel.getRawID(),
							scriptTaskModel.getJobID());

					System.out.println("rawID: [" + response.getRawID() + "]\t status: [" + response.getStatus() + "]");

					iEventBroker.send(Constants.SCRIPT_TASK_HISTORY_REFRESH_EVENT_BUS_NAME, scriptTaskModel.getRawID());
				}
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof ScriptTaskModel) {

			ScriptTaskModel scriptTaskModel = (ScriptTaskModel) object;

			if (scriptTaskModel.getStatus().equals(Constants.STATUS_RUN)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
