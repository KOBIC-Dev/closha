package org.kobic.bioexpress.rcp.raonwiz.handler;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

import raonwiz.k.livepass.client.KLivepassClient;

public class ReconnectHandler {

	final static Logger logger = Logger.getLogger(ReconnectHandler.class);

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (activePart.getElementId().equals(Constants.RAONWIZ_VIEW_ID)) {

			boolean res = false;

			res = Activator.getRapidantService().isConnect();

			String message = null;

			if (res) {
				message = "Connected to the GBox server. Do you want to try reconnecting?";
			} else {
				message = "Try reconnecting to the GBox?";
			}

			res = MessageDialog.openConfirm(shell, "GBox connect", message);

			if (res) {

				Activator.getRapidantService().disconnect();

				res = Activator.getRapidantService().connect();

				System.out.println("raonwiz reconnection: [" + res + "]");

				if (res) {

					KLivepassClient klivepassClient = Activator.getRapidantService().getRapidantClient();
					long sessionTime = System.currentTimeMillis();

					Activator.setKlivepassClient(klivepassClient);
					Activator.setSessionTime(sessionTime);

					iEventBroker.send(Constants.RAONWIZ_REFRESH_EVENT_BUS_NAME, new UID().toString());

					MessageDialog.openInformation(shell, "GBox connect", "GBox is connected.");
				} else {
					MessageDialog.openInformation(shell, "GBox connect",
							"A failure occurred when connecting to the GBox. Please contact us by email at cloud_team@kobic.kr");
				}
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		return true;
	}

}
