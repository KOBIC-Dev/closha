package org.kobic.bioexpress.rcp.handler;

import java.io.IOException;
import java.rmi.server.UID;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClient;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class DeleteHandler {

	final static Logger logger = Logger.getLogger(DeleteHandler.class);

	@Inject
	private IEventBroker iEventBroker;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (object instanceof WorkspaceModel) {

			WorkspaceModel workspaceModel = (WorkspaceModel) object;

			boolean confirm = MessageDialog.openConfirm(shell, "Delete Workspace", "'"
					+ workspaceModel.getWorkspaceName() + "'" + "\n\nAre you sure you want to delete the workspace?");

			if (confirm) {
				WorkspaceClient workspaceClient = new WorkspaceClientImpl();
				int res = workspaceClient.deleteWorkspace(workspaceModel.getRawID());

				if (res == 1) {
					iEventBroker.send(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME, new UID().toString());
				}
			}

		} else if (object instanceof PipelineDataModel) {

			PipelineDataModel pipeline = (PipelineDataModel) object;

			if (pipeline.isIsPublic()) {

				if (Activator.isAdmin()) {

					boolean confirm = MessageDialog.openConfirm(shell, "Delete Pipeline", "'"
							+ pipeline.getPipelineName() + "'" + "\n\nAre you sure you want to delete the pipeline?");

					if (confirm) {
						try {
							PipelineClient pipelineClient = new PipelineClientImpl();
							pipelineClient.deletePipeline(pipeline.getRawID());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						iEventBroker.send(Constants.PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
					}

				} else {
					MessageDialog.openWarning(shell, "Delete Pipeline", "Public Pipeline cannot be deleted.");
				}

			} else {

				boolean confirm = MessageDialog.openConfirm(shell, "Delete Pipeline",
						"'" + pipeline.getPipelineName() + "'" + "\n\nAre you sure you want to delete the pipeline?");

				if (confirm) {
					try {
						PipelineClient pipelineClient = new PipelineClientImpl();
						pipelineClient.deletePipeline(pipeline.getRawID());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					iEventBroker.send(Constants.WORKSPACE_REFRESH_EVENT_BUS_NAME, new UID().toString());
				}
			}

		} else if (object instanceof ProgramDataModel) {

			ProgramDataModel program = (ProgramDataModel) object;

			if (program.isIsPublic()) {

				if (Activator.isAdmin()) {

					boolean confirm = MessageDialog.openConfirm(shell, "Delete Program",
							"'" + program.getProgramName() + "'" + "\n\nAre you sure you want to delete the program?");

					if (confirm) {
						ProgramClient programClient = new ProgramClientImpl();
						programClient.deleteProgram(program.getRawID());

						iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
					}

				} else {
					MessageDialog.openWarning(shell, "Delete Program", "Public program cannot be deleted.");
				}

			} else {

				boolean confirm = MessageDialog.openConfirm(shell, "Delete Program",
						"'" + program.getProgramName() + "'" + "\n\nAre you sure you want to delete the program?");

				if (confirm) {
					ProgramClient programClient = new ProgramClientImpl();
					programClient.deleteProgram(program.getRawID());

					iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
				}
			}

		} else if (object instanceof CategoryModel) {

			/**
			 * 1.공개 카테고리는 워크벤치에서 삭제할 수 없고 바이오 익스프레스 웹 관리자 시스템에서 하위 콘텐츠를 확인 후 삭제할 수 있다.
			 * 2.파이프라인 또는 프로그램 카테고리는 사용자가 자유로이 사용자 카테고리 생성, 삭제, 수정을 할 수 있다. 3.사용자 파이프라인
			 * 카테고리는 루트 또는 서브 카테고리가 비워 있지 않으면 삭제할 수 없다. 4.사용자 프로그램 서브 카테고리 삭제 시 하위에 포함된 사용자
			 * 프로그램도 삭제 처리되며, 사용자 루트 카테고리는 비워있지 않으면 삭제가 불가능 하다. 5.파이프라인 또는 프로그램 등록 시 사용자
			 * 파이프라인 카테고리를 선택하면, 관리자 승인 후 해당 카테고리는 공개로 전환되어 사용자가 수정, 삭젝가 불가능하다.
			 */

			CategoryModel category = (CategoryModel) object;

			CategoryClient categoryClient = new CategoryClientImpl();

			if (category.isIsPublic()) {

				if (Activator.isAdmin()) {
					MessageDialog.openWarning(shell, "Delete Category", "'" + category.getCategoryName() + "'"
							+ "\n\nIt can be deleted public categories in the Bio-Express admin manager.");
				} else {
					MessageDialog.openWarning(shell, "Delete category",
							"'" + category.getCategoryName() + "'" + "\n\nIt cannot be deleted public categories.");
				}

			} else {

				@SuppressWarnings("unused")
				String categoryType = activePart.getElementId().equals(Constants.PIPELINE_VIEW_ID) ? "Pipeline"
						: "Program";

				boolean confirm = MessageDialog.openConfirm(shell, "Delete Category",
						"'" + category.getCategoryName() + "'" + "\n\nAre you sure you want to delete the category?");

				if (confirm) {

					if (activePart.getElementId().equals(Constants.PIPELINE_VIEW_ID)) {

						if (category.getObjectCount() != 0) {
							MessageDialog.openError(shell, "Category Error", "'" + category.getCategoryName() + "' "
									+ "\n\nIt cannot be deleted because there is an item in the category.");
						} else {
							categoryClient.deleteCategory(category.getRawID());
						}

						iEventBroker.send(Constants.PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());

					} else {

						if (category.isIsRoot()) {

							if (category.getObjectCount() != 0) {
								MessageDialog.openError(shell, "Category Error", "'" + category.getCategoryName() + "' "
										+ "\n\nIt cannot be deleted because there is an item in the category.");
							} else {
								categoryClient.deleteCategory(category.getRawID());
							}

						} else {

							ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);

							try {
								progressDialog.run(true, true, new IRunnableWithProgress() {
									@Override
									public void run(IProgressMonitor monitor) throws InterruptedException {

										ProgramClient programClient = new ProgramClientImpl();

										List<ProgramDataModel> programs = programClient.getProgramDataList(
												category.getCategoryID(), Activator.getMember().getMemberId());

										monitor.beginTask("Deleteting a category in progress.", programs.size());

										TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

										monitor.subTask("[" + category.getCategoryName()
												+ "] Deleting a category with programs.");

										TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

										for (int i = 0; i < programs.size(); i++) {

											ProgramDataModel program = programs.get(i);

											monitor.subTask("[" + program.getProgramName()
													+ "] Deleting a program in progress.");

											programClient.deleteProgram(program.getRawID());

											TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

											monitor.worked(1);
										}

										categoryClient.deleteCategory(category.getRawID());

										if (monitor.isCanceled()) {
											monitor.done();
											return;
										}

										monitor.done();
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						iEventBroker.send(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME, new UID().toString());
					}
				}
			}

		} else if (object instanceof FileModel) {

			if (activePart.getLabel().toLowerCase().equals(Constants.SCRIPT_LABEL)) {

				iEventBroker.send(Constants.SCRIPT_DELETE_EVENT_BUS_NAME, new UID().toString());

			} else if (activePart.getElementId().equals(Constants.FILE_TABLE_VIEW_ID)) {

				iEventBroker.send(Constants.FILE_TABLE_DELETE_EVENT_BUS_NAME, new UID().toString());

			} else if (activePart.getElementId().equals(Constants.GBOX_TABLE_VIEW_ID)) {

				iEventBroker.send(Constants.GBOX_DELETE_EVENT_BUS_NAME, new UID().toString());
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		// 파이프라인 뷰 선택하고 관리자가 아니면 삭제 버튼 비활성화
		if (activePart != null && activePart.getLabel().toLowerCase().equals(Constants.PIPELINE_LABEL)
				&& !Activator.isAdmin()) {
			return false;
		} else {
			return object != null;
		}
	}
}
