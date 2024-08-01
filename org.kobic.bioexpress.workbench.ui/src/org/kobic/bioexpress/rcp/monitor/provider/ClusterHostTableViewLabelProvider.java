package org.kobic.bioexpress.rcp.monitor.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.cluster.GridHostModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ClusterHostTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof GridHostModel) {

			GridHostModel cm = (GridHostModel) object;
			String value = null;

			switch (index) {
			case 0:
				value = cm.getHost();
				break;
			case 1:
				value = cm.getCpu();
				break;
			case 2:
				value = cm.getLoad();
				break;
			case 3:
				value = cm.getMemTo();
				break;
			case 4:
				value = cm.getMemUse();
				break;
			case 5:
				value = cm.getSwapTo();
				break;
			case 6:
				value = cm.getSwapUse();
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

		if (object instanceof GridHostModel) {
			
			@SuppressWarnings("unused")
			GridHostModel hostModel = (GridHostModel) object;
			
			return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.CLUSTER_VIEW_HOST_ICON);
		}

		return null;
	}
}