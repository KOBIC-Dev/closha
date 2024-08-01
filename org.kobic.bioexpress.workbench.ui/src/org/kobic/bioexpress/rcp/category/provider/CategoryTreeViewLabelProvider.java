package org.kobic.bioexpress.rcp.category.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class CategoryTreeViewLabelProvider extends LabelProvider {

	final static Logger logger = Logger.getLogger(CategoryTreeViewLabelProvider.class);

	@Override
	public String getText(Object object) {

		if (object instanceof CategoryModel) {
			CategoryModel categoryModel = (CategoryModel) object;
			return categoryModel.getCategoryName();
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {

		if (object instanceof CategoryModel) {

			CategoryModel categoryModel = (CategoryModel) object;

			if (categoryModel.isPipeline) {

				if (categoryModel.isRoot) {
					
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_VIEW_ROOT_CATEGORY_ICON);

				} else {
					
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_VIEW_SUB_CATEGORY_ICON);

				}
			} else {

				if (categoryModel.isRoot) {

					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_ROOT_CATEGORY_ICON);

				} else {
					
					return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_SUB_CATEGORY_ICON);

				}

			}

		}

		return null;
	}
}