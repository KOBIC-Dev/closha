package org.kobic.bioexpress.rcp.log.provider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.task.SubTaskModel;

public class LogTreeViewCompator extends ViewerComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		if (e1 instanceof NodeModel && e2 instanceof NodeModel) {

			NodeModel d1 = (NodeModel) e1;
			NodeModel d2 = (NodeModel) e2;

			TreeViewer treeViewer = (TreeViewer) viewer;

			TreeColumn treeColumn = treeViewer.getTree().getSortColumn();

			String columnText = null;

			if (treeColumn == null) {
				return 0;
			} else {
				columnText = treeColumn.getText();
			}

			if (columnText.equals("Name")) {
				if (treeViewer.getTree().getSortDirection() == SWT.DOWN) {
					return d2.getNodeData().getNodeName().compareTo(d1.getNodeData().getNodeName());
				} else {
					return d1.getNodeData().getNodeName().compareTo(d2.getNodeData().getNodeName());
				}
			} else if (columnText.equals("Description")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getNodeData().getProgramDesc().compareTo(d2.getNodeData().getProgramDesc());
				} else {
					return d2.getNodeData().getProgramDesc().compareTo(d1.getNodeData().getProgramDesc());
				}
			} else if (columnText.equals("Category")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getNodeData().getCategoryName().compareTo(d2.getNodeData().getCategoryName());
				} else {
					return d2.getNodeData().getCategoryName().compareTo(d1.getNodeData().getCategoryName());
				}
			} else if (columnText.equals("Version")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getNodeData().getVersion().compareTo(d2.getNodeData().getVersion());
				} else {
					return d2.getNodeData().getVersion().compareTo(d1.getNodeData().getVersion());
				}
			} else if (columnText.equals("Status")) {
				if (treeViewer.getTree().getSortDirection() == SWT.UP) {
					return d1.getNodeData().getStatus().compareTo(d2.getNodeData().getStatus());
				} else {
					return d2.getNodeData().getStatus().compareTo(d1.getNodeData().getStatus());
				}
			} else {
				return 0;
			}

		} else if (e1 instanceof SubTaskModel && e2 instanceof SubTaskModel) {
			SubTaskModel t1 = (SubTaskModel) e1;
			SubTaskModel t2 = (SubTaskModel) e2;
			return t2.getSubmissionTime().compareTo(t1.getSubmissionTime());

		} else {
			return 0;
		}

	}
}