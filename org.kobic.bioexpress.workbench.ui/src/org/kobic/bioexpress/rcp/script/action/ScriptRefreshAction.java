package org.kobic.bioexpress.rcp.script.action;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.swt.component.CreateImageDescriptor;

@SuppressWarnings("unused")
public class ScriptRefreshAction extends Action {

	private IEventBroker iEventBroker;
	private Composite parent;
	private TreeViewer viewer;

	public ScriptRefreshAction(IEventBroker iEventBroker, Composite parent, TreeViewer viewer) {
		this.iEventBroker = iEventBroker;
		this.parent = parent;
		this.viewer = viewer;

		setText("Refresh");
		setToolTipText("Refesh on script viwer");
		setImageDescriptor(
				CreateImageDescriptor.getInstance().getImageDescriptor(Constants.PARAMETER_DIALOG_REFRESH_ICON));
	}

	@Override
	public void run() {

	}
}