package org.kobic.bioexpress.rcp.pipeline.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.member.provider.MemberTableViewLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

public class SharedPipelineDialogComponent {

	public Text pipelineNameText;
	public Text descriptionText;
	public Text workspaceIDText;
	public Text versionText;
	public Text referenceText;
	public Text keywordText;
	public Text workspaceNameText;
	public TableViewer shareTableViewer;
	public CTabItem shareMemberTabItem;
	public CTabItem pipelineInfoTabitem;
	public CTabFolder tabFolder;
	public Button addMemberBtn;
	public Button removeMemberBtn;
	private Group workspaceGroup;
	private Label workspaceNameLabel;
	private Label workspaceIDLabel;
	private Composite basicComposite;
	private Composite shareComposite;
	private TableColumn memberIDCol;
	private TableColumn meberNameCol;
	private TableColumn memberEmailCol;

	public SharedPipelineDialogComponent(Composite composite) {
		
		Composite container = new Composite(composite, SWT.NONE);
		Label pipelineName = new Label(container, SWT.NONE);
		pipelineName.setText("Pipeline Name:");
		pipelineNameText = new Text(container, SWT.BORDER);
		pipelineNameText.setEditable(false);
		pipelineNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label nameSeparator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		nameSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		tabFolder = new CTabFolder(container, SWT.NONE);

		GridData tabFolderGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		tabFolderGridData.grabExcessHorizontalSpace = true;
		tabFolderGridData.grabExcessVerticalSpace = true;
		tabFolder.setLayoutData(tabFolderGridData);

		pipelineInfoTabitem = new CTabItem(tabFolder, SWT.NONE);
		pipelineInfoTabitem.setText("Basic Information");
		pipelineInfoTabitem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PARAMETER_BASIC_TAB_ICON));

		basicComposite = new Composite(tabFolder, SWT.NONE);
		pipelineInfoTabitem.setControl(basicComposite);

		workspaceGroup = new Group(basicComposite, SWT.NONE);
		workspaceGroup.setLayout(new GridLayout(2, false));
		workspaceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		workspaceGroup.setText("Workspace");

		workspaceNameLabel = new Label(workspaceGroup, SWT.NONE);
		workspaceNameLabel.setText("Name:");

		workspaceNameText = new Text(workspaceGroup, SWT.BORDER);
		workspaceNameText.setEditable(false);
		workspaceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		workspaceIDLabel = new Label(workspaceGroup, SWT.NONE);
		workspaceIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		workspaceIDLabel.setText("ID:");

		workspaceIDText = new Text(workspaceGroup, SWT.BORDER);
		workspaceIDText.setEditable(false);
		workspaceIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group optionGroup = new Group(basicComposite, SWT.NONE);
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

		Group descriptionGroup = new Group(basicComposite, SWT.NONE);
		descriptionGroup.setText("Description");
		descriptionText = new Text(descriptionGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		descriptionText.setEditable(false);

		basicComposite.setLayout(new GridLayout(2, false));
		basicComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

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

		shareMemberTabItem = new CTabItem(tabFolder, SWT.NONE);
		shareMemberTabItem.setText("Share Member");
		shareMemberTabItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.MEMBER_ICON));

		shareComposite = new Composite(tabFolder, SWT.NONE);
		shareMemberTabItem.setControl(shareComposite);
		shareComposite.setLayout(new GridLayout(2, false));

		shareTableViewer = new TableViewer(shareComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.CHECK);
		shareTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		shareTableViewer.setLabelProvider(new MemberTableViewLabelProvider());
		
		Table table = shareTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		memberIDCol = new TableColumn(table, SWT.NONE);
		memberIDCol.setWidth(100);
		memberIDCol.setText("ID");
		
		meberNameCol = new TableColumn(table, SWT.NONE);
		meberNameCol.setWidth(150);
		meberNameCol.setText("Name");
		
		memberEmailCol = new TableColumn(table, SWT.NONE);
		memberEmailCol.setWidth(150);
		memberEmailCol.setText("Email");

		GridData addMemerBtnGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		addMemerBtnGridData.widthHint = 63;

		addMemberBtn = new Button(shareComposite, SWT.NONE);

		addMemberBtn.setText("Add...");
		addMemberBtn.setLayoutData(addMemerBtnGridData);
		addMemberBtn.setLayoutData(addMemerBtnGridData);

		removeMemberBtn = new Button(shareComposite, SWT.NONE);
		removeMemberBtn.setText("Remove...");
		removeMemberBtn.setLayoutData(addMemerBtnGridData);

	}
}