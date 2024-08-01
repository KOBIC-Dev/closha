package org.kobic.bioexpress.rcp.program.wizard;

import java.io.File;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.kobic.bioexpress.channel.Constant;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.channel.client.script.ScriptClient;
import org.kobic.bioexpress.channel.client.script.ScriptClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.parameter.ParameterModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;
import org.kobic.bioexpress.model.script.ScriptModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.Utils;

public class NewProgramWizard extends Wizard {

	private NewProgramWizardPage1 page1;
	private NewProgramWizardPage2 page2;
	private NewProgramWizardPage3 page3;

	private IEventBroker iEventBroker;

	private CategoryModel categoryModel;

	public NewProgramWizard(IEventBroker iEventBroker, CategoryModel categoryModel) {

		page1 = new NewProgramWizardPage1(categoryModel);
		page2 = new NewProgramWizardPage2();
		page3 = new NewProgramWizardPage3();

		this.categoryModel = categoryModel;

		setWindowTitle("New Program");
		this.iEventBroker = iEventBroker;
	}

	@Override
	public void addPages() {
		addPage(page1);
		addPage(page2);
		addPage(page3);
	}

	@Override
	public boolean performFinish() {

		String programName = page1.getProgramName();
		String subCategoryID = page1.getSubCategoryID();
		String subCategoryName = page1.getSubCategoryName();
		String version = page1.getVersion();
		String keyword = Utils.getInstance().getKeywordFormat(page1.getKeyword());
		String url = page1.getUrl();
		String description = page1.getDescription();
		String memberID = Activator.getMember().getMemberId();
		String icon = null;

		String env = null;

		if (page2.getProgramComponent2().hpcRadio.getSelection()) {
			env = Constant.CLUSTER_ENV;
		} else if (page2.getProgramComponent2().gpuRadio.getSelection()) {
			env = Constant.GPU_ENV;
		} else if (page2.getProgramComponent2().bigRadio.getSelection()) {
			env = Constant.BIGMEM_ENV;
		}

		boolean isMulti = page2.getIsMulti();
		String scripType = page2.getScriptType();
		String scriptPath = page2.getScriptPath();

		List<ProgramDataModel> relationProgram = new ArrayList<ProgramDataModel>();

		for (Map.Entry<String, ProgramDataModel> elem : page2.getMap().entrySet()) {
			relationProgram.add(elem.getValue());
		}

		switch (scripType.toLowerCase()) {
		case Constants.PYTHON:
			icon = Constants.PYTHON_ICON;
			break;
		case Constants.BASH:
			icon = Constants.BASH_ICON;
			break;
		case Constants.R:
			icon = Constants.R_ICON;
			break;
		default:
			icon = Constants.DEFAULT_LANGUAGE_ICON;
			break;
		}

		CategoryClient categoryClient = new CategoryClientImpl();
		CategoryModel rootCategoryModel = categoryClient.getCategoryWithID(this.categoryModel.getParentID());

		ProgramDataModel programDataModel = new ProgramDataModel();
		programDataModel.setProgramName(programName);
		programDataModel.setProgramDesc(description);
		programDataModel.setUrl(url);
		programDataModel.setVersion(version);

		programDataModel.setRootCategoryID(rootCategoryModel.getCategoryID());
		programDataModel.setRootCategoryName(rootCategoryModel.getCategoryName());
		programDataModel.setSubCategoryID(subCategoryID);
		programDataModel.setSubCategoryName(subCategoryName);

		programDataModel.setKeyword(Utils.getInstance().getKeywordFormat(keyword));
		programDataModel.setIsMultiCore(isMulti);
		programDataModel.setEnv(env);
		programDataModel.setScriptPath(scriptPath);
		programDataModel.setScriptType(scripType);
		programDataModel.setMemberID(memberID);
		programDataModel.setIcon(icon);

		File file = new File(scriptPath);
		String scriptName = file.getName();

		ScriptClient scriptClient = new ScriptClientImpl();

		boolean isExist = scriptClient.isExistScript(memberID, scriptPath, scriptName);

		System.out.println(String.join("\t", String.valueOf(isExist), "====>", memberID, scriptPath, scriptName));

		if (isExist) {

			ScriptModel scriptModel = scriptClient.getSelectScript(memberID, scriptPath, scriptName);

			programDataModel.setPodmanID(scriptModel.getPodmanID());
			programDataModel.setPodmanImgID(scriptModel.getPodmanImgID());
			programDataModel.setPodmanName(scriptModel.getPodmanName());
			programDataModel.setPodmanRepo(scriptModel.getPodmanRepo());
			programDataModel.setPodmanTag(scriptModel.getPodmanTag());
		} else {
			programDataModel.setPodmanID(Constants.DEFAULT_VALUE);
			programDataModel.setPodmanImgID(Constants.DEFAULT_VALUE);
			programDataModel.setPodmanName(Constants.DEFAULT_VALUE);
			programDataModel.setPodmanRepo(Constants.DEFAULT_VALUE);
			programDataModel.setPodmanTag(Constants.DEFAULT_VALUE);
		}

		int count = 0;

		List<ParameterDataModel> parameterInput = new ArrayList<ParameterDataModel>();

		count = page3.getInputTableViewer().getTable().getItemCount();

		if (count != 0) {
			for (int i = 0; i < count; i++) {
				ParameterDataModel inputParam = (ParameterDataModel) page3.getInputTableViewer().getElementAt(i);
				parameterInput.add(inputParam);
			}
		}

		List<ParameterDataModel> parameterOutput = new ArrayList<ParameterDataModel>();

		count = page3.getOutputTableViewer().getTable().getItemCount();

		if (count != 0) {
			for (int i = 0; i < count; i++) {
				ParameterDataModel inputParam = (ParameterDataModel) page3.getOutputTableViewer().getElementAt(i);
				parameterOutput.add(inputParam);
			}
		}

		List<ParameterDataModel> parameterOption = new ArrayList<ParameterDataModel>();

		count = page3.getOptionTableViewer().getTable().getItemCount();

		if (count != 0) {
			for (int i = 0; i < count; i++) {
				ParameterDataModel inputParam = (ParameterDataModel) page3.getOptionTableViewer().getElementAt(i);
				parameterOption.add(inputParam);
			}
		}

		ParameterModel parameterModel = new ParameterModel();
		parameterModel.setParameterInput(parameterInput);
		parameterModel.setParameterOutput(parameterOutput);
		parameterModel.setParameterOption(parameterOption);

		ProgramModel program = new ProgramModel();
		program.setProgramData(programDataModel);
		program.setParameter(parameterModel);
		program.setRelationProgram(relationProgram);

		ProgramClient programClient = new ProgramClientImpl();
		String rawID = programClient.insertProgram(program);

		System.out.println(rawID);

		iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());

		return true;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		// TODO Auto-generated method stub
		return super.getNextPage(currentPage);
	}
}