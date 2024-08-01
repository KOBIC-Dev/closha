package org.kobic.bioexpress.rcp.raonwiz.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.model.raonwiz.RaonwizStateModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.raonwiz.provider.RaonwizViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.eclipse.swt.layout.FillLayout;

public class RaonwizView {

	final static Logger logger = Logger.getLogger(RaonwizView.class);

	@Inject
	@Optional
	private EModelService eModelService;

	@SuppressWarnings("unused")
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

	private TableViewer raonwizViewer;
	private Table raonwizTable;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		raonwizViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		raonwizTable = raonwizViewer.getTable();
		raonwizTable.setLinesVisible(true);
		raonwizTable.setHeaderVisible(true);
		raonwizViewer.setContentProvider(ArrayContentProvider.getInstance());
		raonwizViewer.setLabelProvider(new RaonwizViewLabelProvider());

		TableColumn serverIDCol = new TableColumn(raonwizTable, SWT.LEFT);
		serverIDCol.setText("Server ID");
		serverIDCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		serverIDCol.setWidth(100);

		TableColumn serverNameCol = new TableColumn(raonwizTable, SWT.LEFT);
		serverNameCol.setText("Server Name");
		serverNameCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		serverNameCol.setWidth(150);

		TableColumn stateCol = new TableColumn(raonwizTable, SWT.LEFT);
		stateCol.setText("State");
		stateCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		stateCol.setWidth(100);

		TableColumn labelCol = new TableColumn(raonwizTable, SWT.LEFT);
		labelCol.setText("Connection");
		labelCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		labelCol.setWidth(150);

		TableColumn sessionCol = new TableColumn(raonwizTable, SWT.LEFT);
		sessionCol.setText("Session Time");
		sessionCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		sessionCol.setWidth(150);

		TableColumn sessionTimeCol = new TableColumn(raonwizTable, SWT.LEFT);
		sessionTimeCol.setText("Duration Time");
		sessionTimeCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		sessionTimeCol.setWidth(150);

		TableColumn sessionCountCol = new TableColumn(raonwizTable, SWT.LEFT);
		sessionCountCol.setText("Total Session Count");
		sessionCountCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		sessionCountCol.setWidth(150);

		TableColumn transferCountCol = new TableColumn(raonwizTable, SWT.LEFT);
		transferCountCol.setText("Total Transfer Count");
		transferCountCol.addSelectionListener(new TableViewColumnSelectionListener(this.raonwizViewer));
		transferCountCol.setWidth(150);

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

		Utils utils = Utils.getInstance();

		String state = Activator.getRapidantService().getSessionInfo().getNowTransferCount() < 30 ? "Stable"
				: "Confusion";
		String label = Activator.getRapidantService().isConnect() ? "Connected" : "Disconnected";

		String sessionCount = String.valueOf(Activator.getRapidantService().getSessionInfo().getNowSessionCount());
		String transferCount = String.valueOf(Activator.getRapidantService().getSessionInfo().getNowTransferCount());

		Calendar before = Calendar.getInstance();
		before.setTimeInMillis(Activator.getSessionTime());

		Calendar after = Calendar.getInstance();

		int diffMinute = after.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);

		String sessionTime = utils.getDateTime(Activator.getSessionTime());
		String durationTime = String.valueOf(diffMinute + "m");

		RaonwizStateModel model = new RaonwizStateModel();
		model.setServerID(Constants.RAONWIZ_SERVER_ID);
		model.setServerName(Constants.RAONWIZ_SERVER_NAME);
		model.setState(state);
		model.setLabel(label);
		model.setSessionStartTime(sessionTime);
		model.setSessionDurationTime(durationTime);
		model.setSessionCount(sessionCount);
		model.setTransferCount(transferCount);

		List<RaonwizStateModel> list = new ArrayList<RaonwizStateModel>();
		list.add(model);

		raonwizViewer.setInput(list);

	}

	private void event() {
	}

	@Inject
	@Optional
	private void wandiscoReloadsubscribeTopicTodoUpdated(
			@UIEventTopic(Constants.RAONWIZ_REFRESH_EVENT_BUS_NAME) String uid) {

		setTableViewerDataBind();

		logger.info(uid + " : raonwiz view refresh");
	}

}
