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

import org.kobic.bioexpress.channel.Constant;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.settings.NOWPSettingsEditorUtils;
import gov.sandia.dart.workflow.util.PropertyUtils;

public class NodeStartFeature extends AbstractCustomFeature {

	private static final String PORT_NAME = "Port name";
	private static final String FILE_NAME = "File name";

	public NodeStartFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Start Node";
	}

	@Override
	public void execute(ICustomContext context) {
		final PictogramElement pe = context.getPictogramElements()[0];

		// get pipeline..
		WorkflowDiagramEditor editor = (WorkflowDiagramEditor) getFeatureProvider().getDiagramTypeProvider()
				.getDiagramBehavior().getDiagramContainer();

		PipelineModel pipeline = editor.getPipeline();

		String workspaceName = pipeline.getPipelineData().getWorkspaceName();
		String pipelineID = pipeline.getPipelineData().getPipelineID();
		String pipelineName = pipeline.getPipelineData().getPipelineName();
		String pRawID = pipeline.getRawID();
		String pstatus = pipeline.getPipelineData().getStatus();
		//
		Member member = WorkflowEditorPlugin.getDefault().getMember();
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();
			if(pstatus.equals("run")) {
				MessageDialog.openInformation(shell, "Wanning", "The pipeline is currently running.");
				return;	
			}
			WFNode node = (WFNode) bo;
			String nstatus = node.getStatus();
			//다른 노드 실행 체크.
			for (NodeModel n : pipeline.getNode()) {
				String st = n.getNodeData().getStatus();
				if(st.equals("run")) {
					//MessageDialog.openInformation(shell, "Error", "There are other nodes running in the pipeline.");
					MessageDialog.openInformation(shell, "Wanning", "There are nodes running in the pipeline.");
					return;
				}
			}
			for (NodeModel n : pipeline.getNode()) {
				if (n.getNodeID().equals(node.getId())) {
					//BIOEXPRESS : SAVE
					boolean check = editor.checkSaveStatus();
					if(check) {
						System.out.println("save check : start node run...!!!!");
						break;
					}
					//
					boolean IS_CONFIRM = MessageDialog.openConfirm(shell, "Start Confirm",
							 "[" + n.getNodeData().getProgramName() + "] Do you want to run the node?");					
					if (IS_CONFIRM) {
						TaskClient taskClient = new TaskClientImpl();
						taskClient.executeSingleNodeJob(n, workspaceName, pipelineID, pipelineName,
								Constant.DEFAULT_NA_VALUE, member.getMemberId(), member.getMemberEmail(), pRawID);
						//TODO : BIOEXPRESS
						MessageDialog.openInformation(shell, "Confirm", "[" + n.getNodeData().getProgramName() + "] It was normally operated.");						
						//editor.doSave(null);
					}
					Display.getDefault().asyncExec(new Runnable() {								
						@Override
						public void run() {
							editor.refreshPipeline();
							editor.doSave(null);
							editor.disableItems();
						}
					});
					break;
				}
			}

//			MultipleInputDialog dialog = new MultipleInputDialog(shell, "Grab Output File From Workflow Node") {
//				@Override public int open() {
//					addTextField(PORT_NAME, "", false);
//					addTextField(FILE_NAME, "", false);
//					validators.add(new Validator() {
//						@Override
//						protected boolean validate() {
//							String text = ((Text) controlList.stream().
//									filter(c -> c.getData(FIELD_NAME).equals(PORT_NAME)).findFirst().get()).getText();
//							return !text.contains(".");
//						}
//					});
//					return super.open();
//				}
//			};
//
//			if (dialog.open() == InputDialog.OK) {
//				//WFNode node = (WFNode) bo;
//				String name = NOWPSettingsEditorUtils.createUniqueName(dialog.getStringValue(PORT_NAME), node.getOutputPorts());					
//				String file = dialog.getStringValue(FILE_NAME);				
//
//				OutputPort port = DomainFactory.eINSTANCE.createOutputPort();
//				port.setName(name);
//				port.setType("output_file");	
//				node.getOutputPorts().add(port);
//				node.eResource().getContents().add(port);
//				PropertyUtils.setProperty(port, "filename", file);
//
//			}
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
