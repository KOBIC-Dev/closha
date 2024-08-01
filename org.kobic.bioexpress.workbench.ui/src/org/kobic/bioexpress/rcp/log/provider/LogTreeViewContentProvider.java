package org.kobic.bioexpress.rcp.log.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.log.LogClient;
import org.kobic.bioexpress.channel.client.log.LogClientImpl;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.log.JobLogModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.task.SubTaskModel;

public class LogTreeViewContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof NodeModel) {
			return true;
		} else if (element instanceof SubTaskModel) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ArrayContentProvider.getInstance().getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object object) {

		if (object instanceof NodeModel) {

			NodeModel nodeModel = (NodeModel) object;

			List<SubTaskModel> list = new ArrayList<SubTaskModel>();

			TaskClient taskClient = new TaskClientImpl();
			list = taskClient.getAllSubTask(nodeModel.getNodeID());

			return list.toArray();

		} else if (object instanceof SubTaskModel) {

			SubTaskModel subTaskModel = (SubTaskModel) object;

			List<JobLogModel> list = new ArrayList<JobLogModel>();

			LogClient logClient = new LogClientImpl();
			list = logClient.getAllJobLogData(subTaskModel.getSubTaskID());

			return list.toArray();
		}

		return null;
	}
}