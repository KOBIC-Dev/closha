package org.kobic.bioexpress.rcp.gbox.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.utils.Utils;

public class GBoxTableViewCompator extends ViewerComparator {

	private String defaultColumnText = "Name";
	
	private Utils utils = Utils.getInstance();

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		FileModel fm1 = (FileModel) e1;
		FileModel fm2 = (FileModel) e2;
		
		String size1 = utils.humanReadableByteCount(fm1.getSize(), true);
		String size2 = utils.humanReadableByteCount(fm2.getSize(), true);
		
		String type1 = fm1.getExtension().length() == 0 ? "NA" : fm1.getExtension();
		String type2 = fm2.getExtension().length() == 0 ? "NA" : fm2.getExtension();

//		String date1 = utils.getDateTime(Long.parseLong(fm1.getCreateDate()));
//		String date2 = utils.getDateTime(Long.parseLong(fm2.getCreateDate()));
		
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
				return type1.compareTo(type2);
			} else {
				return type2.compareTo(type1);
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