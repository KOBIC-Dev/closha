package org.kobic.bioexpress.workbench.editor.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.db.utils.Utils;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.parameter.ParameterModel;
import org.kobic.bioexpress.model.pipeline.LinkModel;
import org.kobic.bioexpress.model.pipeline.NodeDataModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.NamedObject;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.Property;
import gov.sandia.dart.workflow.domain.WFNode;

public class PipelineUtil {

	public static WFNode getNodeFromProgram(String pid, ICreateContext context, PipelineModel pipeline,
			String scriptType) {
		ProgramClient programClient = new ProgramClientImpl();
		ProgramModel program = programClient.getProgram(pid);
		ProgramDataModel programData = program.getProgramData();

		WFNode newNode = DomainFactory.eINSTANCE.createWFNode();
		String newName = makeNameUnique(programData.getProgramName(), context);
		newNode.setName(newName);
		newNode.setType(scriptType);
		// String label = nodeType.getDisplayLabel();
		newNode.setLabel(newName);// programData.getProgramName());
		// TODO : BIOEXPRESS
		newNode.setDesc(programData.getProgramDesc());
		//
		if (programData.isIsPublic()) {
			newNode.setPublic(true);
		} else {
			newNode.setPublic(false);
		}
		/// $memberID/$workspaceName/$pipelineName/$nodeName/output
		String outPath = "/" + WorkflowEditorPlugin.getDefault().getMember().getMemberId();
		outPath = outPath + "/" + pipeline.getPipelineData().getWorkspaceName();
		outPath = outPath + "/" + pipeline.getPipelineData().getPipelineName() + "/" + newName + "/output";

		List<ParameterDataModel> inputs = program.getParameter().getParameterInput();
		List<ParameterDataModel> outputs = program.getParameter().getParameterOutput();
		List<ParameterDataModel> options = program.getParameter().getParameterOption();
		if (inputs == null)
			inputs = new ArrayList<ParameterDataModel>();
		if (outputs == null)
			outputs = new ArrayList<ParameterDataModel>();
		if (options == null)
			options = new ArrayList<ParameterDataModel>();

		EList<InputPort> ins = newNode.getInputPorts();
		EList<OutputPort> outs = newNode.getOutputPorts();

		String _id = null;
		Utils utils = Utils.getInstance();

		for (ParameterDataModel param : inputs) {
			InputPort ip = DomainFactory.eINSTANCE.createInputPort();

			_id = utils.getNewRawID();

			ip.setId(_id);
			param.setParameterID(_id);

			ip.setName(param.getParameterName());
			ip.setType(param.getParameterType());
			EList<Property> props = ip.getProperties();
			Property prop = DomainFactory.eINSTANCE.createProperty();
			prop.setValue(param.getParameterValue());
			prop.setType(param.getParameterValueType());
			props.clear();
			props.add(prop);
			ins.add(ip);
		}
		for (ParameterDataModel param : outputs) {
			OutputPort ip = DomainFactory.eINSTANCE.createOutputPort();

			_id = utils.getNewRawID();

			ip.setId(_id);
			param.setParameterID(_id);

			ip.setName(param.getParameterName());
			ip.setType(param.getParameterType());
			EList<Property> props = ip.getProperties();
			Property prop = DomainFactory.eINSTANCE.createProperty();
			if (param.getParameterValueType().equals("Folder") || param.getParameterValueType().equals("HDFS")) {
				prop.setValue(outPath);
				param.setParameterValue(outPath);
			}
			prop.setValue(param.getParameterValue());
			prop.setType(param.getParameterValueType());
			props.clear();
			props.add(prop);
			outs.add(ip);
			System.out.println(param.getParameterType() + " " + prop.getValue() + " " + prop.getType() + "==<<");
			/// $memberID/$workspaceName/$pipelineName/$nodeName/output
		}
		//
		if (context.getTargetContainer() instanceof Diagram) {
			Diagram diagram = (Diagram) context.getTargetContainer();
			// diagram.
		}
		//
		ParameterModel param = program.getParameter();
		NodeDataModel nodeData = getNodeDataModel(programData, newName);
		String nid = Utils.getInstance().getNewRawID();
		newNode.setId(nid);
		NodeModel nodeModel = new NodeModel(pipeline.getRawID(), nid, nodeData, param);
		// get nodes
		List<NodeModel> nodes = pipeline.getNode();
		if (nodes == null)
			nodes = new ArrayList<NodeModel>();
		nodes.add(nodeModel);
		pipeline.setNode(nodes);
		return newNode;
	}

	public static void setLinkFromProgram(String rawId, WFNode source, WFNode target, ICreateConnectionContext context,
			PipelineModel pipeline, OutputPort sourcep, InputPort targetp) {
		//
		String lid = Utils.getInstance().getNewRawID();
		LinkModel linkModel = new LinkModel(rawId, source.getId(), target.getId(), source.getName(), target.getName(),
				sourcep.getName(), targetp.getName(), sourcep.getId(), targetp.getId());
		// source node raw id & target node raw id setting...
		// get nodes
		pipeline.getLink().add(linkModel);
	}

	public static NodeDataModel getNodeDataModel(ProgramDataModel programData, String newName) {

		String programID, programName, programDesc, keyword, registrantID, registedDate, modifiedDate, rootCategoryID,
				rootCategoryName, subCategoryID, subCategoryName, nodeName, scriptPath, scriptType, url, version, env,
				status, coreNum, icon, podmanID, podmanImgID, podmanRepo, podmanTag, podmanName;

		programID = programName = programDesc = keyword = registrantID = registedDate = modifiedDate = rootCategoryID = rootCategoryName = subCategoryID = subCategoryName = nodeName = scriptPath = scriptType = url = version = env = status = coreNum = icon = podmanID = podmanImgID = podmanRepo = podmanTag = podmanName = null;

		boolean isPublic, isMultiCore, isStart, isEnd;
		isPublic = isMultiCore = isStart = isEnd = false;

		programID = programData.getProgramID();
		programName = programData.getProgramName();
		// String newName = makeNameUnique(programData.getProgramName(), context);

		programDesc = programData.getProgramDesc();
		keyword = programData.getKeyword();
		registedDate = programData.getRegistedDate();
		modifiedDate = programData.getModifiedDate();
		rootCategoryID = programData.getRootCategoryID();
		rootCategoryName = programData.getRootCategoryName();
		subCategoryID = programData.getSubCategoryID();
		subCategoryName = programData.getSubCategoryName();
		nodeName = newName;// programData.getProgramName();
		scriptPath = programData.getScriptPath();
		scriptType = programData.getScriptType();
		url = programData.getUrl();
		version = programData.getVersion();
		env = programData.getEnv();
		status = programData.getStatus();
		coreNum = programData.getCoreNum();
		icon = "";
		int x = 0;
		int y = 0;
		isPublic = programData.isIsPublic();
		isMultiCore = programData.isIsMultiCore();

		podmanID = programData.getPodmanID();
		podmanImgID = programData.getPodmanImgID();
		podmanRepo = programData.getPodmanRepo();
		podmanTag = programData.getPodmanTag();
		podmanName = programData.getPodmanName();

		NodeDataModel nodeData = new NodeDataModel(programID, programName, programDesc, keyword, registrantID,
				registedDate, modifiedDate, subCategoryID, subCategoryName, newName, scriptPath, scriptType, url,
				version, env, status, coreNum, icon, isPublic, isMultiCore, isStart, isEnd, x, y, podmanID, podmanImgID,
				podmanRepo, podmanTag, podmanName);
		return nodeData;
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

	public static void setOutValueToIn(OutputPort source, InputPort target, PipelineModel pipeline) {
		WFNode sNode = source.getNode();
		WFNode tNode = target.getNode();
		String sId = source.getId();
		String eId = target.getId();
		NodeModel sNodeModel = null;
		NodeModel tNodeModel = null;
		List<NodeModel> nodes = pipeline.getNode();
		for (NodeModel node : nodes) {
			if (sNode.getId().equals(node.getNodeID())) {
				sNodeModel = node;
			}
			if (tNode.getId().equals(node.getNodeID())) {
				tNodeModel = node;
			}
		}

		ParameterDataModel sPort = null;
		List<ParameterDataModel> oPorts = sNodeModel.getParameter().getParameterOutput();
		for (ParameterDataModel port : oPorts) {
			if (port.getParameterID().equals(sId)) {
				sPort = port;
				break;
			}
		}
		if (sPort != null) {
			List<ParameterDataModel> iPorts = tNodeModel.getParameter().getParameterInput();
			for (ParameterDataModel port : iPorts) {
				if (port.getParameterID().equals(eId)) {
					port.setParameterValue(sPort.getParameterValue());
					port.setParameterValueType(sPort.getParameterValueType());
					break;
				}
			}
		}
	}

	public static void checkParams(NodeModel node, List<LinkModel> links) {
		// check parameter..
		ParameterModel param = node.getParameter();
		List<ParameterDataModel> inputs = param.getParameterInput();
		List<ParameterDataModel> outputs = param.getParameterOutput();
		for (ParameterDataModel p : inputs) {
			String pname = null;
			for (LinkModel link : links) {
				if (link.getTargetID().equals(p.getParameterID())) {
					pname = node.getNodeData().getNodeName() + ";" + link.getSourceName();
					break;
				}
			}
			p.setSourceParamName(pname);
			p.setTargetParamName(new ArrayList<String>());
		}
		for (ParameterDataModel p : outputs) {
			boolean c = true;
			List<String> names = new ArrayList<String>();
			for (LinkModel link : links) {
				if (link.getSourceID().equals(p.getParameterID())) {
					String pname = node.getNodeData().getNodeName() + ";" + link.getTargetName();
					names.add(pname);
					c = false;
				}
			}
			p.setSourceParamName("");
			if (c)
				p.setTargetParamName(new ArrayList<String>());
			else
				p.setTargetParamName(names);
		}
		//
	}

	public static WFNode getWFNodeFromNodeModel(NodeModel mnode) {
		WFNode newNode = DomainFactory.eINSTANCE.createWFNode();
		String newName = mnode.getNodeData().getNodeName();
		newNode.setId(mnode.getNodeID());
		newNode.setName(newName);
		String stype = mnode.getNodeData().getScriptType();
		stype = stype.toLowerCase();
		newNode.setType(stype);
		// String label = nodeType.getDisplayLabel();
		newNode.setLabel(newName);// programData.getProgramName());
		// TODO : BIOEXPRESS
		newNode.setDesc(mnode.getNodeData().getProgramDesc());
		//
		newNode.setPublic(mnode.getNodeData().isPublic());
		/// $memberID/$workspaceName/$pipelineName/$nodeName/output
		List<ParameterDataModel> inputs = mnode.getParameter().getParameterInput();
		List<ParameterDataModel> outputs = mnode.getParameter().getParameterOutput();
		List<ParameterDataModel> options = mnode.getParameter().getParameterOption();
		if (inputs == null)
			inputs = new ArrayList<ParameterDataModel>();
		if (outputs == null)
			outputs = new ArrayList<ParameterDataModel>();
		if (options == null)
			options = new ArrayList<ParameterDataModel>();

		EList<InputPort> ins = newNode.getInputPorts();
		EList<OutputPort> outs = newNode.getOutputPorts();

		String _id = null;
		Utils utils = Utils.getInstance();

		for (ParameterDataModel param : inputs) {
			InputPort ip = DomainFactory.eINSTANCE.createInputPort();

			_id = utils.getNewRawID();

			ip.setId(_id);
			ip.setName(param.getParameterName());
			ip.setType(param.getParameterType());
			EList<Property> props = ip.getProperties();
			Property prop = DomainFactory.eINSTANCE.createProperty();
			prop.setValue(param.getParameterValue());
			prop.setType(param.getParameterValueType());
			props.clear();
			props.add(prop);
			ins.add(ip);
		}
		for (ParameterDataModel param : outputs) {
			OutputPort ip = DomainFactory.eINSTANCE.createOutputPort();

			_id = utils.getNewRawID();

			ip.setId(_id);
			param.setParameterID(_id);

			ip.setName(param.getParameterName());
			ip.setType(param.getParameterType());
			EList<Property> props = ip.getProperties();
			Property prop = DomainFactory.eINSTANCE.createProperty();
			prop.setValue(param.getParameterValue());
			prop.setType(param.getParameterValueType());
			props.clear();
			props.add(prop);
			outs.add(ip);
			System.out.println(param.getParameterType() + " " + prop.getValue() + " " + prop.getType() + "==<<");
			/// $memberID/$workspaceName/$pipelineName/$nodeName/output
		}
		//
		return newNode;
	}
}
