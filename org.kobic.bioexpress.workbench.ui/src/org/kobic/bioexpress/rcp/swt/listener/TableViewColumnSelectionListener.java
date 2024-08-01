package org.kobic.bioexpress.rcp.swt.listener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewColumnSelectionListener implements SelectionListener {

	private int clickCount = 0;
	private TableViewer tableViewer;

	public TableViewColumnSelectionListener(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		TableColumn tableColumn = (TableColumn) e.getSource();
		tableColumn.getParent().setSortColumn(tableColumn);

		if (this.clickCount++ % 2 == 0) {
			tableColumn.getParent().setSortDirection(SWT.DOWN);
		} else {
			tableColumn.getParent().setSortDirection(SWT.UP);
		}
		this.tableViewer.refresh();
	}
}
