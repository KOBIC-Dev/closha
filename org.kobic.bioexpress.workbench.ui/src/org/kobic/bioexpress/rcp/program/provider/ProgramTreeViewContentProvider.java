package org.kobic.bioexpress.rcp.program.provider;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.db.Constant;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.Activator;

public class ProgramTreeViewContentProvider implements ITreeContentProvider {

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
				list = categoryClient.getProgramSubCategory(categoryID, memberID).toArray();
			} else {
				ProgramClient programClient = new ProgramClientImpl();

				List<ProgramDataModel> programs = programClient.getProgramDataList(categoryID, memberID);

				if (programs == null) {
					return null;
				} else {
					list = programs.toArray();
				}
			}

			return list;
		}
		return null;
	}
}