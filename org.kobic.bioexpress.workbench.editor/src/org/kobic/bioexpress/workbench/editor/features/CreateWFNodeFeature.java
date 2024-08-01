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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramContainer;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.NamedObject;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFNode;

import org.kobic.bioexpress.channel.client.ChannelClient;
import org.kobic.bioexpress.channel.client.ChannelClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.WorkflowImageProvider;
import org.kobic.bioexpress.workbench.editor.configuration.NodeType;
import org.kobic.bioexpress.workbench.editor.utils.PipelineUtil;

import gov.sandia.dart.workflow.util.ParameterUtils;

public class CreateWFNodeFeature extends AbstractCreateNodeFeature {
	final static Logger logger = Logger.getLogger(CreateWFNodeFeature.class);

	NodeType nodeType;
	private boolean duplicating;

	public CreateWFNodeFeature(IFeatureProvider featureProvider, NodeType nodeType) {
		super(featureProvider, "Workflow Node", "Create Workflow Node");
		this.nodeType = nodeType;
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		//
		boolean c = false;
		if(context.getTargetContainer() instanceof Diagram) {
			c = true;
			Diagram dr = (Diagram)context.getTargetContainer();
			//if(dr.getPstatus().startsWith("run") || dr.getPstatus().contains("PI-REG-0020")
			//		|| dr.getPstatus().contains("PI-REG-0030")) {
			if (dr.getPstatus().equals("run") || dr.getPstatus().equals("exec")) {
				c = false;
			}
		}
		return c;//context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context) {
		String memberID = nodeType.getMid();
		String programID = nodeType.getPid();
		WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
				.getDiagramBehavior().getDiagramContainer();
		PipelineModel pipeline = editor.getPipeline();
		WFNode newNode = PipelineUtil.getNodeFromProgram(programID, context, pipeline, nodeType.getScriptType());
		addPortsAndProperties(newNode, nodeType);

		if (ParameterUtils.isParameter(newNode) && !duplicating)
			ParameterUtils.setValue(newNode, "");

		// do the add
		addGraphicalRepresentation(context, newNode);

		// activate direct editing after object creation
		if (!duplicating)
			getFeatureProvider().getDirectEditingInfo().setActive(true);

		// return newly created business object(s)
		return new Object[] { newNode };
	}

	static String makeNameUnique(String newName, ICreateContext context) {
		String checkName = newName;
		if (context.getTargetContainer() instanceof Diagram) {
			Diagram diagram = (Diagram) context.getTargetContainer();

			boolean foundMatch = false;
			int suffix = 1;

			do {
				if (foundMatch) {
					checkName = newName + suffix;
					suffix++;
				}

				foundMatch = false;

				for (EObject object : diagram.eResource().getContents()) {
					if (object instanceof WFNode && ((NamedObject) object).getName().equals(checkName)) {
						foundMatch = true;
						break;
					}
				}
			} while (foundMatch);
		}
		return checkName;
	}

	private String getNodeName() {
		if (duplicating)
			return nodeType.getLabel();

		String name = nodeType.getLabel();
		if (StringUtils.isEmpty(name))
			name = nodeType.getName();
		return name;
	}

	@Override
	public String getCreateImageId() {
		String id = WorkflowImageProvider.PREFIX + nodeType.getScriptType();
		if (WorkflowImageProvider.get().getImageFilePath(id) != null)
			return id;
		else
			return null;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId();
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setDuplicating() {
		duplicating = true;
	}
	
	public Object[] createEmpty(ICreateContext context, NodeModel mnode) {
		WFNode newNode = PipelineUtil.getWFNodeFromNodeModel(mnode);
		addPortsAndProperties(newNode, nodeType);
		ParameterUtils.setValue(newNode, "");
		// do the add
		addGraphicalRepresentation(context, newNode);

		// activate direct editing after object creation
		getFeatureProvider().getDirectEditingInfo().setActive(true);

		// return newly created business object(s)
		return new Object[] { newNode };
	}
}
