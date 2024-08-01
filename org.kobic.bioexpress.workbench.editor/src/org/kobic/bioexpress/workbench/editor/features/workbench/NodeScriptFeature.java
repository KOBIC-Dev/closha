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

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.service.sso.MemberService;
import org.kobic.bioexpress.channel.service.sso.MemberServiceImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.settings.NOWPSettingsEditorUtils;
import org.kobic.bioexpress.workbench.editor.utils.CanvasUtil;

import com.strikewire.snl.apc.GUIs.MultipleInputDialog;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFNode;
import gov.sandia.dart.workflow.util.PropertyUtils;

public class NodeScriptFeature extends AbstractCustomFeature {

	private static final String PORT_NAME = "Port name";
	private static final String FILE_NAME = "File name";

	public NodeScriptFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Edit Node Script";
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
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();
			if(pstatus.equals("run")) {
				MessageDialog.openInformation(shell, "Wanning", "The pipeline is currently running.");
				return;			
			}
			WFNode node = (WFNode) bo;
			// open editor...
			String nid = node.getId();
			PipelineModel pipe = editor.getPipeline();
			List<NodeModel> nodes = pipe.getNode();
			for (NodeModel n : pipeline.getNode()) {
				String st = n.getNodeData().getStatus();
				if(st.equals("run")) {
					MessageDialog.openInformation(shell, "Wanning", "There are nodes running in the pipeline.");
					return;
				}
			}
			//check public & admin
			for (NodeModel model : nodes) {
				if (model.getNodeID().equals(nid)) {
					// run node program.
					//System.out.println("--->" + model.getNodeData().getNodeName()+" "+model.getNodeData().isPublic());
					boolean cc = WorkflowEditorPlugin.isAmdin();
					if(model.getNodeData().isPublic()) {
						if(!cc) {
							MessageDialog.openInformation(shell, "Wanning", "This node's program is public.");
							return;
						}
					}else {						
						
					}
				}
			}
			//
			NodeModel nodeModel = null;
			for(NodeModel nn:nodes) {
				if(nn.getNodeID().equals(nid)) {
					nodeModel = nn;
					break;
				}
			}
			if(nodeModel == null)
				return;
			String path = nodeModel.getNodeData().getScriptPath();
			System.out.println(path);
			FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
			FileModel fileModel = fileUtilsClient.getFile(path);
			CanvasUtil.openEditor(fileModel);
		} else {
			Display.getCurrent().beep();
		}
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		
		//node.getNodeData().isPublic()
		//MemberService sso = new MemberServiceImpl();
		//Member member = Activator.getMember();
		//sso.isAdmin(member.getMemberNo())
		
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
