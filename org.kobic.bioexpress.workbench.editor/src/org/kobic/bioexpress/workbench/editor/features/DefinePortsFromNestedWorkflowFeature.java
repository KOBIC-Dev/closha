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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.Response;
import gov.sandia.dart.workflow.domain.WFNode;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import gov.sandia.dart.workflow.util.Loader;
import gov.sandia.dart.workflow.util.PropertyUtils;

public class DefinePortsFromNestedWorkflowFeature extends AbstractFileReferenceFeature {

	public DefinePortsFromNestedWorkflowFeature(IFeatureProvider fp, String nodeType, String property) {
		super(fp, nodeType, property);
	}

	@Override
	public void execute(ICustomContext context) {		
		final PictogramElement pe = context.getPictogramElements()[0];
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			WFNode node = (WFNode) bo;
			if (canOperateOn(node)) {
				String filename = PropertyUtils.getProperty(node, "fileName");			
				IFile file = getDiagramFolder().getFile(new Path(filename));
				if (file.exists()) {
					try {
						Loader loader = new Loader();
						loader.load(file.getLocation().toOSString());
						for (Response r: loader.getResponses()) {
								if (node.getOutputPorts().stream().noneMatch(x -> x.getName().equals(r.getName()))) {
									OutputPort port = DomainFactory.eINSTANCE.createOutputPort();
									port.setName(r.getName());
									port.setType(r.getType());
									node.getOutputPorts().add(port);
									node.eResource().getContents().add(port);
								}
						}
						for (WFNode param: loader.getParameters()) {
							if (node.getInputPorts().stream().noneMatch(x -> x.getName().equals(param.getName()))) {
								InputPort port = DomainFactory.eINSTANCE.createInputPort();
								port.setName(param.getName());
								port.setType("default");
								node.getInputPorts().add(port);
								node.eResource().getContents().add(port);
							}
						}
						
						getFeatureProvider().getDiagramTypeProvider().getNotificationService().updatePictogramElements(new PictogramElement[] {pe});

					} catch (Exception e) {
						ErrorDialog.openError(Display.getCurrent().getActiveShell(), "Error", e.getMessage(), WorkflowEditorPlugin.getDefault().newErrorStatus(e));
					}
				} else {
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "File missing", "The nested workflow file does not exist!");
				}
			}			
		}
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		if (context.getPictogramElements().length != 1)
			return false;

		final PictogramElement pe = context.getPictogramElements()[0];;
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			return canOperateOn((WFNode) bo);
		}
		return false;
	}
	
	@Override
	public String getName() {
		return "Define Ports from Nested Workflow";				
	}
}
