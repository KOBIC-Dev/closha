package org.kobic.bioexpress.rcp.file.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.Utils;

public class FileTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	final static Logger logger = Logger.getLogger(FileTableViewLabelProvider.class);

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof FileModel) {

			FileModel fm = (FileModel) object;

			String value = null;

			switch (index) {
			case 0:
				value = fm.getName();
				break;
			case 1:
				value = Utils.getInstance().humanReadableByteCount(fm.getSize(), true);
				break;
			case 2:
				value = fm.getExtension();
				break;
			case 3:
				value = fm.getCreateDate();
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

		if (object instanceof FileModel) {

			FileModel fileModel = (FileModel) object;

			if (fileModel.isIsDir()) {
				
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.FILE_VIEW_FOLDER_ICON);
				
			} else {

				String extIcon = Constants.EXTENSION_MAP.get(fileModel.getExtension().toLowerCase());
				
				if (extIcon != null && extIcon.length() != 0) {
					
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, extIcon);
	
				} else {
				
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.EXTENSION_MAP.get(Constants.FILE_EXTENSION_BASIC));
				}
			}
		}

		return null;
	}
}