package org.kobic.bioexpress.rcp.keyword.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.common.KeywordModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.keyword.provider.KeywordTableViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;

public class KeywordDialog extends Dialog {

	public Text searchKeywordText;
	public Text selectedItemsText;

	public TableViewer tableViewer;

	public Table table;

	private List<KeywordModel> temp;
	private List<String> selectKeyword;

	private String keyword = null;
	private String filter = null;

	int size = 0;
	
	public KeywordDialog(Shell parentShell) {
		super(parentShell);
		this.selectKeyword = new ArrayList<String>();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		
		super.configureShell(newShell);
		
		newShell.setText("Search Keyword");
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label searchKeywordLabel = new Label(container, SWT.NONE);
		searchKeywordLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		searchKeywordLabel.setText("Search keyword:");

		searchKeywordText = new Text(container, SWT.BORDER);
		searchKeywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label helpLabel = new Label(container, SWT.NONE);
		helpLabel.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.HELP_ICON));
		helpLabel.setToolTipText("Please search by keyword name.");
		

		Label keyworditemsLabel = new Label(container, SWT.NONE);
		keyworditemsLabel.setText("Matching keyword items:");
		new Label(container, SWT.NONE);

		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new KeywordTableViewLabelProvider());

		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn idCol = new TableColumn(table, SWT.LEFT);
		idCol.setText("ID");
		idCol.setWidth(100);
		idCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn nameCol = new TableColumn(table, SWT.LEFT);
		nameCol.setText("Name");
		nameCol.setWidth(200);
		nameCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn descCol = new TableColumn(table, SWT.LEFT);
		descCol.setText("Description");
		descCol.setWidth(200);
		descCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn categoryCol = new TableColumn(table, SWT.LEFT);
		categoryCol.setText("Category");
		categoryCol.setWidth(200);
		categoryCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		selectedItemsText = new Text(container, SWT.BORDER);
		selectedItemsText.setEditable(false);
		selectedItemsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectedItemsText.setText("Please select a keyword.");
		new Label(container, SWT.NONE);
		
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
		setTableDataBind();
	}

	private void event() {

		searchKeywordText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub

				if (searchKeywordText.getText().length() >= 3) {

					filter = searchKeywordText.getText();

					temp = new ArrayList<KeywordModel>();

					Iterator<KeywordModel> iterator = Activator.getKeyword().iterator();

					while (iterator.hasNext()) {
						KeywordModel keyword = iterator.next();

						if (keyword.getName().toLowerCase().contains(filter.toLowerCase())) {
							temp.add(keyword);
						}
					}

					tableViewer.setInput(temp);
				} else if (searchKeywordText.getText().length() == 0) {
					List<KeywordModel> keyword = Activator.getKeyword();
					tableViewer.setInput(keyword);
				}
			}
		});

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				size = tableViewer.getStructuredSelection().size();
				
				if (size == 1) {
					selectKeyword.clear();
					
					KeywordModel keywordModel = (KeywordModel) tableViewer.getStructuredSelection().getFirstElement();
					keyword = String.format(Constants.KEYWORD_FORMAT, keywordModel.getName());
					selectKeyword.add(keyword);
					selectedItemsText.setText(keywordModel.getName());
					
				} else if (size > 1) {
					selectKeyword.clear();
					
					Iterator<KeywordModel> iterator = event.getStructuredSelection().iterator();

					while (iterator.hasNext()) {
						KeywordModel keywordModel = iterator.next();
						keyword = String.format(Constants.KEYWORD_FORMAT, keywordModel.getName());

						if (!selectKeyword.contains(keyword)) {
							selectKeyword.add(keyword);
						}
					}

					selectedItemsText.setText("Selected keyword : " + size);
				} else if (size == 0) {
					selectKeyword.clear();
					selectedItemsText.setText("Please enter a keyword.");
				}

			}
		});

	}

	@Override
	protected void okPressed() {

		if (selectKeyword.size() == 0) {
			
			boolean res = MessageDialog.openConfirm(getShell(), "Confirm", "Do you want to continue without selecting a keyword?");
			
			if(res) {
				super.okPressed();
			}
		} else {
			super.okPressed();
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(406, 479);
	}

	private void setTableDataBind() {
		List<KeywordModel> keyword = Activator.getKeyword();
		tableViewer.setInput(keyword);
	}

	public List<String> getSelectKeyword() {
		return selectKeyword;
	}
}
