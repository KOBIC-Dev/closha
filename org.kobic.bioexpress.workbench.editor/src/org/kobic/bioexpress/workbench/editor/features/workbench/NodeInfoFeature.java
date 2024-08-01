/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.features.workbench;

import java.util.List;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.strikewire.snl.apc.GUIs.MultipleInputDialog;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFNode;

import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.settings.NOWPSettingsEditorUtils;
import org.kobic.bioexpress.workbench.editor.utils.EventUtils;

import gov.sandia.dart.workflow.util.PropertyUtils;

public class NodeInfoFeature extends AbstractCustomFeature {

	private static final String PORT_NAME = "Port name";
	private static final String FILE_NAME = "File name";

	public NodeInfoFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Open Node Info";
	}
	
	@Override
	public void execute(ICustomContext context) {		
		// get pipeline..
		WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
				.getDiagramBehavior().getDiagramContainer();
		PipelineModel pipeline = editor.getPipeline();
		String workspaceName = pipeline.getPipelineData().getWorkspaceName();
		String pipelineID = pipeline.getPipelineData().getPipelineID();
		String pipelineName = pipeline.getPipelineData().getPipelineName();
		String pRawID = pipeline.getRawID();
		String pstatus = pipeline.getPipelineData().getStatus();
			
		final PictogramElement pe = context.getPictogramElements()[0];
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		
		if (bo instanceof WFNode) {
			Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();
			if(pstatus.equals("run")) {
				MessageDialog.openInformation(shell, "Wanning", "The pipeline is currently running.");
				return;		
			}
			WFNode node = (WFNode) bo;
			String rawId = node.getId();
			List<NodeModel> nodes = pipeline.getNode();
			for (NodeModel n : pipeline.getNode()) {
				String st = n.getNodeData().getStatus();
				if(st.equals("run")) {
					MessageDialog.openInformation(shell, "Wanning", "There are nodes running in the pipeline.");
					return;
				}
			}
			for(NodeModel model:nodes) {
				if(model.getNodeID().equals(rawId)) {
					String nstatus = node.getStatus();
					if(nstatus.equals("run")) {
						MessageDialog.openInformation(shell, "Wanning", "There are other nodes running in the pipeline.");
						return;
					}
					EventUtils.getEventBroker().send("OPEN_NODEINFO", model);
					break;
				}
			}
		} else {
			Display.getCurrent().beep();
		}
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		if (context.getPictogramElements().length != 1)
			return false;

		final PictogramElement pe = context.getPictogramElements()[0];	
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			// TODO Should be more restrictive
			WFNode node = (WFNode) bo;
			return !"parameter".equals(node.getType());
		}
		return false;
	}
	
	@Override
	public boolean hasDoneChanges() {
		return true;
	}

}
