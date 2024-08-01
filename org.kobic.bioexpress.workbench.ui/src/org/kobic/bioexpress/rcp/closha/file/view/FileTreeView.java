package org.kobic.bioexpress.rcp.closha.file.view;

import java.net.URL;
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
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.file.provider.FileTreeViewContentProvider;
import org.kobic.bioexpress.rcp.file.provider.FileTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.presenter.Presenter;
import org.kobic.bioexpress.rcp.viewer.view.ViewerView;
import org.kobic.bioexpress.model.file.FileModel;

public class FileTreeView implements Presenter {

	final static Logger logger = Logger.getLogger(FileTreeView.class);

	@Inject
	private MDirtyable dirty;

	@Inject
	private EMenuService eMenuService;

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	@Optional
	private MPartStack mPartStack;

	@Inject
	@Optional
	private IWorkbench iworkbench;

	@Inject
	private IEventBroker iEventBroker;

	private TreeViewer treeViewer;

	@Inject
	private UISynchronize sync;

	private List<Object> expands;

	@PostConstruct
	public void createComposite(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setLabelProvider(new FileTreeViewLabelProvider());
		treeViewer.setContentProvider(new FileTreeViewContentProvider());
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

		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.GBOX_LOCAL_BROWSER_POPUP_MENU_ID);

		init();
	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	@Inject
	@Optional
	private void fileReloadsubscribeTopicTodoUpdated(
			@UIEventTopic(Constants.CLOSHA_FILE_TREE_DATA_RELOAD_EVENT_BUS_NAME) String uid) {

		logger.info("tree view reload job id: " + uid + "\tpath: []");

//		expands.clear();

		setTreeDataBind();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		bind();
		event();

		expands = new ArrayList<Object>();
	}

	@Override
	public void bind() {
		// TODO Auto-generated method stub

		setTreeDataBind();

	}

	private void setTreeDataBind() {

		Job job = new Job("Binding local data files") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("Local data file.", IProgressMonitor.UNKNOWN);

				List<FileModel> root = Activator.getFileService().getRoot();

				monitor.beginTask("Loading Local data file list.", root.size());

				for (FileModel file : root) {
					monitor.worked(1);
					monitor.setTaskName(file.getPath());
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

	@Override
	public void event() {
		// TODO Auto-generated method stub

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {

				String partID = ePartService.getActivePart().getElementId();

				System.out.println(partID);

				if (!treeViewer.getSelection().isEmpty()) {

					Object object = treeViewer.getStructuredSelection().getFirstElement();

					if (object instanceof FileModel) {

						FileModel fileModel = (FileModel) object;

						System.out.println(fileModel.getName());

						System.out.println(fileModel.isIsDir());
						System.out.println(fileModel.isIsFile());

						if (fileModel.isIsDir()) {

							System.out.println(partID + "\t" + Constants.GBOX_LOCAL_TREE_PART_ID);

							if (partID.equals(Constants.GBOX_LOCAL_TREE_PART_ID)) {

								MPart mPart = ePartService.findPart(Constants.FILE_TABLE_VIEW_ID);

								System.out.println(mPart.getParent().getElementId());
								System.out.println(mPart.getParent().getSelectedElement().getElementId());

								ePartService.showPart(mPart, PartState.VISIBLE);

								iEventBroker.send(Constants.FILE_TREE_SELECT_EVENT_BUS_NAME, fileModel);
							} else {
								expands.add(object);
								treeViewer.setExpandedElements(expands.toArray());
							}

						} else if (fileModel.isIsFile()) {

							if (partID.equals(Constants.CLOSHA_FILE_TREE_VIEW_ID)) {

								Display.getDefault().asyncExec(new Runnable() {

									@SuppressWarnings("unused")
									@Override
									public void run() {
										// TODO Auto-generated method stub

										IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench()
												.getBrowserSupport();

										try {
											URL url = new URL("file:///" + fileModel.getPath());
											String browserID = fileModel.getPath();
											String title = fileModel.getName();
											String tooltip = fileModel.getName();

											IWebBrowser br = browserSupport.createBrowser(
													IWorkbenchBrowserSupport.AS_EDITOR
															| IWorkbenchBrowserSupport.NAVIGATION_BAR
															| IWorkbenchBrowserSupport.LOCATION_BAR,
													browserID, title, tooltip);

											// br.openURL(url);
											MPart part = ePartService.findPart("org.eclipse.ui.console.FileViewer");
											ePartService.showPart(part, PartState.ACTIVATE); // Show part
											ViewerView fv = (ViewerView) part.getObject();
											fv.loadURL("file:///" + fileModel.getPath());

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
							}
						}
					}
				}
			}
		});

		treeViewer.addTreeListener(new ITreeViewerListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
			}
		});
	}
}