package org.kobic.bioexpress.rcp.monitor.component;

import java.util.Objects;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class GobalProgressMonitor implements IProgressMonitor {

	private long runningTasks = 0L;

	private final UISynchronize sync;

	private ProgressBar progressBar;

	@Inject
	public GobalProgressMonitor(ProgressBar progressBar, UISynchronize sync) {
		this.progressBar = progressBar;
		this.sync = Objects.requireNonNull(sync);
	}

	@Override
	public void beginTask(final String name, final int totalWork) {

		sync.syncExec(new Runnable() {

			@Override
			public void run() {
				if (runningTasks <= 0) {
					// --- no task is running at the moment ---
					progressBar.setSelection(0);
					progressBar.setMaximum(totalWork);

				} else {
					// --- other tasks are running ---
					progressBar.setMaximum(progressBar.getMaximum() + totalWork);
				}

				runningTasks++;
				progressBar.setToolTipText("Currently running: " + runningTasks + "\nLast task: " + name);
			}
		});
	}

	@Override
	public void worked(final int work) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setSelection(progressBar.getSelection() + work);

				System.out.println(progressBar.getSelection() + work);
			}
		});
	}

	public IProgressMonitor addJob(Job job) {
		if (job != null) {
			job.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					sync.syncExec(new Runnable() {

						@Override
						public void run() {
							runningTasks--;
							if (runningTasks > 0) {
								// --- some tasks are still running ---
								progressBar.setToolTipText("Currently running: " + runningTasks);

							} else {
								// --- all tasks are done (a reset of selection could also be done) ---
								progressBar.setToolTipText("No background progress running.");
							}
						}
					});

					// clean-up
					event.getJob().removeJobChangeListener(this);
				}
			});
		}
		return this;
	}

	@Override
	public void done() {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalWorked(double work) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCanceled(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subTask(String name) {
		// TODO Auto-generated method stub

	}
}