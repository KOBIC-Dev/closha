package org.kobic.bioexpress.rcp.keyword.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.common.KeywordModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.osgi.framework.FrameworkUtil;

public class KeywordTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	final static Logger logger = Logger.getLogger(KeywordTableViewLabelProvider.class);

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof KeywordModel) {

			KeywordModel item = (KeywordModel) object;

			String value = null;

			switch (index) {
			case 0:
				value = item.getId();
				break;
			case 1:
				value = item.getName();
				break;
			case 2:
				value = item.getDescription();
				break;
			case 3:
				value = item.getCategory();
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

		if (index == 0) {
			return getImage(object);
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {
		
		String symbolicName = FrameworkUtil.getBundle(getClass()).getSymbolicName();
		
		return ResourceManager.getPluginImage(symbolicName, Constants.KEYWORD_ICON);
	}
}