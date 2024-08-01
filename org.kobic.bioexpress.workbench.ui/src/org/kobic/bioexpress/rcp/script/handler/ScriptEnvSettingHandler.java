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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClient;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClientImpl;
import org.kobic.bioexpress.channel.client.script.ScriptClient;
import org.kobic.bioexpress.channel.client.script.ScriptClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.model.script.ScriptModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.script.dialog.EnvSettingDialog;
import org.kobic.bioexpress.rcp.utils.Utils;

public class ScriptEnvSettingHandler {

	final static Logger logger = Logger.getLogger(ScriptEnvSettingHandler.class);

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

		if (object instanceof FileModel) {

			Utils utils = Utils.getInstance();

			FileModel file = (FileModel) object;

			String memberId = Activator.getMember().getMemberId();
			String savePath = file.getPath();
			String name = file.getName();

			System.out.println(String.join("\t", memberId, file.getPath(), name));

			ScriptClient client = new ScriptClientImpl();
			ScriptModel model = client.getSelectScript(memberId, savePath, name);

			System.out.println(model.getPodmanName());

			EnvSettingDialog dialog = new EnvSettingDialog(Display.getCurrent().getActiveShell(), file.getName(),
					model.getPodmanName());

			if (dialog.open() == Window.OK) {

				String podmanRawID = dialog.getPodmanRawID();

				PodmanAPIClient podmanAPI = new PodmanAPIClientImpl();
				PodmanModel podmanModel = podmanAPI.getSelectPodman(podmanRawID);

				model.setPodmanID(podmanModel.getRawID());
				model.setPodmanImgID(podmanModel.getImageID());
				model.setPodmanName(podmanModel.getName());
				model.setPodmanRepo(podmanModel.getRepo());
				model.setPodmanTag(podmanModel.getTag());
				model.setTimeStamp(utils.getCurruntTime());
				model.setModifyDate(utils.getDate());

				int res = client.updateScript(model.getRawID(), model);

				System.out.println(res);

				if (res == 1) {
					MessageDialog.openInformation(shell, "Setting Complete", "Pordman environment setup is complete.");
				} else {
					MessageDialog.openError(shell, "Setting Error", "Failure during podman setup. please try again.");
				}
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof FileModel) {

			FileModel file = (FileModel) object;

			if (file.isIsFile()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

}
