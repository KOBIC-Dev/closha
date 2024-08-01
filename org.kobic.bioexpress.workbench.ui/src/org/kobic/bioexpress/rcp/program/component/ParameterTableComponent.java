package org.kobic.bioexpress.rcp.program.component;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ParameterTableComponent {

	public Table getParameterTableComponent(TableViewer tableViewer) {

		GridData gdInputTable = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(gdInputTable);

		TableColumn inputRequiredCol = new TableColumn(table, SWT.NONE);
		inputRequiredCol.setWidth(80);
		inputRequiredCol.setText("Required");
		
		TableColumn inputTypeCol = new TableColumn(table, SWT.NONE);
		inputTypeCol.setWidth(80);
		inputTypeCol.setText("Type");

		TableColumn inputNameCol = new TableColumn(table, SWT.NONE);
		inputNameCol.setWidth(80);
		inputNameCol.setText("Name");

		TableColumn inputValueCol = new TableColumn(table, SWT.NONE);
		inputValueCol.setWidth(150);
		inputValueCol.setText("Value");

		TableColumn inputDecriptionCol = new TableColumn(table, SWT.NONE);
		inputDecriptionCol.setWidth(300);
		inputDecriptionCol.setText("Description");
		
		return table;
	}
}
