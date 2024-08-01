package org.kobic.bioexpress.rcp.program.handler;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class InstalledProgramTreeViewLabelProvider extends LabelProvider {

	final static Logger logger = Logger.getLogger(InstalledProgramTreeViewLabelProvider.class);

	@Override
	public String getText(Object object) {

		if (object instanceof FileModel) {
			FileModel fileModel = (FileModel) object;
			return fileModel.getName();
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