package org.kobic.bioexpress.rcp.podman.dialog;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClient;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClientImpl;
import org.kobic.bioexpress.channel.utils.Utils;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.podman.component.RegistPodmanImageDialogComponent;

public class RegistPodmanImageDialog extends TitleAreaDialog {

	final static Logger logger = Logger.getLogger(RegistPodmanImageDialog.class);

	private RegistPodmanImageDialogComponent podmanImageDialogComponent;

	@SuppressWarnings("unused")
	private Shell parent;

	private Utils utils;

	public RegistPodmanImageDialog(Shell parent) {
		super(parent);
		this.parent = parent;
		this.utils = Utils.getInstance();
	}

	@Override
	public void create() {
		super.create();

		setTitle("Regist Podman Image");
		setMessage(Constants.REGISTER_PODMAN_IMAGE_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PODMAN_REGISTER_DIALOG_TITLE_ICON));
		this.getShell().setText("Regist Podman Image");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		podmanImageDialogComponent = new RegistPodmanImageDialogComponent(composite);

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	@SuppressWarnings("unused")
	private void init() {

		String memberID = Activator.getMember().getMemberId();

		bind();
		event();
	}

	private void event() {

	}

	private void bind() {

	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 680);
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	private String imgType;
	private String imgName;
	private String imgID;
	private String repo;
	private String tag;
	private String status;
	private boolean isOfficial;
	private String desc;

	@Override
	protected void okPressed() {

		imgID = podmanImageDialogComponent.getIdText().getText();
		imgName = podmanImageDialogComponent.getImgNameText().getText();
		imgType = podmanImageDialogComponent.getImgTypeCombo().getText();
		repo = podmanImageDialogComponent.getRepoText().getText();
		tag = podmanImageDialogComponent.getTagText().getText();
		status = podmanImageDialogComponent.getStatusCombo().getText();
		desc = podmanImageDialogComponent.getDescriptionText().getText();
		isOfficial = podmanImageDialogComponent.getBtnOfficial().getSelection();

		System.out.println(isOfficial);

		if (podmanImageDialogComponent.getImgTypeCombo().getSelectionIndex() == 0) {
			imgType = Constants.PODMAN_BUILD_FILE_TYPE;
		} else {
			imgType = Constants.PODMAN_IMAGE_TYPE;
		}

		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InterruptedException {

					monitor.beginTask("Starting to build podman image.", IProgressMonitor.UNKNOWN);

					monitor.subTask("Entering information into the podman image.");

					TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

					monitor.subTask("Building podman image from podman file.");

					TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

					String memberID = Activator.getMember().getMemberId();

					/**
					 * podman register login
					 */

					PodmanModel podmanModel = new PodmanModel();
					podmanModel.setRawID(utils.getNewExeID());
					podmanModel.setPodmanID(utils.getNewExeID());
					podmanModel.setImageID(imgID);
					podmanModel.setName(imgName);
					podmanModel.setRepo(repo);
					podmanModel.setTag(tag);
					podmanModel.setSavePath(Constants.DEFAULT_VALUE);
					podmanModel.setCreateDate(utils.getDate());
					podmanModel.setModifyDate(utils.getDate());
					podmanModel.setMemberID(memberID);
					podmanModel.setPodmanType(imgType);
					podmanModel.setStatus(status);
					podmanModel.setDescription(desc);
					podmanModel.setTimeStamp(utils.getCurruntTime());
					podmanModel.setIsDelete(false);
					podmanModel.setIsOfficial(isOfficial);

					PodmanAPIClient api = new PodmanAPIClientImpl();
					int res = api.insertPodman(podmanModel);

					System.out.println(res);

					TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

					if (monitor.isCanceled()) {
						monitor.done();
						return;
					}

					monitor.done();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.okPressed();
	}
}