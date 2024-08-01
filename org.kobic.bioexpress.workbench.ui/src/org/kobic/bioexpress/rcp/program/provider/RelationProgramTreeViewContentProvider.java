package org.kobic.bioexpress.rcp.program.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;

public class RelationProgramTreeViewContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof ProgramDataModel) {
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

		ProgramDataModel programDataModel = (ProgramDataModel) parentElement;

		ProgramClient programClient = new ProgramClientImpl();
		ProgramModel program = programClient.getProgram(programDataModel.getRawID());

		List<ParameterDataModel> list = new ArrayList<ParameterDataModel>();
		list.addAll(program.getParameter().getParameterInput());
		list.addAll(program.getParameter().getParameterOutput());
		list.addAll(program.getParameter().getParameterOption());

		return list.toArray();
	}
}