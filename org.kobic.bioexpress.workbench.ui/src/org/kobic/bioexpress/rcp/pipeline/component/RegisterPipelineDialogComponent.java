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

public class RegisterPipelineDialogComponent {

	public Text pipelineNameText;
	public Text descriptionText;
	public Text workspaceIDText;
	public Text versionText;
	public Text referenceText;
	public Text keywordText;
	public Text workspaceNameText;
	public Table instanceComboTable;
	public Text categoryNameText;
	public Text categoryIDText;
	public Text requestMsgText;
	public Button categorySelectBtn;
	private Group grpWorkspace;
	private Label workspaceNameLabel;
	private Label workspaceIDLabel;
	private Label categoryNameLabel;
	private Label categoryIDLabel;
	private Label reqestSeparator;
	private Label requestMsgLabel;
	

	public RegisterPipelineDialogComponent(Composite composite) {

		Composite container = new Composite(composite, SWT.NONE);
		Label pipelineName = new Label(container, SWT.NONE);
		pipelineName.setText("Pipeline Name:");
		pipelineNameText = new Text(container, SWT.BORDER);
		pipelineNameText.setEditable(true);
		pipelineNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label nameSeparator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		nameSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Group typeGroup = new Group(container, SWT.NONE);
		typeGroup.setText("Category");

		typeGroup.setLayout(new GridLayout(3, false));
		typeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		categoryNameLabel = new Label(typeGroup, SWT.NONE);
		categoryNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		categoryNameLabel.setText("Name:");

		categoryNameText = new Text(typeGroup, SWT.BORDER);
		categoryNameText.setEditable(false);
		categoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		categorySelectBtn = new Button(typeGroup, SWT.NONE);
		categorySelectBtn.setText("Select...");

		categoryIDLabel = new Label(typeGroup, SWT.NONE);
		categoryIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		categoryIDLabel.setText("ID:");

		categoryIDText = new Text(typeGroup, SWT.BORDER);
		categoryIDText.setEditable(false);
		categoryIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		grpWorkspace = new Group(container, SWT.NONE);
		grpWorkspace.setLayout(new GridLayout(2, false));
		grpWorkspace.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		grpWorkspace.setText("Workspace");

		workspaceNameLabel = new Label(grpWorkspace, SWT.NONE);
		workspaceNameLabel.setText("Name:");

		workspaceNameText = new Text(grpWorkspace, SWT.BORDER);
		workspaceNameText.setEditable(false);
		workspaceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		workspaceIDLabel = new Label(grpWorkspace, SWT.NONE);
		workspaceIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		workspaceIDLabel.setText("ID:");

		workspaceIDText = new Text(grpWorkspace, SWT.BORDER);
		workspaceIDText.setEditable(false);
		workspaceIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group optionGroup = new Group(container, SWT.NONE);
		optionGroup.setLayout(new GridLayout(2, false));
		optionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		optionGroup.setText("Option");

		Label versionLabel = new Label(optionGroup, SWT.NONE);
		versionLabel.setText("Version:");

		versionText = new Text(optionGroup, SWT.BORDER);
		versionText.setEditable(false);
		versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label referenceLabel = new Label(optionGroup, SWT.NONE);
		referenceLabel.setText("Reference:");

		referenceText = new Text(optionGroup, SWT.BORDER);
		referenceText.setEditable(false);
		referenceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label keywordLabel = new Label(optionGroup, SWT.NONE);
		keywordLabel.setText("Keyword:");

		keywordText = new Text(optionGroup, SWT.BORDER);
		keywordText.setEditable(false);
		keywordText.setEnabled(true);
		keywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group descriptionGroup = new Group(container, SWT.NONE);
		descriptionGroup.setText("Description");
		descriptionText = new Text(descriptionGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		descriptionText.setEditable(true);

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
		
		reqestSeparator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		reqestSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		requestMsgLabel = new Label(container, SWT.NONE);
		requestMsgLabel.setText("Request Message:");
		
		requestMsgText = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_requestMsgText = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_requestMsgText.heightHint = 58;
		requestMsgText.setLayoutData(gd_requestMsgText);
	}
}