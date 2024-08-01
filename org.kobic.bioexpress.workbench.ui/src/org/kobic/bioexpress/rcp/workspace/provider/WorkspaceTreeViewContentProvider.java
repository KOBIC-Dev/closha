package org.kobic.bioexpress.rcp.workspace.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;

public class WorkspaceTreeViewContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof WorkspaceModel) {

			WorkspaceModel workspace = (WorkspaceModel) element;

			if (workspace.getPipelineCount() != 0) {
				return true;
			} else {
				return false;
			}
		}

		return false;
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
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof WorkspaceModel) {

			WorkspaceModel workspaceModel = (WorkspaceModel) parentElement;

			String memberID = Activator.getMember().getMemberId();
			String workspaceID = workspaceModel.getWorkspaceID();

			PipelineClient pipelineClient = new PipelineClientImpl();
			List<PipelineDataModel> pipelineDataModel = pipelineClient.getPipelineDataOfWorkspace(memberID,
					workspaceID);

			if (pipelineDataModel == null) {
				pipelineDataModel = new ArrayList<PipelineDataModel>();
				return pipelineDataModel.toArray();
			} else {
				return pipelineDataModel.toArray();
			}
		}

		return null;
	}
}