package org.kobic.bioexpress.rcp.pipeline.provider;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.rcp.Activator;

public class PipelineTreeViewContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof CategoryModel) {

			CategoryModel categoryModel = (CategoryModel) element;

			if (categoryModel.getObjectCount() != 0) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ArrayContentProvider.getInstance().getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof CategoryModel) {

			CategoryModel categoryModel = (CategoryModel) parentElement;

			String categoryID = categoryModel.getCategoryID();
			String memberID = Activator.getMember().getMemberId();

			Object[] list = null;

			if (categoryModel.getParentID().equals(Constant.ROOT_CATEGORY_DEFAULT_ID)) {
				CategoryClient categoryClient = new CategoryClientImpl();
				list = categoryClient.getPipelineSubCategory(categoryID, memberID).toArray();
			} else {
				PipelineClient piplnieClient = new PipelineClientImpl();
				List<PipelineDataModel> pipeline = piplnieClient.getPipelineDataOfCetegory(categoryID);

				if (pipeline == null) {
					return null;
				} else {
					list = pipeline.toArray();
				}
			}

			return list;
		}

		return null;
	}
}