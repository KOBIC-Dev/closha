package org.kobic.bioexpress.rcp.workspace.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.ide.IDE;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClient;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.CanvasUtil;
import org.kobic.bioexpress.rcp.utils.EventUtils;
import org.kobic.bioexpress.rcp.utils.InfoUtils;
import org.kobic.bioexpress.rcp.workspace.provider.WorkspaceTreeViewContentProvider;
import org.kobic.bioexpress.rcp.workspace.provider.WorkspaceTreeViewLabelProvider;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@SuppressWarnings("unused")
public class WorkspaceView {

	final static Logger logger = Logger.getLogger(WorkspaceView.class);

	private Composite parent;

	private TreeViewer treeViewer;

	@Inject
	private MDirtyable dirty;

	@Inject
	private ESelectionService selectionService;

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
	private UISynchronize sync;

	@Inject
	private IEventBroker iEventBroker;
	
	private List<Object> expands;

	private class NodeDoubleClickListener implements EventHandler {
		
		public static final String TOPIC = "123456";

		@Override
		public void handleEvent(Event event) {
			Object data = event.getProperty("org.eclipse.e4.data");
			System.out.println("evnet..!!! " + data);
		}
	}

	private final NodeDoubleClickListener nodeDoubleClickListener = new NodeDoubleClickListener();

	@PostConstruct
	public void createComposite(Composite parent, EMenuService eMenuService) {

		this.parent = parent;
		this.parent.setLayout(new GridLayout(1, false));

		PatternFilter filter = new PatternFilter();
		int styleBits = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI;

		@SuppressWarnings("deprecation")
		FilteredTree filteredTree = new FilteredTree(parent, styleBits, filter, true);
		filteredTree.setQuickSelectionMode(true);
		filteredTree.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		treeViewer = filteredTree.getViewer();
		treeViewer.setLabelProvider(new WorkspaceTreeViewLabelProvider());
		treeViewer.setContentProvider(new WorkspaceTreeViewContentProvider());
		treeViewer.getTree().setHeaderVisible(false);
		treeViewer.getTree().setLinesVisible(false);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {

				if (e1 instanceof WorkspaceModel && e2 instanceof WorkspaceModel) {
					WorkspaceModel t1 = (WorkspaceModel) e1;
					WorkspaceModel t2 = (WorkspaceModel) e2;
					return t1.getWorkspaceName().toLowerCase().compareTo(t2.getWorkspaceName().toLowerCase());
				} else {
					PipelineDataModel t1 = (PipelineDataModel) e1;
					PipelineDataModel t2 = (PipelineDataModel) e2;
					return t1.getPipelineName().toLowerCase().compareTo(t2.getPipelineName().toLowerCase());
				}

			};
		});

		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.WORKSPACE_POPUP_MENU_ID);
		init();
		//
		EventUtils.registerForEvent(NodeDoubleClickListener.TOPIC, nodeDoubleClickListener);

	}

	private void setTreeDataBind() {

//		sync.asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				String memberID = Activator.getMember().getMemberId();
//
//				WorkspaceClient workspaceClient = new WorkspaceClientImpl();
//				List<WorkspaceModel> list = workspaceClient.getUserWorkspace(memberID);
//
//				treeViewer.setInput(list);
//			}
//		});

		Job job = new Job("Binding workspace") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("Workspace information.", IProgressMonitor.UNKNOWN);

				String memberID = Activator.getMember().getMemberId();

				WorkspaceClient workspaceClient = new WorkspaceClientImpl();
				List<WorkspaceModel> list = workspaceClient.getUserWorkspace(memberID);

				monitor.beginTask("Loading workspace list.", list.size());

				for (WorkspaceModel workspace : list) {
					monitor.worked(1);
					monitor.setTaskName(workspace.getWorkspaceName() + "(" + workspace.getWorkspaceID() + ")");
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
						treeViewer.setInput(list);
					}
				});

				monitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();
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

			public void doubleClick(DoubleClickEvent event) {
				
				if (!treeViewer.getSelection().isEmpty()) {

					Object object = treeViewer.getStructuredSelection().getFirstElement();

					if (object instanceof WorkspaceModel) {

						WorkspaceModel workspace = (WorkspaceModel) object;
						System.out.println(workspace.getWorkspaceID());

						MPart mPart = ePartService.findPart(Constants.DETAIL_DATA_VIEW_ID);

						ePartService.showPart(mPart, PartState.ACTIVATE);

						Map<String, Object> map = InfoUtils.getInstance().converDataToMap(workspace);

						iEventBroker.send("_TEST_011", map);
						
						/**
						 * object double click to expanded
						 */
						expands.add(object);
						treeViewer.setExpandedElements(expands.toArray());
					}

					if (object instanceof PipelineDataModel) {

						PipelineDataModel pipelineData = (PipelineDataModel) object;
						/**
						 * event
						 */

						MPart mPart1 = ePartService.findPart(Constants.DETAIL_DATA_VIEW_ID);

						ePartService.showPart(mPart1, PartState.ACTIVATE);

						Map<String, Object> map = InfoUtils.getInstance().converDataToMap(pipelineData);

						iEventBroker.send("_TEST_011", map);

						/**
						 * 
						 */

						MPart mPart2 = ePartService.findPart(Constants.PIPELINE_DETAIL_VIEW_ID);

						ePartService.showPart(mPart2, PartState.ACTIVATE);

						iEventBroker.send("_TEST_012", pipelineData);

						/**
						 * 
						 */
						PipelineModel pipeline = null;

						PipelineClient pipelineClient = new PipelineClientImpl();
						try {
							pipeline = pipelineClient.getPipeline(pipelineData.getRawID());
							// pipelineClient.updatePipeline(pipelineData.getRawID(), pipeline);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//
						try {
							String emfData = pipeline.getPipelineData().getPipelineTemplate();
							pipelineData = pipeline.getPipelineData();
							String fname = pipelineData.getPipelineName();// + "(" + pipelineData.getRawID() + ")";//
																			// item.split("
																			// ")[2];
							IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
							IProject project = root.getProject("temp");

							String path = pipeline.getPipelineData().getWorkspaceName();
							IFolder parentF = CanvasUtil.createFolders(path, project);
							IFile file = parentF.getFile(fname + ".iwf");
							// IFile file = project.getFile(fname + ".iwf");

							String charset = "UTF-8";
							String status = pipelineData.getStatus();
							String pattern = "pstatus=\"(?:.*?)\"";
							Pattern pat = Pattern.compile(pattern);
							Matcher m = pat.matcher(emfData);
							emfData = m.replaceFirst("pstatus=\"" + status + "\"");
							System.out.println("pipeline status : " + status);
							if (file.exists()) {
								try {
									ByteArrayInputStream inputStream = new ByteArrayInputStream(emfData.getBytes());
									file.setContents(inputStream, true, true, null);
									inputStream.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								//
								IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
										.getActivePage();
								IEditorPart part = IDE.openEditor(page, file, false);
								WorkflowDiagramEditor editor = (WorkflowDiagramEditor) part;
								editor.setPipeline(pipeline);
								// EventUtils.getEventBroker().send("0000000001", "");
							} else {
								try {
									ByteArrayInputStream inputStream = new ByteArrayInputStream(emfData.getBytes());
									file.create(inputStream, true, null);
									inputStream.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								//
								IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
										.getActivePage();
								IEditorPart part = IDE.openEditor(page, file, false);
								WorkflowDiagramEditor editor = (WorkflowDiagramEditor) part;
								editor.setPipeline(pipeline);
							}
						} catch (Exception e) {
							e.printStackTrace();
							MessageDialog.openError(parent.getShell(), "Creating Workflow Error",
									"Creating workflow error message: " + e.getMessage());
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
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME) String uid) {
		
//		expands.clear(); 
		
		setTreeDataBind();
	}
}