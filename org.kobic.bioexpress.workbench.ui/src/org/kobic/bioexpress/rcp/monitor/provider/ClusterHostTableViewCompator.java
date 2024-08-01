package org.kobic.bioexpress.rcp.monitor.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.cluster.GridHostModel;

public class ClusterHostTableViewCompator extends ViewerComparator {

	private String defaultColumnText = "Host";

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		GridHostModel d1 = (GridHostModel) e1;
		GridHostModel d2 = (GridHostModel) e2;

		TableViewer tableViewer = (TableViewer) viewer;

		TableColumn tableColumn = tableViewer.getTable().getSortColumn();

		String columnText = null;

		if (tableColumn == null) {
			columnText = defaultColumnText;
		} else {
			columnText = tableColumn.getText();
		}

		if (columnText.equals("Host")) {
			if (tableViewer.getTable().getSortDirection() == SWT.DOWN) {
				return d2.getHost().compareTo(d1.getHost());
			} else {
				return d1.getHost().compareTo(d2.getHost());
			}
		} else if (columnText.equals("CPU")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getCpu().compareTo(d2.getCpu());
			} else {
				return d2.getCpu().compareTo(d1.getCpu());
			}
		} else if (columnText.equals("Load")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getLoad().compareTo(d2.getLoad());
			} else {
				return d2.getLoad().compareTo(d1.getLoad());
			}
		} else if (columnText.equals("Memory Total")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getMemTo().compareTo(d2.getMemTo());
			} else {
				return d2.getMemTo().compareTo(d1.getMemTo());
			}
		} else if (columnText.equals("Memory Use")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getMemUse().compareTo(d2.getMemUse());
			} else {
				return d2.getMemUse().compareTo(d1.getMemUse());
			}
		} else if (columnText.equals("Swap Total")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getSwapTo().compareTo(d2.getSwapTo());
			} else {
				return d2.getSwapTo().compareTo(d1.getSwapTo());
			}
		} else if (columnText.equals("Swap Use")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getSwapUse().compareTo(d2.getSwapUse());
			} else {
				return d2.getSwapUse().compareTo(d1.getSwapUse());
			}
		} else {
			return 0;
		}
	}
}