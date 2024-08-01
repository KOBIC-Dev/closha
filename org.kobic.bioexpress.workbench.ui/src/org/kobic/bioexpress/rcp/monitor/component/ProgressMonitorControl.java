package org.kobic.bioexpress.rcp.monitor.component;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ProgressMonitorControl {

	private ProgressBar progressBar;
	private GobalProgressMonitor monitor;

	private Composite parent;
	private Composite composite;

	private Button closeButton;

	private Label progressType;
	private Label titleLabel;
	private Label messageLabel;

	private GridLayout compostieGridLayout;

	private String type;
	private String title;
	
	private final UISynchronize sync;

	@Inject
	public ProgressMonitorControl(Composite parent, UISynchronize sync, String type, String title) {
		this.parent = parent;
		this.type = type;
		this.title = title;
		this.sync = Objects.requireNonNull(sync);
	}

	public void setMonitorMessage(String title, String message) {

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				titleLabel.setText(title);
				messageLabel.setText(message);
			}
		});
	}

	@PostConstruct
	public void createControls() {

		this.composite = new Composite(this.parent, SWT.None);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		compostieGridLayout = new GridLayout(3, false);
		composite.setLayout(compostieGridLayout);

		this.progressType = new Label(composite, SWT.NONE);
		GridData typeGridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 3);
		progressType.setLayoutData(typeGridData);

		if (type.equals(org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_TRANSFER_SEND)) {
			progressType.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.TRANSFER_MONITOR_UPLOAD_ICON));
		} else if (type.equals(org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_TRANSFER_RECEIVE)) {
			progressType.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.TRANSFER_MONITOR_DOWNLOAD_ICON));
		} else {
			progressType.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, "icons/flatLayout.png"));
		}

		this.titleLabel = new Label(composite, SWT.NONE);
		this.titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.titleLabel.setText(title);

		this.closeButton = new Button(composite, SWT.NONE);
		this.closeButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3));
		this.closeButton.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.TRANSFER_MONITOR_CLOSE_ICON));

		this.progressBar = new ProgressBar(composite, SWT.SMOOTH);
		this.progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.messageLabel = new Label(composite, SWT.None);
		this.messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.messageLabel.setSize(300, 100);
		this.messageLabel.setText(Constants.DEFAULT_MESSAGE);

		this.parent.pack();

		monitor = new GobalProgressMonitor(progressBar, sync);

		Job.getJobManager().setProgressProvider(new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(Job job) {
				return monitor.addJob(job);
			}
		});
	}
}