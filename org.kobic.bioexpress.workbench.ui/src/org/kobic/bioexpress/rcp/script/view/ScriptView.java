package org.kobic.bioexpress.rcp.script.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.impl.HandledToolItemImpl;
import org.eclipse.e4.ui.model.application.ui.menu.impl.ToolBarContributionImpl;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.kobic.bioexpress.channel.client.closha.CloshaClient;
import org.kobic.bioexpress.channel.client.closha.CloshaClientImpl;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.client.script.ScriptClient;
import org.kobic.bioexpress.channel.client.script.ScriptClientImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.script.provider.ScriptTreeViewContentProvider;
import org.kobic.bioexpress.rcp.script.provider.ScriptTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.utils.CanvasUtil;

@SuppressWarnings("restriction")
public class ScriptView {

	final static Logger logger = Logger.getLogger(ScriptView.class);

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

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	private EModelService mModelService;

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

	private Composite parent;

	private List<Object> expands;

	@PostConstruct
	public void createComposite(Composite parent) {

		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setLabelProvider(new ScriptTreeViewLabelProvider());
		treeViewer.setContentProvider(new ScriptTreeViewContentProvider());
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

		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.SCRIPT_POPUP_MENU_ID);

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

						if (fileModel.isIsDir()) {
							/**
							 * object double click to expanded
							 */
							expands.add(object);
							treeViewer.setExpandedElements(expands.toArray());
						} else {
							openEditor(fileModel);
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

//		sync.syncExec(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				String memberID = Activator.getMember().getMemberId();
//
//				CloshaClient closhaClient = new CloshaClientImpl();
//				List<FileModel> files = closhaClient.getUserRootScriptFiles(memberID);
//
//				treeViewer.setInput(files);
//			}
//		});

		Job job = new Job("Binding script files") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("Stating get script file.", IProgressMonitor.UNKNOWN);

				String memberID = Activator.getMember().getMemberId();

				CloshaClient closhaClient = new CloshaClientImpl();
				List<FileModel> files = closhaClient.getUserRootScriptFiles(memberID);

				monitor.beginTask("Loading script file list.", files.size());

				for (FileModel file : files) {
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
						treeViewer.setInput(files);
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
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.SCRIPT_REFRESH_EVENT_BUS_NAME) String uid) {

//		expands.clear();

		setTreeDataBind();
	}

	@Inject
	@Optional
	private void subscribeTopicTodoScriptDelete(@UIEventTopic(Constants.SCRIPT_DELETE_EVENT_BUS_NAME) String uid) {

		if (!treeViewer.getStructuredSelection().isEmpty()) {

			TreeItem[] items = treeViewer.getTree().getSelection();

			List<FileModel> enable = new ArrayList<FileModel>();
			List<String> disable = new ArrayList<String>();
			List<String> paths = new ArrayList<String>();

			for (TreeItem item : items) {

				FileModel fileModel = (FileModel) item.getData();

				if (fileModel.isIsDir()) {
					if (fileModel.isIsSub()) {
						disable.add(fileModel.getName());
					} else {
						enable.add(fileModel);
					}
				} else {
					enable.add(fileModel);
				}
			}

			String message = "Are you sure you want to delete " + enable.size() + " folder or file?";

			if (disable.size() != 0) {
				message += "\nCannot Remove Folder, Directory is not empty.\n" + disable.toString();
			}

			boolean res = MessageDialog.openConfirm(parent.getShell(), "Script File Delete", message);

			if (res) {

				FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();

				CloshaClient closhaClient = new CloshaClientImpl();

				Job job = new Job("Delete script files") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						// TODO Auto-generated method stub

						monitor.beginTask("Deleting tasks from the Process cloud storage.", enable.size());

						for (FileModel fileModel : enable) {

							monitor.worked(1);
							monitor.setTaskName(fileModel.getName());

							if (!fileModel.isIsSymbol()) {
								if (fileModel.isIsDir()) {
									fileUtilsClient.deleteDir(fileModel.getPath());
								} else {
									fileUtilsClient.deleteFile(fileModel.getPath());
									paths.add(fileModel.getPath());
								}
							}
						}

						monitor.beginTask("Deleting tasks from the Process database.", IProgressMonitor.UNKNOWN);

						ScriptClient scriptClient = new ScriptClientImpl();
						scriptClient.deleteScript(paths);

						sync.asyncExec(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								String memberID = Activator.getMember().getMemberId();
								List<FileModel> files = closhaClient.getUserRootScriptFiles(memberID);
								treeViewer.setInput(files);
							}
						});

						if (monitor.isCanceled()) {
							monitor.done();
						}

						monitor.done();

						return Status.OK_STATUS;
					}
				};
				job.schedule();

				setTreeDataBind();
			}
		}

//		boolean res = MessageDialog.openConfirm(shell, "Delete Script File",
//				fileModel.getName() + "\n\nAre you sure you want to delete the script file?");
//
//		if (res) {
//			FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
//
//			String path = fileModel.getPath();
//
//			if (fileModel.isIsDir()) {
//				fileUtilsClient.deleteDir(path);
//			} else {
//				fileUtilsClient.deleteFile(path);
//			}
//
//			iEventBroker.send(Constants.SCRIPT_REFRESH_EVENT_BUS_NAME, fileModel.getName());
//		}
//		
//		setTreeDataBind();
	}

	@SuppressWarnings({ "unused" })
	private void openEditor(FileModel fileModel) {
		
		if (fileModel.isIsFile()) {
			
			FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
			System.out.println(fileModel.getPath());
			List<String> code = fileUtilsClient.readFile(fileModel.getPath());

			// System.out.println(code);
			String script = null;
			for (String str : code) {
				if (script == null)
					script = str;
				else
					script = script + "\n" + str;
			}
			// open sh or python
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject("temp");
			String path = fileModel.getParentPath();
			String name = fileModel.getName();
			System.out.println(path + " " + name);
			IFolder parentF = CanvasUtil.createFolders(path, project);
//			
			IFile file = parentF.getFile(name);
			if (file.exists()) {
				try {
					if (script == null) {
						script = "";
					}
					ByteArrayInputStream inputStream = new ByteArrayInputStream(script.getBytes());
					file.setContents(inputStream, true, true, null);
					inputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				try {
					if (script == null) {
						script = "";
					}
					ByteArrayInputStream inputStream = new ByteArrayInputStream(script.getBytes());
					file.create(inputStream, true, null);
					inputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				List<MToolBarContribution> menus = mApplication.getToolBarContributions();
				for (MToolBarContribution menu : menus) {
					// System.out.println(menu);
					List<MToolBarElement> eles = menu.getChildren();
					for (MToolBarElement ele : eles) {
						try {
							// System.out.println(ele.getElementId()+"
							// "+((HandledToolItemImpl)ele).getLabel());
						} catch (Exception ex) {

						}
						System.out.println(ele.getElementId() + " " + ele.toString());
						if (ele.getElementId().equals("org.eclipse.jdt.ui.edit.text.java.toggleMarkOccurrences")) {
							System.out.println(ele);

							HandledToolItemImpl item = (HandledToolItemImpl) ele;
							item.setLabel("");
							item.setTooltip("");
							item.setIconURI("platform:/plugin/org.kobic.bioexpress.workbench.ui/icons/closha.png");
							item.setVisible(false);

							ToolBarContributionImpl toobar = (ToolBarContributionImpl) menu;
						}
					}
				}
				Object obj = mModelService.find("org.eclipse.jdt.ui.edit.text.java.toggleMarkOccurrences",
						mApplication);
				if (obj != null) {
					// System.out.println(obj);
					mModelService.deleteModelElement((MUIElement) obj);
				}
				//
				IEditorPart part = IDE.openEditor(page, file, true);

				//
				part.addPropertyListener(new IPropertyListener() {
					boolean c = false;

					@Override
					public void propertyChanged(Object source, int propId) {
						// TODO Auto-generated method stub
						if (propId == 257) {
							if (c) {
								// save editor to server..
								TextEditor editor = (TextEditor) source;
								IDocumentProvider provider = editor.getDocumentProvider();
								IEditorInput input = editor.getEditorInput();
								IDocument document = provider.getDocument(input);
								String text = document.get();
								// System.out.println(text);
								FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
								System.out.println(fileModel.getPath());
								fileUtilsClient.writeFile(fileModel.getPath(), text);// readFile(fileModel.getPath());
								c = false;
							} else {
								c = true;
							}
						}
						System.out.println(source + " " + propId);
					}
				});

				page.addPartListener(new IPartListener() {

					@Override
					public void partOpened(IWorkbenchPart part) {
						// TODO Auto-generated method stub
						// System.out.println("partOpened editor script!!!");
					}

					@Override
					public void partDeactivated(IWorkbenchPart part) {
						// TODO Auto-generated method stub
						// System.out.println("partDeactivated editor script!!!");
					}

					@Override
					public void partClosed(IWorkbenchPart part) {
						// TODO Auto-generated method stub
						// System.out.println("partClosed " + part);
					}

					@Override
					public void partBroughtToTop(IWorkbenchPart part) {
						// TODO Auto-generated method stub
						// System.out.println("partBroughtToTop editor script!!!");
					}

					@Override
					public void partActivated(IWorkbenchPart part) {
						// TODO Auto-generated method stub
						// System.out.println("partActivated editor script!!!");
					}
				});
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
