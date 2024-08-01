package org.kobic.bioexpress.rcp.podman.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class PodmanTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof PodmanModel) {

			PodmanModel cm = (PodmanModel) object;
			String value = null;

			switch (index) {
			case 0:
				value = cm.getPodmanType();
				break;
			case 1:
				value = cm.getName();
				break;
			case 2:
				value = cm.getPodmanID();
				break;
			case 3:
				value = cm.getImageID();
				break;
			case 4:
				value = cm.getRepo();
				break;
			case 5:
				value = cm.getTag();
				break;
			case 6:
				value = cm.getStatus();
				break;
			case 7:
				value = cm.getCreateDate();
				value = value.split(Constants.DEFAULT_SPACE_VALUE)[0];
				break;
			case 8:
				value = String.valueOf(cm.isIsOfficial());
				break;
			case 9:
				value = cm.getDescription();
				break;
			default:
				value = Constants.DEFAULT_NULL_VALUE;
				break;
			}

			return value;
		}
		return "";
	}

	@Override
	public Image getColumnImage(Object object, int index) {

		if (index == 0) {
			return getImage(object);
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {

		if (object instanceof PodmanModel) {

			PodmanModel m = (PodmanModel) object;

			if (m.getPodmanType().equalsIgnoreCase(Constants.PODMAN_BUILD_FILE_TYPE)) {
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PODMAN_BUILD_FILE);
			} else if (m.getPodmanType().equalsIgnoreCase(Constants.PODMAN_IMAGE_TYPE)) {
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PODMAN_SAVE_IMAGE_FILE);
			} else {
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PODMAN_BUILD_FILE);
			}
		}

		return null;
	}
}