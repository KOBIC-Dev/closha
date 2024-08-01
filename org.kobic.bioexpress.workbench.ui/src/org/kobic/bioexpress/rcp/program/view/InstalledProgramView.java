package org.kobic.bioexpress.rcp.program.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.handler.InstalledProgramTreeViewContentProvider;
import org.kobic.bioexpress.rcp.program.handler.InstalledProgramTreeViewLabelProvider;

public class InstalledProgramView {

	final static Logger logger = Logger.getLogger(ProgramView.class);

	private TreeViewer treeViewer;

	@Inject
	private ESelectionService selectionService;

	@Inject
	private EMenuService eMenuService;

	@Inject
	private UISynchronize sync;

	@Inject
	@Optional
	private EPartService ePartService;

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

	private List<Object> expands;

	@PostConstruct
	public void createComposite(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setLabelProvider(new InstalledProgramTreeViewLabelProvider());
		treeViewer.setContentProvider(new InstalledProgramTreeViewContentProvider());
		treeViewer.getTree().setHeaderVisible(false);
		treeViewer.getTree().setLinesVisible(false);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				FileModel t1 = (FileModel) e1;
				FileModel t2 = (FileModel) e2;
				return t1.getName().toLowerCase().compareTo(t2.getName().toLowerCase());
			};
		});

		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.INSTALLED_PROGRAM_POPUP_MENU_ID);

		init();
	}

	private void init() {
		bind();
		event();

		expands = new ArrayList<Object>();
	}

	private void bind() {
		setTreeDataBind();
	}

	private void event() {

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub

				if (!treeViewer.getSelection().isEmpty()) {

					Object object = treeViewer.getStructuredSelection().getFirstElement();

					if (object instanceof FileModel) {

						FileModel fileModel = (FileModel) object;

						System.out.println(fileModel.getName());
						System.out.println(fileModel.isIsDir());
						System.out.println(fileModel.isIsFile());

						if (fileModel.isIsDir()) {
							/**
							 * object double click to expanded
							 */
							expands.add(object);
							treeViewer.setExpandedElements(expands.toArray());
						}
					}
				}
			}
		});
	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	private void setTreeDataBind() {

		Job job = new Job("Binding installed program") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("Installed program information.", IProgressMonitor.UNKNOWN);

				FileUtilsClient client = new FileUtilsClientImpl();
				List<FileModel> root = client.getFiles("/opt/apps");

				monitor.beginTask("Loading intalled program list..", root.size());

				for (FileModel file : root) {
					monitor.worked(1);
					monitor.setTaskName(file.getName());
					try {
						TimeUnit.MILLISECONDS.sleep(Constants.DEFAULT_DELAY_MILLISECONDS_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				sync.asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						treeViewer.setInput(root);
					}
				});

				monitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(
			@UIEventTopic(Constants.INSTALLED_PROGRAM_REFRESH_EVENT_BUS_NAME) String uid) {

//		expands.clear();

		setTreeDataBind();
	}

}
