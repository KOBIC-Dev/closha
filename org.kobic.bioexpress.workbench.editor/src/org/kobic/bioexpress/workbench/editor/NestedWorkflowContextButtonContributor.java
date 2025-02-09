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
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ContextButtonEntry;

import gov.sandia.dart.workflow.domain.WFNode;
import org.kobic.bioexpress.workbench.editor.features.DefinePortsFromNestedWorkflowFeature;
import org.kobic.bioexpress.workbench.editor.features.OpenReferencedFileFeature;

public class NestedWorkflowContextButtonContributor implements IContextButtonContributor {

	@Override
	public List<ContextButtonEntry> getContextButtons(			
			IFeatureProvider featureProvider, CustomContext customContext) {

		List<ContextButtonEntry> buttons = new ArrayList<>(); 
		PictogramElement[] pe = customContext.getPictogramElements();
		if (pe.length == 1 && featureProvider.getBusinessObjectForPictogramElement(pe[0]) instanceof WFNode) {
			Object bo = featureProvider.getBusinessObjectForPictogramElement(pe[0]);
			if (bo instanceof WFNode) {
				WFNode node = (WFNode) bo;
				OpenReferencedFileFeature feature = new OpenReferencedFileFeature(featureProvider, "nestedWorkflow", "fileName");
				if (feature.canOperateOn(node)) {
					ContextButtonEntry button2 = new ContextButtonEntry(feature, customContext);
					button2.setText("Open nested workflow");
					button2.setIconId(WorkflowImageProvider.IMG_WORKFLOW);	   
					buttons.add(button2);
				}
				OpenReferencedFileFeature feature4 = new OpenReferencedFileFeature(featureProvider, "remoteNestedWorkflow", "fileName");
				if (feature4.canOperateOn(node)) {
					ContextButtonEntry button4 = new ContextButtonEntry(feature4, customContext);
					button4.setText("Open nested workflow");
					button4.setIconId(WorkflowImageProvider.IMG_WORKFLOW);	   
					buttons.add(button4);
				}

				DefinePortsFromNestedWorkflowFeature feature2 = new DefinePortsFromNestedWorkflowFeature(featureProvider, "nestedWorkflow", "fileName");
				if (feature2.canOperateOn(node)) {
					ContextButtonEntry button2 = new ContextButtonEntry(feature2, customContext);
					button2.setText("Define ports from nested workflow");
					button2.setIconId(WorkflowImageProvider.IMG_PORTS);	   
					buttons.add(button2);
				}
				
				DefinePortsFromNestedWorkflowFeature feature3 = new DefinePortsFromNestedWorkflowFeature(featureProvider, "remoteNestedWorkflow", "fileName");
				if (feature3.canOperateOn(node)) {
					ContextButtonEntry button3 = new ContextButtonEntry(feature3, customContext);
					button3.setText("Define ports from nested workflow");
					button3.setIconId(WorkflowImageProvider.IMG_PORTS);	   
					buttons.add(button3);
				}

			}
		}
		return buttons;
	}

}
