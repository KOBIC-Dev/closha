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
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.pipeline.LinkModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;

import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.NamedObjectWithProperties;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFArc;
import gov.sandia.dart.workflow.domain.WFNode;

public class DeleteWFArcFeature extends DefaultDeleteFeature {

	public DeleteWFArcFeature(IFeatureProvider fp) {
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
		if (bo instanceof WFArc) {
			WFArc arc = (WFArc) bo;
			String arcId = arc.getId();
			Set<EObject> toDelete = new HashSet<>();
			
			toDelete.addAll(arc.getProperties());			
			
			if (toDelete.size() > 0) {
				//check pipeline.
				WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
						.getDiagramBehavior().getDiagramContainer();
				PipelineModel pipeline = editor.getPipeline();
				List<NodeModel> nodes = pipeline.getNode();
//				for(NodeModel node:nodes) {
//					OutputPort outp = arc.getSource();
//					String nodeid = outp.getNode().getId();
//					if(nodeid.equals(node.getNodeID())) {
//						List<ParameterDataModel> outs = node.getParameter().getParameterOutput();
//						for(ParameterDataModel out:outs) {
//							if(outp.getId().equals(out.getParameterID())) {
//								out.sourceParamName = "NA";
//							}
//						}
//					}
//				}
//				for(NodeModel node:nodes) {
//					InputPort inp = arc.getTarget();
//					String nodeid = inp.getNode().getId();
//					if(nodeid.equals(node.getNodeID())) {
//						List<ParameterDataModel> ins = node.getParameter().getParameterInput();
//						for(ParameterDataModel in:ins) {
//							if(inp.getId().equals(in.getParameterID())) {
//								in.targetParamName = new ArrayList<String>();
//							}
//						}
//					}
//				}
				//
				Iterator<LinkModel> iter = pipeline.getLinkIterator();
				List<LinkModel> list = new ArrayList<LinkModel>();
				while(iter.hasNext()) { 
					LinkModel nm = iter.next();
					if(nm.getLinkID().equals(arcId)) {
						//iter.remove();
					}else {
						list.add(nm);
					}
				}
				pipeline.setLink(list);
				
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
		if (bo instanceof WFArc) {
			WFArc node = (WFArc) bo;
			String arcId = node.getId();
			Set<EObject> toDelete = new HashSet<>();
			
			toDelete.addAll(node.getProperties());			
			
			if (toDelete.size() > 0) {
				//check pipeline.
				deleteBusinessObjects(toDelete.toArray());
				setDoneChanges(true);
			}
		}
		//
		if (isDeleteAbort()) {
			throw new OperationCanceledException();
		}
		if (pe instanceof CompositeConnection) {
			// Find all domain objects for the children connections of the
			// composite connection...
			List<Object> compositeChildConnectionsBOs = collectCompositeConnectionsBOs((CompositeConnection) pe);
			// ... and add them to the list of BOs to delete (no duplicates)
			for (Object object : businessObjectsForPictogramElement) {
				if (!compositeChildConnectionsBOs.contains(object)) {
					compositeChildConnectionsBOs.add(object);
				}
			}
			// Update BOs to delete
			businessObjectsForPictogramElement = compositeChildConnectionsBOs
					.toArray(new Object[compositeChildConnectionsBOs.size()]);
		}
		//
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
