package org.kobic.bioexpress.rcp.pipeline.component;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineTableViewLabelProvider;

public class NewPipelineDialogComponent {

	public Text pipelineNameText;
	public Text descriptionText;
	public Text workspaceIDText;
	public Text versionText;
	public Text referenceText;
	public Text keywordText;
	public Text workspaceNameText;
	public Button workspaceSelectBtn;
	public Button developRadio;
	public Button instanceRadio;
	public TableCombo instanceComboTable;
	public TableComboViewer instanceComboTableView;
	private Group grpWorkspace;
	private Label workspaceNameLabel;
	private Label workspaceIDLabel;
	
	public NewPipelineDialogComponent(Composite composite) {
		
		Composite container = new Composite(composite, SWT.NONE);
		Label pipelineName = new Label(container, SWT.NONE);
		pipelineName.setText("Pipeline Name:");
		pipelineNameText = new Text(container, SWT.BORDER);
		pipelineNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label nameLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		grpWorkspace = new Group(container, SWT.NONE);
		grpWorkspace.setLayout(new GridLayout(3, false));
		grpWorkspace.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		grpWorkspace.setText("Workspace");
		
		workspaceNameLabel = new Label(grpWorkspace, SWT.NONE);
		workspaceNameLabel.setText("Name:");
		
		workspaceNameText = new Text(grpWorkspace, SWT.BORDER);
		workspaceNameText.setEditable(false);
		workspaceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		workspaceSelectBtn = new Button(grpWorkspace, SWT.NONE);
		workspaceSelectBtn.setText("Select...");
		
		workspaceIDLabel = new Label(grpWorkspace, SWT.NONE);
		workspaceIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		workspaceIDLabel.setText("ID:");
		
		workspaceIDText = new Text(grpWorkspace, SWT.BORDER);
		workspaceIDText.setEditable(false);
		workspaceIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Group typeGroup = new Group(container, SWT.NONE);
		typeGroup.setText("Type");

		developRadio = new Button(typeGroup, SWT.RADIO);
		developRadio.setText("Develop a new pipeline");
		developRadio.setSelection(true);

		instanceRadio = new Button(typeGroup, SWT.RADIO);
		instanceRadio.setText("Develop an instance pipeline");
		instanceRadio.setSelection(false);

		instanceComboTable = new TableCombo(typeGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		instanceComboTable.setEditable(false);
		instanceComboTable.setEnabled(false);
		instanceComboTable.setShowTableHeader(true);
		instanceComboTable.setShowTableLines(true);
		instanceComboTable.setTableWidthPercentage(100);
		
		instanceComboTableView = new TableComboViewer(instanceComboTable);
		instanceComboTableView.setContentProvider(ArrayContentProvider.getInstance());
		instanceComboTableView.setLabelProvider(new PipelineTableViewLabelProvider());
		instanceComboTableView.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				PipelineDataModel t1 = (PipelineDataModel) e1;
				PipelineDataModel t2 = (PipelineDataModel) e2;
				return t1.getPipelineName().compareTo(t2.getPipelineName());
			};
		});
				
		instanceComboTableView.getTableCombo().defineColumns(new String[] { "Name", "Keyword", "Description"}, 
				new int[] { 100 , 100, 100});
		
		Group optionGroup = new Group(container, SWT.NONE);
		optionGroup.setLayout(new GridLayout(3, false));
		optionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
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

		typeGroup.setLayout(new GridLayout(1, false));
		typeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		instanceComboTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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