package org.kobic.bioexpress.rcp.monitor.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.kobic.bioexpress.channel.client.cluster.ClusterClient;
import org.kobic.bioexpress.channel.client.cluster.ClusterClientImpl;
import org.kobic.bioexpress.model.cluster.GridHostModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.monitor.provider.ClusterHostTableViewCompator;
import org.kobic.bioexpress.rcp.monitor.provider.ClusterHostTableViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;
import org.eclipse.swt.widgets.TableColumn;

@SuppressWarnings("unused")
public class ClusterHostMonitorView {

	final static Logger logger = Logger.getLogger(ClusterHostMonitorView.class);

	@Inject
	private MDirtyable dirty;

	@Inject
	private UISynchronize sync;

	private TableViewer treeViewer;

	@PostConstruct
	public void createComposite(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		treeViewer.setContentProvider(ArrayContentProvider.getInstance());
		treeViewer.setLabelProvider(new ClusterHostTableViewLabelProvider());
		treeViewer.setComparator(new ClusterHostTableViewCompator());

		Table table = treeViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		TableColumn hostCol = new TableColumn(table, SWT.LEFT);
		hostCol.setText("Host");
		hostCol.setWidth(200);
		hostCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));

		TableColumn cpuCol = new TableColumn(table, SWT.LEFT);
		cpuCol.setText("CPU");
		cpuCol.setWidth(200);
		cpuCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));
		
		TableColumn loadCol = new TableColumn(table, SWT.LEFT);
		loadCol.setText("Load");
		loadCol.setWidth(200);
		loadCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));

		TableColumn memTotalCol = new TableColumn(table, SWT.LEFT);
		memTotalCol.setText("Memory Total");
		memTotalCol.setWidth(200);
		memTotalCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));
		
		TableColumn memUseCol = new TableColumn(table, SWT.LEFT);
		memUseCol.setText("Memory Use");
		memUseCol.setWidth(200);
		memUseCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));

		TableColumn swapTotalCol = new TableColumn(table, SWT.LEFT);
		swapTotalCol.setText("Swap Total");
		swapTotalCol.setWidth(200);
		swapTotalCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));

		TableColumn swapUseCol = new TableColumn(table, SWT.LEFT);
		swapUseCol.setText("Swap Use");
		swapUseCol.setWidth(200);
		swapUseCol.addSelectionListener(new TableViewColumnSelectionListener(this.treeViewer));
		
		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
		setTableViewerDataBind();
	}

	private void event() {

	}

	@Focus
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	private void setTableViewerDataBind() {

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String memberID = Activator.getMember().getMemberId();

				ClusterClient clusterClient = new ClusterClientImpl();
				List<GridHostModel> compute = clusterClient.getGridHostInfom();

				treeViewer.setInput(compute);
			}
		});
	}
	
	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.CLUSTER_DATA_RELOAD_EVENT_BUS_NAME) String uid) {
		setTableViewerDataBind();
	}
}