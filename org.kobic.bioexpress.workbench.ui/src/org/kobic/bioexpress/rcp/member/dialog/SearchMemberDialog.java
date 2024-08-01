package org.kobic.bioexpress.rcp.member.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.service.sso.MemberService;
import org.kobic.bioexpress.channel.service.sso.MemberServiceImpl;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.member.provider.MemberTableViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;

public class SearchMemberDialog extends Dialog {

	public Text searchMemberText;
	public Text selectedItemsText;

	public TableViewer tableViewer;

	public Table table;

	private Map<String, Member> map;

	private List<Member> members;
	private List<Member> temp;
	private List<Member> shareMembers;

	private int size;

	private String filter = null;

	public SearchMemberDialog(Shell parentShell) {
		super(parentShell);
		this.map = new HashMap<String, Member>();
		this.shareMembers = new ArrayList<Member>();
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		
		super.configureShell(newShell);
		
		newShell.setText("Search Member");
		
		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;

		Label searchKeywordLabel = new Label(container, SWT.NONE);
		searchKeywordLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		searchKeywordLabel.setText("Search Member:");

		searchMemberText = new Text(container, SWT.BORDER);
		searchMemberText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label helpLabel = new Label(container, SWT.NONE);
		helpLabel.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.HELP_ICON));
		helpLabel.setToolTipText("Please search by member Id.");

		Label keyworditemsLabel = new Label(container, SWT.NONE);
		keyworditemsLabel.setText("Matching member items:");
		new Label(container, SWT.NONE);

		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.CHECK);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new MemberTableViewLabelProvider());

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
		nameCol.setWidth(100);
		nameCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn emailCol = new TableColumn(table, SWT.LEFT);
		emailCol.setText("Email");
		emailCol.setWidth(200);
		emailCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		selectedItemsText = new Text(container, SWT.BORDER);
		selectedItemsText.setEditable(false);
		selectedItemsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		selectedItemsText.setText("Please select a member to share with.");

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	private void init() {

		if (Activator.getMembers() == null) {

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("To retrieve all user information.", IProgressMonitor.UNKNOWN);

						monitor.subTask("Attempt to connect to the database.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Loading all user information.");

						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								MemberService memberService = new MemberServiceImpl();
								members = memberService.getAllMember();
								Activator.setMembers(members);
							}
						});

						if (monitor.isCanceled()) {
							monitor.done();
							return;
						}

						monitor.done();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.members = Activator.getMembers();
		}

		bind();
		event();
	}

	private void bind() {
		setTableDataBind();
	}

	private void event() {

		searchMemberText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				if (searchMemberText.getText().length() >= 3) {

					filter = searchMemberText.getText();

					temp = new ArrayList<Member>();

					Iterator<Member> iterator = members.iterator();

					while (iterator.hasNext()) {
						Member member = iterator.next();

						if (member.getMemberId().toLowerCase().contains(filter.toLowerCase())) {
							temp.add(member);
						}
					}

					tableViewer.setInput(temp);

				} else if (searchMemberText.getText().length() == 0) {
					tableViewer.setInput(members);
				}

			}
		});

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

			}
		});

		tableViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				TableItem item = (TableItem) e.item;

				Object obj = item.getData();

				if (e.detail == SWT.CHECK) {

					if (obj instanceof Member) {

						Member member = (Member) obj;

						if (item.getChecked()) {
							System.out.println("+++++" + obj);
							map.put(member.getMemberEmail(), member);
						} else {
							System.out.println("-----" + obj);
							map.remove(member.getMemberEmail());
						}

						size = map.size();

						if (size == 1) {
							for (Map.Entry<String, Member> elem : map.entrySet()) {
								selectedItemsText.setText(elem.getValue().getMemberId());
							}
						} else if (size > 1) {
							selectedItemsText.setText("Selected Share Member : " + size);
						} else if (size == 0) {
							selectedItemsText.setText("Please enter a keyword.");
						}
					}
				}
			}
		});

	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(406, 479);
	}

	private void setTableDataBind() {

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tableViewer.setInput(members);
			}
		});
	}

	public Map<String, Member> getMap() {
		return map;
	}

	public List<Member> getShareMembers() {

		for (Map.Entry<String, Member> elem : map.entrySet()) {
			shareMembers.add(elem.getValue());
		}

		return shareMembers;
	}

}
