package org.kobic.bioexpress.rcp.monitor.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.kobic.bioexpress.model.gbox.TransferLogModel;
import org.kobic.bioexpress.model.rcp.MonitorMessageModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.monitor.component.ProgressMonitorControl;

public class GBoxTransferMonitorView {

	@Inject
	private MDirtyable dirty;

	@Inject
	private UISynchronize sync;

	private Composite composite;

	@PostConstruct
	public void createComposite(Composite parent) {

		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		scrolledComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		scrolledComposite.setExpandHorizontal(true);

		this.composite = new Composite(scrolledComposite, SWT.NONE);
		this.composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		this.composite.setLayout(new GridLayout());
		this.composite.pack();

		scrolledComposite.setContent(composite);
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	public Composite getComposite() {
		return this.composite;
	}

	private ProgressMonitorControl progressMonitorControl;

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(
			@UIEventTopic(Constants.GBOX_TRANSFER_MONITOR_INIT_EVENT_BUS_NAME) String type) {

		progressMonitorControl = new ProgressMonitorControl(this.getComposite(), sync, type, "GBox transfer file");
		progressMonitorControl.createControls();
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(
			@UIEventTopic(Constants.GBOX_TRANSFER_MONITOR_STATUS_EVENT_BUS_NAME) TransferLogModel transferLogModel) {

		String title = transferLogModel.getName() + " (" + transferLogModel.getPercent() + ")";
		String message = String.format(Constants.TRANSFER_SPEED_MONITOR_FORMAT, transferLogModel.getTransferSize(),
				transferLogModel.getTotalSize(), transferLogModel.getSpeed());

		progressMonitorControl.setMonitorMessage(title, message);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(
			@UIEventTopic(Constants.GBOX_TRANSFER_MONITOR_STATUS_EVENT_BUS_NAME) MonitorMessageModel message) {
		progressMonitorControl.setMonitorMessage(message.getTitle(), message.getMessage());
	}
}