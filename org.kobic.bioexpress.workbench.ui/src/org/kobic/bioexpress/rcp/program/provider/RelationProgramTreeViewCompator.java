package org.kobic.bioexpress.rcp.program.provider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;
import org.kobic.bioexpress.model.program.ProgramDataModel;

public class RelationProgramTreeViewCompator extends ViewerComparator {

	private String defaultColumnText = "Name";

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		if (e1 instanceof ProgramDataModel && e2 instanceof ProgramDataModel) {

			ProgramDataModel d1 = (ProgramDataModel) e1;
			ProgramDataModel d2 = (ProgramDataModel) e2;

			TreeViewer treeViewer = (TreeViewer) viewer;

			TreeColumn treeColumn = treeViewer.getTree().getSortColumn();

			String columnText = null;

			if (treeColumn == null) {
				columnText = defaultColumnText;
			} else {
				columnText = treeColumn.getText();
			}
			if (columnText.equals("Name")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getProgramName().compareTo(d2.getProgramName());
				} else {
					return d2.getProgramName().compareTo(d1.getProgramName());
				}
			} else if (columnText.equals("Description")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getProgramDesc().compareTo(d2.getProgramDesc());
				} else {
					return d2.getProgramDesc().compareTo(d1.getProgramDesc());
				}
			} else if (columnText.equals("Category")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getSubCategoryID().compareTo(d2.getSubCategoryID());
				} else {
					return d2.getSubCategoryID().compareTo(d1.getSubCategoryID());
				}
			} else if (columnText.equals("Register Date")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getRegistedDate().compareTo(d2.getRegistedDate());
				} else {
					return d2.getRegistedDate().compareTo(d1.getRegistedDate());
				}
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}