package org.kobic.bioexpress.rcp.program.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ProgramWizardComponent1 {

	public Text subCategoryNameText;
	public Text subCategoryIdText;
	public Text versionText;
	public Text descriptionText;
	public Text urlText;
	public Text keywordText;
	public Button selectButton;

	public Composite getProgramWizardComponent1(Composite container) {

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Group categoryGroup = new Group(composite, SWT.NONE);
		categoryGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		categoryGroup.setText("Category");
		categoryGroup.setLayout(new GridLayout(2, false));

		Label categoryNameLabel = new Label(categoryGroup, SWT.NONE);
		categoryNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		categoryNameLabel.setText("Name:");

		subCategoryNameText = new Text(categoryGroup, SWT.BORDER);
		subCategoryNameText.setEditable(false);
		subCategoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		selectButton = new Button(categoryGroup, SWT.NONE);
		GridData selectButtonGidData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		selectButtonGidData.heightHint = 23;
		selectButton.setLayoutData(selectButtonGidData);
		selectButton.setText("Select...");

		Label categoryLabel = new Label(categoryGroup, SWT.NONE);
		categoryLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		categoryLabel.setText("ID:");

		subCategoryIdText = new Text(categoryGroup, SWT.BORDER);
		subCategoryIdText.setEditable(false);
		subCategoryIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Group basicGroup = new Group(composite, SWT.NONE);
		basicGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		basicGroup.setLayout(new GridLayout(2, false));
		basicGroup.setText("Basic Information");

		Label versionLabel = new Label(basicGroup, SWT.NONE);
		versionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		versionLabel.setText("Version:");

		versionText = new Text(basicGroup, SWT.BORDER);
		versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label keywordLabel = new Label(basicGroup, SWT.NONE);
		keywordLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		keywordLabel.setText("Keyword:");

		keywordText = new Text(basicGroup, SWT.BORDER);
		keywordText.setEditable(true);
		keywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		keywordText.setToolTipText("Please separate the keywords with comma(,).");

		Label urlLabel = new Label(basicGroup, SWT.NONE);
		urlLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		urlLabel.setText("URL:");

		urlText = new Text(basicGroup, SWT.BORDER);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Group descriptionGroup = new Group(composite, SWT.NONE);
		descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout descriptionGroupGridLayout = new GridLayout(1, false);
		descriptionGroupGridLayout.marginTop = 5;
		descriptionGroup.setLayout(descriptionGroupGridLayout);
		descriptionGroup.setText("Description");

		descriptionText = new Text(descriptionGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return composite;
	}

}
