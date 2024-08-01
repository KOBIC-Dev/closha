package org.kobic.bioexpress.rcp.swt.listener;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.log.provider.LogTreeViewCompator;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineDetailTreeViewCompator;

public class TreeViewColumnSelectionListener implements SelectionListener {

	private int clickCount = 0;
	private TreeViewer treeViewer;
	private String element = null;

	public TreeViewColumnSelectionListener(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	public TreeViewColumnSelectionListener(TreeViewer treeViewer, String element) {
		this.treeViewer = treeViewer;
		this.element = element;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		if (element != null) {
			if (element.equals(Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID) && treeViewer.getComparator() == null) {
				treeViewer.setComparator(new PipelineDetailTreeViewCompator());
			} else if (element.equals(Constants.LOG_TREE_TABLE_VIEW_ID) && treeViewer.getComparator() == null) {
				treeViewer.setComparator(new LogTreeViewCompator());
			}
		}

		TreeColumn treeColumn = (TreeColumn) e.getSource();
		Tree tree = treeColumn.getParent();
		tree.setSortColumn(treeColumn);

		if (this.clickCount++ % 2 == 0) {
			tree.setSortDirection(SWT.DOWN);
		} else {
			tree.setSortDirection(SWT.UP);
		}
		this.treeViewer.refresh();
	}
}
