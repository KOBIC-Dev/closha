package org.kobic.bioexpress.rcp.log.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.channel.client.log.LogClient;
import org.kobic.bioexpress.channel.client.log.LogClientImpl;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.log.JobDetailLogModel;
import org.kobic.bioexpress.model.log.JobLogModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.model.task.SubTaskModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.log.provider.LogTreeViewCompator;
import org.kobic.bioexpress.rcp.log.provider.LogTreeViewContentProvider;
import org.kobic.bioexpress.rcp.log.provider.LogTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;
import org.kobic.bioexpress.rcp.utils.InfoUtils;

public class LogView {

	final static Logger logger = Logger.getLogger(LogView.class);

	private TreeViewer treeViewer;

	@SuppressWarnings("unused")
	@Inject
	private EMenuService eMenuService;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	private IEventBroker iEventBroker;

	private PipelineModel pipelineModel;

	@PostConstruct
	public void createComposite(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		treeViewer.setLabelProvider(new LogTreeViewLabelProvider());
		treeViewer.setContentProvider(new LogTreeViewContentProvider());
		treeViewer.setComparator(new LogTreeViewCompator());
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(150);
		nameCol.getColumn().setText("Name");
		nameCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.LOG_TREE_TABLE_VIEW_ID));
		nameCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getNodeName();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn descCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		descCol.getColumn().setWidth(300);
		descCol.getColumn().setText("Description");
		descCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.LOG_TREE_TABLE_VIEW_ID));
		descCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getProgramDesc();
				} else if (element instanceof SubTaskModel) {
					return ((SubTaskModel) element).getSubTaskID();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn categoryCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		categoryCol.getColumn().setWidth(150);
		categoryCol.getColumn().setText("Category");
		categoryCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.LOG_TREE_TABLE_VIEW_ID));
		categoryCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getCategoryName();
				} else if (element instanceof SubTaskModel) {
					return ((SubTaskModel) element).getSubmissionTime();
				} else if (element instanceof JobLogModel) {
					return ((JobLogModel) element).getJobID();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn versionCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		versionCol.getColumn().setWidth(150);
		versionCol.getColumn().setText("Version");
		versionCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.LOG_TREE_TABLE_VIEW_ID));
		versionCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {

				if (element instanceof SubTaskModel) {
					if (((SubTaskModel) element).isIsSingle()) {

						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_SMALL_ICON);

					} else {

						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PIPELINE_SMALL_ICON);

					}
				} else {
					return null;
				}
			}

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getVersion();
				} else if (element instanceof SubTaskModel) {
					if (((SubTaskModel) element).isIsSingle()) {
						return Constants.RUN_SINGLE_NODE;
					} else {
						return Constants.RUN_PIPELINE;
					}
				} else if (element instanceof JobLogModel) {
					return ((JobLogModel) element).getSubmissionTime();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn statusCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		statusCol.getColumn().setWidth(150);
		statusCol.getColumn().setText("Status");
		statusCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.LOG_TREE_TABLE_VIEW_ID));
		statusCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {

				if (element instanceof SubTaskModel) {

					switch (((SubTaskModel) element).getStatus()) {
					case Constants.STATUS_COMPLETE:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_COMPLETE_ICON);

					case Constants.STATUS_RUN:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_RUN_ICON);

					case Constants.STATUS_WAIT:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_WAIT_ICON);

					case Constants.STATUS_ERROR:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_ERROR_ICON);

					case Constants.STATUS_STOP:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_STOP_ICON);

					default:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_STOP_ICON);
					}
				} else if (element instanceof NodeModel) {

					switch (((NodeModel) element).getNodeData().getStatus()) {
					case Constants.STATUS_COMPLETE:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_COMPLETE_ICON);

					case Constants.STATUS_RUN:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_RUN_ICON);

					case Constants.STATUS_WAIT:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_WAIT_ICON);

					case Constants.STATUS_ERROR:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_ERROR_ICON);

					case Constants.STATUS_STOP:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_STOP_ICON);

					default:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.STATUS_STOP_ICON);
					}
				} else {
					return null;
				}
			}

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getStatus();
				} else if (element instanceof SubTaskModel) {
					return ((SubTaskModel) element).getStatus();
				} else {
					return Constants.DEFAULT_NULL_VALUE;
				}
			}

		});

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
	}

	private void event() {

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				if (!treeViewer.getSelection().isEmpty()) {

					Object object = treeViewer.getStructuredSelection().getFirstElement();

					if (object instanceof JobLogModel) {

						JobLogModel jobLog = (JobLogModel) object;

						MessageConsoleStream msgConsoleStream = Activator.getMessageConsole().newMessageStream();
//						msgConsoleStream.println(jobLog.toString());

						LogClient logClient = new LogClientImpl();
						JobLogModel jobLogModel = logClient.getJobLogData(jobLog.getNodeID(), jobLog.getJobID());

						JobDetailLogModel jobDetailLogModel = logClient.getJobDetailLogData(jobLog.getNodeID(),
								jobLog.getJobID());

						MPart mPart1 = ePartService.findPart(Constants.DETAIL_DATA_VIEW_ID);

						ePartService.showPart(mPart1, PartState.CREATE);

						Map<String, Object> map = InfoUtils.getInstance().converDataToMap(jobDetailLogModel);

						iEventBroker.send("_TEST_011", map);

						FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
						List<String> sysout = fileUtilsClient.reverseReader(jobLogModel.getStdoutPath(),
								Constants.DEFAULT_CONSOLE_LOG_LINE);
						List<String> syserr = fileUtilsClient.reverseReader(jobLogModel.getStderrPath(),
								Constants.DEFAULT_CONSOLE_LOG_LINE);

						msgConsoleStream.println(String.join("\n", sysout).trim());
						msgConsoleStream.println(String.join("\n", syserr).trim());

						try {
							msgConsoleStream.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}

						MPart mPart2 = ePartService.findPart(Constants.CONSOLE_VIEW_ID);

						ePartService.showPart(mPart2, PartState.ACTIVATE);
					}
				}
			}
		});

	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic("_TEST_01311") PipelineModel pipelineModel) {

		this.pipelineModel = pipelineModel;

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				treeViewer.setInput(pipelineModel.getNode());
			}
		});

	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.LOG_DATA_RELOAD_EVENT_BUS_NAME) String uid) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					PipelineClient pipelineClient = new PipelineClientImpl();
					pipelineModel = pipelineClient.getPipeline(pipelineModel.getRawID());

					treeViewer.getTree().setSortDirection(SWT.NONE);
					treeViewer.setComparator(new LogTreeViewCompator());
					treeViewer.setInput(pipelineModel.getNode());
					treeViewer.refresh();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
