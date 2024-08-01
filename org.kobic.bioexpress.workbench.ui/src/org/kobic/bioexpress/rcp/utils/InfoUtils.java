package org.kobic.bioexpress.rcp.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;

import com.google.gson.Gson;

public class InfoUtils {

	protected static InfoUtils instance = null;

	public final static InfoUtils getInstance() {

		if (instance == null) {
			instance = new InfoUtils();
		} else {
			return instance;
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> converDataToMap(Object obj) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (obj instanceof WorkspaceModel) {

			WorkspaceModel workspace = (WorkspaceModel) obj;
			map.put("Workspace name", workspace.getWorkspaceName());
			map.put("Workspace id", workspace.getWorkspaceID());
			map.put("Workspace description", workspace.getDescription());
			map.put("Workspace keyword", workspace.getKeyword());
			map.put("Workspace create date", workspace.getCreateDate());
			map.put("Workspace update date", workspace.getUpdateDate());

		} else if (obj instanceof PipelineDataModel) {

			PipelineDataModel pipelineData = (PipelineDataModel) obj;
			map.put("Pipeline name", pipelineData.getPipelineName());
			map.put("Pipeline id", pipelineData.getPipelineID());
			map.put("Pipeline status", pipelineData.getStatus());
			map.put("Pipeline registCode", pipelineData.getRegistCode());
			map.put("Pipeline version", pipelineData.getVersion());
			map.put("Pipeline description", pipelineData.getPipelineDesc());
			map.put("Pipeline keyword", pipelineData.getKeyword());
			map.put("Pipeline workspace name", pipelineData.getWorkspaceName());
			map.put("Pipeline category name", pipelineData.getCategoryName());
			map.put("Pipeline public", pipelineData.isPublic);
			map.put("Pipeline shared", pipelineData.isShared);
			map.put("Pipeline create date", pipelineData.getCreateDate());
			map.put("Pipeline update date", pipelineData.getUpdateDate());

		} else if (obj instanceof ProgramDataModel) {

			ProgramDataModel programData = (ProgramDataModel) obj;
			map.put("Program name", programData.getProgramName());
			map.put("Program id", programData.getProgramID());
			map.put("Program status", programData.getStatus());
			map.put("Pipeline registCode", programData.getRegistCode());
			map.put("Program version", programData.getVersion());
			map.put("Program root category name", programData.getRootCategoryName());
			map.put("Program sub category name", programData.getSubCategoryName());
			map.put("Program description", programData.getProgramDesc());
			map.put("Program keyword", programData.getKeyword());
			map.put("Program script type", programData.getScriptType());
			map.put("Program sctipy path", programData.getScriptPath());
			map.put("Program url", programData.getUrl());
			map.put("Program multi core", programData.isMultiCore);
			map.put("Program public", programData.isPublic);
			map.put("Program registed date", programData.getRegistedDate());
			map.put("Program modified date", programData.getModifiedDate());

		} else if (obj instanceof CategoryModel) {

			CategoryModel category = (CategoryModel) obj;
			map.put("Category name", category.getCategoryName());
			map.put("Category id", category.getCategoryID());
			map.put("Category description", category.getCategoryDesc());
			map.put("Category public", category.isPublic);
			map.put("Pipeline category", category.isPipeline);
			map.put("Program ctegory", category.isProgram);
			map.put("Category create date", category.createDate);
			map.put("Category update date", category.updateDate);

		} else {

			Gson gson = new Gson();
			map = gson.fromJson(gson.toJson(obj), Map.class);

		}

		return map;
	}

}
