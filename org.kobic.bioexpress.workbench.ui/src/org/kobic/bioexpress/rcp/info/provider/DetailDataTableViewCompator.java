package org.kobic.bioexpress.rcp.info.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.common.ItemModel;

public class DetailDataTableViewCompator extends ViewerComparator {


	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		ItemModel d1 = (ItemModel) e1;
		ItemModel d2 = (ItemModel) e2;

		TableViewer tableViewer = (TableViewer) viewer;

		TableColumn tableColumn = tableViewer.getTable().getSortColumn();

		String columnText = null;

		if (tableColumn == null) {
			return 0;
			
		} else {
			columnText = tableColumn.getText();
		}

		if (columnText.equals("Name")) {
			if (tableViewer.getTable().getSortDirection() == SWT.DOWN) {
				return d1.getKey().compareTo(d2.getKey());
			} else {
				return d1.getKey().compareTo(d1.getKey());
			}
		} else if (columnText.equals("Value")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getValue().compareTo(d2.getValue());
			} else {
				return d2.getValue().compareTo(d1.getValue());
			}
		} else {
			return 0;
		}
	}
}