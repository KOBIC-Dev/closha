package org.kobic.bioexpress.rcp.script.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.task.ScriptTaskModel;

public class ScriptHistoryTableViewCompator extends ViewerComparator {

	private String defaultColumnText = "Start Time";

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		ScriptTaskModel d1 = (ScriptTaskModel) e1;
		ScriptTaskModel d2 = (ScriptTaskModel) e2;

		TableViewer tableViewer = (TableViewer) viewer;

		TableColumn tableColumn = tableViewer.getTable().getSortColumn();

		String columnText = null;

		if (tableColumn == null) {
			columnText = defaultColumnText;
		} else {
			columnText = tableColumn.getText();
		}

		if (columnText.equals("Status")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getStatus().compareTo(d2.getStatus());
			} else {
				return d2.getStatus().compareTo(d1.getStatus());
			}
		} else if (columnText.equals("Type")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getLanguage().compareTo(d2.getLanguage());
			} else {
				return d2.getLanguage().compareTo(d1.getLanguage());
			}
		} else if (columnText.equals("Script Name")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getScriptName().compareTo(d2.getScriptName());
			} else {
				return d2.getScriptName().compareTo(d1.getScriptName());
			}
		} else if (columnText.equals("Job Number")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getJobID().compareTo(d2.getJobID());
			} else {
				return d2.getJobID().compareTo(d1.getJobID());
			}
		} else if (columnText.equals("Start Time")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getSubmissionTime().compareTo(d2.getSubmissionTime());
			} else {
				return d2.getSubmissionTime().compareTo(d1.getSubmissionTime());
			}
		} else if (columnText.equals("End Time")) {
			if (tableViewer.getTable().getSortDirection() == SWT.UP) {
				return d1.getFinalCompleteTime().compareTo(d2.getFinalCompleteTime());
			} else {
				return d2.getFinalCompleteTime().compareTo(d1.getFinalCompleteTime());
			}
		} else {
			return 0;
		}
	}
}