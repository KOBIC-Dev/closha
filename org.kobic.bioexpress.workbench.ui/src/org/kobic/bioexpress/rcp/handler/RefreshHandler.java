package org.kobic.bioexpress.rcp.handler;

import java.rmi.server.UID;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.constant.Constants;

public class RefreshHandler {

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void reload(IWorkbench iWorkbench, Shell shell, @Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		System.out.println(activePart.getLabel());
		System.out.println(activePart.getElementId());

		if (activePart.getElementId().equals(Constants.PROGRAM_VIEW_ID)) {
			iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.PIPELINE_VIEW_ID)) {
			iEventBroker.send(Constants.PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.WORKSPACE_VIEW_ID)) {
			iEventBroker.send(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.GBOX_BROWSER_PART_ID)) {
			iEventBroker.send(Constants.GBOX_TREE_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.HOST_MONITOR_VIEW_ID)) {
			iEventBroker.send(Constants.CLUSTER_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.LOG_VIEW_ID)) {
			iEventBroker.send(Constants.LOG_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.SCRIPT_VIEW_ID)) {
			iEventBroker.send(Constants.SCRIPT_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.GBOX_TABLE_VIEW_ID)) {
			iEventBroker.send(Constants.GBOX_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.FILE_TABLE_VIEW_ID)) {
			iEventBroker.send(Constants.FILE_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.RAONWIZ_VIEW_ID)) {
			iEventBroker.send(Constants.RAONWIZ_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.GBOX_LOCAL_TREE_PART_ID)) {
			iEventBroker.send(Constants.FILE_TREE_DATA_RELOAD_EVENT_BUS_NAME + "2", new UID().toString());

		} else if (activePart.getElementId().equals(Constants.CLOSHA_FILE_TREE_VIEW_ID)) {
			iEventBroker.send(Constants.CLOSHA_FILE_TREE_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.CLOSHA_GBOX_TREE_VIEW_ID)) {
			iEventBroker.send(Constants.CLOSHA_GBOX_TREE_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.PIPELINE_DETAIL_VIEW_ID)) {
			iEventBroker.send(Constants.PIPELINE_DETAIL_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.DETAIL_DATA_VIEW_ID)) {
			iEventBroker.send(Constants.DETAIL_DATA_REFRESH_EVENT_BUS_NAME, new UID().toString());

		} else if (activePart.getElementId().equals(Constants.SCRIPT_TASK_HISTORY_VIEW_ID)) {

			iEventBroker.send(Constants.SCRIPT_TASK_HISTORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
		} else if (activePart.getElementId().equals(Constants.INSTALLED_PROGRAM_VIEW_ID)) {

			iEventBroker.send(Constants.INSTALLED_PROGRAM_REFRESH_EVENT_BUS_NAME, new UID().toString());
		} else if (activePart.getElementId().equals(Constants.PODMAN_VIEW_ID)) {

			iEventBroker.send(Constants.PODMAN_DATA_RELOAD_EVENT_BUS_NAME, new UID().toString());
		}
	}
}
