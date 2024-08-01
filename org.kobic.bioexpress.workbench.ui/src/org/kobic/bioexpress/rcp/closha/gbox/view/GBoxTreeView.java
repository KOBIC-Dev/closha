package org.kobic.bioexpress.rcp.closha.gbox.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.opal.commons.SWTGraphicUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.gbox.provider.GBoxTreeViewContentProvider;
import org.kobic.bioexpress.rcp.gbox.provider.GBoxTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.presenter.Presenter;
import org.kobic.bioexpress.rcp.viewer.view.ViewerView;

public class GBoxTreeView implements Presenter {

	final static Logger logger = Logger.getLogger(GBoxTreeView.class);

	@Inject
	private MDirtyable dirty;

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

	@Inject
	private ESelectionService selectionService;

	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_PROPERTIES);

	private TreeViewer treeViewer;

	@Inject
	private UISynchronize sync;

	private Composite parent;

	private List<Object> expands;

	@PostConstruct
	public void createComposite(Composite parent, EMenuService eMenuService) {

		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setLabelProvider(new GBoxTreeViewLabelProvider());
		treeViewer.setContentProvider(new GBoxTreeViewContentProvider());
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

		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.GBOX_REMOTE_BROWSER_POPUP_MENU_ID);
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
	private void gboxReloadsubscribeTopicTodoUpdated(
			@UIEventTopic(Constants.CLOSHA_GBOX_TREE_DATA_RELOAD_EVENT_BUS_NAME) String uid) {

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

	@Override
	public void event() {
		// TODO Auto-generated method stub

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {

				if (!treeViewer.getSelection().isEmpty()) {

					Object object = treeViewer.getStructuredSelection().getFirstElement();

					if (object instanceof FileModel) {

						FileModel fileModel = (FileModel) object;
						System.out.println(fileModel.getName());

						String partID = ePartService.getActivePart().getElementId();

						if (partID.equals(Constants.GBOX_BROWSER_PART_ID)) {

							if (fileModel.isIsDir() && !fileModel.isIsSymbol()) {
								iEventBroker.send(Constants.GBOX_TREE_SELECT_EVENT_BUS_NAME, fileModel);
							}

						} else if (partID.equals(Constants.CLOSHA_GBOX_TREE_VIEW_ID)) {

							if (fileModel.isIsFile()) {

								List<String> extensionList = new ArrayList<String>();
								extensionList.add(Constants.FILE_EXTENSION_PNG);
								extensionList.add(Constants.FILE_EXTENSION_HTML);
								extensionList.add(Constants.FILE_EXTENSION_SVG);
								extensionList.add(Constants.FILE_EXTENSION_JPG);

								Display.getDefault().asyncExec(new Runnable() {

									@SuppressWarnings("unused")
									@Override
									public void run() {
										// TODO Auto-generated method stub

										IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench()
												.getBrowserSupport();

										try {
											String root = Constants.GBOX_RAPIDANT_ROOT + "/";
											String rapidantUrl = props
													.getProperty("closha.workbench.gbox.rapidant.url");
											String filePath = fileModel.getPath().replaceAll(root, "");
											String browserID = fileModel.getPath();
											String title = fileModel.getName();
											String tooltip = fileModel.getName();

											if (extensionList.contains(fileModel.getExtension().toLowerCase())) {

												System.out.println(fileModel.getExtension().toLowerCase());
												URL url = new URL(rapidantUrl + filePath);

												IWebBrowser br = browserSupport.createBrowser(
														IWorkbenchBrowserSupport.AS_EDITOR
																| IWorkbenchBrowserSupport.NAVIGATION_BAR
																| IWorkbenchBrowserSupport.LOCATION_BAR,
														browserID, title, tooltip);

												if (SWTGraphicUtil.isLinux() == true) {
													br.openURL(url);
												}else {
													MPart part = ePartService.findPart("org.eclipse.ui.console.FileViewer");
													ePartService.showPart(part, PartState.ACTIVATE); // Show part
													ViewerView fv = (ViewerView) part.getObject();
													fv.loadURL(url.toString());
												}
												
											} else {

												MessageDialog.openInformation(parent.getShell(), "Infomation",
														"Please download '" + fileModel.getName()
																+ "' file using GBox.");

											}

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
							} else {
								expands.add(object);
								treeViewer.setExpandedElements(expands.toArray());
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

	private void setTreeDataBind() {

		Job job = new Job("Binding GBox tree viewer data files") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("GBox tree viewer data file initializing..", IProgressMonitor.UNKNOWN);

				String path = Constants.GBOX_RAPIDANT_ROOT + "/" + Activator.getMember().getMemberId();

				List<FileModel> files = Activator.getRapidantService().getFiles(path);

				monitor.beginTask("Loading GBox tree viewer data file list..", files.size());

				for (FileModel file : files) {

					monitor.worked(1);
					monitor.setTaskName(file.getPath().replace(Constants.GBOX_RAPIDANT_ROOT, ""));
					try {
						TimeUnit.MILLISECONDS.sleep(Constants.DEFAULT_DELAY_MILLISECONDS_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				sync.syncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						treeViewer.setInput(files);
					}
				});

				monitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}