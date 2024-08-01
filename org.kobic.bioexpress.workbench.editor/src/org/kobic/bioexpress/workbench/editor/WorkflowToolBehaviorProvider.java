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
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.ISingleClickContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.PlatformGraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.platform.IDiagramBehavior;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.ContextMenuEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IContextMenuEntry;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.workbench.editor.features.AddSelectedNodeType;
import org.kobic.bioexpress.workbench.editor.features.BringToFrontFeature;
import org.kobic.bioexpress.workbench.editor.features.DuplicateNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.ICustomFeatureProvider;
import org.kobic.bioexpress.workbench.editor.features.MinMaxFeature;
import org.kobic.bioexpress.workbench.editor.features.OpenReferencedFileFeature;
import org.kobic.bioexpress.workbench.editor.features.RebuildGraphicsFeature;
import org.kobic.bioexpress.workbench.editor.features.SendToBackFeature;
import org.kobic.bioexpress.workbench.editor.features.ToggleConnectionAlphaFeature;
import org.kobic.bioexpress.workbench.editor.monitoring.IWorkflowListener;
import org.kobic.bioexpress.workbench.editor.monitoring.WorkflowTracker;
import org.kobic.bioexpress.workbench.editor.preferences.IWorkflowEditorPreferences;
import org.kobic.bioexpress.workbench.editor.utils.EventUtils;

import com.strikewire.snl.apc.util.ExtensionPointUtils;

import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.NamedObject;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.ResponseArc;
import gov.sandia.dart.workflow.domain.WFArc;
import gov.sandia.dart.workflow.domain.WFNode;

public class WorkflowToolBehaviorProvider extends DefaultToolBehaviorProvider implements IWorkflowListener {
	private boolean status = true;
	
	public WorkflowToolBehaviorProvider(IDiagramTypeProvider dtp) {
		super(dtp);
		WorkflowTracker.addWorkflowListener(this);
	}

	@Override
	public IContextMenuEntry[] getContextMenu(ICustomContext context) {
		//TODO : BIOEXPRESS
		PictogramElement[] pics = context.getPictogramElements();
		
		final CustomContext customContext = new CustomContext();
		customContext.setInnerPictogramElement(pics[0]);
		customContext.setPictogramElements(pics);

		List<ContextButtonEntry> buttons = getContextButtons(pics);
		List<IContextMenuEntry> entries = new ArrayList<>();
		for (ContextButtonEntry button: buttons) {
			IFeature feature = button.getFeature();
			ContextMenuEntry item = new ContextMenuEntry(feature, context);
			entries.add(item);
		}

		
		if (pics.length == 1 && ! (pics[0] instanceof Diagram) ) {
			ContextMenuEntry entry = new ContextMenuEntry(null, customContext);
			entry.add(new ContextMenuEntry(new BringToFrontFeature(getFeatureProvider()), customContext));
			entry.add(new ContextMenuEntry(new SendToBackFeature(getFeatureProvider()), customContext));
			entry.setSubmenu(false);
			entries.add(entry);
		}
		
		if (pics.length == 1 && pics[0] instanceof Diagram) {			
			entries.add(new ContextMenuEntry(new AddSelectedNodeType(getFeatureProvider()), customContext));
			entries.add(new ContextMenuEntry(new ToggleConnectionAlphaFeature(getFeatureProvider()), customContext));
		}

		entries.add(new ContextMenuEntry(new RebuildGraphicsFeature(getFeatureProvider()), customContext));


		return entries.toArray(new IContextMenuEntry[entries.size()]);
	}


	@Override
	public IPaletteCompartmentEntry[] getPalette() {
		//TODO BIOEXPRESS
		IPaletteCompartmentEntry[] entries = new PaletteBuilder().createPaletteEntries(getFeatureProvider(), getDiagramFile());
		return entries;
	}

	@Override
	public boolean isShowMarqueeTool() {
		return true;
	}
	
	@Override
	public boolean isShowSelectionTool() {
		return true;
	}
	
	@Override
	public boolean isShowFlyoutPalette() {
		//TODO : BIOEXPRESS..
		//System.out.println("palette : "+status);
//		IDiagramContainer container = getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer();
//		if (container instanceof WorkflowDiagramEditor) {
//			//((WorkflowDiagramEditor)container).getDiagramBehavior().get
//		}
//		
//		Diagram dr = getFeatureProvider().getDiagramTypeProvider().getDiagram();
//		if(dr.getPstatus().equals("run"))
//			status = false;
//		else
//			status = true;
		return true;
	}

	@Override
	public Object getToolTip(GraphicsAlgorithm ga) {
		PictogramElement pe = ga.getPictogramElement();
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);

		if (bo instanceof WFNode) {
			String tip;
			// TODO: should part of this be cached at startup?
			if (WorkflowEditorPlugin.getDefault().getPreferenceStore().getBoolean(IWorkflowEditorPreferences.NODE_TYPE_HEADERS))
				tip = ((WFNode) bo).getName();
			else
				tip = ((WFNode) bo).getType();
			if (tip != null && !tip.isEmpty()) {
				return tip;
			}
		} else if (bo instanceof NamedObject) {
			String name = ((NamedObject) bo).getName();
			if (name != null && !name.isEmpty()) {
				return name;
			}
		}
		return super.getToolTip(ga);
	}

	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
		IContextButtonPadData data = super.getContextButtonPad(context);
		PictogramElement pe = context.getPictogramElement();		
		setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE);

		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
				
		if (bo instanceof WFNode) {
			List<ContextButtonEntry> buttons = getContextButtons(pe);			
			
			data.getDomainSpecificContextButtons().addAll(buttons);
		}

		return data;
	}

	private List<ContextButtonEntry> getContextButtons(PictogramElement... pe) {
		final CustomContext customContext = new CustomContext();
		customContext.setInnerPictogramElement(pe[0]);
		customContext.setPictogramElements(pe);

		IFeatureProvider featureProvider = getFeatureProvider();

		List<IContextButtonContributor> contributors =
				ExtensionPointUtils.getExtensionInstances(WorkflowEditorPlugin.PLUGIN_ID, "contextButtonContributor", "contributor", "class");
		List<ContextButtonEntry> buttons = new ArrayList<>();
		contributors.forEach(c -> buttons.addAll(c.getContextButtons(featureProvider, customContext)));
		return buttons;
	}


	private URI getDiagramFile() {
		return getDiagramTypeProvider().getDiagram().eResource().getURI();		
	};


	@Override
	public IDecorator[] getDecorators(PictogramElement pe) {
		try {
			IFeatureProvider featureProvider = getFeatureProvider();
			Object bo = featureProvider.getBusinessObjectForPictogramElement(pe);
			Map<EObject, IDecorator> validationMarkers = DecoratorManager.getDecoratorMap(getDiagramTypeProvider().getDiagram().eResource());
			IDecorator validationMarker = validationMarkers.get(bo);
			if (validationMarker != null)
				return new IDecorator[] { validationMarker };
			
			return super.getDecorators(pe);
		} catch (Throwable t) {
			t.printStackTrace();
			return super.getDecorators(pe);
		}
	}

	@Override
	public void postExecute(IExecutionInfo executionInfo) {
		//TODO : BIOEXPRESS
		Map<EObject, IDecorator> decorators = DecoratorManager.getDecoratorMap(getDiagramTypeProvider().getDiagram().eResource());
		new WorkflowValidator().validate(executionInfo, decorators);
		getDiagramTypeProvider().getDiagramBehavior().refresh();
	}

	@Override 
	public ICustomFeature getSingleClickFeature(ISingleClickContext context) {
		return super.getSingleClickFeature(context);
	}
	
	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		if (context.getPictogramElements().length == 1) {
			final PictogramElement pe = context.getPictogramElements()[0];
			//CHECK NODE & LINK INFO
			EList<EObject> objs = pe.eContents();
			for(EObject obj:objs) {
				System.out.println(obj);
				if(obj instanceof PlatformGraphicsAlgorithm) {
					PlatformGraphicsAlgorithm plt = (PlatformGraphicsAlgorithm)obj;
					plt.getX();
					plt.getY();
					plt.getWidth();
					plt.getHeight();
					System.out.println("Rec : "+plt);
				}
				if(obj instanceof Polyline) {
					Polyline line = (Polyline)obj;
					line.getX();
					line.getY();
					line.getWidth();
					line.getHeight();
					System.out.println("Line : "+line);
				}
				if(obj instanceof Point) {
					Point point = (Point)obj;
					point.getX();
					point.getY();
					System.out.println("Point : "+point);
				}
			}
			//
			Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
			if (bo instanceof WFNode) {
				//TODO : BIOEXPRESS
				WFNode node = (WFNode) bo;
				
				String rawId = node.getId();
				WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
						.getDiagramBehavior().getDiagramContainer();
				PipelineModel pipeline = editor.getPipeline();
				List<NodeModel> nodes = pipeline.getNode();
				//파이프라인 실행 체크.
				String pstatus = pipeline.getPipelineData().getStatus();
				if(!pstatus.equals("run")) {
					boolean c = true;
					for(NodeModel model:nodes) {
						String st = model.getNodeData().getStatus();
						if(st.equals("run")) {
							c = false;
							break;
						}
					}
					if(c) {
						for(NodeModel model:nodes) {
							if(model.getNodeID().equals(rawId)) {
								EventUtils.getEventBroker().send("OPEN_NODEINFO", model);
								break;
							}
						}	
					} else {
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Error", "Running pipeline cannot be changed.");
					}
				}else {
					boolean c = true;
					for(NodeModel model:nodes) {
						String st = model.getNodeData().getStatus();
						if(st.equals("run")) {
							c = false;
							break;
						}
					}
					if(c) {
						for(NodeModel model:nodes) {
							if(model.getNodeID().equals(rawId)) {
								EventUtils.getEventBroker().send("OPEN_NODEINFO", model);
								break;
							}
						}	
					} else {
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Error", "Running pipeline cannot be changed.");
					}
				}
				//
				WorkflowDiagramBehavior dProv = (WorkflowDiagramBehavior)getDiagramTypeProvider().getDiagramBehavior();
				
				List list = dProv.getPaletteViewerProvider().getEditDomain().getPaletteViewer().getContents().getChildren();
				//EventUtils.getEventBroker().send("0000000001", list);
				
				//
				MinMaxFeature mmf = new MinMaxFeature(getFeatureProvider());
				if (mmf.canExecute(context)) {
					return mmf;
				}
				initialize();
				
				//Node Info
				
				node.getName();
				node.getLabel();
				EList<InputPort> inputs = node.getInputPorts();
				for(InputPort input:inputs) {
					input.getName();
					input.getType();
				}
				EList<OutputPort> outputs = node.getOutputPorts();
				for(OutputPort output:outputs) {
					output.getName();
					output.getType();
				}				
				
				DoubleClickAction action = doubleClickActions.get(node.getType());
				if(action != null)
				{
					ICustomFeature feature = action.getCustomFeature(getFeatureProvider());
					if(feature.canExecute(context))
					{
						return feature;
					}
				}
			} else if (bo instanceof WFArc || bo instanceof ResponseArc) {
				//MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "INFO", "OPEN ARC INFO!!!");
				//
				initialize();
				if (arcDoubleClickFeature != null) {
					try {
						Constructor<? extends ICustomFeature> constructor = arcDoubleClickFeature.getConstructor(IFeatureProvider.class);
						ICustomFeature feature = constructor.newInstance(getFeatureProvider());
						if (feature.canExecute(context)) {
							return feature;
						}
					} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						WorkflowEditorPlugin.getDefault().logError("Can't create arc double-click extension", e);
					}	
				}				
			}
		}
		return super.getDoubleClickFeature(context);
	}

	private static Map<String, DoubleClickAction> doubleClickActions = null;
	private static class DoubleClickAction
	{
		private final IConfigurationElement element;
		private final String nodeType;
		private final String property;
		private final String customFeature;

		
		private DoubleClickAction(IConfigurationElement element)
		{
			this.element = element;
			
			this.nodeType = element.getAttribute("nodeType");
			this.property = element.getAttribute("property");
			this.customFeature = element.getAttribute("customFeature");

		}
		
		public String getNodeType()
		{
			return nodeType;
		}
				
		public ICustomFeature getCustomFeature(IFeatureProvider fp)
		{
			if (customFeature != null) {
				try {
					Object obj = element.createExecutableExtension("customFeature");
					if(obj instanceof ICustomFeatureProvider)
					{
						ICustomFeatureProvider provider = (ICustomFeatureProvider) obj;
						return provider.createFeature(fp, nodeType, property);
					}
				} catch (Throwable t) {
					WorkflowEditorPlugin.getDefault().logError("Error generating custom feature for double click action", t);
				}
			}
			
			return new OpenReferencedFileFeature(fp, nodeType, property);
		}
	}
	private static Class<? extends ICustomFeature> arcDoubleClickFeature = null;
	private synchronized static void initialize() {
		if (doubleClickActions == null) {
			doubleClickActions = new ConcurrentHashMap<>();
			List<IConfigurationElement> elements =
					ExtensionPointUtils.getConfigurationElements(WorkflowEditorPlugin.PLUGIN_ID, "mouseAction", "doubleClick");
			for (IConfigurationElement element : elements) {
				DoubleClickAction action = new DoubleClickAction(element);
				doubleClickActions.put(action.getNodeType(), action);
			}
			elements =
					ExtensionPointUtils.getConfigurationElements(WorkflowEditorPlugin.PLUGIN_ID, "mouseAction", "arcDoubleClick");
			for (IConfigurationElement element : elements) {
				String feature = element.getAttribute("feature");				
				try {
					arcDoubleClickFeature = (Class<? extends ICustomFeature>) Platform.getBundle(element.getContributor().getName()).loadClass(feature);
				} catch (ClassNotFoundException e) {
					WorkflowEditorPlugin.getDefault().logError("Can't load arc double-click extension", e);
				}
			}
		}				
	}

	public DuplicateNodeFeature getDuplicateNodeFeature(ICustomContext context) {
		return new DuplicateNodeFeature(getFeatureProvider());
	}
	
	@Override
	public void dispose() {
		WorkflowTracker.removeWorkflowListener(this);
	}
	
	@Override
	public void nodeEntered(String name, IFile workflow, File workDir) {
		refreshIfAppropriate();
	}
	
	@Override
	public void breakpointHit(String name, IFile workflow, File workDir) {
		refreshIfAppropriate();
	}


	@Override
	public void nodeExited(String name, IFile workflow, File workDir) {
		refreshIfAppropriate();
	}
	
	@Override
	public void nodeAborted(String name, IFile workflow, File workDir, Throwable t) {
		clearStatusMessage(workflow);
		refreshIfAppropriate();
	}
	
	@Override
	public void workflowStopped(IFile workflow, File workDir) {
		clearStatusMessage(workflow);
		refreshIfAppropriate();
	}
	
	// TODO But somehow preserve state when doing "run from here" ?
	@Override
	public void workflowStarted(IFile workflow, File workDir, Collection<String> startNodes) {
		refreshIfAppropriate();
	}

	private void refreshIfAppropriate() {
		IDiagramBehavior diagramBehavior = getDiagramTypeProvider().getDiagramBehavior();
		if (diagramBehavior instanceof WorkflowDiagramBehavior)
			diagramBehavior.refresh();
	}

	@Override
	public void status(String name, IFile workflow, File workDir, String status) {
		setStatusMessage(name, workflow, status);
		refreshIfAppropriate();
	}
	
	private void setStatusMessage(String name, IFile workflow, String status) {
		String message = "Node '" + name + "': " + status;
		IDiagramContainer container = getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer();
		if (container instanceof WorkflowDiagramEditor)
			Display.getDefault().asyncExec(() -> ((WorkflowDiagramEditor) container).setStatus(message));
	}

	private void clearStatusMessage(IFile workflow) {
		IDiagramContainer container = getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer();
		if (container instanceof WorkflowDiagramEditor)
			Display.getDefault().asyncExec(() -> ((WorkflowDiagramEditor) container).setStatus(""));

	}
	
	@Override
	public String getTitleToolTip() {
		try {
			URI uri = getDiagramFile();				
			String path = uri.toPlatformString(true);
			if (path != null)
				return path;
		} catch (Exception ex) {}
		return super.getTitleToolTip();
	}
	
	public void setStatus(boolean status) {
		this.status = status;
		//get
	}
}
