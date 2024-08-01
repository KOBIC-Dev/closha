package org.kobic.bioexpress.rcp.gbox.handler;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.Utils;

import raonwiz.k.livepass.client.KLivepassClientEvent;
import raonwiz.k.livepass.client.KLivepassClientEventVo;

public class GBoxTransferHandler extends Action {

	final static Logger logger = Logger.getLogger(GBoxTransferHandler.class);

	private IEventBroker iEventBroker;

	private List<FileModel> source;
	private String target;
	private String type;
	private IProgressMonitor monitor;
	private Composite parent;

	public GBoxTransferHandler(Composite parent, IEventBroker iEventBroker, IProgressMonitor monitor,
			List<FileModel> source, String target, String type) {
		this.source = source;
		this.target = target;
		this.type = type;
		this.monitor = monitor;
		this.iEventBroker = iEventBroker;
		this.parent = parent;
	}

	@Override
	public void run() {

		boolean overwrite = false;

		String parentPath = source.get(0).getParentPath() + "/";

		String[] name = new String[source.size()];

		for (int i = 0; i < source.size(); i++) {
			FileModel fileModel = source.get(i);
			name[i] = fileModel.getName();
			System.out.println(i + ")." + name[i]);
		}

		logger.info("Start transfer GBox data");

		TransferFutureHandler handler = new TransferFutureHandler(name, monitor);

		KLivepassClientEvent event = new KLivepassEventHandler().setHandler(handler);

		/**
		 * 이 부분 정리하기
		 */
		System.out.println(type);
		System.out.println(parentPath.replace("\\", "/"));
		System.out.println(target.replace("\\", "/"));
		System.out.println(name);

		/**
		 * Mbps 단위로 입력, 0:무제한 true: ClientBandWidth 우선적용, false: 모니터링과 비교 후 적은 Bandwidth
		 * 적용
		 */

//		boolean isForce = Activator.isEnableLimit();
		boolean isForce = false;
		String speed;

		if (Activator.isEnableLimit()) {
			if (type.equalsIgnoreCase(Constants.GBOX_RAPIDANT_TRANSFER_SEND)) {
				speed = String.valueOf(8 * Activator.getUP_MAX_VALUE());
			} else {
				speed = String.valueOf(8 * Activator.getDOWN_MAX_VALUE());
			}
		} else {
			speed = "0";
		}

		System.out.println("**************************************");
		System.out.println("transfer speed: " + speed);
		System.out.println("transfer type: " + type);
		System.out.println("transfer limit: " + Activator.isEnableLimit());
		System.out.println("transfer overwrite: " + overwrite);
		System.out.println("**************************************");
		
		String userID = Activator.getMember().getMemberId();

		Activator.getRapidantService().transfer(userID, parentPath.replace("\\", "/"), name, target.replace("\\", "/"),
				type, overwrite, speed, isForce, event);

		logger.info("GBox Data Upload Complete");
	}

	class TransferFutureHandler {

		final Logger logger = Logger.getLogger(TransferFutureHandler.class);

		private String[] name;

		private IProgressMonitor monitor;

		private int N_PROGRESS = 0;
		private int PROGRESS = 0;
		private int P_PROGRESS = 0;

		public TransferFutureHandler(String[] name, IProgressMonitor monitor) {
			this.name = name;
			this.monitor = monitor;
		}

		public void configured(KLivepassClientEventVo event) {
			logger.info("Transfer configured: " + event);
		}

		public void started(KLivepassClientEventVo event) {

			int workload = 100;
			monitor.beginTask("Start high-speed transfer of data files.", workload);

			logger.info("Transfer started: " + event + "\n전송 파일 목록: " + String.join(",", name) + "\n파일 전송을 시작합니다.");
			logger.info("List of transferred files:" + String.join(",", name) + "\nStart the file transfer.");
		}

		public void progressed(KLivepassClientEventVo event) {

			N_PROGRESS = (int) (event.getProgressedRatio());

			logger.info(N_PROGRESS);

			if (N_PROGRESS != P_PROGRESS || P_PROGRESS == 0) {

				PROGRESS = N_PROGRESS - P_PROGRESS;
				P_PROGRESS = N_PROGRESS;

				logger.info("Current overall progress: " + N_PROGRESS + "\t" + "Previous progress: " + P_PROGRESS
						+ "\tCurrent transfer progress==>" + PROGRESS);

				monitor.setTaskName(event.getSourceFiles().toString());
				monitor.subTask("Transfer Progress.." + Utils.getInstance().getNumberFormat(event.getProgressedRatio())
						+ "%" + "(Max Speed: "
						+ Utils.getInstance().humanReadableByteCount((long) event.getTransferSpeed(), true) + ")"
						+ "(Transfer Speed: "
						+ Utils.getInstance().humanReadableByteCount((long) event.getTransferSpeed(), true) + ")" + "("
						+ Utils.getInstance().humanReadableByteCount((long) event.getTransferredSize(), true) + "/"
						+ Utils.getInstance().humanReadableByteCount((long) event.getTotalSize(), true) + ")");

				monitor.worked(PROGRESS);
			}

			if (monitor.isCanceled()) {

				System.out.println("request for k-livepass transferring cancel event..");

				boolean res = Activator.getRapidantService().stop();

				System.out.println("k-livepass transferring stop: [" + res + "]");
			}

			logger.info("[Info]\ttMax transfer speed " + (int) event.getMaxSpeed() + "MB\tCurrent transfer speed: "
					+ (int) event.getTransferSpeed() / 1024 / 1024 + " MB\tTransfer status: "
					+ event.getTransferredSize() / 1024 / 1024 + " MB/" + event.getTotalSize() / 1024 / 1024
					+ "MB Transfer\t " + Utils.getInstance().getNumberFormat(event.getProgressedRatio())
					+ "% Transfer");
		}

		public void finished(KLivepassClientEventVo event) {

			if (event.getIsSuccessful()) {

				if (type.equals(org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_TRANSFER_SEND)) {
					iEventBroker.send(Constants.GBOX_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());
				} else {
					iEventBroker.send(Constants.FILE_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());
				}

				monitor.worked(100);
				monitor.done();

				logger.info("[Info]\tMax transfer speed: " + (int) event.getTransferSpeed()
						+ "MB\tAverage transfer speed: " + (int) event.getTransferSpeed() / 1024 / 1024
						+ " MB\tTransfer status: " + +event.getTotalSize() / 1024 / 1024 + " MB/"
						+ event.getTotalSize() / 1024 / 1024
						+ "MB Transfer\t100% Transfer\nFile transfer has been completed normally..\nComplete file list: "
						+ String.join(",", name));

			} else {
				logger.fatal("\nAn error occurred during file transfe. Please try again.\nFile list: "
						+ String.join(",", name));
			}
		}

		public void stop(KLivepassClientEventVo event) {
			/**
			 * 전송 취소 이후 프로세스 구현
			 */

			List<String> names = new ArrayList<String>();

			source.forEach(s -> {
				names.add(s.getName());
			});

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					MessageDialog.openConfirm(parent.getShell(), "GBox Data transfer Cancel Confirm",
							"Data transmission has ended normally.\n" + String.join("\n", names));
				}
			});

		}
	}
}