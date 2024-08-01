package org.kobic.bioexpress.rcp.pipeline.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class EditPublicPipelineDialogComponent {

	public Text pipelineNameText;
	public Text descriptionText;
	public Text categoryIDText;
	public Text versionText;
	public Text referenceText;
	public Text keywordText;
	public Text categoryNameText;
	public Button categorySelectBtn;
	public Table instanceComboTable;
	private Group grpWorkspace;
	private Label categoryNameLabel;
	private Label categoryIDLabel;
	
	public EditPublicPipelineDialogComponent(Composite composite) {

		Composite container = new Composite(composite, SWT.NONE);
		Label pipelineName = new Label(container, SWT.NONE);
		pipelineName.setText("Pipeline Name:");
		pipelineNameText = new Text(container, SWT.BORDER);
		pipelineNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label nameLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		grpWorkspace = new Group(container, SWT.NONE);
		grpWorkspace.setLayout(new GridLayout(3, false));
		grpWorkspace.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		grpWorkspace.setText("Category");
		
		categoryNameLabel = new Label(grpWorkspace, SWT.NONE);
		categoryNameLabel.setText("Name:");
		
		categoryNameText = new Text(grpWorkspace, SWT.BORDER);
		categoryNameText.setEditable(false);
		categoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		categorySelectBtn = new Button(grpWorkspace, SWT.NONE);
		categorySelectBtn.setText("Select...");
		
		categoryIDLabel = new Label(grpWorkspace, SWT.NONE);
		categoryIDLabel.setText("ID:");
		
		categoryIDText = new Text(grpWorkspace, SWT.BORDER);
		categoryIDText.setEditable(false);
		categoryIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Group optionGroup = new Group(container, SWT.NONE);
		optionGroup.setLayout(new GridLayout(3, false));
		optionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		optionGroup.setText("Option");
		
		Label versionLabel = new Label(optionGroup, SWT.NONE);
		versionLabel.setText("Version:");
		
		versionText = new Text(optionGroup, SWT.BORDER);
		versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label referenceLabel = new Label(optionGroup, SWT.NONE);
		referenceLabel.setText("Reference:");
		
		referenceText = new Text(optionGroup, SWT.BORDER);
		referenceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label keywordLabel = new Label(optionGroup, SWT.NONE);
		keywordLabel.setText("Keyword:");
		
		keywordText = new Text(optionGroup, SWT.BORDER);
		keywordText.setEditable(true);
		keywordText.setEnabled(true);
		keywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		keywordText.setToolTipText("Please separate the keywords with comma(,)");
		
		Group descriptionGroup = new Group(container, SWT.NONE);
		descriptionGroup.setText("Description");
		descriptionText = new Text(descriptionGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout descriptionGroupGridLayout = new GridLayout(1, false);
		descriptionGroupGridLayout.marginTop = 5;
		descriptionGroup.setLayout(descriptionGroupGridLayout);

		GridData descriptionGropGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		descriptionGropGridData.grabExcessHorizontalSpace = true;
		descriptionGropGridData.grabExcessVerticalSpace = true;
		descriptionGroup.setLayoutData(descriptionGropGridData);

		GridData descriptionTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		descriptionTextGridData.grabExcessHorizontalSpace = true;
		descriptionTextGridData.grabExcessVerticalSpace = true;
		descriptionText.setLayoutData(descriptionTextGridData);
	}
		
}