package org.kobic.bioexpress.rcp.category.provider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;

public class CategoryTreeViewContentProvider implements ITreeContentProvider {

	private String type;

	public CategoryTreeViewContentProvider(String type) {
		this.type = type;
	}

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof CategoryModel) {

			CategoryModel categoryModel = (CategoryModel) element;

			if (categoryModel.getObjectCount() != 0) {
				return true;
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

		CategoryModel categoryModel = (CategoryModel) parentElement;

		String categoryID = categoryModel.getCategoryID();

		CategoryClient categoryClient = new CategoryClientImpl();
		Object[] list = categoryClient.getPublicSubCategory(type, categoryID).toArray();

		return list;
	}
}