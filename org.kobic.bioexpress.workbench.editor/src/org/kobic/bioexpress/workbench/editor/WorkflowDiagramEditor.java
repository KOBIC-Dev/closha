/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.activation.ActivationID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.algorithms.PlatformGraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.DiagramImpl;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramComposite;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.graphiti.ui.internal.parts.DiagramEditPart;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.Constant;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.parameter.ParameterModel;
import org.kobic.bioexpress.model.pipeline.LinkModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.model.task.SubTaskResponseModel;
import org.kobic.bioexpress.model.task.TaskResponseModel;
import org.kobic.bioexpress.workbench.editor.configuration.Input;
import org.kobic.bioexpress.workbench.editor.configuration.NodeType;
import org.kobic.bioexpress.workbench.editor.configuration.Output;
import org.kobic.bioexpress.workbench.editor.features.AddWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.CreateArcFeature;
import org.kobic.bioexpress.workbench.editor.features.CreateWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.DeleteWFArcFeature;
import org.kobic.bioexpress.workbench.editor.features.DeleteWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.preferences.IWorkflowEditorPreferences;
import org.kobic.bioexpress.workbench.editor.settings.WFArcSettingsEditor;
import org.kobic.bioexpress.workbench.editor.utils.EventUtils;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.strikewire.snl.apc.GUIs.CompositeUtils;
import com.strikewire.snl.apc.GUIs.browsers.CommonFileSelectionDialog;
import com.strikewire.snl.apc.selection.DefaultSelectionProviderWithFocusListener;
import com.strikewire.snl.apc.selection.ISelectionProviderWithFocusListener;
import com.strikewire.snl.apc.selection.MultiControlSelectionProvider;
import com.strikewire.snl.apc.temp.TempFileManager;
import com.strikewire.snl.apc.util.ResourceUtils;

import gov.sandia.dart.workflow.domain.Arc;
import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFArc;
import gov.sandia.dart.workflow.domain.WFNode;
import gov.sandia.dart.workflow.util.PropertyUtils;
import gov.sandia.dart.workflow.util.WorkflowUtils;

@SuppressWarnings("restriction")
public class WorkflowDiagramEditor extends DiagramEditor implements IPropertyChangeListener {

	public WorkflowDiagramEditor() {
	}

	public static final String ID = "org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor";
//	private Label messageLabel;
	private AtomicReference<IFile> _baseFile = new AtomicReference<>();
	private Composite _pathComposite;
	private Composite _mainComposite;
	private StackLayout _mainStack;
	private Composite _rootComposite;
	private DefaultEditDomain _rootEditDomain;
	private KeyboardStateListener listener;

	// private ToolItem addNodeItem;
	private ToolItem saveItem;
	private ToolItem refreshItem;
	// private ToolItem clearItem;
	// private ToolItem revertItem;
	private ToolItem runItem;
	private ToolItem runAllItem;
	// private ToolItem deleteItem;
	// private ToolItem editItem;
	private ToolItem cancelItem;
	private ToolItem refreshPaletteItem;
	// private ToolItem showPipelineItem;

	private IStructuredSelection select;

	private Stack<NestedChild> _nestedChildrenStack = new Stack<>();

	private MultiControlSelectionProvider _selectionProvider = new MultiControlSelectionProvider();

	private PipelineModel pipeline;

	static boolean saveCheck = false;

	@SuppressWarnings("unused")
	private class PaletteRefeshClickListener implements EventHandler {

		public static final String TOPIC = "123456";

		@Override
		public void handleEvent(Event event) {
			Object data = event.getProperty("org.eclipse.e4.data");
			System.out.println("evnet..!!! " + data + " " + event);
			getDiagramBehavior().refreshPalette();
		}
	}

	private final PaletteRefeshClickListener paletteRefeshClickListener = new PaletteRefeshClickListener();

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		this._rootEditDomain = getEditDomain();
		WorkflowEditorPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		//
		EventUtils.registerForEvent("PROGRAM_CATEGORY_REFRESH_EVENT", paletteRefeshClickListener);
		//
		saveCheck = false;
		System.out.println("init state : " + saveCheck);
	}

	@Override
	public DiagramBehavior createDiagramBehavior() {
		return new WorkflowDiagramBehavior(this);
	}

	@Override
	public void setFocus() {
		super.setFocus();
		EventUtils.getEventBroker().send("REFRESH_PIPELINE_CHECK", pipeline.getRawID());
		updateHasRunData();
	}

	@SuppressWarnings("unused")
	@Override
	public void createPartControl(Composite parent) {
		Display d = parent.getDisplay();
		if (listener == null) {
			listener = new KeyboardStateListener(d);
			d.addFilter(SWT.KeyDown, listener);
			d.addFilter(SWT.KeyUp, listener);
		}

		GridLayout parentLayout = new GridLayout(1, false);
		parentLayout.marginHeight = 0;
		parentLayout.marginWidth = 0;
		parentLayout.horizontalSpacing = 0;
		parentLayout.verticalSpacing = 0;
		parent.setLayout(parentLayout);

		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GridLayout contentsLayout = new GridLayout(1, false);
		contentsLayout.marginHeight = 0;
		contentsLayout.marginWidth = 0;
		contentsLayout.horizontalSpacing = 0;
		contentsLayout.verticalSpacing = 0;
		contents.setLayout(contentsLayout);

		Composite toolbarLine = new Composite(contents, SWT.BORDER);
		toolbarLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		toolbarLine.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		GridLayout toolbarLayout = new GridLayout(4, false);
		toolbarLayout.marginWidth = 1;
		toolbarLayout.marginHeight = 1;
		toolbarLayout.verticalSpacing = 0;
		toolbarLine.setLayout(toolbarLayout);

		// BIOEXPRESS
		ToolBar toolBar = new ToolBar(toolbarLine, SWT.FLAT | SWT.RIGHT);
		refreshItem = new ToolItem(toolBar, SWT.NONE);
		refreshItem
				.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/refresh.png"));
		refreshItem.setToolTipText("Refresh Editor");

		ToolItem separatorItem0 = new ToolItem(toolBar, SWT.SEPARATOR);

		saveItem = new ToolItem(toolBar, SWT.NONE);
		saveItem.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/save.png"));
		saveItem.setToolTipText("Save Editor");
		saveCheck = false;

//		clearItem = new ToolItem(toolBar, SWT.NONE);
//		clearItem.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/clear.png"));
//		clearItem.setToolTipText("Clear Editor");

//		revertItem = new ToolItem(toolBar, SWT.NONE);
//		revertItem
//				.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/reverse.png"));
//		revertItem.setToolTipText("Revert Editor");

		ToolItem separatorItem1 = new ToolItem(toolBar, SWT.SEPARATOR);

		runItem = new ToolItem(toolBar, SWT.NONE);
		runItem.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/program_start.png"));
		runItem.setToolTipText("Run Selected Program");
		//
		runAllItem = new ToolItem(toolBar, SWT.NONE);
		runAllItem.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/all_start.png"));
		runAllItem.setToolTipText("Run Pipeline");

		cancelItem = new ToolItem(toolBar, SWT.NONE);
		cancelItem.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/all_stop.png"));
		cancelItem.setToolTipText("Cancel Running Program");

		ToolItem separatorItem2 = new ToolItem(toolBar, SWT.SEPARATOR);
		refreshPaletteItem = new ToolItem(toolBar, SWT.NONE);
		refreshPaletteItem
				.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/palette_refresh.png"));
		refreshPaletteItem.setToolTipText("Refresh Palette");
		ToolItem separatorItem3 = new ToolItem(toolBar, SWT.SEPARATOR);

//		addNodeItem = new ToolItem(toolBar, SWT.NONE);
//		addNodeItem.setImage(
//				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/program_new.png"));
//		addNodeItem.setToolTipText("Add program");

//		editItem = new ToolItem(toolBar, SWT.NONE);
//		editItem.setImage(
//				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/program_edit.png"));
//		editItem.setToolTipText("Edit program");

//		deleteItem = new ToolItem(toolBar, SWT.NONE);
//		deleteItem
//				.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor", "icons/delete.png"));
//		deleteItem.setToolTipText("Delete program");

//		ToolItem separatorItem3 = new ToolItem(toolBar, SWT.SEPARATOR);

//		showPipelineItem = new ToolItem(toolBar, SWT.NONE);
//		showPipelineItem.setToolTipText("Show Pipeline detail xml");
//		showPipelineItem.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.editor",
//				"icons/pipeline_edit_information.png"));

		//
		_pathComposite = new Composite(contents, SWT.NONE);
		_pathComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		_pathComposite.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		RowLayout pathLayout = new RowLayout();
		pathLayout.marginTop = 0;
		pathLayout.marginBottom = 0;
		pathLayout.marginLeft = 0;
		pathLayout.marginRight = 0;
		_pathComposite.setLayout(pathLayout);
		_pathComposite.setVisible(false);

		_mainComposite = new Composite(contents, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(_mainComposite);
		_mainStack = new StackLayout();
		_mainComposite.setLayout(_mainStack);

		_rootComposite = buildRootComposite(_mainComposite);
		_mainStack.topControl = _rootComposite;

		IFile file = getWorkflowFile();
		if (file != null) {
			updateRunLocationLabelFromMarker(file);
		}

		setToolbarEvents();
		disableItems();

//		messageLabel = new Label(contents, SWT.NONE);
//		messageLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
//		messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// System.out.println("grid unit
		// "+getDiagramTypeProvider().getDiagram().getGridUnit());

	}

	protected Composite buildRootComposite(Composite parent) {
		return buildGraphComposite(parent);
	}

	protected Composite buildGraphComposite(Composite parent) {
		Composite graphComposite = new Composite(parent, SWT.NONE);
		graphComposite.setLayout(new FillLayout());

		super.createPartControl(graphComposite);

		return graphComposite;
	}

	/**
	 * This belongs in the embedded plugin, but how?
	 * 
	 * @return
	 */

	@SuppressWarnings("unused")
	protected Object updateHasRunData() {
		// TODO : BIOEXPRESS
		System.out.println("updateHashRunData...");
		IToolBehaviorProvider provider = getDiagramTypeProvider().getCurrentToolBehaviorProvider();
		if (provider instanceof WorkflowToolBehaviorProvider) {
			IFile workflowFile = getWorkflowFile();
//			IPath path = runLocationCombo.getPath();
//			File workdir = new File(path.toOSString());
//			WorkflowTracker.updateRunData(workflowFile, workdir);
		}
		getDiagramTypeProvider().getDiagramBehavior().refresh();
		return null;
	}

	public void clearStatus() {
		// messageLabel.setText("");
		System.out.println("clear status");
	}
	
	public boolean checkSaveStatus() {
		if (saveCheck) {
			MessageDialog.openWarning(getEditorSite().getShell(), "Warning",
					"Please save the pipline first.");
			return true;
		}
		return false;
	}

	public void setStatus(String message) {
		// messageLabel.setText(message);
		System.out.println("set status " + message);
	}

	@Override
	protected void setInput(IEditorInput input) {
		// TODO BIOEXPRESS
		super.setInput(input);
		// openPaletteView();

		IFile file = getWorkflowFile();
		if (file != null && file.exists()) {// && runLocationCombo != null) {
			updateRunLocationLabelFromMarker(file);
		}
		// Would like to do this in persistency behavior but don't know how
		Diagram d = getDiagramTypeProvider().getDiagram();
		IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
		TransactionalEditingDomain editingDomain = getEditingDomain();
		final CommandStack commandStack = editingDomain.getCommandStack();
		final boolean[] changed = new boolean[1];
		commandStack.execute(new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				for (Connection c : getDiagramTypeProvider().getDiagram().getConnections()) {
					Object bo = getDiagramTypeProvider().getFeatureProvider().getBusinessObjectForPictogramElement(c);
					if (bo instanceof Arc) {
						changed[0] = changed[0] | WFArcSettingsEditor.updateConnectionAppearance(d, fp, (Arc) bo);
					}
				}
			}
		});
		try {
			if (changed[0]) {
				// doSave(null);
			} else {

			}
			((WorkflowDiagramBehavior) getDiagramBehavior()).markClean();
			//updateDirtyState();
		} catch (Exception e) {
			// MUST NOT FAIL
			e.printStackTrace();
		}

	}

	public IFile getWorkflowFile() {
		IDiagramTypeProvider diagramTypeProvider = getDiagramBehavior().getDiagramTypeProvider();
		Diagram diagram = diagramTypeProvider.getDiagram();
		Resource eResource = diagram.eResource();
		URI uri = eResource.getURI();
		String pathString = uri.toPlatformString(true);
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(pathString));
	}

	@Override
	public void refreshTitle() {
		IFile baseFile = getBaseFile();
		setPartName(baseFile.getName());
//		String name = URI.decode(getDiagramTypeProvider().getDiagram().eResource().getURI().lastSegment());	
//		setPartName(name);		
	}

	@SuppressWarnings("unused")
	protected void openPaletteView() {
		try {
			IWorkbenchPartSite site = getSite();
			if (site != null) {
				IWorkbenchWindow workbenchWindow = site.getWorkbenchWindow();
				if (workbenchWindow != null) {
					IWorkbenchPage activePage = workbenchWindow.getActivePage();
					IPerspectiveDescriptor per = activePage.getPerspective();
					if (activePage != null) {
						IViewPart view = activePage.showView("org.eclipse.gef.ui.palette_view",
								"org.eclipse.gef.ui.palette_view1", IWorkbenchPage.VIEW_VISIBLE);
						// System.out.println(view);
					}
				}
			}

		} catch (Exception e) {
			// Whatever
			e.printStackTrace();
		}
	}

	@Override
	protected DiagramEditorInput convertToDiagramEditorInput(IEditorInput input) throws PartInitException {
		if (input instanceof FileStoreEditorInput) {
			final java.net.URI uri = ((FileStoreEditorInput) input).getURI();
			final String path = uri.getPath();
			org.eclipse.emf.common.util.URI emfURI = org.eclipse.emf.common.util.URI.createFileURI(path);
			input = new URIEditorInput(emfURI);
		}
		return super.convertToDiagramEditorInput(input);
	}

	@Override
	public boolean isSaveAsAllowed() {
		System.out.println("open canvas..");
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		//
		try {
			super.doSave(monitor);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		Diagram dr = getDiagramTypeProvider().getDiagram();
		String pstatus = dr.getPstatus();
		if(!pstatus.equals("run") && !pstatus.equals("exec")) {
			savePipeline();			
		}	
		//
		saveItem.setEnabled(false);
		saveCheck = false;
	}
	
	public void savePipeline() {
		// TODO BIOEXPRESS
//		String pstatus = pipeline.getPipelineData().getStatus();
//		if (pstatus.equals("run") || pstatus.equals("exec")) {
//			MessageDialog.openWarning(getEditorSite().getShell(), "Warning Message",
//					"You can't modify or save pipeline when pipeline be running.");
//		} else {
//			
//		}
		List<String> _empty = new ArrayList<String>();
		String _null = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//
		try {
			IDiagramTypeProvider prov = getDiagramTypeProvider();
			WorkflowDiagramTypeProvider prov02 = (WorkflowDiagramTypeProvider)prov;
			Diagram dr = prov02.getDiagram();
			DiagramImpl dr02 = (DiagramImpl)dr;
			Resource res = dr02.eResource();
			XMIResourceImpl resImpl = (XMIResourceImpl)res;
			resImpl.save(stream, null);
			String finalString = new String(stream.toByteArray());
			//System.out.println(finalString);
			stream.close();
			PipelineClient pipelineClient = new PipelineClientImpl();
			try {
				List<LinkModel> links = pipeline.getLink();
				List<NodeModel> nodes = pipeline.getNode();

				IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
				EList<Connection> conns = dr.getConnections();
				EList<Shape> childs = dr.getChildren();
				if (conns.size() == 0)
					links = new ArrayList<LinkModel>();
				if (childs.size() == 0)
					nodes = new ArrayList<NodeModel>();
				//
				List<LinkModel> nlinks = new ArrayList<LinkModel>();
				List<NodeModel> nnodes = new ArrayList<NodeModel>();
				//노드 파라미터 링크가 안결려 있을 경우 다 초기화.
//				for(Connection conn:conns) {
//					Object eobj = fp.getBusinessObjectForPictogramElement(conn);
//					// System.out.println(obj);
//					if (eobj instanceof WFArc) {
//						WFArc arc = (WFArc) eobj;
//						for(NodeModel node:nodes) {
//							OutputPort outp = arc.getSource();
//							String nodeid = outp.getNode().getId();
//							if(nodeid.equals(node.getNodeID())) {
//								List<ParameterDataModel> outs = node.getParameter().getParameterOutput();
//								for(ParameterDataModel out:outs) {
//									if(outp.getId().equals(out.getParameterID())) {
//										String pname = node.getNodeData().getNodeName()+ ":" + arc.getTarget().getName();
//										out.sourceParamName = "NA";
//										break;
//									}
//								}
//							}
//						}
//						for(NodeModel node:nodes) {
//							InputPort inp = arc.getTarget();
//							String nodeid = inp.getNode().getId();
//							if(nodeid.equals(node.getNodeID())) {
//								List<ParameterDataModel> ins = node.getParameter().getParameterInput();
//								for(ParameterDataModel in:ins) {
//									if(inp.getId().equals(in.getParameterID())) {
//										in.targetParamName = new ArrayList<String>();
//									}
//								}
//							}
//						}
//					}
//				}
				//링크 체크..
				for (LinkModel link : links) {
					for (Connection conn : conns) {
						Object obj = fp.getBusinessObjectForPictogramElement(conn);
						// System.out.println(obj);
						if (obj instanceof WFArc) {
							WFArc arc = (WFArc) obj;
							String id = arc.getId();
							if (id.equals(link.getLinkID())) {
								OutputPort out = arc.getSource();
								InputPort in = arc.getTarget();
								link.setSourceParamID(out.getId());
								link.setSourceParamName(out.getName());
								link.setTargetParamID(in.getId());
								link.setTargetParamName(in.getName());
								nlinks.add(link);
								break;
							}
						}
					}
				}
				//노드 위치 체크..
				for (NodeModel node : nodes) {
					// find link...
					for (Shape anc : childs) {
						Object obj = fp.getBusinessObjectForPictogramElement(anc);
						if (obj instanceof WFNode) {
							WFNode wnode = (WFNode) obj;
							String id = wnode.getId();
							if (id.equals(node.getNodeID())) {
								EList<EObject> objs = anc.eContents();
								//위치 체크.
								for (EObject eo : objs) {
									// System.out.println(eo);
									if (eo instanceof PlatformGraphicsAlgorithm) {
										PlatformGraphicsAlgorithm plt = (PlatformGraphicsAlgorithm) eo;
										node.getNodeData().setX(plt.getX());
										node.getNodeData().setY(plt.getY());
										plt.getWidth();
										plt.getHeight();
										break;
									}
								}
							}
						}
					}
				}
				//노드 파람 체크
				for(NodeModel node: nodes) {
					// check params.
					String sNodeName = "";
					String tNodeName = "";
					ParameterModel param = node.getParameter();
					List<ParameterDataModel> inputs = param.getParameterInput();
					List<ParameterDataModel> outputs = param.getParameterOutput();
					//먼저 소스노드 정보 체크..
					for (ParameterDataModel p : outputs) {
						p.setNodeID(node.getNodeID());
						List<String> names = new ArrayList<String>();
						for (LinkModel link : nlinks) {
							String sourceNI = link.sourceID;
							String spi = link.sourceParamID;
							String spn = link.sourceParamName;
							String tpi = link.targetParamID;
							String tpn = link.targetParamName;
							if(p.getNodeID().equals(sourceNI)) {
								String pname = link.targetName + ":" + tpn;
								names.add(pname);
							}
						}
						p.setSourceParamName(_null);
						p.setTargetParamName(names);
						//p.setNodeID(node.getNodeID());
					}
					//
					for (ParameterDataModel p : inputs) {
						p.setNodeID(node.getNodeID());
						String pname = null;
						for (LinkModel link : nlinks) {
							String targetNI = link.targetID;
							String spi = link.sourceParamID;
							String spn = link.sourceParamName;
							String tpi = link.targetParamID;
							String tpn = link.targetParamName;
							if(p.getNodeID().equals(targetNI)) {
								pname = link.sourceName + ":" + spn;
								break;
							}
						}
						p.setSourceParamName(pname);
						p.setTargetParamName(_empty);
						//p.setNodeID(node.getNodeID());
					}
					param.setParameterInput(inputs);
					param.setParameterOutput(outputs);
					node.setParameter(param);
					//PipelineUtil.checkParams(node, links);
				}
				//
				pipeline.setLink(nlinks);
				pipeline.setNode(nodes);
				//
				pipeline.getPipelineData().setPipelineTemplate(finalString);
				pipelineClient.updatePipeline(pipeline.getPipelineData().getRawID(), pipeline);
				// send event...
				EventUtils.getEventBroker().send("SAVE_PIPELINE", pipeline.getRawID());
				//}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
		try {
			Diagram oldDiagram = getDiagramTypeProvider().getDiagram();
			URI oldURI = oldDiagram.eResource().getURI();
			oldURI = oldDiagram.eResource().getResourceSet().getURIConverter().normalize(oldURI);

			String scheme = oldURI.scheme();
			IFile oldFile = null;
			if ("platform".equals(scheme) && oldURI.segmentCount() > 1 && "resource".equals(oldURI.segment(0))) {
				StringBuffer platformResourcePath = new StringBuffer();
				for (int j = 1, size = oldURI.segmentCount(); j < size; ++j) {
					platformResourcePath.append('/');
					platformResourcePath.append(oldURI.segment(j));
				}
				oldFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformResourcePath.toString()));

			}
			if (oldFile == null) {
				Display.getDefault().beep();
				return;
			}
			CommonFileSelectionDialog dialog = new CommonFileSelectionDialog(getSite().getShell(), oldFile.toString(),
					"Select file to Save As...", SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.iwf" });
			// TODO make dialog browse to file!

//			SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());	
//			dialog.setOriginalFile(oldFile);

			int ret = dialog.open();
			if (ret != Window.OK) {
				return;
			}

			IFile newFile = dialog.getFirstSelectionAsCommonFile().asIFile();
			IPath newLoc = newFile.getFullPath();
//			IPath newLoc = dialog.getResult();	
//			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newLoc);

			if (newLoc != null) {
				URI newURI = URI.createPlatformResourceURI(newLoc.toString(), true);
				if (newFile.exists()) {
					if (oldURI.equals(newURI)) {
						doSave(null);
						return;
					} else {
						newFile.delete(true, null);
					}
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				oldDiagram.eResource().save(baos, null);
				ResourceUtils.setContents(newFile, baos.toString());

				DiagramEditorInput editorInput = new DiagramEditorInput(newURI, null);
				WorkflowDiagramEditor workflowEditor = (WorkflowDiagramEditor) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, WorkflowDiagramEditor.ID);
				Diagram newDiagram = workflowEditor.getDiagramTypeProvider().getDiagram();

				TransactionalEditingDomain editingDomain = workflowEditor.getEditingDomain();
				final CommandStack commandStack = editingDomain.getCommandStack();
				commandStack.execute(new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						newDiagram.setName(newFile.getName());
					}
				});
				workflowEditor.doSave(null);

				editingDomain.dispose();

				close();
			}
		} catch (CoreException | IOException e) {

		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (listener != null) {
			listener.display.removeFilter(SWT.KeyDown, listener);
			listener.display.removeFilter(SWT.KeyUp, listener);

			listener = null;
		}
		WorkflowEditorPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		//
		EventUtils.unregisterEventHandler(paletteRefeshClickListener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		getGraphicalViewer().getRootEditPart().refresh();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// System.out.println(selection);
		super.selectionChanged(part, selection);
		getGraphicalViewer().getRootEditPart().getViewer().getControl().redraw();
		getGraphicalViewer().getRootEditPart().refresh();
		try {
			IStructuredSelection sel = (IStructuredSelection) selection;
			this.select = sel;
			Object obj = sel.getFirstElement();
			if (obj instanceof ContainerShapeEditPart) {
//				clearItem.setEnabled(true);
//				deleteItem.setEnabled(true);
//				runItem.setEnabled(true);
//				runAllItem.setEnabled(true);
			} else if (obj instanceof DiagramEditPart) {
//				deleteItem.setEnabled(false);
//				runItem.setEnabled(true);
//				runAllItem.setEnabled(true);
			} else if (obj instanceof FreeFormConnectionImpl) {
				//
			}
		} catch (Exception ex) {
			//
		}
	}

	// TODO Change this to a URI after it's working
	public void setRunLocation(File folder) {
		final IFile workflowFile = getWorkflowFile();
		WorkflowUtils.updateRunLocationMarker(workflowFile, folder);
		updateRunLocationLabelFromMarker(workflowFile);
	}

	@Override
	public void updateDirtyState() {
		String pstatus = pipeline.getPipelineData().getStatus();
		// if (!dr.getPstatus().startsWith("run") ||
		// !dr.getPstatus().contains("PI-REG-0020") ||
		// !dr.getPstatus().contains("PI-REG-0030")) {
		if (!pstatus.equals("run") && !pstatus.equals("exec")) {
			super.updateDirtyState();
			saveItem.setEnabled(true);
			saveCheck = true;
			System.out.println("update check "+pstatus+" "+saveCheck);
		}else {
			saveItem.setEnabled(false);
			saveCheck = false;
			
		}		
		// firePropertyChange(IEditorPart.PROP_DIRTY);
		// System.out.println(isDirty());
		//System.out.println("update dirty state..."+pstatus+" "+isDirty());
		//if (isDirty()) {
		//	saveItem.setEnabled(true);
		//	saveCheck = true;
		//	System.out.println("udpateState : " + saveCheck);
//			clearItem.setEnabled(false);
//			deleteItem.setEnabled(true);
//			runItem.setEnabled(false);
//			runAllItem.setEnabled(false);
		//}
	}

	@Override
	public void hookGraphicalViewer() {
		GraphicalViewer gv = getGraphicalViewer();
		getSelectionSynchronizer().addViewer(gv);
		ISelectionProviderWithFocusListener provider = new DefaultSelectionProviderWithFocusListener(gv,
				gv.getControl());
		addGraphicalViewerToSelectionProvider(provider);
		getSite().setSelectionProvider(_selectionProvider);
	}

	private IFile getBaseFile() {
		return _baseFile.updateAndGet(f -> {
			return f != null ? f : getWorkflowFile();
		});
	}

	private void updateRunLocationLabelFromMarker(IFile file) {
		try {
			// runLocationCombo.setInput(file);
		} catch (Exception e) {
			WorkflowEditorPlugin.getDefault().logError("Problem getting run location marker", e);
		}
	}

	public void openNestedInternalWorkflow(WFNode node, String property) {
		try {
			NestedChild child = new NestedChild(node, property);
			child.load();
		} catch (CoreException e) {
			WorkflowEditorPlugin.getDefault().log(e.getStatus());
		}
	}

	private void addGraphicalViewerToSelectionProvider(ISelectionProviderWithFocusListener provider) {
		_selectionProvider.addSelectionProvider(provider);
	}

	private void setMainCanvas(Control control) {
		if (_mainStack.topControl != control) {
			_mainStack.topControl = control;
			_mainComposite.layout();
		}
	}

	private class NestedChild {
		private final WFNode _parentNode;
		private final String _property;
		private final DiagramEditorInput _input;
		private Link _lnk;
		private Label _arrow;
		private MyDiagramComposite _diagram;
		private ISelectionProviderWithFocusListener _wrappedProvider;

		private NestedChild(WFNode parentNode, String property) throws CoreException {
			this._parentNode = parentNode;
			this._property = property;

			IFolder tmpDir = TempFileManager.getUniqueTempDir();
			IFile tmpFile = tmpDir.getFile(parentNode.getName() + ".iwf.tmp");

			if (tmpFile.exists()) {
				tmpFile.delete(false, new NullProgressMonitor());
			}

			String fileContents = getContents();

			DiagramEditorInput input;
			if (StringUtils.isBlank(fileContents)) {
				Diagram diagram = Graphiti.getPeCreateService().createDiagram("dartWorkflow", _parentNode.getName(),
						true);
				URI uri = URI.createPlatformResourceURI(tmpFile.getFullPath().toString(), true);
				FileService.createEmfFileForDiagram(uri, diagram);
				String providerId = GraphitiUi.getExtensionManager()
						.getDiagramTypeProviderId(diagram.getDiagramTypeId());
				input = DiagramEditorInput.createEditorInput(diagram, providerId);
			} else {
				InputStream source = new ByteArrayInputStream(fileContents.getBytes());
				tmpFile.create(source, false, new NullProgressMonitor());
				java.net.URI locationURI = tmpFile.getLocationURI();
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(locationURI);
				FileStoreEditorInput fsinput = new FileStoreEditorInput(fileStore);
				input = convertToDiagramEditorInput(fsinput);
			}

			this._input = input;
		}

		private String getContents() {
			return PropertyUtils.getProperty(_parentNode, _property);
		}

		private void load() {
			if (_diagram == null) {
				makeButton();

				_diagram = new MyDiagramComposite(_mainComposite, SWT.NONE);
				_diagram.setInput(_input);

				// listen for changes
				IDiagramTypeProvider typeProvider = _diagram.getDiagramTypeProvider();
				Diagram diagram = typeProvider.getDiagram();
				Resource resource = diagram.eResource();
				resource.eAdapters().add(new ModificationListener());

				// hook ourselves up as a selection provider
				GraphicalViewer gv = _diagram.getGraphicalViewer();
				_wrappedProvider = new DefaultSelectionProviderWithFocusListener(gv, gv.getControl());
				addGraphicalViewerToSelectionProvider(_wrappedProvider);

				_nestedChildrenStack.push(this);
			} else {
				// we're already loaded, so we just need to pop down to us
				while (!_nestedChildrenStack.empty() && _nestedChildrenStack.peek() != this) {
					_nestedChildrenStack.pop().dispose();
				}
			}

			// show this one
			setMainCanvas(_diagram);
			updateEditDomainAndPalette(_diagram.getEditDomain(), _diagram.getDiagramBehavior().myGetPaletteRoot(),
					false);
		}

		private void updateEditDomainAndPalette(DefaultEditDomain editDomain, PaletteRoot paletteRoot,
				boolean clearRoot) {
			DefaultEditDomain origEditDomain = getEditDomain();
			if (origEditDomain == editDomain) {
				return;
			}

			if (clearRoot) {
				editDomain.setPaletteRoot(null);
			}
			setEditDomain(editDomain);
			editDomain.setPaletteViewer(origEditDomain.getPaletteViewer());
			editDomain.setPaletteRoot(paletteRoot);
		}

		private void dispose() {
			if (_wrappedProvider != null) {
				_selectionProvider.removeSelectionProvider(_wrappedProvider);
				_wrappedProvider = null;
			}

			if (_diagram != null) {

				if (!_diagram.isDisposed()) {
					_diagram.dispose();
				}
				_diagram = null;
			}

			if (_lnk != null) {
				if (!_lnk.isDisposed()) {
					_lnk.dispose();
				}
				_lnk = null;
			}

			if (_arrow != null) {
				if (!_arrow.isDisposed()) {
					_arrow.dispose();
				}
				_arrow = null;
			}
		}

		private void makeButton() {
			if (!_pathComposite.isVisible()) {
				showPathComposite(true);
				addTopFileButton();
			}

			_arrow = new Label(_pathComposite, SWT.NONE);
			_arrow.setText(">");
			_lnk = new Link(_pathComposite, SWT.NONE);
			_lnk.setText("<a>" + _parentNode.getName() + "</a>");
			_lnk.setToolTipText(_parentNode.getLabel());
			_lnk.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					load();
				}
			});

			_pathComposite.getParent().layout(true, true);
		}

		private void addTopFileButton() {
			Link lnk = new Link(_pathComposite, SWT.NONE);

			IFile baseFile = getBaseFile();
			lnk.setText("<a>" + baseFile.getName() + "</a>");
			lnk.setToolTipText("Root Workflow File");
			lnk.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					while (!_nestedChildrenStack.empty()) {
						_nestedChildrenStack.pop().dispose();
					}
					CompositeUtils.removeChildrenFromComposite(_pathComposite);
					showPathComposite(false);
					setMainCanvas(_rootComposite);
					updateEditDomainAndPalette(_rootEditDomain, getPaletteRoot(), true);
				}
			});
		}

		private void showPathComposite(boolean show) {
			if (_pathComposite == null || _pathComposite.isDisposed() || _pathComposite.isVisible() == show) {
				return;
			}

			_pathComposite.setVisible(show);
			Layout layout = _pathComposite.getLayout();
			if (layout instanceof RowLayout) {
				RowLayout rowLayout = (RowLayout) layout;
				rowLayout.marginHeight = show ? 3 : 0;
				rowLayout.marginWidth = show ? 3 : 0;
			}

			_pathComposite.getParent().layout(true, true);
		}

		private class ModificationListener extends AdapterImpl {
			private AtomicInteger _retry = new AtomicInteger();

			private final Job _saveJob = new Job("Saving to parent") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						saveToParent();
					} catch (Throwable t) {
						if (_retry.get() > 10) {
							String msg = "Unable to save internal nested workflow after 10 tries.";
							return WorkflowEditorPlugin.getDefault().newErrorStatus(msg, t);
						} else {
							String msg = "Error attempting to save internal nested workflow.  Attempt: "
									+ (_retry.get() + 1) + ".  Will try again...";
							WorkflowEditorPlugin.getDefault().logWarning(msg, t);
						}
						scheduleSave(true);
					}
					return Status.OK_STATUS;
				}
			};

			public ModificationListener() {
				_saveJob.setSystem(true);
			}

			private void scheduleSave(boolean retry) {
				if (retry) {
					_retry.incrementAndGet();
				} else {
					_retry.set(0);
				}
				_saveJob.schedule(500);
			}

			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getFeatureID(null) == Resource.RESOURCE__IS_MODIFIED) {
					if (msg.getNewBooleanValue()) {
						scheduleSave(false);
					}
				}
			}

			private String getDiagramAsString() throws IOException {
				IDiagramTypeProvider typeProvider = _diagram.getDiagramTypeProvider();
				Diagram diagram = typeProvider.getDiagram();
				Resource resource = diagram.eResource();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				resource.save(outputStream, null);
				return outputStream.toString();
			}

			private void saveToParent() throws IOException {
				String newValue = getDiagramAsString();
				String oldValue = getContents();
				if (StringUtils.equals(newValue, oldValue)) {
//					System.out.println("  no change");
					return;
				}

				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(_parentNode);

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					public void doExecute() {
						PropertyUtils.setProperty(_parentNode, _property, newValue);
					}
				});
				getSite().getShell().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						updateDirtyState();
					}
				});
			}
		}
	}

	private class MyDiagramComposite extends DiagramComposite {
		public MyDiagramComposite(Composite parent, int style) {
			super(WorkflowDiagramEditor.this, parent, style);
		}

		@Override
		protected DiagramBehavior createDiagramBehavior() {
			return new MyWorkflowDiagramBehavior(this);
		}

		@Override
		public MyWorkflowDiagramBehavior getDiagramBehavior() {
			return (MyWorkflowDiagramBehavior) super.getDiagramBehavior();
		}

		@Override
		public void configureGraphicalViewer() {
			super.configureGraphicalViewer();
			if (WorkflowEditorPlugin.getDefault().getPreferenceStore()
					.getBoolean(IWorkflowEditorPreferences.CONNECTIONS_BEHIND)) {
				try {
					GraphicalViewer viewer = getGraphicalViewer();
					pushConnectionsToBack(viewer);

				} catch (Exception e) {
					// Oh well, something has changed. Connections will be in front.
					WorkflowEditorPlugin.getDefault()
							.logWarning("Unable to move connection layer behind figures in nested workflow", e);
				}
			}

		}
	}

	private static class MyWorkflowDiagramBehavior extends WorkflowDiagramBehavior {

		public MyWorkflowDiagramBehavior(IDiagramContainerUI diagramContainer) {
			super(diagramContainer);
		}

		@Override
		public void initActionRegistry(ZoomManager zoomManager) {
			// NOOP
		}

		/**
		 * Overriding for visibility
		 */
		protected PaletteRoot myGetPaletteRoot() {
			return getPaletteRoot();
		}

		@Override
		public void disposeAfterGefDispose() {
			IDiagramContainerUI container = getDiagramContainer();
			if (container != null) {
				container.setEditDomain(null);
			}
			super.disposeAfterGefDispose();
		}
	}

	@Override
	public void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		if (WorkflowEditorPlugin.getDefault().getPreferenceStore()
				.getBoolean(IWorkflowEditorPreferences.CONNECTIONS_BEHIND)) {
			try {
				GraphicalViewer viewer = getGraphicalViewer();
				pushConnectionsToBack(viewer);

			} catch (Exception e) {
				// Oh well, something has changed. Connections will be in front.
				WorkflowEditorPlugin.getDefault().logWarning("Unable to move connection layer behind figures", e);
			}
		}
	}

	/**
	 * By putting the connection layer before the primary in the layer stack, we can
	 * make GEF draw connections underneath the other figures. We could do this by
	 * overriding createPrintableLayers in a custom RootEditPane, and calling
	 * setRootEditPart on the GraphicalViewer, but unfortunately Graphiti already
	 * provides one, in an internal class.
	 */

	private static void pushConnectionsToBack(GraphicalViewer viewer)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// The main layered pane contains several sub-layered panes; the middle one
		// contains the "printable layers."
		// We're just getting that "sub-stack."
		FreeformGraphicalRootEditPart rootEditPart = (FreeformGraphicalRootEditPart) viewer.getRootEditPart();
		Method m = FreeformGraphicalRootEditPart.class.getDeclaredMethod("getPrintableLayers");
		m.setAccessible(true);

		LayeredPane pane = (LayeredPane) m.invoke(rootEditPart);
		Layer primary = pane.getLayer(LayerConstants.PRIMARY_LAYER);
		Layer connections = pane.getLayer(LayerConstants.CONNECTION_LAYER);

		// The reason we're here; move the connection layer in the stack.
		pane.remove(connections);
		pane.addLayerAfter(connections, LayerConstants.CONNECTION_LAYER, primary);
	}

	@Override
	protected void createActions() {
		super.createActions();
		// Print action enablement testing causes issues on GTK. Weirdly, although this
		// ticet says it's fixed, it's not:
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=449384
		IAction printAction = getActionRegistry().getAction(ActionFactory.PRINT.getId());
		getActionRegistry().removeAction(printAction);
	}

	private void focusEditor(EObject bo) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId("dartWorkflow");
		DiagramEditorInput input = new DiagramEditorInput(bo.eResource().getURI(), providerId);
		IEditorPart part = page.findEditor(input);
		if (part != null) {
			page.activate(part);
			part.setFocus();
		}
	}

	public PipelineModel getPipeline() {
		return pipeline;
	}

	public void setPipeline(PipelineModel pipeline) {
		System.out.println("set Pipeline..");
		this.pipeline = pipeline;
		this.pipeline.setRawID(pipeline.getPipelineData().getRawID());
		//여기에서 현재 파이프라인과 디자인 파일 비교
		resetPipeLine();
		//
		refreshPipeline();
		doSave(null);
		disableItems();
	}

	public void aaa(String name) {
		IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
		Diagram dr = getDiagramTypeProvider().getDiagram();

		NodeType nodeType = new NodeType(name.toString());
		nodeType.setLabel(name);
		nodeType.setDisplayLabel(name);

		List<Input> inputs = new ArrayList<>();
		Input input = new Input("stdin", "default");
		inputs.add(input);
		Input input2 = new Input("stdin2", "default");
		inputs.add(input2);
		nodeType.setInputs(inputs);
		List<Output> outputs = new ArrayList<>();
		Output output = new Output("stdout", "default", "File");
		outputs.add(output);
		Output output2 = new Output("stdout2", "default", "File");
		outputs.add(output2);
		nodeType.setOutputs(outputs);

		Object obj = CreateArcFeature.createWorkbenchNode(fp, dr, 100, 100, nodeType);
		Object obj2 = CreateArcFeature.createWorkbenchNode(fp, dr, 400, 100, nodeType);

		WFNode sourceNode = (WFNode) obj;
		WFNode targetNode = (WFNode) obj2;

		final OutputPort op = sourceNode.getOutputPorts().get(0);
		final InputPort ip = targetNode.getInputPorts().get(0);
		// WFArc arc = CreateArcFeature.createWorkbenchArc(op, ip);

		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(dr);
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			public void doExecute() {
				WFArc arc = DomainFactory.eINSTANCE.createWFArc();
				arc.setSource(op);
				arc.setTarget(ip);
				// arc.setName(getWFArcName(arc));

//				PropertyUtils.setProperty(arc, PropertyUtils.LINK_INCOMING_FILE_TO_TARGET, "false");
//				PropertyUtils.setProperty(arc, PropertyUtils.EXPAND_WILDCARDS, "false");
//				PropertyUtils.setProperty(arc, PropertyUtils.READ_IN_FILE, "false");
//				PropertyUtils.setProperty(arc, PropertyUtils.COPY_INCOMING_FILE_TO_TARGET, "false");

				Anchor opa = (Anchor) fp.getPictogramElementForBusinessObject(op);
				Anchor ipa = (Anchor) fp.getPictogramElementForBusinessObject(ip);
				AddConnectionContext acc = new AddConnectionContext(opa, ipa);
				acc.setNewObject(arc);
				acc.setTargetContainer(dr);
				fp.addIfPossible(acc);
				// WFArcSettingsEditor.updateConnectionAppearance(dr, fp, arc);
			}
		});

		Display.getDefault().asyncExec(() -> focusEditor(dr));
	}

	@SuppressWarnings("unused")
	public String getDiagramResource() {

		IDiagramTypeProvider dt = getDiagramTypeProvider();
		Diagram dr = getDiagramTypeProvider().getDiagram();
		DiagramImpl dri = (DiagramImpl) dr;

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			Resource res = dr.eResource();
			dr.eResource().save(stream, null);
			String str = new String(stream.toByteArray());
			stream.close();
			return str;
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return null;
	}

	public void disableItems() {
		Diagram dr = getDiagramTypeProvider().getDiagram();
		System.out.println("disableitme :  " + dr.getPstatus());
		if (dr.getPstatus().equals("run") || dr.getPstatus().equals("exec")) {
			refreshItem.setEnabled(true);
			// addNodeItem.setEnabled(false);
			saveItem.setEnabled(false);
			// clearItem.setEnabled(false);
			// revertItem.setEnabled(false);
			runItem.setEnabled(false);
			runAllItem.setEnabled(false);
			cancelItem.setEnabled(true);
			//System.out.println("items run "+dr.getPstatus());
		//}else if(dr.getPstatus().equals("exec")){
		//	refreshItem.setEnabled(true);
		//	saveItem.setEnabled(false);
		//	runItem.setEnabled(false);
		//	runAllItem.setEnabled(false);
		//	cancelItem.setEnabled(false);
			//System.out.println("items exec "+dr.getPstatus());
		} else {
			refreshItem.setEnabled(true);
			// addNodeItem.setEnabled(true);
			saveItem.setEnabled(false);
			saveCheck = false;
			// clearItem.setEnabled(true);
			// revertItem.setEnabled(false);
			runItem.setEnabled(true);
			runAllItem.setEnabled(true);
			cancelItem.setEnabled(false);
			//System.out.println("items other "+dr.getPstatus());
		}
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	private void setToolbarEvents() {
		refreshItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (saveCheck) {
					boolean IS_CONFIRM = MessageDialog.openConfirm(getEditorSite().getShell(), "Refresh Pipeline Confirm",
							"Do you want to save Pipeline before refresh the Pipeline?");
					if (IS_CONFIRM) {
						doSave(null);
					}else {
						return;
					}
				}
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				try {
					progressDialog.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InterruptedException {
							monitor.beginTask("Starting to refresh pipeline.", IProgressMonitor.UNKNOWN);
							Display.getDefault().asyncExec(new Runnable() {								
								@Override
								public void run() {
									resetPipeLine();
									//
									refreshPipeline();
									doSave(null);
									disableItems();		
									EventUtils.getEventBroker().send("REFRESH_PIPELINE_CHECK", pipeline.getRawID());
									System.out.println("refresh end..");
								}
							});
							monitor.subTask("Finish....");
							if (monitor.isCanceled()) {
								monitor.done();
								return;
							}
							monitor.done();
							//					
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		saveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSave(null);
			}
		});

//		clearItem.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//
//			}
//		});

//		revertItem.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//
//			}
//		});

		runItem.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (select == null)
					return;
				if (select.getFirstElement() instanceof DiagramEditPart)
					return;
				// check save state..
				if (saveCheck) {
					boolean IS_CONFIRM = MessageDialog.openConfirm(getEditorSite().getShell(), "Run Programs Confirm",
							"Do you want to save Pipeline before run the program?");
					if (IS_CONFIRM) {
						doSave(null);
					}else {
						return;
					}
				}
				IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
				Iterator<Object> iters = select.iterator();
				//
				List<NodeModel> select_nodes = new ArrayList<NodeModel>();
				List<String> node_names = new ArrayList<String>();
				//
				while (iters.hasNext()) {
					Object obj = iters.next();
					if (obj instanceof ContainerShapeEditPart) {
						// run node...
						ContainerShapeEditPart shape = (ContainerShapeEditPart) obj;
						PictogramElement element = shape.getPictogramElement();
						WFNode node = (WFNode) fp.getBusinessObjectForPictogramElement(element);
						String nid = node.getId();
						List<NodeModel> nodes = pipeline.getNode();
						for (NodeModel model : nodes) {
							if (model.getNodeID().equals(nid)) {
								// run node program.
								System.out.println("--->" + model.getNodeData().getNodeName());
								select_nodes.add(model);
								node_names.add(model.getNodeData().getNodeName());
							}
						}
					}
				}

				boolean IS_CONFIRM = MessageDialog.openConfirm(getEditorSite().getShell(), "Run Pipeline Node Confirm",
						"selected node" + node_names.toString() + " Do you want to run it?");

				if (IS_CONFIRM) {
					//
					Member member = WorkflowEditorPlugin.getDefault().getMember();

					String pipelineName = pipeline.getPipelineData().getPipelineName();
					String pipelineID = pipeline.getPipelineData().getPipelineID();
					String userID = pipeline.getPipelineData().getOwner();
					String userEmail = member.getMemberEmail();
					String workspaceName = pipeline.getPipelineData().getWorkspaceName();
					String pRawID = pipeline.getRawID();

					TaskClient taskClient = new TaskClientImpl();

					for (NodeModel node : select_nodes) {
						SubTaskResponseModel  response = taskClient.executeSingleNodeJob(node, workspaceName, pipelineID, pipelineName,
								Constant.DEFAULT_NA_VALUE, userID, userEmail, pRawID);
						System.out.println("run : "+response);
					}
					MessageDialog.openInformation(getEditorSite().getShell(), "Operated Information",
							String.join(" ", node_names.toString(), " It was normally operated."));
					Display.getDefault().asyncExec(new Runnable() {								
						@Override
						public void run() {
							refreshPipeline();
							doSave(null);
							disableItems();
						}
					});
					//BIOEXPRESS : SAVE			
				}
			}
		});
		//
		runAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// check save state..
				if (saveCheck) {
					boolean IS_CONFIRM = MessageDialog.openConfirm(getEditorSite().getShell(), "Run Programs Confirm",
							"Do you want to save Pipeline before run the program?");
					if (IS_CONFIRM) {
						doSave(null);
					}else {
						return;
					}
				}
				
				Member member = WorkflowEditorPlugin.getDefault().getMember();

				String pipelineName = pipeline.getPipelineData().getPipelineName();
				String userID = pipeline.getPipelineData().getOwner();
				String workspaceName = pipeline.getPipelineData().getWorkspaceName();
				String userEmail = member.getMemberEmail();

				String logDir = String.format(Constant.USER_PIPELINE_TASK_LOG_ROOT_PATH, userID, workspaceName,
						pipelineName);

				System.out.println("[" + pipelineName + "]: log_dir => [" + logDir + "]");

				boolean IS_CONFIRM = MessageDialog.openConfirm(getEditorSite().getShell(), "Run Pipeline Confirm",
						"[" + pipelineName + "] Do you want to run the pipeline?");

				if (IS_CONFIRM) {
					TaskClient taskClient = new TaskClientImpl();
					TaskResponseModel response = taskClient.executePipelineTask(pipeline, workspaceName, userID, userEmail);
					System.out.println("runall : "+response);
					MessageDialog.openInformation(getEditorSite().getShell(), "Operated Information",
							"[" + pipelineName + "] It was normally operated.");
					//
					IToolBehaviorProvider provider = getDiagramTypeProvider().getCurrentToolBehaviorProvider();
					if (provider instanceof WorkflowToolBehaviorProvider) {
						((WorkflowToolBehaviorProvider) provider).setStatus(false);
						// getPalettePreferences().setDockLocation(PositionConstants.WEST);

					}
					Display.getDefault().asyncExec(new Runnable() {								
						@Override
						public void run() {
							refreshPipeline();
							doSave(null);
							disableItems();
						}
					});
				}
			}
		});

		cancelItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String pipelineName = pipeline.getPipelineData().getPipelineName();

				boolean IS_CONFIRM = MessageDialog.openConfirm(getEditorSite().getShell(), "Stop Pipeline Confirm",
						"[" + pipelineName + "] Do you want to stop the pipeline?");

				if (IS_CONFIRM) {
					TaskClient taskClient = new TaskClientImpl();
					TaskResponseModel response = taskClient.stopPipelineTask(pipeline);
					System.out.println("stop : "+response);
					MessageDialog.openInformation(getEditorSite().getShell(), "Terminated Information",
							"[" + pipelineName + "] It was forcefully terminated.");
					//
					IToolBehaviorProvider provider = getDiagramTypeProvider().getCurrentToolBehaviorProvider();
					if (provider instanceof WorkflowToolBehaviorProvider) {
						((WorkflowToolBehaviorProvider) provider).setStatus(false);
						// getPalettePreferences().setDockLocation(PositionConstants.WEST);
						// disableItems();
					}
					Display.getDefault().asyncExec(new Runnable() {								
						@Override
						public void run() {
							refreshPipeline();
							doSave(null);
							disableItems();
						}
					});
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		refreshPaletteItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getEditorSite().getShell());
				try {
					progressDialog.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InterruptedException {

							monitor.beginTask("Starting to refresh palette.", IProgressMonitor.UNKNOWN);
							getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									getDiagramBehavior().refreshPalette();
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

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public void refreshPipeline() {
		try {
			// pipelineClient.updatePipeline(pipelineData.getRawID(), pipeline);
			Diagram dr = getDiagramTypeProvider().getDiagram();
			IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(dr);
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				public void doExecute() {
					//디자인 업데이트 체크 -> 먼저 파이라인 새로 가져오기.
					PipelineClient pipelineClient = new PipelineClientImpl();
					try {
						pipeline = pipelineClient.getPipeline(pipeline.getRawID());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//
					String sts = pipeline.getPipelineData().getStatus();
					dr.setPstatus(sts);
					System.out.println("refresh pipline : " + sts);
					List<NodeModel> nodes = pipeline.getNode();
					EList<Shape> childs = dr.getChildren();
					for (Shape anc : childs) {
						Object obj = fp.getBusinessObjectForPictogramElement(anc);
						WFNode wnode = (WFNode) obj;
						for (NodeModel node : nodes) {
							String status = node.getNodeData().getStatus();
							if (node.getNodeID().equals(wnode.getId())) {
								// System.out.println("status "+status);
								wnode.setStatus(status);
								break;
							}
						}
						// System.out.println(obj);
					}
				}
			});			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public FlyoutPreferences getPaletteComposite() {
		return super.getPalettePreferences();
	}
	
	public void resetPipeLine() {
		try {
			// pipelineClient.updatePipeline(pipelineData.getRawID(), pipeline);
			Diagram dr = getDiagramTypeProvider().getDiagram();
			IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(dr);
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				public void doExecute() {
					PipelineClient pipelineClient = new PipelineClientImpl();
					try {
						pipeline = pipelineClient.getPipeline(pipeline.getRawID());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//먼저 노드에 없는 wfnode 삭제
					List<NodeModel> nodes = pipeline.getNode();
					EList<Shape> childs = dr.getChildren();
					List<Shape> rlist = new ArrayList<Shape>();
					for (Shape anc : childs) {
						Object obj = fp.getBusinessObjectForPictogramElement(anc);
						if (obj instanceof WFNode) {
							WFNode wnode = (WFNode) obj;
							String id = wnode.getId();
							boolean c = true;
							for (NodeModel node : nodes) {
								if (id.equals(node.getNodeID())) {
									c = false;
									break;
								}
							}
							if(c) {
								rlist.add(anc);
							}
						}
					}
					for(Shape shape:rlist) {
						//delete wfnode
						DeleteContext context = new DeleteContext(shape);
						DeleteWFNodeFeature df =new DeleteWFNodeFeature(fp);
						df.deleteEmpty(context);
					}
					//먼저 링크에 없는 wfarc 삭제
					List<LinkModel> links = pipeline.getLink();
					EList<Connection> conns = dr.getConnections();
					List<Connection> rlist2 = new ArrayList<Connection>();
					for (Connection conn : conns) {
						Object obj = fp.getBusinessObjectForPictogramElement(conn);
						if (obj instanceof WFArc) {
							WFArc arc = (WFArc) obj;
							String id = arc.getId();
							boolean c = true;
							for (LinkModel link : links) {
								if (id.equals(link.getLinkID())) {
									c = false;
								}
							}
							if(c) {
								rlist2.add(conn);
							}
						}
					}
					for(Connection conn:rlist2) {
						//delete wfarc
						DeleteContext context = new DeleteContext(conn);
						DeleteWFArcFeature df =new DeleteWFArcFeature(fp);
						df.deleteEmpty(context);
					}
					//파이프라인모델에 있는데 다이어그램에 없는 경우 WFNODE 체크 및 생성.
					List<NodeModel> nnodes = checkNodes();
					for(NodeModel node:nnodes) {
						int x = node.getNodeData().getX();
						int y = node.getNodeData().getY();
						String nname = node.getNodeData().getNodeName();
						String pid = node.getNodeData().getProgramID();
						String stype = node.getNodeData().getScriptType();
						//String mid = node.getNodeData().get
						NodeType nodeType = new NodeType(nname, pid, "", stype);
						int h = node.getParameter().getParameterInput().size();
						h = AddWFNodeFeature.PARAMETER_HEIGHT+(16*h)+3;
						if(h < 80)
							h = 80;
						//
						CreateContext createContext = new CreateContext();
						createContext.setTargetContainer(dr);
						createContext.setLocation(x, y);
						createContext.setSize(AddWFNodeFeature.NODE_WIDTH, h);
						CreateWFNodeFeature cf = new CreateWFNodeFeature(fp, nodeType);
						cf.createEmpty(createContext, node);
					}
					//다이어그램에 있는 WFNODE 체크 및 링크 처리
					List<WFNode> wnodes = new ArrayList<WFNode>();
					EList<Shape> childs2 = childs = dr.getChildren();
					for (Shape anc : childs2) {
						Object obj = fp.getBusinessObjectForPictogramElement(anc);
						if (obj instanceof WFNode) {
							WFNode wnode = (WFNode) obj;
							wnodes.add(wnode);
						}
					}
					List<LinkModel> nlinks = checkLinks();
					for(LinkModel link:nlinks) {
						String sourceni = link.sourceID;
						String targetni = link.targetID;
						String sourcenn = link.sourceName;
						String targetnn = link.targetName;
						String sourcepi = link.sourceParamID;
						String sourcepn = link.sourceParamName;
						String targetpi = link.targetParamID;
						String targetpn = link.targetParamName;
						//
						WFNode sourceNode = null;
						WFNode targetNode = null;
						for(WFNode wnode:wnodes) {
							if(wnode.getId().equals(sourceni)) {
								sourceNode = wnode;
								break;
							}
						}
						for(WFNode wnode:wnodes) {
							if(wnode.getId().equals(targetni)) {
								targetNode = wnode;
								break;
							}
						}
						if(sourceNode == null)
							continue;
						if(targetNode == null)
							continue;
						EList<OutputPort> outs = sourceNode.getOutputPorts();
						EList<InputPort> inputs = targetNode.getInputPorts();
						OutputPort op = sourceNode.getOutputPorts().get(0); //이건..무조건..
						InputPort ip = targetNode.getInputPorts().get(0);
						for(InputPort port:inputs) {
							String pname = port.getName();
							if(pname.equals(targetpn)) {
								ip = port;
								break;
							}
						}
						//
						CreateArcFeature caf = new CreateArcFeature(fp);
						WFArc arc = caf.createDARTArc(op, ip);
						arc.setId(link.getLinkID());
						Anchor opa = (Anchor) fp.getPictogramElementForBusinessObject(op);
						Anchor ipa = (Anchor) fp.getPictogramElementForBusinessObject(ip);
						AddConnectionContext acc = new AddConnectionContext(opa, ipa);
						acc.setNewObject(arc);
						acc.setTargetContainer(dr);
						fp.addIfPossible(acc);
					}
				}
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private List<NodeModel> checkNodes() {
		List<NodeModel> nnodes = new ArrayList<NodeModel>();
		Diagram dr = getDiagramTypeProvider().getDiagram();
		IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
		EList<Shape> childs = dr.getChildren();
		List<NodeModel> nodes = pipeline.getNode();
		for (NodeModel node : nodes) {
			boolean c = true;
			for (Shape anc : childs) {
				Object obj = fp.getBusinessObjectForPictogramElement(anc);
				PictogramElement pe = fp.getPictogramElementForBusinessObject(obj);
				if (obj instanceof WFNode) {
					WFNode wnode = (WFNode) obj;
					String id = wnode.getId();
					if (id.equals(node.getNodeID())) {
						//check x y
						EList<EObject> objs = anc.eContents();
						for (EObject eo : objs) {
							// System.out.println(eo);
							if (eo instanceof PlatformGraphicsAlgorithm) {
								PlatformGraphicsAlgorithm plt = (PlatformGraphicsAlgorithm) eo;
								plt.setX(node.getNodeData().x);
								plt.setY(node.getNodeData().y);
								break;
							}
						}
						c = false;
					}
				}
			}
			if(c) {
				nnodes.add(node);
			}
		}
		return nnodes;
	}
	
	private List<LinkModel> checkLinks() {
		List<LinkModel> nlinks = new ArrayList<LinkModel>();
		Diagram dr = getDiagramTypeProvider().getDiagram();
		IFeatureProvider fp = getDiagramTypeProvider().getFeatureProvider();
		EList<Connection> conns = dr.getConnections();
		List<LinkModel> links = pipeline.getLink();
		for (LinkModel link : links) {
			boolean c = true;
			for (Connection anc : conns) {
				Object obj = fp.getBusinessObjectForPictogramElement(anc);
				// System.out.println(obj);
				if (obj instanceof WFArc) {
					WFArc arc = (WFArc) obj;
					String id = arc.getId();
					if (id.equals(link.getLinkID())) {
						c = false;
					}
				}
			}
			if(c) {
				nlinks.add(link);
			}
		}
		return nlinks;
	}
}