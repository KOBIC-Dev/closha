package org.kobic.bioexpress.rcp.podman.handler;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClient;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClientImpl;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class DeletePodmanHandler {

	final static Logger logger = Logger.getLogger(DeletePodmanHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		System.out.println(object instanceof PodmanModel);

		if (object instanceof PodmanModel) {

			PodmanModel model = (PodmanModel) object;

			boolean res = MessageDialog.openConfirm(shell, "Delete podman image execution",
					String.format("%s [image ID: %s] delete podman image?", model.getName(), model.getImageID()));

			if (res) {
				PodmanAPIClient api = new PodmanAPIClientImpl();
				api.deletePodman(model.getRawID());

				iEventBroker.send(Constants.PODMAN_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (Activator.isAdmin()) {
			return true;
		} else {
			return false;
		}
	}
}
