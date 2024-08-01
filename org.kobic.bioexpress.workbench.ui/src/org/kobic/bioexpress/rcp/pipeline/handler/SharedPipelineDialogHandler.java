package org.kobic.bioexpress.rcp.pipeline.handler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.pipeline.ShareModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.dialog.SharePipelineDialog;

public class SharedPipelineDialogHandler {

	final static Logger logger = Logger.getLogger(SharedPipelineDialogHandler.class);

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

		if (object instanceof PipelineDataModel) {

			PipelineDataModel pipelineData = (PipelineDataModel) object;

			SharePipelineDialog dialog = new SharePipelineDialog(shell, pipelineData.getRawID());
			if (dialog.open() == Window.OK) {

				List<ShareModel> share = dialog.getShare();

				String pipelineName = pipelineData.getPipelineName();

				MPart mPart = ePartService.findPart(Constants.PROGRESS_VIEW_ID);

				ePartService.showPart(mPart, PartState.ACTIVATE);
				
				Job job = new Job(pipelineName + " Starting to share pipeline.") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						// TODO Auto-generated method stub

						monitor.beginTask(pipelineName, share.size());

						try {

							PipelineClient pipelineClient = new PipelineClientImpl();

							for (int i = 0; i < share.size(); i++) {

								if (monitor.isCanceled())
									return Status.CANCEL_STATUS;

								ShareModel shareModel = share.get(i);

								monitor.subTask(shareModel.getMemberID() + " Sharing a pipeline in progress.");

								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								monitor.worked(1);

								try {
									TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);
									TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								try {
									PipelineModel pipelineModel = dialog.getPipeline();
									pipelineModel.getPipelineData().setOwner(shareModel.getMemberID());
									pipelineModel.getPipelineData().setIsShared(true);

									pipelineClient.insertPipeline(pipelineModel);

								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}

						} finally {
							monitor.done();
						}

						return Status.OK_STATUS;
					}
				};

				job.schedule();
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {

		if (object instanceof PipelineDataModel) {
			return true;
		} else {
			return false;
		}
	}
}
