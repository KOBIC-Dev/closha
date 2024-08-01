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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import gov.sandia.dart.workflow.domain.WFNode;

import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;

public class NodeStopFeature extends AbstractCustomFeature {

	public NodeStopFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Stop Node";
	}

	@Override
	public void execute(ICustomContext context) {

		final PictogramElement pe = context.getPictogramElements()[0];

		WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
				.getDiagramBehavior().getDiagramContainer();

		PipelineModel pipeline = editor.getPipeline();
		String pstatus = pipeline.getPipelineData().getStatus();
		if(pstatus.equals("run"))
			return;
		Member member = WorkflowEditorPlugin.getDefault().getMember();

		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);

		if (bo instanceof WFNode) {
			Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();
			WFNode node = (WFNode) bo;
			String nstatus = node.getStatus();
			if(!nstatus.equals("run"))
				return;

			for (NodeModel n : pipeline.getNode()) {

				if (n.getNodeID().equals(node.getId())) {

					boolean IS_CONFIRM = MessageDialog.openConfirm(shell, "Stop Confirm",
							"["+ n.getNodeData().getProgramName() + "] Do you want to stop the node?");

					if (IS_CONFIRM) {
						TaskClient taskClient = new TaskClientImpl();
						taskClient.stopSingleNodeJob(n, pipeline);
						MessageDialog.openInformation(shell, "Confirm", "[" + n.getNodeData().getProgramName() + "] It was cancel to stop.");
						//
						break;
					} else {
						System.out.println("Cancel to stop.");
					}
				}
			}
			//
			Display.getDefault().asyncExec(new Runnable() {								
				@Override
				public void run() {
					editor.refreshPipeline();
					editor.doSave(null);
					editor.disableItems();
				}
			});
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
