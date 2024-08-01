package org.kobic.bioexpress.rcp.raonwiz.provider;

import org.apache.commons.lang3.Range;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.raonwiz.RaonwizStateModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class RaonwizViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	final static Logger logger = Logger.getLogger(RaonwizViewLabelProvider.class);

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof RaonwizStateModel) {

			RaonwizStateModel item = (RaonwizStateModel) object;

			String value = null;

			switch (index) {
			case 0:
				value = item.getServerID();
				break;
			case 1:
				value = item.getServerName();
				break;
			case 2:
				value = item.getState();
				break;
			case 3:
				value = item.getLabel();
				break;
			case 4:
				value = item.getSessionStartTime();
				break;
			case 5:
				value = item.getSessionDurationTime();
				break;
			case 6:
				value = item.getSessionCount();
				break;
			case 7:
				value = item.getTransferCount();
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

		if (object instanceof RaonwizStateModel) {

			RaonwizStateModel model = (RaonwizStateModel) object;

			if (index == 2) {

				if (model.getState().equalsIgnoreCase(Constants.RAONWIZ_STATE_STABLE)) {

					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.SYNCHRONIZED_ICON);

				} else if (model.getState().equalsIgnoreCase(Constants.RAONWIZ_STATE_DISCONNECTED)) {

					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.UNSYNCHRONIZED_ICON);

				}
			} else if (index == 0) {

				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.CLUSTER_VIEW_HOST_ICON);

			} else if (index == 6) {

				Range<Integer> range = Range.between(50, 100);

				if (Integer.parseInt(model.getSessionCount()) < 50) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.SYNCHRONIZED_ICON);
				} else if (range.contains(Integer.parseInt(model.getSessionCount()))) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.WARNING_ICON);
				} else {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.UNSYNCHRONIZED_ICON);
				}

			} else if (index == 7) {

				Range<Integer> range = Range.between(30, 50);

				if (Integer.parseInt(model.getTransferCount()) < 30) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.SYNCHRONIZED_ICON);
				} else if (range.contains(Integer.parseInt(model.getTransferCount()))) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.WARNING_ICON);
				} else {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.UNSYNCHRONIZED_ICON);
				}
			}
		}

		return null;
	}

}