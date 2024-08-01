package org.kobic.bioexpress.rcp.program.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ParameterTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	final static Logger logger = Logger.getLogger(ParameterTableViewLabelProvider.class);

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof ParameterDataModel) {

			ParameterDataModel item = (ParameterDataModel) object;

			String value = null;

			switch (index) {
			case 0:
				value = String.valueOf(item.isIsRequire());
				break;
			case 1:
				value = item.getParameterValueType();
				break;
			case 2:
				value = item.getParameterName();
				break;
			case 3:
				value = item.getParameterValue();
				break;
			case 4:
				value = item.getParameterDesc();
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

		if (object instanceof ParameterDataModel) {
			
			ParameterDataModel model = (ParameterDataModel) object;
			
			if (index == 0 && model.isRequire == true) {
				
				return getImage(object);
			}
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {

		return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.REQUIRED_ICON);
	}
}