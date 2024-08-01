package org.kobic.bioexpress.rcp.gbox.provider;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;

public class GBoxTreeViewContentProvider implements ITreeContentProvider {

	final static Logger logger = Logger.getLogger(GBoxTreeViewContentProvider.class);

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof FileModel) {
			FileModel fileModel = (FileModel) element;

			if (fileModel.isIsSub()) {
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

		FileModel file = (FileModel) parentElement;
		
		List<FileModel> files = Activator.getRapidantService().getFiles(file.getPath());

		return files.toArray();
	}
}