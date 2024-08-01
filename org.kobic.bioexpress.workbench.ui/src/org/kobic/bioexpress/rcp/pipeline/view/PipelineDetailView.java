package org.kobic.bioexpress.rcp.pipeline.view;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClient;
import org.kobic.bioexpress.channel.client.pipeline.PipelineClientImpl;
import org.kobic.bioexpress.model.parameter.ParameterDataModel;
import org.kobic.bioexpress.model.pipeline.NodeDataModel;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.pipeline.PipelineModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineDetailTreeViewCompator;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineDetailTreeViewContentProvider;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineDetailTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.program.dialog.EditNodeDialog;
import org.kobic.bioexpress.rcp.swt.listener.TreeViewColumnSelectionListener;

public class PipelineDetailView {

	final static Logger logger = Logger.getLogger(PipelineDetailView.class);

	private Composite parent;

	private TreeViewer treeViewer;

	@Inject
	@Optional
	private EModelService eModelService;

	@SuppressWarnings("unused")
	@Inject
	private EMenuService eMenuService;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private MPartStack mPartStack;

	@Inject
	@Optional
	private IWorkbench iworkbench;

	private PipelineModel pipelineModel;

	@PostConstruct
	public void createComposite(Composite parent) {

		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		treeViewer.setLabelProvider(new PipelineDetailTreeViewLabelProvider());
		treeViewer.setContentProvider(new PipelineDetailTreeViewContentProvider());
		treeViewer.setComparator(new PipelineDetailTreeViewCompator());
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameCol.getColumn().setWidth(150);
		nameCol.getColumn().setText("Name");
		nameCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID));
		nameCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getNodeName();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterName();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn descCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		descCol.getColumn().setWidth(300);
		descCol.getColumn().setText("Description");
		descCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID));
		descCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getProgramDesc();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterDesc();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn categoryCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		categoryCol.getColumn().setWidth(150);
		categoryCol.getColumn().setText("Category");
		categoryCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID));
		categoryCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getCategoryName();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterValue();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn scriptType = new TreeViewerColumn(treeViewer, SWT.NONE);
		scriptType.getColumn().setWidth(150);
		scriptType.getColumn().setText("Script Type");
		scriptType.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID));
		scriptType.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof NodeModel) {
					NodeDataModel nodeDataModel = ((NodeModel) element).getNodeData();

					if (nodeDataModel.isPublic()) {

						switch (nodeDataModel.getScriptType().toLowerCase()) {
						case Constants.PYTHON:
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
									Constants.PYTHON_PUBLIC_ICON);
						case Constants.BASH:
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BASH_PUBLIC_ICON);
						case Constants.R:
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.R_PUBLIC_ICON);
						default:
							return null;
						}

					} else {
						switch (nodeDataModel.getScriptType().toLowerCase()) {
						case Constants.PYTHON:
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PYTHON_ICON);
						case Constants.BASH:
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BASH_ICON);
						case Constants.R:
							return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.R_ICON);
						default:
							return null;
						}
					}

				} else if (element instanceof ParameterDataModel) {

					switch (((ParameterDataModel) element).getParameterValueType()) {
					case Constants.PARAMETER_VALUE_TYPE_STRING:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
								Constants.PARAMETER_DIALOG_STRING_ICON);
					case Constants.PARAMETER_VALUE_TYPE_INTEGER:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
								Constants.PARAMETER_DIALOG_INTEGER_ICON);
					case Constants.PARAMETER_VALUE_TYPE_BOOLEAN:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
								Constants.PARAMETER_DIALOG_BOOLEAN_ICON);
					case Constants.PARAMETER_VALUE_TYPE_FOLDER:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
								Constants.PARAMETER_DIALOG_FOLDER_ICON);
					case Constants.PARAMETER_VALUE_TYPE_FILE:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
								Constants.PARAMETER_DIALOG_FILE_ICON);
					case Constants.PARAMETER_VALUE_TYPE_FLOAT:
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME,
								Constants.PARAMETER_DIALOG_FLOAT_ICON);
					default:
						return null;
					}
				} else {
					return null;
				}
			}

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getScriptType();
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterValueType();
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn versionCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		versionCol.getColumn().setWidth(150);
		versionCol.getColumn().setText("Version");
		versionCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID));
		versionCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {

				if (element instanceof ParameterDataModel) {
					if (((ParameterDataModel) element).isIsRequire()) {
						return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.REQUIRED_ICON);
					} else {
						return null;
					}
				} else {
					return null;
				}
			}

			@Override
			public String getText(final Object element) {

				if (element instanceof NodeModel) {
					return ((NodeModel) element).getNodeData().getVersion();
				} else if (element instanceof ParameterDataModel) {
					if (((ParameterDataModel) element).isIsRequire()) {
						return Constants.PARAMETER_REQUIRED;
					} else {
						return Constants.PARAMETER_OPTIONAL;
					}
				}

				return Constants.DEFAULT_NULL_VALUE;
			}
		});

		TreeViewerColumn statusCol = new TreeViewerColumn(treeViewer, SWT.NONE);
		statusCol.getColumn().setWidth(150);
		statusCol.getColumn().setText("Status");
		statusCol.getColumn().addSelectionListener(
				new TreeViewColumnSelectionListener(this.treeViewer, Constants.PIPELINE_DETAIL_TREE_TABLE_VIEW_ID));
		statusCol.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {

				if (element instanceof NodeModel) {

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
				} else if (element instanceof ParameterDataModel) {
					return ((ParameterDataModel) element).getParameterType();
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

					if (object instanceof NodeModel) {

						NodeModel node = (NodeModel) object;

						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub

								IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();

								try {
									URL url = new URL(node.getNodeData().getUrl());
									String browserID = node.getNodeID();
									String title = node.getNodeData().getProgramName();
									String tooltip = node.getNodeData().getProgramDesc();

									IWebBrowser br = browserSupport.createBrowser(
											IWorkbenchBrowserSupport.AS_EDITOR | IWorkbenchBrowserSupport.NAVIGATION_BAR
													| IWorkbenchBrowserSupport.LOCATION_BAR,
											browserID, title, tooltip);

									br.openURL(url);

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
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
	private void subscribeTopicTodoUpdated(@UIEventTopic("_TEST_012") PipelineDataModel pipelineDataModel) {

		// System.out.println(pipelineDataModel);

		String rawID = pipelineDataModel.getRawID();

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PipelineClient pipelineClient = new PipelineClientImpl();
				try {
					pipelineModel = pipelineClient.getPipeline(rawID);
					treeViewer.setInput(pipelineModel.getNode());

					MPart mPart = ePartService.findPart(Constants.LOG_VIEW_ID);

					ePartService.showPart(mPart, PartState.CREATE);

					iEventBroker.send("_TEST_01311", pipelineModel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic("SAVE_PIPELINE") String rawID) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					PipelineClient pipelineClient = new PipelineClientImpl();
					pipelineModel = pipelineClient.getPipeline(rawID);

					treeViewer.setInput(pipelineModel.getNode());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic("OPEN_NODEINFO") NodeModel node) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				EditNodeDialog dialog = new EditNodeDialog(Display.getDefault().getActiveShell(), node, pipelineModel);

				if (dialog.open() == Window.OK) {

					ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(parent.getShell());

					try {
						progressDialog.run(true, true, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor monitor) throws InterruptedException {

								monitor.beginTask("Proceed with updating pipeline information.",
										IProgressMonitor.UNKNOWN);

								monitor.subTask("Connecting to database.");

								TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

								monitor.subTask("Setting up a pipeline node.");

								NodeModel nodeModel = dialog.getNodeModel();

								for (int i = 0; i < pipelineModel.getNode().size(); i++) {

									NodeModel node = pipelineModel.getNode().get(i);

									if (node.getNodeID().equals(nodeModel.getNodeID())) {
										pipelineModel.getNode().set(i, nodeModel);
										break;
									}
								}

								TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

								monitor.subTask("Updating a pipeline information.");

								String rawID = pipelineModel.getPipelineData().getRawID();

								try {
									PipelineClient pipelineClient = new PipelineClientImpl();
									int res = pipelineClient.updatePipeline(rawID, pipelineModel);

									if (res == 0) {
										Display.getDefault().asyncExec(new Runnable() {
											@Override
											public void run() {
												// TODO Auto-generated method stub
												treeViewer.refresh(pipelineModel.getNode());
											}
										});

									} else {
										monitor.done();
										MessageDialog.openError(parent.getShell(), "Pipeline Error",
												"An error occurred while updating the pipeline information.");
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

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
			}
		});
	}

	@Inject
	@Optional
	private void subscribeTopicTodoRefresh(@UIEventTopic(Constants.PIPELINE_DETAIL_REFRESH_EVENT_BUS_NAME) String uid) {
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InterruptedException {

					monitor.beginTask("Starting to refresh pipeline detail.", IProgressMonitor.UNKNOWN);
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								PipelineClient pipelineClient = new PipelineClientImpl();
								pipelineModel = pipelineClient.getPipeline(pipelineModel.getRawID());

								treeViewer.getTree().setSortDirection(SWT.NONE);
								treeViewer.setComparator(null);
								treeViewer.setInput(pipelineModel.getNode());
								treeViewer.refresh();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});			
					monitor.subTask("Finish....");
					if (monitor.isCanceled()) {
						monitor.done();
						return;
					}
					monitor.done();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	@Inject
	@Optional
	private void subscribeTopicTodoRefresh2(@UIEventTopic("REFRESH_PIPELINE_CHECK") String rid) {

		// TODO Refresh 이벤트 구현

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					PipelineClient pipelineClient = new PipelineClientImpl();
					pipelineModel = pipelineClient.getPipeline(rid);
					treeViewer.setInput(pipelineModel.getNode());

					MPart mPart = ePartService.findPart(Constants.LOG_VIEW_ID);

					ePartService.showPart(mPart, PartState.CREATE);

					iEventBroker.send("_TEST_01311", pipelineModel);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
