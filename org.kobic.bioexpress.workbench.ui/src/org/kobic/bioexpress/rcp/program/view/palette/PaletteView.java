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
package org.kobic.bioexpress.rcp.program.view.palette;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.ui.internal.editor.GFCreationTool;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.EventUtils;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.configuration.NodeType;
import org.kobic.bioexpress.workbench.editor.features.CreateWFNodeFeature;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@SuppressWarnings("restriction")
public class PaletteView {

	final static Logger logger = Logger.getLogger(PaletteView.class);

	private PaletteTreeViewer treeViewer;

	@Inject
	private ESelectionService selectionService;

	@Inject
	private EMenuService eMenuService;

	@Inject
	private UISynchronize sync;

	@Inject
	@Optional
	private EPartService ePartService;

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

//	private WorkflowDiagramTypeProvider diagramProvider;

	// private FilteredTree tree;

	private class PaletteTreeClickListener implements EventHandler {
		public static final String TOPIC = "0000000001";

		@Override
		public void handleEvent(Event event) {
			Object data = event.getProperty("org.eclipse.e4.data");
			System.out.println("evnet..!!! " + data);
			setTreeDataBind();
		}
	}

	private final PaletteTreeClickListener paletteTreeClickListener = new PaletteTreeClickListener();

	@PostConstruct
	public void createComposite(Composite parent) {

		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		treeViewer = new PaletteTreeViewer();
		treeViewer.createTreeControl(parent);
		treeViewer.addDragSourceListener(new TemplateTransferDragSourceListener(treeViewer));

		eMenuService.registerContextMenu(treeViewer.getControl(), Constants.PROGRAM_POPUP_MENU_ID);
		init();
		//
		EventUtils.registerForEvent(PaletteTreeClickListener.TOPIC, paletteTreeClickListener);
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {
		setTreeDataBind();
	}

	private void event() {

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});
	}

	@Focus
	public void setFocus() {
		// treeViewer.getControl().setFocus();
	}

	private void setTreeDataBind() {

		sync.syncExec(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String memberID = Activator.getMember().getMemberId();

				CategoryClient categoryClient = new CategoryClientImpl();
				List<CategoryModel> list = categoryClient.getProgramRootCategory(memberID);

				// treeViewer.setInput(list);
				List<PaletteTreeNodeEditPart> groups = new ArrayList<PaletteTreeNodeEditPart>();
				for (CategoryModel cal : list) {
					PaletteContainer cont = new PaletteRoot();
					cont.setLabel(cal.getCategoryName());
					PaletteTreeNodeEditPart entry = new PaletteTreeNodeEditPart(cont);
					//
					String categoryID = cal.getCategoryID();
					Object[] pObjs = null;
					if (cal.getObjectCount() == 0) {
						ProgramClient programClient = new ProgramClientImpl();
						pObjs = programClient.getProgramDataList(categoryID, memberID).toArray();
					} else {
						pObjs = categoryClient.getProgramSubCategory(categoryID, memberID).toArray();
					}
					for (Object obj : pObjs) {
						PaletteEntry model = createTool((ProgramDataModel) obj);
						PaletteEntryEditPart ent = new PaletteEntryEditPart(model);
						entry.getChildren().add(ent);
					}
					groups.add(entry);
				}
				//
				((FilteredTree) treeViewer.getControl()).getViewer().setInput(groups);
			}
		});
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(
			@UIEventTopic(Constants.PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME) String uid) {
		setTreeDataBind();
	}

	public void setInput(IPaletteCompartmentEntry[] items) {
		((FilteredTree) treeViewer.getControl()).getViewer().setInput(items);
	}

	@SuppressWarnings({ "unused" })
	private PaletteEntry createTool(ProgramDataModel program) {
		String memberID = Activator.getMember().getMemberId();
		NodeType node = new NodeType(program.getProgramName(), program.getRawID(), memberID,
				program.getScriptType().toLowerCase());

		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		WorkflowDiagramEditor editor = (WorkflowDiagramEditor) part;

		ICreateFeature functionFeature = null;
		if (editor != null)
			functionFeature = new CreateWFNodeFeature(editor.getDiagramTypeProvider().getFeatureProvider(), node);
		else
			functionFeature = new CreateWFNodeFeature(null, node);
		// functionFeature = new CreateWFNodeFeature(null, new
		// NodeType(program.getProgramName()));
		//
		String id = program.getProgramID();
		String label = program.getProgramName();
		String description = program.getProgramDesc();
		//
		DefaultCreationFactory cf = new DefaultCreationFactory(functionFeature, ICreateFeature.class);
		// Object template = cf;
		CombinedTemplateCreationEntry pe = new CombinedTemplateCreationEntry(label, description, cf,
				ResourceManager.getImageDescriptor(PaletteView.class, "/icons/detail.png"),
				ResourceManager.getImageDescriptor("/icons/detail.png"));
		pe.setToolClass(GFCreationTool.class);

		return pe;
	}

	private class DefaultCreationFactory implements CreationFactory {

		private Object obj;

		private Object objType;

		/**
		 * 
		 */
		public DefaultCreationFactory(Object obj, Object objType) {
			super();
			this.obj = obj;
			this.objType = objType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
		 */
		public Object getNewObject() {
			return obj;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
		 */
		public Object getObjectType() {
			return objType;
		}

	}
}
