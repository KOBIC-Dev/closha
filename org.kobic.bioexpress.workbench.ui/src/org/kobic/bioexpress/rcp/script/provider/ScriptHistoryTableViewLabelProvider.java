package org.kobic.bioexpress.rcp.script.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.task.ScriptTaskModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ScriptHistoryTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	final static Logger logger = Logger.getLogger(ScriptHistoryTableViewLabelProvider.class);

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof ScriptTaskModel) {

			ScriptTaskModel item = (ScriptTaskModel) object;

			String value = null;

			switch (index) {
			case 0:
				value = item.getLanguage();
				break;
			case 1:
				value = item.getScriptName();
				break;
			case 2:
				value = item.getJobID();
				break;
			case 3:
				value = item.getSubmissionTime();
				break;
			case 4:
				value = item.getFinalCompleteTime();
				break;
			case 5:
				value = item.getStatus();
				break;
			case 6:
				if (item.getEnv().equalsIgnoreCase(Constants.DEFAULT_VALUE)) {
					value = Constants.PODMAN_BASE_IMAGE_NAME;
				} else {
					value = item.getEnv();
				}
				break;
			default:
				value = Constants.DEFAULT_VALUE;
				break;
			}

			return value;
		}

		return Constants.DEFAULT_VALUE;
	}

	@Override
	public Image getColumnImage(Object object, int index) {

		if (object instanceof ScriptTaskModel) {

			ScriptTaskModel model = (ScriptTaskModel) object;

			if (index == 0) {
				if (model.getLanguage().equals(Constants.PYTHON)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_PYTHON_ICON);
				} else if (model.getLanguage().equals(Constants.BASH)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_BASH_ICON);
				} else if (model.getLanguage().equals(Constants.R)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_R_ICON);
				}
			} else if (index == 5) {

				if (model.getStatus().equals(Constants.STATUS_COMPLETE)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_COMPLETE_ICON);
				} else if (model.getStatus().equals(Constants.STATUS_RUN)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_RUN_ICON);
				} else if (model.getStatus().equals(Constants.STATUS_WAIT)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_WAIT_ICON);
				} else if (model.getStatus().equals(Constants.STATUS_ERROR)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_ERROR_ICON);
				} else if (model.getStatus().equals(Constants.STATUS_STOP)) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_STOP_ICON);
				}
			} else if (index == 6) {
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PODMAN_ICON);
			}
		}

		return null;
	}

}