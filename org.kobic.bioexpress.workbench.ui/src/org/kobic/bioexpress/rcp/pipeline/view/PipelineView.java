/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package org.kobic.bioexpress.rcp.pipeline.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClient;
import org.kobic.bioexpress.channel.client.workspace.WorkspaceClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.model.pipeline.PipelineDataModel;
import org.kobic.bioexpress.model.workspace.WorkspaceModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineTreeViewContentProvider;
import org.kobic.bioexpress.rcp.pipeline.provider.PipelineTreeViewLabelProvider;
import org.kobic.bioexpress.rcp.utils.InfoUtils;

@SuppressWarnings("unused")
public class PipelineView {

	final static Logger logger = Logger.getLogger(PipelineView.class);

	private TreeViewer treeViewer;

	@Inject
	private ESelectionService selectionService;

	@Inject
	private EMenuService eMenuService;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	private UISynchronize sync;
	
	private List<Object> expands;

	@PostConstruct
	public void createComposite(Composite parent, EMenuService eMenuService) {

		parent.setLayout(new GridLayout(1, false));

		PatternFilter filter = new PatternFilter();
		int styleBits = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER| SWT.MULTI;

		@SuppressWarnings("deprecation")
		FilteredTree filteredTree = new FilteredTree(parent, styleBits, filter, true);
		filteredTree.setQuickSelectionMode(true);
		filteredTree.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		treeViewer = filteredTree.getViewer();
		treeViewer.setLabelProvider(new PipelineTreeViewLabelProvider());
		treeViewer.setContentProvider(new PipelineTreeViewContentProvider());
		treeViewer.getTree().setHeaderVisible(false);
		treeViewer.getTree().setLinesVisible(false);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {

				if (e1 instanceof CategoryModel && e2 instanceof CategoryModel) {
					CategoryModel t1 = (CategoryModel) e1;
					CategoryModel t2 = (CategoryModel) e2;
					return t1.getCategoryName().toLowerCase().compareTo(t2.getCategoryName().toLowerCase());
				} else {
					PipelineDataModel t1 = (PipelineDataModel) e1;
					PipelineDataModel t2 = (PipelineDataModel) e2;
					return t1.getPipelineName().toLowerCase().compareTo(t2.getPipelineName().toLowerCase());
				}

			};
		});
		
		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.PIPELINE_POPUP_MENU_ID);
//		Map<String, Object> map = InfoUtils.getInstance().converDataToMap(pipelineData);
//		iEventBroker.send("_TEST_011", map);

		init();
	}

	private void init() {
		bind();
		event();
		
		expands = new ArrayList<Object>();
	}

	private void bind() {
		setTreeDataBind();
	}

	public void event() {

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub

				if (!treeViewer.getSelection().isEmpty()) {

					Object object = treeViewer.getStructuredSelection().getFirstElement();

					if (object instanceof PipelineDataModel) {

						PipelineDataModel pipelinData = (PipelineDataModel) object;

						MPart mPart = ePartService.findPart(Constants.DETAIL_DATA_VIEW_ID);

						ePartService.showPart(mPart, PartState.ACTIVATE);

						Map<String, Object> map = InfoUtils.getInstance().converDataToMap(pipelinData);

						iEventBroker.send("_TEST_011", map);

					} else if (object instanceof CategoryModel) {

						CategoryModel category = (CategoryModel) object;

						MPart mPart = ePartService.findPart(Constants.DETAIL_DATA_VIEW_ID);

						ePartService.showPart(mPart, PartState.ACTIVATE);

						Map<String, Object> map = InfoUtils.getInstance().converDataToMap(category);

						iEventBroker.send("_TEST_011", map);
						
						/**
						 * object double click to expanded
						 */
						expands.add(object);
						treeViewer.setExpandedElements(expands.toArray());
					}
				}
			}
		});
	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	private void setTreeDataBind() {

//		sync.syncExec(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				String memberID = Activator.getMember().getMemberId();
//
//				CategoryClient categoryClient = new CategoryClientImpl();
//				List<CategoryModel> list = categoryClient.getPipelineRootCategory(memberID);
//
//				treeViewer.setInput(list);
//			}
//		});

		Job job = new Job("Binding pipeline") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("Pipeline information.", IProgressMonitor.UNKNOWN);

				String memberID = Activator.getMember().getMemberId();

				CategoryClient categoryClient = new CategoryClientImpl();
				List<CategoryModel> list = categoryClient.getPipelineRootCategory(memberID);

				monitor.beginTask("Loading pipeline category list..", list.size());

				for (CategoryModel category : list) {
					monitor.worked(1);
					monitor.setTaskName(category.getCategoryName() + "(" + category.getCategoryID() + ")");
					try {
						TimeUnit.MILLISECONDS.sleep(Constants.DEFAULT_DELAY_MILLISECONDS_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				sync.asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						treeViewer.setInput(list);
					}
				});

				monitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(
			@UIEventTopic(Constants.PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME) String uid) {
		
//		expands.clear();
		
		setTreeDataBind();
	}
}