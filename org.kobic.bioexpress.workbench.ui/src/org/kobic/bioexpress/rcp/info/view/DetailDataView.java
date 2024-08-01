package org.kobic.bioexpress.rcp.info.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.common.ItemModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.info.provider.DataTableViewLabelProvider;
import org.kobic.bioexpress.rcp.info.provider.DetailDataTableViewCompator;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;

public class DetailDataView {

	final static Logger logger = Logger.getLogger(DetailDataView.class);

	private TableViewer viewer;

	@Inject
	private MDirtyable dirty;

	@SuppressWarnings("unused")
	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_PROPERTIES);

	@PostConstruct
	public void createComposite(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new DataTableViewLabelProvider());
		viewer.setComparator(new DetailDataTableViewCompator());
		
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		TableColumn nameCol = new TableColumn(table, SWT.LEFT);
		nameCol.setText("Name");
		nameCol.setWidth(300);
		nameCol.addSelectionListener(new TableViewColumnSelectionListener(this.viewer));

		TableColumn valueCol = new TableColumn(table, SWT.LEFT);
		valueCol.setText("Value");
		valueCol.setWidth(500);
		valueCol.addSelectionListener(new TableViewColumnSelectionListener(this.viewer));

	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic("_TEST_011") Map<String, Object> map) {

		List<ItemModel> list = new ArrayList<ItemModel>();

		for (Map.Entry<String, Object> elem : map.entrySet()) {
			ItemModel item = new ItemModel();
			item.setKey(elem.getKey());
			item.setValue(String.valueOf(elem.getValue()));

			list.add(item);
		}

		viewer.setInput(list);
	}
	
	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.DETAIL_DATA_REFRESH_EVENT_BUS_NAME) String uid) {
		System.out.println("infomation refresh");
		
	}
}
