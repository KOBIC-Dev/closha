/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.Response;
import gov.sandia.dart.workflow.domain.ResponseArc;
import gov.sandia.dart.workflow.domain.WFArc;
import gov.sandia.dart.workflow.domain.WFNode;

import org.kobic.bioexpress.db.utils.Utils;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.workbench.editor.KeyboardStateListener;
import org.kobic.bioexpress.workbench.editor.PaletteBuilder;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowImageProvider;
import org.kobic.bioexpress.workbench.editor.configuration.Input;
import org.kobic.bioexpress.workbench.editor.configuration.NodeType;
import org.kobic.bioexpress.workbench.editor.configuration.WorkflowTypesManager;
import org.kobic.bioexpress.workbench.editor.settings.NOWPSettingsEditorUtils;
import org.kobic.bioexpress.workbench.editor.utils.PipelineUtil;

import gov.sandia.dart.workflow.util.ParameterUtils;
import gov.sandia.dart.workflow.util.PropertyUtils;

public class CreateArcFeature extends AbstractCreateConnectionFeature {

	private volatile boolean doneChanges;

	public CreateArcFeature(IFeatureProvider fp) {
		// provide name and description for the UI, e.g. the palette
		super(fp, "Make Connections", "Connect elements within a workflow");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		Diagram dr = getFeatureProvider().getDiagramTypeProvider().getDiagram();
		//if(dr.getPstatus().startsWith("run") || dr.getPstatus().contains("PI-REG-0020")
		//		|| dr.getPstatus().contains("PI-REG-0030")) {
		if (dr.getPstatus().equals("run") || dr.getPstatus().equals("exec")) {
			return false;
		}		
		//
		OutputPort source = getOutputPort(context.getSourceAnchor());

		// We have to have a source
		if (source == null) {
			return false;
		}

		// It the target is an input port, it can't have more than one incoming
		// connection
		InputPort ipTarget = getInputPort(context.getTargetAnchor());
		if (ipTarget != null && ipTarget.getArcs().isEmpty()) {
			return true;
		}

		// If it's a response it can.
		Response rTarget = getResponse(context.getTargetAnchor());
		if (rTarget != null) {
			return true;
		}

		WFNode nodeTarget = getWFNode(context);
		if (nodeTarget != null) {
			return true;
		}

		if (KeyboardStateListener.isKeyDown()) {
			return true;
		}
		return false;
	}

	private WFNode getWFNode(ICreateConnectionContext context) {
		PictogramElement pe = context.getTargetPictogramElement();
		if (pe != null) {
			GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof WFNode) {
				ILocation loc = context.getTargetLocation();
				if (loc.getX() - ga.getX() > 10 && (ga.getX() + ga.getWidth()) - loc.getX() > 10)
					return ((WFNode) bo);
			}
		}
		return null;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		OutputPort source = getOutputPort(context.getSourceAnchor());
		return source != null;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		Connection newConnection = null;
		doneChanges = false;
		// get EClasses which should be connected
		OutputPort source = getOutputPort(context.getSourceAnchor());
		InputPort target = getInputPort(context.getTargetAnchor());
		Response rTarget = getResponse(context.getTargetAnchor());
		if(target == null)
			return null;
		WFNode nTarget = target.getNode();//getWFNode(context);
		WFNode nSource = source.getNode();
		//TODO : BIOEXPRESS : 파라미터(Target)에 1개 이상의 링크가 추가되지 않도록 하는 기능 지원
		//두번째 : 소스 노드와 타켓 노드에 링크가 하나 이상 있을 경우 추가 않됨.
		if (source != null) {
			if (target != null) {
				//check other connection
				if(target.getArcs().size() == 0) {
					//check link model
					//check out type and in type
					String itype = "input";
					String otype = "output";
					if(source.getProperties().size() > 0) {
						otype = source.getProperties().get(0).getType();
					}
					if(target.getProperties().size() > 0) {
						itype = target.getProperties().get(0).getType();
					}
					if(otype.equals(itype))
						newConnection = connectToInputPort(context, source, target);
					else {
//						MessageDialog.openWarning(Display.getDefault().getActiveShell(), "Warning", "The target parameter type is not correct.\n"
//								+ "(Source:"+otype+", Target:"+itype+")");
						
						boolean res = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Warning",
								"The target parameter type is not correct.\n" + 
								"You may get an error, would you like to connect?\n" +
								"Output parameter type: [" + otype + "]\n Input parameter type: [" + itype + "]");
						
						if(res) {
							newConnection = connectToInputPort(context, source, target);	
						}
					}
					//node param check.
				}
			} 
		}
		//
		doneChanges = (newConnection != null);
		//get nSource param and nTarget param
		return newConnection;
	}

	@Override
	public boolean hasDoneChanges() {
		return doneChanges;
	}

	protected Connection createNewConnectedNode(ICreateConnectionContext context, OutputPort source) {
		ILocation loc = context.getTargetLocation();
		int y = loc.getY() - AddWFNodeFeature.TOP_PORT;
		Object object = createUserSelectedNode(true, getFeatureProvider(), getDiagram(), loc.getX(), y,
				source.getName());
		if (object instanceof WFNode) {
			WFNode node = (WFNode) object;
			WFArc arc = createDARTArc(source, node.getInputPorts().get(0));
			Anchor anchor = (Anchor) getFeatureProvider()
					.getPictogramElementForBusinessObject(node.getInputPorts().get(0));
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), anchor);
			addContext.setNewObject(arc);
			Display.getDefault().asyncExec(() -> focusEditor(node));

			return (Connection) getFeatureProvider().addIfPossible(addContext);

		} else if (object instanceof Response) {
			Response response = (Response) object;
			// create new business object
			ResponseArc arc = createResponseArc(source, response);
			// add connection for business object
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), findAnchor(response));
			addContext.setNewObject(arc);
			Display.getDefault().asyncExec(() -> focusEditor(response));

			return (Connection) getFeatureProvider().addIfPossible(addContext);
		}

		Display.getDefault().beep();
		return null;
	}

	private Anchor findAnchor(Response response) {
		PictogramElement[] pes = getFeatureProvider().getAllPictogramElementsForBusinessObject(response);
		for (PictogramElement pe : pes) {
			if (pe instanceof Anchor)
				return (Anchor) pe;
		}
		return null;
	}

	@SuppressWarnings("restriction")
	public static Object createUserSelectedNode(boolean needInputs, IFeatureProvider fp, ContainerShape container,
			int x, int y, String suggestedName) {
		// Ugh. We lose the keyup event if it happens while the popup menu is displayed.
		KeyboardStateListener.setKeyDown(false);
		Set<String> nodeTypeNames = WorkflowTypesManager.get().getNodeTypes().keySet();

		ArrayList<Object> aContent = PaletteBuilder.buildMenu(nodeTypeNames);
		PopupMenu popupMenu = new PopupMenu(aContent, new LabelProvider());
		boolean b = popupMenu.show(Display.getCurrent().getActiveShell());
		if (b) {
			List<?> result = (List<?>) popupMenu.getResult();
			CreateContext createContext = new CreateContext();
			createContext.setTargetContainer(container);
			createContext.setLocation(x, y);
			Object name = result.get(1);
			NodeType nodeType = WorkflowTypesManager.get().getNodeType(String.valueOf(name));
			if (nodeType != null && (!needInputs || nodeType.getInputs().size() > 0)) {
				setSizeFor(nodeType, createContext);
				CreateWFNodeFeature cf = new CreateWFNodeFeature(fp, nodeType);
				return (WFNode) cf.create(createContext)[0];

			} else if ("note".equals(name)) {
				createContext.setSize(150, 75);
				CreateNoteFeature cnf = new CreateNoteFeature(fp);
				cnf.create(createContext);
				return null;

			} else if ("image".equals(name)) {
				createContext.setSize(150, 150);
				CreateImageFeature cif = new CreateImageFeature(fp);
				cif.create(createContext);
				return null;

			} else if ("response".equals(name)) {
				createContext.setSize(100, 24);
				createContext.putProperty(CreateResponseFeature.NAME, suggestedName);
				CreateResponseFeature crf = new CreateResponseFeature(fp);
				return crf.create(createContext)[0];
			}
		}
		return null;
	}

	public static Object createWorkbenchNode(boolean needInputs, IFeatureProvider fp, ContainerShape container, int x,
			int y, String suggestedName) {
		// Ugh. We lose the keyup event if it happens while the popup menu is displayed.
		CreateContext createContext = new CreateContext();
		createContext.setTargetContainer(container);
		createContext.setLocation(x, y);
		Object name = suggestedName;
		NodeType nodeType = new NodeType(name.toString());
		nodeType.setLabel(suggestedName);
		nodeType.setDisplayLabel(suggestedName);
		setSizeFor(nodeType, createContext);
		CreateWFNodeFeature cf = new CreateWFNodeFeature(fp, nodeType);
		return (WFNode) cf.create(createContext)[0];
	}
	
	public static Object createWorkbenchNode(IFeatureProvider fp, ContainerShape container, int x, int y, NodeType nodeType) {
		// Ugh. We lose the keyup event if it happens while the popup menu is displayed.
		CreateContext createContext = new CreateContext();
		createContext.setTargetContainer(container);
		createContext.setLocation(x, y);
		setSizeFor(nodeType, createContext);
		CreateWFNodeFeature cf = new CreateWFNodeFeature(fp, nodeType);
		return (WFNode) cf.create(createContext)[0];
	}
	
	public static WFArc createWorkbenchArc(IFeatureProvider fp, ContainerShape container, OutputPort source, InputPort target) {
		WFArc arc = DomainFactory.eINSTANCE.createWFArc();
		arc.setSource(source);
		arc.setTarget(target);
		arc.setName(getWFArcName(arc));

		PropertyUtils.setProperty(arc, PropertyUtils.LINK_INCOMING_FILE_TO_TARGET, "false");
		PropertyUtils.setProperty(arc, PropertyUtils.EXPAND_WILDCARDS, "false");
		PropertyUtils.setProperty(arc, PropertyUtils.READ_IN_FILE, "false");
		PropertyUtils.setProperty(arc, PropertyUtils.COPY_INCOMING_FILE_TO_TARGET, "false");
		return arc;
	}
	

	private static void setSizeFor(NodeType nodeType, CreateContext createContext) {
		if (ParameterUtils.isParameterType(nodeType))
			createContext.setSize(AddWFNodeFeature.NODE_WIDTH, AddWFNodeFeature.PARAMETER_HEIGHT);
		else
			createContext.setSize(AddWFNodeFeature.NODE_WIDTH, AddWFNodeFeature.NODE_HEIGHT);

	}

	protected Connection connectToInputPort(ICreateConnectionContext context, OutputPort source, InputPort target) {
		WorkflowDiagramEditor editor = (WorkflowDiagramEditor)getFeatureProvider().getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer();
    	PipelineModel pipeline = editor.getPipeline();
    	String rawId = Utils.getInstance().getNewRawID();
    	PipelineUtil.setLinkFromProgram(rawId, source.getNode(), target.getNode(), context, pipeline, source, target);
    	PipelineUtil.setOutValueToIn(source, target, pipeline);
		//
		Connection newConnection;
		// create new business object
		WFArc arc = createDARTArc(source, target);
		arc.setId(rawId);
		// add connection for business object
		AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
				context.getTargetAnchor());
		addContext.setNewObject(arc);
		newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		return newConnection;
	}

	protected Connection connectToResponse(ICreateConnectionContext context, OutputPort source, Response rTarget) {
		Connection newConnection;
		// create new business object
		ResponseArc arc = createResponseArc(source, rTarget);
		// add connection for business object
		AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
				context.getTargetAnchor());
		addContext.setNewObject(arc);
		newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		return newConnection;
	}

	protected Connection connectToNodeOnNewPort(ICreateConnectionContext context, OutputPort source, WFNode nTarget) {
		Connection newConnection;
		String inputPortName = getPortName(source);
		Optional<InputPort> oPort = nTarget.getInputPorts().stream().filter(x -> x.getName().equals(inputPortName))
				.findFirst();
		InputPort port;
		if (oPort.isPresent() && oPort.get().getArcs().isEmpty()) {
			port = oPort.get();

		} else {
			port = DomainFactory.eINSTANCE.createInputPort();
			port.setName(NOWPSettingsEditorUtils.createUniqueName(inputPortName, nTarget.getInputPorts()));
			port.setType(getInputPortType(source));
			nTarget.getInputPorts().add(port);
		}

		// Update the node, to create the anchors
		List<PictogramElement> nodePe = Graphiti.getLinkService().getPictogramElements(getDiagram(), nTarget);
		UpdateContext uc = new UpdateContext(nodePe.get(0));
		new UpdateWFNodeFeature(getFeatureProvider()).update(uc);

		// Now create a new connection.
		List<PictogramElement> anchorPe = Graphiti.getLinkService().getPictogramElements(getDiagram(), port);
		WFArc arc = createDARTArc(source, port);
		AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), (Anchor) anchorPe.get(0));
		addContext.setNewObject(arc);
		newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		return newConnection;
	}

	protected String getPortName(OutputPort source) {
		if (ParameterUtils.isParameter(source.getNode()))
			return ParameterUtils.getName(source.getNode());
		else if (source.getNode().getType().equals("constant"))
			return source.getNode().getName();
		else if (isFileOrFolder(source))
			return source.getNode().getName();
		else
			return source.getName();
	}

	protected boolean isFileOrFolder(OutputPort source) {
		String type = source.getNode().getType();
		return type.equals("file") || type.equals("folder");
	}

	private String getInputPortType(OutputPort source) {
		String type = source.getType();
		if ("output_file".equals(type) || "exodus_file".equals(type))
			return "default";
		else
			return type;
	}

	/**
	 * Returns the InputPort belonging to the anchor, or null if not available.
	 */
	private InputPort getInputPort(Anchor anchor) {
		if (anchor != null) {
			Object object = getBusinessObjectForPictogramElement(anchor);
			if (object instanceof InputPort) {
				return (InputPort) object;
			}
		}
		return null;
	}

	private OutputPort getOutputPort(Anchor anchor) {
		if (anchor != null) {
			Object object = getBusinessObjectForPictogramElement(anchor);
			if (object instanceof OutputPort) {
				return (OutputPort) object;
			}
		}
		return null;
	}

	private Response getResponse(Anchor anchor) {
		if (anchor != null) {
			Object object = getBusinessObjectForPictogramElement(anchor);
			if (object instanceof Response) {
				return (Response) object;
			}
		}
		return null;
	}

	public WFArc createDARTArc(OutputPort source, InputPort target) {
		WFArc arc = DomainFactory.eINSTANCE.createWFArc();
		arc.setSource(source);
		arc.setTarget(target);
		arc.setName(getWFArcName(arc));

		PropertyUtils.setProperty(arc, PropertyUtils.LINK_INCOMING_FILE_TO_TARGET, "false");
		PropertyUtils.setProperty(arc, PropertyUtils.EXPAND_WILDCARDS, "false");
		PropertyUtils.setProperty(arc, PropertyUtils.READ_IN_FILE, "false");
		PropertyUtils.setProperty(arc, PropertyUtils.COPY_INCOMING_FILE_TO_TARGET, "false");

		return arc;
	}

	private ResponseArc createResponseArc(OutputPort source, Response target) {
		ResponseArc arc = DomainFactory.eINSTANCE.createResponseArc();
		arc.setSource(source);
		arc.setTarget(target);
		arc.setName(getResponseArcName(arc));

		return arc;
	}

	public static String getWFArcName(WFArc arc) {
		return String.format("%s -> %s", arc.getSource().getName(), arc.getTarget().getName());
	}

	static String getResponseArcName(ResponseArc arc) {
		return String.format("%s -> %s", arc.getSource().getName(), arc.getTarget().getName());
	}

	@Override
	public String getCreateImageId() {
		return WorkflowImageProvider.IMG_PLUG;
	}

	@Override
	public String getCreateLargeImageId() {
		return WorkflowImageProvider.IMG_PLUG;
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
	
}
