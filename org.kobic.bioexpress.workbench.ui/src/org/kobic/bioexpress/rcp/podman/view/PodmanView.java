package org.kobic.bioexpress.rcp.podman.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClient;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClientImpl;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.podman.provider.PodmanTableViewCompator;
import org.kobic.bioexpress.rcp.podman.provider.PodmanTableViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;

public class PodmanView {

	final static Logger logger = Logger.getLogger(PodmanView.class);

	@SuppressWarnings("unused")
	private Composite parent;

	private TableViewer tableViewer;

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

	@PostConstruct
	public void createComposite(Composite parent) {

		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(parent,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		tableViewer.setLabelProvider(new PodmanTableViewLabelProvider());
		tableViewer.setComparator(new PodmanTableViewCompator());

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		TableColumn TypeCol = new TableColumn(table, SWT.LEFT);
		TypeCol.setText("Type");
		TypeCol.setWidth(150);
		TypeCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn NameCol = new TableColumn(table, SWT.LEFT);
		NameCol.setText("Name");
		NameCol.setWidth(200);
		NameCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn podmanIDCol = new TableColumn(table, SWT.LEFT);
		podmanIDCol.setText("Podman ID");
		podmanIDCol.setWidth(200);
		podmanIDCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn imageIDCol = new TableColumn(table, SWT.LEFT);
		imageIDCol.setText("Image ID");
		imageIDCol.setWidth(200);
		imageIDCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn repoCol = new TableColumn(table, SWT.LEFT);
		repoCol.setText("Repo");
		repoCol.setWidth(200);
		repoCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn TagCol = new TableColumn(table, SWT.LEFT);
		TagCol.setText("Tag");
		TagCol.setWidth(200);
		TagCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn statusCol = new TableColumn(table, SWT.LEFT);
		statusCol.setText("Status");
		statusCol.setWidth(200);
		statusCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn DateCol = new TableColumn(table, SWT.LEFT);
		DateCol.setText("Create Date");
		DateCol.setWidth(200);
		DateCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn isOfficialCol = new TableColumn(table, SWT.LEFT);
		isOfficialCol.setText("Official");
		isOfficialCol.setWidth(200);
		isOfficialCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn DescCol = new TableColumn(table, SWT.LEFT);
		DescCol.setText("Description");
		DescCol.setWidth(400);
		DescCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		eMenuService.registerContextMenu(tableViewer.getControl(), Constants.PODMAN_POPUP_MENU_ID);

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
		setTableViewerDataBind();
	}

	private void event() {

		/**
		 * event handler에서 select된 object를 인지할 수 있도록 기본적으로 기술
		 */
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
				// TODO Auto-generated method stub

				if (!tableViewer.getSelection().isEmpty()) {

					Object object = tableViewer.getStructuredSelection().getFirstElement();

					System.out.println(object instanceof PodmanModel);

					if (object instanceof PodmanModel) {

						@SuppressWarnings("unused")
						PodmanModel podmanModel = (PodmanModel) object;
					}
				}
			}
		});
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	private void setTableViewerDataBind() {

		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InterruptedException {

					monitor.beginTask("Starting to refresh podman images.", IProgressMonitor.UNKNOWN);
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub

							PodmanAPIClient api = new PodmanAPIClientImpl();

							List<PodmanModel> podmanData = null;

							if (Activator.isAdmin()) {
								podmanData = api.getAllPodman();
							} else {
								podmanData = api.getOfficialPodman();
							}

							tableViewer.setInput(podmanData);
						}
					});
					monitor.subTask("Finish....");
					if (monitor.isCanceled()) {
						monitor.done();
						return;
					}
					monitor.done();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.PODMAN_DATA_RELOAD_EVENT_BUS_NAME) String uID) {
		setTableViewerDataBind();
	}
}
