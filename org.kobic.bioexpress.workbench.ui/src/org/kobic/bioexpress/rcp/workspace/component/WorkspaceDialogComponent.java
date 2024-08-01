package org.kobic.bioexpress.rcp.workspace.component;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.kobic.bioexpress.rcp.constant.Constants;

public class WorkspaceDialogComponent {

	public Text workspaceNameText;
	public Text workspaceDescriptionText;
	public Text workspaceKeywordText;

	public WorkspaceDialogComponent(Composite composite, TitleAreaDialog dialog) {
		
		Composite container = new Composite(composite, SWT.NONE);

		final Label workspaceNameLabel = new Label(container, SWT.NONE);
		workspaceNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		workspaceNameLabel.setText("Name: ");
		workspaceNameText = new Text(container, SWT.BORDER);
		workspaceNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		workspaceNameText.setToolTipText(Constants.NAMING_RULE);

		final Label workspaceKeywordLabel = new Label(container, SWT.NONE);
		workspaceKeywordLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		workspaceKeywordLabel.setText("Keyword: ");

		workspaceKeywordText = new Text(container, SWT.BORDER);
		workspaceKeywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		workspaceKeywordText.setToolTipText("Please separate the keywords with comma(,).");

		final Label workspaceDescriptionLabel = new Label(container, SWT.NONE);
		workspaceDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		workspaceDescriptionLabel.setText("Description:");
		workspaceDescriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		GridData workspaceDescriptionGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		workspaceDescriptionGridData.grabExcessHorizontalSpace = true;
		workspaceDescriptionGridData.grabExcessVerticalSpace = true;
		workspaceDescriptionText.setLayoutData(workspaceDescriptionGridData);

		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

	}

}
