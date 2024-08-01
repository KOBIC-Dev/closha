package org.kobic.bioexpress.rcp.program.component;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.provider.RelationProgramTreeViewCompator;
import org.kobic.bioexpress.rcp.program.provider.RelationProgramTreeViewContentProvider;
import org.kobic.bioexpress.rcp.program.provider.RelationProgramTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;

public class ProgramWizardComponent2 {

	public Text scriptPathText;

	public Button selectButton;
	public Button singleCoreRadio;
	public Button multiCoreRadio;
	public Button hpcRadio;
	public Button gpuRadio;
	public Button bigRadio;

	public TableCombo scriptTypeCombo;
	public Group executeGroup;
	public Group platformGroup;
	public Group scriptGroup;
	public Group relationGroup;

	public TreeViewer treeViewer;

	public Composite getProgramWizardComponent2(Composite container) {

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		scriptGroup = new Group(composite, SWT.NONE);
		scriptGroup.setLayout(new GridLayout(2, false));
		scriptGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		scriptGroup.setText("Script Information");

		Label urlLabel = new Label(scriptGroup, SWT.NONE);
		urlLabel.setText("Script Type:");
		new Label(scriptGroup, SWT.NONE);

		scriptTypeCombo = new TableCombo(scriptGroup, SWT.BORDER);
		scriptTypeCombo.setEditable(false);
		scriptTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		TableItem stringItem = new TableItem(scriptTypeCombo.getTable(), SWT.BORDER);
		stringItem.setText("Python");
		stringItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_PYTHON_ICON));

		TableItem integerItem = new TableItem(scriptTypeCombo.getTable(), SWT.BORDER);
		integerItem.setText("Bash");
		integerItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_BASH_ICON));

		TableItem boolItem = new TableItem(scriptTypeCombo.getTable(), SWT.BORDER);
		boolItem.setText("R");
		boolItem.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_R_ICON));

		Label categoryLabel = new Label(scriptGroup, SWT.NONE);
		categoryLabel.setText("Script Path:");
		new Label(scriptGroup, SWT.NONE);

		scriptPathText = new Text(scriptGroup, SWT.BORDER);
		scriptPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		scriptPathText.setEditable(false);

		GridData selectBtnGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		selectBtnGridData.heightHint = 23;

		selectButton = new Button(scriptGroup, SWT.NONE);
		selectButton.setLayoutData(selectBtnGridData);
		selectButton.setText("Select...");

		executeGroup = new Group(composite, SWT.NONE);
		executeGroup.setLayout(new GridLayout(1, false));

		GridData executeGroupGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		executeGroupGridData.grabExcessHorizontalSpace = true;
		executeGroup.setLayoutData(executeGroupGridData);
		executeGroup.setText("Execute Core");

		singleCoreRadio = new Button(executeGroup, SWT.RADIO);
		singleCoreRadio.setText("Run analysis tools using a single core");
		singleCoreRadio.setSelection(true);

		multiCoreRadio = new Button(executeGroup, SWT.RADIO);
		multiCoreRadio.setText("Run analysis tools with up to 6 multi-cores");
		multiCoreRadio.setSelection(false);

		platformGroup = new Group(composite, SWT.NONE);
		platformGroup.setLayout(new GridLayout(1, false));

		GridData platformGroupGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		platformGroupGridData.grabExcessHorizontalSpace = true;
		platformGroup.setLayoutData(platformGroupGridData);
		platformGroup.setText("Platform Environment");

		hpcRadio = new Button(platformGroup, SWT.RADIO);
		hpcRadio.setText("Use the high performance computing cluster");
		hpcRadio.setSelection(true);

		gpuRadio = new Button(platformGroup, SWT.RADIO);
		gpuRadio.setText(
				"Use a GPU(Graphics Processing Unit) server (supported for machine learning and deep learning)");
		gpuRadio.setSelection(false);

		bigRadio = new Button(platformGroup, SWT.RADIO);
		bigRadio.setText("Use a large amount of memory(12T) analysis task server");
		bigRadio.setSelection(false);

		relationGroup = new Group(composite, SWT.NONE);
		relationGroup.setText("Relation Program");

		GridLayout relationGroupGridLayout = new GridLayout(1, false);
		relationGroupGridLayout.marginTop = 5;
		relationGroup.setLayout(relationGroupGridLayout);

		GridData relationGroupGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		relationGroupGridData.grabExcessHorizontalSpace = true;
		relationGroupGridData.grabExcessVerticalSpace = true;
		relationGroup.setLayoutData(relationGroupGridData);

		GridData relationTableGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		relationTableGridData.grabExcessHorizontalSpace = true;
		relationTableGridData.grabExcessVerticalSpace = true;

		treeViewer = new TreeViewer(relationGroup,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		treeViewer.setLabelProvider(new RelationProgramTreeViewLabelProvider());
		treeViewer.setContentProvider(new RelationProgramTreeViewContentProvider());
		treeViewer.setComparator(new RelationProgramTreeViewCompator());
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setLayoutData(relationTableGridData);

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(150);
		nameCol.getColumn().setText("Name");
		nameCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		nameCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof ProgramDataModel) {
					return ((ProgramDataModel) element).getProgramName();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterName();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn descCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		descCol.getColumn().setWidth(300);
		descCol.getColumn().setText("Description");
		descCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		descCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof ProgramDataModel) {
					return ((ProgramDataModel) element).getProgramDesc();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterDesc();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn categoryCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		categoryCol.getColumn().setWidth(150);
		categoryCol.getColumn().setText("Category");
		categoryCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		categoryCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof ProgramDataModel) {
					return ((ProgramDataModel) element).getSubCategoryName();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterType();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn registerDateCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		registerDateCol.getColumn().setWidth(150);
		registerDateCol.getColumn().setText("Register Date");
		registerDateCol.getColumn().addSelectionListener(new TreeViewColumnSelectionListener(this.treeViewer));
		registerDateCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof ProgramDataModel) {
					return ((ProgramDataModel) element).getRegistedDate();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterType();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		return composite;
	}
}
