package org.kobic.bioexpress.rcp.category.component;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.eclipse.swt.widgets.Button;

public class CategoryDialogComponent {

	public Text categoryNameText;
	public Text categoryDescriptionText;
	public Button publicCheck;

	public CategoryDialogComponent(Composite composite, TitleAreaDialog dialog, boolean isAdmin) {

		Composite container = new Composite(composite, SWT.NONE);

		final Label categoryNameLabel = new Label(container, SWT.NONE);
		categoryNameLabel.setText("Name:");

		categoryNameText = new Text(container, SWT.BORDER);
		categoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		categoryNameText.setToolTipText(Constants.NAMING_RULE);

		final Label categoryDescriptionLabel = new Label(container, SWT.NONE);
		categoryDescriptionLabel.setText("Description:");

		categoryDescriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

		GridData categoryDescriptionTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		categoryDescriptionTextGridData.grabExcessHorizontalSpace = true;
		categoryDescriptionTextGridData.grabExcessVerticalSpace = true;
		categoryDescriptionText.setLayoutData(categoryDescriptionTextGridData);

		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		publicCheck = new Button(container, SWT.CHECK);
		publicCheck.setText("Do you want to request to register public the category?");
		publicCheck.setVisible(isAdmin);
	}
}
