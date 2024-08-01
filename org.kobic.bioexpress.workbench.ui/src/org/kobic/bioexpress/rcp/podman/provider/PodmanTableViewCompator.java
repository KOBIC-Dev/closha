package org.kobic.bioexpress.rcp.podman.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.podman.PodmanModel;

public class PodmanTableViewCompator extends ViewerComparator {

	private String defaultColumnText = "Name";

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		PodmanModel d1 = (PodmanModel) e1;
		PodmanModel d2 = (PodmanModel) e2;

		TableViewer tableViewer = (TableViewer) viewer;

		TableColumn tableColumn = tableViewer.getTable().getSortColumn();

		String columnText = null;

		if (tableColumn == null) {
			columnText = defaultColumnText;
		} else {
			columnText = tableColumn.getText();
		}

		if (columnText.equals("Type")) {
			if (tableViewer.getTable().getSortDirection() == SWT.DOWN) {
				return d2.getPodmanType().compareTo(d1.getPodmanType());
			} else {
				return d1.getPodmanType().compareTo(d2.getPodmanType());
			}
		} else if (columnText.equals("Name")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getName().compareTo(d2.getName());
			} else {
				return d2.getName().compareTo(d1.getName());
			}
		} else if (columnText.equals("Tag")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getTag().compareTo(d2.getTag());
			} else {
				return d2.getTag().compareTo(d1.getTag());
			}
		} else {
			return 0;
		}
	}
}