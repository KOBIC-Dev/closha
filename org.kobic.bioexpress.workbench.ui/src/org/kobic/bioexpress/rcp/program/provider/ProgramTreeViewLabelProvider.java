package org.kobic.bioexpress.rcp.program.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ProgramTreeViewLabelProvider extends LabelProvider {

	final static Logger logger = Logger.getLogger(ProgramTreeViewLabelProvider.class);

	@Override
	public String getText(Object object) {

		if (object instanceof CategoryModel) {
			CategoryModel categoryModel = (CategoryModel) object;
			return categoryModel.getCategoryName() + " (" + categoryModel.getCreateDate() + ")";
		} else if (object instanceof ProgramDataModel) {
			ProgramDataModel programModel = (ProgramDataModel) object;
			return programModel.getProgramName() + " (" + programModel.getRegistedDate() + ")";
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {

		if (object instanceof CategoryModel) {
			
			CategoryModel categoryModel = (CategoryModel) object;

			if (categoryModel.isRoot) {
				
				if(categoryModel.isPublic) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_PUBLIC_ROOT_CATEGORY_ICON);
				}else {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_ROOT_CATEGORY_ICON);
				}
				
			} else {
				
				if(categoryModel.isPublic) {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_PUBLIC_SUB_CATEGORY_ICON);
				}else {
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_SUB_CATEGORY_ICON);
				}
				
			}
			
		} else {
			
			ProgramDataModel programDataModel = (ProgramDataModel) object;
			
			if (programDataModel.isPublic) {
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_PUBLIC_PROGRAM_ICON);
			} else {
				return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_PROGRAM_ICON);
			}
			
		}
	}
}