package org.kobic.bioexpress.rcp.script.view;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.console.MessageConsoleStream;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.task.ScriptTaskModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.script.provider.ScriptHistoryTableViewCompator;
import org.kobic.bioexpress.rcp.script.provider.ScriptHistoryTableViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;

public class ScriptHistoryView {

	final static Logger logger = Logger.getLogger(ScriptHistoryView.class);

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	private EMenuService eMenuService;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private MPartStack mPartStack;

	@Inject
	@Optional
	private IWorkbench iworkbench;

	@Inject
	private ESelectionService selectionService;

	private TableViewer tableViewer;
	private Table table;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new ScriptHistoryTableViewLabelProvider());
		tableViewer.setComparator(new ScriptHistoryTableViewCompator());

		TableColumn typeCol = new TableColumn(table, SWT.LEFT);
		typeCol.setText("Type");
		typeCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		typeCol.setWidth(100);

		TableColumn scriptNameCol = new TableColumn(table, SWT.LEFT);
		scriptNameCol.setText("Script Name");
		scriptNameCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		scriptNameCol.setWidth(200);

		TableColumn jobNumberCol = new TableColumn(table, SWT.LEFT);
		jobNumberCol.setText("Job Number");
		jobNumberCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		jobNumberCol.setWidth(150);

		TableColumn startTimeCol = new TableColumn(table, SWT.LEFT);
		startTimeCol.setText("Start Time");
		startTimeCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		startTimeCol.setWidth(200);

		TableColumn endTimeCol = new TableColumn(table, SWT.LEFT);
		endTimeCol.setText("End Time");
		endTimeCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		endTimeCol.setWidth(200);

		TableColumn statusCol = new TableColumn(table, SWT.LEFT);
		statusCol.setText("Status");
		statusCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		statusCol.setWidth(150);
		
		TableColumn envCol = new TableColumn(table, SWT.LEFT);
		envCol.setText("Env");
		envCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));
		envCol.setWidth(200);

		eMenuService.registerContextMenu(tableViewer.getControl(), Constants.SCRIPT_POPUP_MENU_ID);

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {

		setTableViewerDataBind();
	}

	private void setTableViewerDataBind() {

		String memberID = Activator.getMember().getMemberId();

		TaskClient client = new TaskClientImpl();
		List<ScriptTaskModel> list = client.getScriptTaskByMember(memberID);

		tableViewer.setInput(list);
	}

	private void event() {

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				if (!tableViewer.getSelection().isEmpty()) {

					Object object = tableViewer.getStructuredSelection().getFirstElement();

					if (object instanceof ScriptTaskModel) {

						ScriptTaskModel scriptTaskModel = (ScriptTaskModel) object;

						MessageConsoleStream msgConsoleStream = Activator.getMessageConsole().newMessageStream();

						FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();

						System.out.println(scriptTaskModel.getStdoutPath());

						List<String> sysout = null;

						if (fileUtilsClient.isExist(scriptTaskModel.getStdoutPath())) {
							sysout = fileUtilsClient.reverseReader(scriptTaskModel.getStdoutPath(),
									Constants.DEFAULT_CONSOLE_LOG_LINE);
						} else {
							msgConsoleStream.println("The log file does not exist.");
						}

						List<String> syserr = null;

						if (fileUtilsClient.isExist(scriptTaskModel.getStderrPath())) {
							syserr = fileUtilsClient.reverseReader(scriptTaskModel.getStderrPath(),
									Constants.DEFAULT_CONSOLE_LOG_LINE);
						}

						if (sysout != null) {
							msgConsoleStream.println(String.join("\n", sysout).trim().replace("/script", ""));
						}

						if (syserr != null) {
							msgConsoleStream.println(String.join("\n", syserr).trim().replace("/script", ""));
						}

//						System.out.println(sysout.toString());
//						System.out.println(syserr.toString());

						try {
							msgConsoleStream.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}

						MPart mPart = ePartService.findPart(Constants.CONSOLE_VIEW_ID);

						ePartService.showPart(mPart, PartState.ACTIVATE);
					}
				}
			}
		});

	}

	@Inject
	@Optional
	private void wandiscoReloadsubscribeTopicTodoUpdated(
			@UIEventTopic(Constants.SCRIPT_TASK_HISTORY_REFRESH_EVENT_BUS_NAME) String uid) {

		setTableViewerDataBind();

		logger.info(uid + " : script task history view refresh");
	}
}
