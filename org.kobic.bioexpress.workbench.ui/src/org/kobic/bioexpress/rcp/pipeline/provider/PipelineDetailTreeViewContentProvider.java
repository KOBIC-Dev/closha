package org.kobic.bioexpress.rcp.pipeline.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;

public class PipelineDetailTreeViewContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof NodeModel) {
			return true;
		} else {
			return false;
		}
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

		NodeModel nodeModel = (NodeModel) parentElement;

		List<ParameterDataModel> list = new ArrayList<ParameterDataModel>();
		if(nodeModel.getParameter().getParameterInput() !=null)
			list.addAll(nodeModel.getParameter().getParameterInput());
		if(nodeModel.getParameter().getParameterOutput() !=null)
			list.addAll(nodeModel.getParameter().getParameterOutput());
		if(nodeModel.getParameter().getParameterOption() !=null)
			list.addAll(nodeModel.getParameter().getParameterOption());
		return list.toArray();
	}
}