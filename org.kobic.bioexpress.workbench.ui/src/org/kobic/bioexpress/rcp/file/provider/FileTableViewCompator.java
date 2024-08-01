package org.kobic.bioexpress.rcp.file.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.utils.Utils;

public class FileTableViewCompator extends ViewerComparator {

	private String defaultColumnText = "Name";
	
	private Utils utils = Utils.getInstance();

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		FileModel fm1 = (FileModel) e1;
		FileModel fm2 = (FileModel) e2;
		
		String size1 = utils.humanReadableByteCount(fm1.getSize(), true);
		String size2 = utils.humanReadableByteCount(fm2.getSize(), true);
		
		TableViewer tableViewer = (TableViewer) viewer;

		TableColumn tableColumn = tableViewer.getTable().getSortColumn();

		String columnText = null;

		if (tableColumn == null) {
			columnText = defaultColumnText;
		} else {
			columnText = tableColumn.getText();
		}

		if (columnText.equals("Name")) {
			if (tableViewer.getTable().getSortDirection() == SWT.DOWN) {
				return fm2.getName().compareTo(fm1.getName());
			} else {
				return fm1.getName().compareTo(fm2.getName());
			}
		} else if (columnText.equals("Size")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return size1.compareTo(size2);
			} else {
				return size2.compareTo(size1);
			}
		} else if (columnText.equals("Type")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return fm1.getExtension().compareTo(fm2.getExtension());
			} else {
				return fm2.getExtension().compareTo(fm1.getExtension());
			}
		} else if (columnText.equals("Date")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return fm1.getCreateDate().compareTo(fm2.getCreateDate());
			} else {
				return fm2.getCreateDate().compareTo(fm1.getCreateDate());
			}
		} else {
			return 0;
		}
	}
}