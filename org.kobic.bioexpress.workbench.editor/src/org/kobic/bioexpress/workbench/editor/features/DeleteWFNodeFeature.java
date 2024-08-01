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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IMultiDeleteInfo;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.mm.pictograms.CompositeConnection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;

import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.NamedObjectWithProperties;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFNode;

public class DeleteWFNodeFeature extends DefaultDeleteFeature {

	public DeleteWFNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void preDelete(IDeleteContext context) {
		Diagram dr = getFeatureProvider().getDiagramTypeProvider().getDiagram();
		//if(dr.getPstatus().startsWith("run") || dr.getPstatus().contains("PI-REG-0020")
		//		|| dr.getPstatus().contains("PI-REG-0030")) {
		if (dr.getPstatus().equals("run") || dr.getPstatus().equals("exec")) {
			return;
		}
		
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			WFNode node = (WFNode) bo;
			String nodeId = node.getId();
			Set<EObject> toDelete = new HashSet<>();
			for (InputPort port: node.getInputPorts()) {
				for (NamedObjectWithProperties obj: port.getArcs()) {
					toDelete.addAll(obj.getProperties());
					toDelete.add(obj);
				}
				toDelete.addAll(port.getProperties());
				toDelete.add(port);
			}			
			
			for (OutputPort port: node.getOutputPorts()) {
				for (NamedObjectWithProperties obj: port.getArcs()) {
					toDelete.addAll(obj.getProperties());
					toDelete.add(obj);
				}
				for (NamedObjectWithProperties obj: port.getResponseArcs()) {
					toDelete.addAll(obj.getProperties());
					toDelete.add(obj);
				}
				toDelete.addAll(port.getProperties());
				toDelete.add(port);
			}	
			
			toDelete.addAll(node.getProperties());			
			
			toDelete.addAll(node.getConductors());
			
			if (toDelete.size() > 0) {
				//check pipeline.
				WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
						.getDiagramBehavior().getDiagramContainer();
				PipelineModel pipeline = editor.getPipeline();
				Iterator<NodeModel> iter = pipeline.getNodeIterator();
				List<NodeModel> list = new ArrayList<NodeModel>();
				while(iter.hasNext()) { 
					NodeModel nm = iter.next();
					if(nm.getNodeID().equals(nodeId)) {
						//iter.remove();
					}else {
						list.add(nm);
					}
				}
				pipeline.setNode(list);
				deleteBusinessObjects(toDelete.toArray());
				setDoneChanges(true);
				
			}
		}
	}
	
	
	public void deleteEmpty(IDeleteContext context) {
		// we need this reset, since the an instance of this feature can be
		// used multiple times, e.g. as a part of a pattern
		setDoneChanges(false);

		IMultiDeleteInfo multiDeleteInfo = context.getMultiDeleteInfo();
		if (multiDeleteInfo != null && multiDeleteInfo.isDeleteCanceled()) {
			return;
		}
		PictogramElement pe = context.getPictogramElement();
		Object[] businessObjectsForPictogramElement = getAllBusinessObjectsForPictogramElement(pe);
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			WFNode node = (WFNode) bo;
			String nodeId = node.getId();
			Set<EObject> toDelete = new HashSet<>();
			for (InputPort port: node.getInputPorts()) {
				for (NamedObjectWithProperties obj: port.getArcs()) {
					toDelete.addAll(obj.getProperties());
					toDelete.add(obj);
				}
				toDelete.addAll(port.getProperties());
				toDelete.add(port);
			}			
			
			for (OutputPort port: node.getOutputPorts()) {
				for (NamedObjectWithProperties obj: port.getArcs()) {
					toDelete.addAll(obj.getProperties());
					toDelete.add(obj);
				}
				for (NamedObjectWithProperties obj: port.getResponseArcs()) {
					toDelete.addAll(obj.getProperties());
					toDelete.add(obj);
				}
				toDelete.addAll(port.getProperties());
				toDelete.add(port);
			}	
			
			toDelete.addAll(node.getProperties());			
			
			toDelete.addAll(node.getConductors());
			
			if (toDelete.size() > 0) {
				deleteBusinessObjects(toDelete.toArray());
				setDoneChanges(true);
			}
		}
		//
		if (isDeleteAbort()) {
			throw new OperationCanceledException();
		}
		IRemoveContext rc = new RemoveContext(pe);
		IFeatureProvider featureProvider = getFeatureProvider();
		IRemoveFeature removeFeature = featureProvider.getRemoveFeature(rc);
		if (removeFeature != null) {
			removeFeature.remove(rc);
			// Bug 347421: Set hasDoneChanges flag only after first modification
			setDoneChanges(true);
		}

		deleteBusinessObjects(businessObjectsForPictogramElement);

		postDelete(context);
	}
}
