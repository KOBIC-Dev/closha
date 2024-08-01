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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ContextButtonEntry;

import gov.sandia.dart.workflow.domain.WFNode;

import org.kobic.bioexpress.channel.service.sso.MemberService;
import org.kobic.bioexpress.channel.service.sso.MemberServiceImpl;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.workbench.editor.features.CopyToPaletteFeature;
import org.kobic.bioexpress.workbench.editor.features.DuplicateNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.GrabOutputFileFeature;
import org.kobic.bioexpress.workbench.editor.features.GrabOutputVariableFeature;
import org.kobic.bioexpress.workbench.editor.features.PackageComponentFeature;
import org.kobic.bioexpress.workbench.editor.features.workbench.NodeInfoFeature;
import org.kobic.bioexpress.workbench.editor.features.workbench.NodeScriptFeature;
import org.kobic.bioexpress.workbench.editor.features.workbench.NodeStartFeature;
import org.kobic.bioexpress.workbench.editor.features.workbench.NodeStopFeature;

public class BasicContextButtonContributor implements IContextButtonContributor {

	@Override
	public List<ContextButtonEntry> getContextButtons(IFeatureProvider featureProvider, CustomContext customContext) {
		List<ContextButtonEntry> buttons = new ArrayList<>();
		PictogramElement[] pe = customContext.getPictogramElements();

		Diagram dr = featureProvider.getDiagramTypeProvider().getDiagram();
		String pstatus = dr.getPstatus();
		WFNode node = (WFNode)featureProvider.getBusinessObjectForPictogramElement(pe[0]);
		String nid = node.getId();
		String nstatus = node.getStatus();
		String astatus = "";
		String ispub = "public";
		String isadmin = "user";
		WorkflowDiagramEditor editor = (WorkflowDiagramEditor) featureProvider.getDiagramTypeProvider()
				.getDiagramBehavior().getDiagramContainer();
		PipelineModel pipeline = editor.getPipeline();
		List<NodeModel> nodes = pipeline.getNode();
		for (NodeModel model : nodes) {
			String st = model.getNodeData().getStatus();
			if(st.equals("run")) {
				astatus = "run";
				break;
			}
		}
		for (NodeModel model : nodes) {
			if (model.getNodeID().equals(nid)) {
				// run node program.
				System.out.println("--->" + model.getNodeData().getNodeName()+" "+model.getNodeData().isPublic());
				if(!model.getNodeData().isPublic()) {
					ispub = "private";
				}else {
					boolean cc = WorkflowEditorPlugin.isAmdin();
					if(cc) {
						isadmin = "admin";
					}
				}
			}
		}
		//System.out.println("open node menu status : "+nid+" "+pstatus+" "+nstatus+" "+ispub+" "+isadmin);
		NodeStartFeature feature1 = new NodeStartFeature(featureProvider);
		if (feature1.canExecute(customContext)) {
			ContextButtonEntry button3 = new ContextButtonEntry(feature1, customContext);
			button3.setText("Start Node");
			if(pstatus.equals("run"))
				button3.setIconId(WorkflowImageProvider.IMG_START_DIS);
			else {
				if(astatus.equals("run"))
					button3.setIconId(WorkflowImageProvider.IMG_START_DIS);
				else
					button3.setIconId(WorkflowImageProvider.IMG_START);
			}				
			//if(!status.equals("run")) {
			buttons.add(button3);
			//}
		}		
		
		NodeStopFeature feature2 = new NodeStopFeature(featureProvider);
		if (feature2.canExecute(customContext)) {
			ContextButtonEntry button3 = new ContextButtonEntry(feature2, customContext);
			button3.setText("Stop Node");
			if(pstatus.equals("run"))
				button3.setIconId(WorkflowImageProvider.IMG_STOP_DIS);
			else {
				if(astatus.equals("run")) {
					if(nstatus.equals("run"))
						button3.setIconId(WorkflowImageProvider.IMG_STOP);
					else
						button3.setIconId(WorkflowImageProvider.IMG_STOP_DIS);
				} else {
					button3.setIconId(WorkflowImageProvider.IMG_STOP_DIS);
				}
			}				
			buttons.add(button3);
		}
		
		NodeInfoFeature feature3 = new NodeInfoFeature(featureProvider);
		if (feature3.canExecute(customContext)) {
			ContextButtonEntry button3 = new ContextButtonEntry(feature3, customContext);
			button3.setText("Open Node Info");
			if(pstatus.equals("run"))
				button3.setIconId(WorkflowImageProvider.IMG_INFO_DIS);
			else {
				if(astatus.equals("run"))
					button3.setIconId(WorkflowImageProvider.IMG_INFO_DIS);
				else
					button3.setIconId(WorkflowImageProvider.IMG_INFO);
			}
			//if(!status.equals("run"))
			buttons.add(button3);
		}
		
		NodeScriptFeature feature4 = new NodeScriptFeature(featureProvider);
		if (feature4.canExecute(customContext)) {
			ContextButtonEntry button3 = new ContextButtonEntry(feature4, customContext);
			button3.setText("Edit Script");
			
			if(pstatus.equals("run"))
				button3.setIconId(WorkflowImageProvider.IMG_EDIT_DIS);
			else {
				if(astatus.equals("run"))
					button3.setIconId(WorkflowImageProvider.IMG_EDIT_DIS);
				else
					button3.setIconId(WorkflowImageProvider.IMG_EDIT);
			}	
			buttons.add(button3);
			/*
			if(!pstatus.equals("run") || !pstatus.equals("exec")) {
				//check manager...
				for (NodeModel model : nodes) {
					if (model.getNodeID().equals(nid)) {
						// run node program.
						//System.out.println("--->" + model.getNodeData().getNodeName()+" "+model.getNodeData().isPublic());
						if(!model.getNodeData().isPublic()) {
							if(!pstatus.equals("run") && !pstatus.equals("exec"))
								buttons.add(button3);
						}else {
							boolean cc = WorkflowEditorPlugin.isAmdin();
							if(cc) {
								if(!pstatus.equals("run") && !pstatus.equals("exec"))
									buttons.add(button3);
							}
						}
						break;
					}
				}
			}
			*/
		}

		// TODO : BIOEXPRESS
//		ContextButtonEntry button2 = new ContextButtonEntry(new DuplicateNodeFeature(featureProvider), customContext);
//		button2.setText("Edit Node");
//		button2.setIconId(WorkflowImageProvider.IMG_INFO);
//		buttons.add(button2);
//
//		ContextButtonEntry button3 = new ContextButtonEntry(new DuplicateNodeFeature(featureProvider), customContext);
//		button3.setText("Edit Script");
//		button3.setIconId(WorkflowImageProvider.IMG_EDIT);
//		buttons.add(button3);

//		if (pe.length == 1 && featureProvider.getBusinessObjectForPictogramElement(pe[0]) instanceof WFNode) {
//			ContextButtonEntry button = new ContextButtonEntry(new CopyToPaletteFeature(featureProvider), customContext);
//			button.setText("Start Node");
//			button.setIconId(WorkflowImageProvider.IMG_PALETTE);	   
//			buttons.add(button);
//		}
//		
//		PackageComponentFeature feature = new PackageComponentFeature(featureProvider);
//		if (feature.canExecute(customContext)) {
//			ContextButtonEntry button3 = new ContextButtonEntry(feature, customContext);
//			button3.setText("Stop Node");
//			button3.setIconId(WorkflowImageProvider.IMG_PACKAGE); 
//			buttons.add(button3);
//		}

//		GrabOutputFileFeature feature2 = new GrabOutputFileFeature(featureProvider);
//		if (feature2.canExecute(customContext)) {
//			ContextButtonEntry button3 = new ContextButtonEntry(feature2, customContext);
//			button3.setText("Edit Script");
//			button3.setIconId(WorkflowImageProvider.IMG_PORT);	   
//			buttons.add(button3);
//		}

//		GrabOutputVariableFeature feature3 = new GrabOutputVariableFeature(featureProvider);
//		if (feature3.canExecute(customContext)) {
//			ContextButtonEntry button4 = new ContextButtonEntry(feature3, customContext);
//			button4.setText("Grab Output Variable");
//			button4.setIconId(WorkflowImageProvider.IMG_PORT);	   
//			buttons.add(button4);
//		}

		return buttons;
	}
}
