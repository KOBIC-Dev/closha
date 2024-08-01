package org.kobic.bioexpress.rcp.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.kobic.bioexpress.channel.utils.Utils;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.pipeline.LinkModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gov.sandia.dart.workflow.domain.DomainFactory;
import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.WFNode;

public class PipelineUtil {

	final static Logger logger = Logger.getLogger(PipelineUtil.class);

	protected static PipelineUtil instance = null;

	public final static PipelineUtil getInstance() {

		if (instance == null) {
			instance = new PipelineUtil();
		} else {
			return instance;
		}

		return instance;
	}

	@SuppressWarnings("unused")
	public static WFNode convertProgramToNode(ProgramDataModel model) {

		WFNode newNode = DomainFactory.eINSTANCE.createWFNode();
		newNode.setLabel(model.getProgramName());
		newNode.setName(model.getProgramName());
		EList<InputPort> inputs = newNode.getInputPorts();
		EList<OutputPort> outputs = newNode.getOutputPorts();

		return newNode;
	}

	private Map<String, String> getParameterSource(List<NodeModel> node) {

		String parameterValue = null;

		Map<String, String> nodesMap = new HashMap<String, String>();

		for (NodeModel n : node) {

			for (ParameterDataModel input : n.getParameter().getParameterInput()) {
				parameterValue = input.getParameterValue();
				nodesMap.put(input.getParameterID(), parameterValue);
				System.out.println("++" + input.getParameterID() + "\t" + parameterValue);
			}

			for (ParameterDataModel output : n.getParameter().getParameterOutput()) {
				parameterValue = output.getParameterValue();
				nodesMap.put(output.getParameterID(), parameterValue);
				System.out.println("--" + output.getParameterID() + "\t" + parameterValue);
			}

			for (ParameterDataModel option : n.getParameter().getParameterOption()) {
				parameterValue = option.getParameterValue();
				nodesMap.put(option.getParameterID(), parameterValue);
			}
		}

		return nodesMap;
	}

	String nodeID;
	String pipelineTemplate;

	public PipelineModel instancePipeline(PipelineModel pipelineModel) {

		Map<String, String> links = new HashMap<String, String>();

		if (pipelineModel.getLink() != null) {

			String key, value = null;

			for (LinkModel link : pipelineModel.getLink()) {

				key = link.getTargetParamID();
				value = link.getSourceParamID();

				System.out.println("key:" + key + "\tvalue:" + value);

				links.put(key, value);
			}
		}

		pipelineTemplate = pipelineModel.getPipelineData().getPipelineTemplate();

		Map<String, String> nodeIDMap = new HashMap<String, String>();

		Utils utils = Utils.getInstance();

		if (pipelineModel.getNode() != null) {

			// info
			pipelineModel.getNode().forEach(node -> {

				nodeID = utils.getNewExeID();
				String oNodeID = node.getNodeID();
				nodeIDMap.put(oNodeID, nodeID);

				String nRawID = utils.getNewExeID();
				String oRawID = node.getRawID();
				node.setRawID(nRawID);
				pipelineTemplate = pipelineTemplate.replaceAll(oRawID, nRawID);

				node.setNodeID(nodeID);
				pipelineTemplate = pipelineTemplate.replaceAll(oNodeID, nodeID);

				node.getNodeData().setRegistrantID(Constants.DEFAULT_VALUE);
				node.getNodeData().setRegistedDate(Constants.DEFAULT_VALUE);
				node.getNodeData().setModifiedDate(utils.getDate());
				node.getNodeData().setStatus(Constants.STATUS_WAIT);

				String nParamRawID = utils.getNewExeID();
				String oParamRawID = node.getParameter().getRawID();
				node.getParameter().setRawID(nParamRawID);

				try {
					pipelineTemplate = pipelineTemplate.replaceAll(oParamRawID, nParamRawID);
				} catch (Exception ex) {
					logger.error(oParamRawID + " " + nParamRawID);
				}
			});

			// output
			pipelineModel.getNode().forEach(node -> {
				if (node.getParameter().getParameterOutput() != null) {
					node.getParameter().getParameterOutput().forEach(output -> {
						if (output.getParameterValueType().equals("Folder")
								|| output.getParameterValueType().equals("HDFS")) {
							String outPath = "/" + WorkflowEditorPlugin.getDefault().getMember().getMemberId();
							outPath = outPath + "/" + pipelineModel.getPipelineData().getWorkspaceName();
							outPath = outPath + "/" + pipelineModel.getPipelineData().getPipelineName() + "/"
									+ node.getNodeData().getNodeName() + "/output";
							output.setParameterValue(outPath);
							System.out.println("@@" + outPath);
						}
					});
				}
			});

			Map<String, String> sourceParamNameMap = getParameterSource(pipelineModel.getNode());

			// input
			pipelineModel.getNode().forEach(node -> {

				if (node.getParameter().getParameterInput() != null) {

					node.getParameter().getParameterInput().forEach(input -> {

						System.out.println("->" + node.getNodeData().getNodeName() + ":" + input.getParameterName()
								+ "\t" + input.getParameterType() + "\t" + input.getSourceParamName() + "\t"
								+ input.getParameterID());

						String sourceParamID = links.get(input.getParameterID());
						String sourceParamValue = sourceParamNameMap.get(sourceParamID);

						if (sourceParamID != null && sourceParamValue != null) {

							System.out.println(input.getParameterValue() + "\t-->\t" + sourceParamValue);

							input.setParameterValue(sourceParamValue);
						}
					});
				}
			});

			// option
			pipelineModel.getNode().forEach(node -> {
				if (node.getParameter().getParameterOption() != null) {
					node.getParameter().getParameterOption().forEach(option -> {
						option.setNodeID(node.getNodeID());
						option.setParameterID(utils.getNewExeID());
					});
				}
			});

			// output parameter ID changed
			pipelineModel.getNode().forEach(node -> {
				if (node.getParameter().getParameterOutput() != null) {
					node.getParameter().getParameterOutput().forEach(output -> {
						String nParamID = utils.getNewExeID();
						String oParamID = output.getParameterID();
						output.setNodeID(node.getNodeID());
						output.setParameterID(nParamID);
						pipelineTemplate = pipelineTemplate.replaceAll(oParamID, nParamID);
					});
				}
			});

			// input parameter ID changed
			pipelineModel.getNode().forEach(node -> {

				if (node.getParameter().getParameterInput() != null) {

					node.getParameter().getParameterInput().forEach(input -> {

						String nParamID = utils.getNewExeID();
						String oParamID = input.getParameterID();
						input.setNodeID(node.getNodeID());
						input.setParameterID(nParamID);
						pipelineTemplate = pipelineTemplate.replaceAll(oParamID, nParamID);
					});
				}
			});

			// link id changed
			if (pipelineModel.getLink() != null) {
				pipelineModel.getLink().forEach(link -> {
					String nLinkID = utils.getNewExeID();
					String oLinkID = link.getLinkID();
					link.setLinkID(nLinkID);
					pipelineTemplate = pipelineTemplate.replaceAll(oLinkID, nLinkID);

					link.setSourceID(nodeIDMap.get(link.getSourceID()));
					link.setTargetID(nodeIDMap.get(link.getTargetID()));
				});
			}
		}
		
		pipelineModel.getPipelineData().setPipelineTemplate(pipelineTemplate);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(pipelineModel));

		return pipelineModel;
	}
}