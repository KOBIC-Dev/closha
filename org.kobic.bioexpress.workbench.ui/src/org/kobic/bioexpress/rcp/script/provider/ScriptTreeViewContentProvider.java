package org.kobic.bioexpress.rcp.script.provider;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.model.file.FileModel;

public class ScriptTreeViewContentProvider implements ITreeContentProvider {

	final static Logger logger = Logger.getLogger(ScriptTreeViewContentProvider.class);

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

		FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
		List<FileModel> files = fileUtilsClient.getFiles(file.getPath());

		return files.toArray();
	}
}