package org.kobic.bioexpress.rcp.handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class UpdateHandler {
	final static Logger logger = Logger.getLogger(UpdateHandler.class);

	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_PROPERTIES);

	@Execute
	public void execute(final IProvisioningAgent agent, final Shell parent, final UISynchronize sync,
			final IWorkbench workbench) {

		// BioConstants.getRepository() simply returns the path to my repository - tried
		// both, local directory and online on my webhost
		String REPOSITORY_LOC = props.getProperty("closha.workbench.update.url");
		
		@SuppressWarnings("unused")
		String VERSION = props.getProperty("closha.workbench.version");

		Job j = new Job("Update Job") {
			private boolean doInstall = false;

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				/* 1. Prepare update plumbing */
				final ProvisioningSession session = new ProvisioningSession(agent);
				final UpdateOperation operation = new UpdateOperation(session);

				// create uri - optional?
				URI uri = null;
				try {
					uri = new URI(REPOSITORY_LOC);
					logger.info("Repository Location: " + uri);
				} catch (final URISyntaxException e) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(parent, "URI Invalid Error", e.getMessage());
						}
					});
					return Status.CANCEL_STATUS;
				}
				// set location of artifact and metadata repo
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });
				logger.info("Searching for Bio-Express workbench update @ " + REPOSITORY_LOC);
				// end of optional

				/* 2. check for updates */

				// run update checks causing I/O
				SubMonitor sub = SubMonitor.convert(monitor, "Checking for Bio-Express workbench updates...", 200);
				IStatus status = operation.resolveModal(sub.newChild(100));
				logger.info("status -> " + status);

				// failed to find updates (inform user and exit)
				switch (status.getCode()) {
				case UpdateOperation.STATUS_NOTHING_TO_UPDATE:
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openInformation(parent, "No Update Information", String.format(
									"There are no updates for the currently installed Bio-Express workbench version '%s'",
									REPOSITORY_LOC));
						}
					});
					return Status.CANCEL_STATUS;

				default:
					/*
					 * 3. Ask if updates should be installed and run installation
					 */

					// found updates, ask user if to install?
					logger.info("found updates -> ask user to install");
					if (status.isOK() && status.getSeverity() != IStatus.ERROR) {
						String updates = "";
						Update[] possibleUpdates = operation.getPossibleUpdates();
						for (Update update : possibleUpdates) {
							updates += update + "\n";
						}
						final String fUpdates = updates;
						sync.syncExec(new Runnable() {
							@Override
							public void run() {
								doInstall = MessageDialog.openQuestion(parent,
										"There is an update to a new version. Do you want to proceed with the update?",
										fUpdates);
							}
						});
					}
					break;
				}
				// start installation
				if (doInstall) {
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);
					// updates cannot run from within Eclipse IDE!!!
					if (provisioningJob == null) {
						logger.error("Running update from within Bio-Express Workbench? This won't work!!!");
						throw new NullPointerException();
					}

					// register a job change listener to track
					// installation progress and notify user upon success
					provisioningJob.addJobChangeListener(new JobChangeAdapter() {
						@Override
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								sync.syncExec(new Runnable() {

									@Override
									public void run() {
										boolean restart = MessageDialog.openQuestion(parent, "Updates Completed",
												"Updates completed successfully. Do you want to restart?");
										if (restart) {
											workbench.restart();
										}
									}
								});

							}
							super.done(event);
						}
					});

					provisioningJob.schedule();
				}
				return Status.OK_STATUS;
			}
		};
		j.schedule();
	}
}