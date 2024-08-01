package org.kobic.bioexpress.rcp.workspace.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;

public class WorkspaceTreeViewLabelProvider extends LabelProvider {

	final static Logger logger = Logger.getLogger(WorkspaceTreeViewLabelProvider.class);

	@Override
	public String getText(Object object) {

		if (object instanceof WorkspaceModel) {

			WorkspaceModel workspace = (WorkspaceModel) object;

			return workspace.getWorkspaceName();

		} else if (object instanceof PipelineDataModel) {

			PipelineDataModel pipelineDataModel = (PipelineDataModel) object;

			return pipelineDataModel.getPipelineName() + " (" + pipelineDataModel.getCreateDate() + ")";
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {

		if (object instanceof WorkspaceModel) {

			return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.WORKSPACE_VIEW_WORKSPACE_ICON);
		} else if (object instanceof PipelineDataModel) {

			PipelineDataModel pipelineDataModel = (PipelineDataModel) object;

			switch (pipelineDataModel.getStatus()) {
			case Constants.STATUS_RUN:
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
						Constants.PIPELINE_VIEW_RUN_PIPELINE_ICON);
			case Constants.STATUS_EXEC:
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
						Constants.PIPELINE_VIEW_RUN_PIPELINE_ICON);
			default:
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_VIEW_PIPELINE_ICON);
			}
		} else {
			return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_VIEW_PIPELINE_ICON);
		}
	}
}