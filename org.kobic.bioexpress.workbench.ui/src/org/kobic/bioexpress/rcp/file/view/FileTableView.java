package org.kobic.bioexpress.rcp.file.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.common.dialog.CommonInputDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.file.provider.FileTableViewCompator;
import org.kobic.bioexpress.rcp.file.provider.FileTableViewLabelProvider;
import org.kobic.bioexpress.rcp.gbox.handler.GBoxTransferHandler;
import org.kobic.bioexpress.rcp.presenter.Presenter;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.kobic.bioexpress.model.file.FileModel;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;

public class FileTableView implements Presenter {

	final static Logger logger = Logger.getLogger(FileTableView.class);

	@Inject
	private MDirtyable dirty;

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	@Optional
	private MPartStack mPartStack;

	@Inject
	@Optional
	private IWorkbench iworkbench;

	@Inject
	private IEventBroker iEventBroker;

	@Inject
	private ESelectionService selectionService;

	@Inject
	private UISynchronize sync;

	private TableViewer tableViewer;
	private Table table;

	private ComboViewer pathComboViewer;
	private Combo pathCombo;

	private Composite parent;

	private List<FileModel> source;

	private String LOCAL_PATH;

	private List<String> LOCAL_HISTORY_PATH;
	private List<FileModel> STORE_COPY_PATH;
	private List<String> FILE_ITEMS;

	private int IDX = 0;

	@PostConstruct
	public void createComposite(Composite parent, EMenuService eMenuService) {

		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		pathComboViewer = new ComboViewer(parent, SWT.NONE | SWT.READ_ONLY);
		pathCombo = pathComboViewer.getCombo();
		pathCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new FileTableViewLabelProvider());
		tableViewer.setComparator(new FileTableViewCompator());

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tableViewer.refresh();

		TableColumn nameCol = new TableColumn(table, SWT.LEFT);
		nameCol.setText("Name");
		nameCol.setWidth(200);
		nameCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn sizeCol = new TableColumn(table, SWT.LEFT);
		sizeCol.setText("Size");
		sizeCol.setWidth(200);
		sizeCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn typeCol = new TableColumn(table, SWT.LEFT);
		typeCol.setText("Type");
		typeCol.setWidth(200);
		typeCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		TableColumn dateCol = new TableColumn(table, SWT.LEFT);
		dateCol.setText("Date");
		dateCol.setWidth(200);
		dateCol.addSelectionListener(new TableViewColumnSelectionListener(this.tableViewer));

		eMenuService.registerContextMenu(tableViewer.getControl(), Constants.GBOX_LOCAL_EXPLORER_POPUP_MENU_ID);

		init();
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoDownload(
			@UIEventTopic(Constants.GBOX_RAPIDANT_DOWNLOAD_EVENT_BUS_NAME) List<FileModel> files) {
		download(files);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpload(@UIEventTopic(Constants.FILE_TABLE_UPLOAD_EVENT_BUS_NAME) String uid) {

		List<FileModel> files = new ArrayList<FileModel>();

		for (TableItem tableItem : table.getSelection()) {
			FileModel fileModel = (FileModel) tableItem.getData();
			files.add(fileModel);
		}

		iEventBroker.send(Constants.FILE_TABLE_RAPIDANT_UPLOAD_EVENT_BUS_NAME, files);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.FILE_TREE_SELECT_EVENT_BUS_NAME) FileModel file) {

		logger.info("tree view select file name: " + file.getName() + "\tdir: [" + file.isIsDir() + "]");

		LOCAL_PATH = file.getPath();

//		if (!LOCAL_HISTORY_PATH.contains(LOCAL_PATH)) {
//
//			LOCAL_HISTORY_PATH.add(LOCAL_PATH);
//
//			String[] strings = LOCAL_HISTORY_PATH.stream().toArray(String[]::new);
//			Arrays.sort(strings);
//			pathCombo.setItems(strings);
//		}

		LOCAL_HISTORY_PATH.add(LOCAL_PATH);
		IDX = LOCAL_HISTORY_PATH.size() - 1;

		String[] strings = LOCAL_HISTORY_PATH.stream().distinct().collect(Collectors.toList()).stream()
				.toArray(String[]::new);
		Arrays.sort(strings);
		pathCombo.setItems(strings);

		setComboDataBind();
		setTableDataBind(LOCAL_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoHome(@UIEventTopic(Constants.GBOX_LOCAL_EXPLORER_HOME_EVENT_BUS_NAME) String uid) {

		LOCAL_PATH = Constants.USER_LOCAL_HOME;

		if (!LOCAL_HISTORY_PATH.contains(LOCAL_PATH)) {

			LOCAL_HISTORY_PATH.add(LOCAL_PATH);

			String[] strings = LOCAL_HISTORY_PATH.stream().toArray(String[]::new);
			Arrays.sort(strings);
			pathCombo.setItems(strings);
		}

		setComboDataBind();
		setTableDataBind(LOCAL_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoFileDataReload(@UIEventTopic(Constants.FILE_DATA_RELOAD_EVENT_BUS_NAME) String uid) {

		logger.info("tree view reload job id: " + uid + "\tpath: [" + LOCAL_PATH + "]");
		setTableDataBind(LOCAL_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoForwardHistory(
			@UIEventTopic(Constants.FILE_HISTORY_FORWARD_EVENT_BUS_NAME) String uid) {

//		System.out.println(LOCAL_HISTORY_PATH.indexOf(LOCAL_PATH));
//
//		int idx = LOCAL_HISTORY_PATH.indexOf(LOCAL_PATH) + 1;
//
//		System.out.println(idx);
//		System.out.println(idx >= LOCAL_HISTORY_PATH.size());
//
//		if (idx >= LOCAL_HISTORY_PATH.size()) {
//			MessageDialog.openInformation(parent.getShell(), "Info", "No more pages can be loaded.");
//		} else {
//
//			LOCAL_PATH = LOCAL_HISTORY_PATH.get(idx);
//
//			setComboDataBind();
//			setTableDataBind();
//		}

		IDX = IDX + 1;

		if (IDX > LOCAL_HISTORY_PATH.size() - 1) {
			IDX = LOCAL_HISTORY_PATH.size() - 1;
			MessageDialog.openInformation(parent.getShell(), "Browser Information", "No more pages can be loaded.");
		} else {

			System.out.println(LOCAL_HISTORY_PATH.toString());
			System.out.println(IDX + ":" + LOCAL_HISTORY_PATH.get(IDX));

			LOCAL_PATH = LOCAL_HISTORY_PATH.get(IDX);

			setComboDataBind();
			setTableDataBind(LOCAL_PATH);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoPreviousHistory(
			@UIEventTopic(Constants.FILE_HISTORY_PREVIOUS_EVENT_BUS_NAME) String uid) {

//		int idx = LOCAL_HISTORY_PATH.indexOf(LOCAL_PATH) - 1;
//
//		if (idx < 0) {
//			MessageDialog.openInformation(parent.getShell(), "Info", "No more pages can be loaded.");
//		} else {
//
//			LOCAL_PATH = LOCAL_HISTORY_PATH.get(idx);
//
//			setComboDataBind();
//			setTableDataBind();
//		}

		IDX = IDX - 1;

		if (IDX < 0) {
			IDX = 0;
			MessageDialog.openInformation(parent.getShell(), "Browser Information", "No more pages can be loaded.");
		} else {

			System.out.println(LOCAL_HISTORY_PATH.toString());
			System.out.println(IDX + ":" + LOCAL_HISTORY_PATH.get(IDX));

			LOCAL_PATH = LOCAL_HISTORY_PATH.get(IDX);

			setComboDataBind();
			setTableDataBind(LOCAL_PATH);

			LOCAL_HISTORY_PATH.add(LOCAL_PATH);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpHistory(@UIEventTopic(Constants.FILE_HISTORY_UP_EVENT_BUS_NAME) String uid) {

		File file = new File(LOCAL_PATH);

		String _parent = file.getParent();

		if (_parent != null) {

			LOCAL_PATH = _parent;

			if (!LOCAL_HISTORY_PATH.contains(LOCAL_PATH)) {

				LOCAL_HISTORY_PATH.add(LOCAL_PATH);

				String[] strings = LOCAL_HISTORY_PATH.stream().toArray(String[]::new);
				Arrays.sort(strings);
				pathCombo.setItems(strings);
			}

			setComboDataBind();
			setTableDataBind(LOCAL_PATH);
		} else {
			MessageDialog.openInformation(parent.getShell(), "Browser Information", "No more pages can be loaded.");
		}

	}

	@Inject
	@Optional
	private void subscribeTopicTodoDelete(@UIEventTopic(Constants.FILE_TABLE_DELETE_EVENT_BUS_NAME) String uid) {

		if (!tableViewer.getStructuredSelection().isEmpty()) {

			TableItem[] items = tableViewer.getTable().getSelection();

			boolean res = MessageDialog.openConfirm(parent.getShell(), "Delete",
					"Are you sure you want to delete" + items.length + "  files?");

			if (res) {
				for (TableItem item : items) {
					FileModel fileModel = (FileModel) item.getData();
					Activator.getFileService().forceDelete(fileModel.getPath());
				}

				setTableDataBind(LOCAL_PATH);
			}
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoNewFile(@UIEventTopic(Constants.FILE_TABLE_NEW_FILE_EVENT_BUS_NAME) String uid) {

		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {

				if (newText.isEmpty()) {
					return "Please enter a new file name.";
				} else {
					for (TableItem item : tableViewer.getTable().getItems()) {

						FileModel fileModel = (FileModel) item.getData();

						if (fileModel.isIsFile()) {
							if (newText.equals(fileModel.getName())) {
								return "The file name already exists.";

							} else if (!ValidationUtils.getInstance().isFileNameValidation(newText)) {
								return Constants.NEW_NAMING_RULE;
							}
						}
					}
				}
				return null;
			}
		};

		CommonInputDialog dialog = new CommonInputDialog(Display.getCurrent().getActiveShell(), "New File",
				"New file name:", Constants.DEFAULT_NULL_VALUE, validator);

		if (dialog.open() == Window.OK) {

			String path = LOCAL_PATH + File.separator + dialog.getName();
			Activator.getFileService().createFile(path);
		}

		setTableDataBind(LOCAL_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoNewFolder(@UIEventTopic(Constants.FILE_TABLE_NEW_FOLDER_EVENT_BUS_NAME) String uid) {

		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {

				if (newText.isEmpty()) {
					return "Please enter a new folder name.";
				} else {
					for (TableItem item : tableViewer.getTable().getItems()) {

						FileModel fileModel = (FileModel) item.getData();

						if (fileModel.isIsDir()) {
							if (newText.equals(fileModel.getName())) {
								return "The folder name already exists.";

							} else if (!ValidationUtils.getInstance().isFileNameValidation(newText)) {
								return Constants.NEW_NAMING_RULE;
							}
						}
					}
				}
				return null;
			}
		};

		CommonInputDialog dialog = new CommonInputDialog(Display.getCurrent().getActiveShell(), "New Folder",
				"New folder name:", Constants.DEFAULT_NULL_VALUE, validator);

		if (dialog.open() == Window.OK) {

			String path = LOCAL_PATH + File.separator + dialog.getName();
			Activator.getFileService().mkdir(path);
		}

		setTableDataBind(LOCAL_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoRename(
			@UIEventTopic(Constants.FILE_TABLE_RENAME_EVENT_BUS_NAME) FileModel fileModel) {

		boolean isDir = fileModel.isIsDir();

		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {

				if (newText.isEmpty()) {
					return "please enter a new name.";
				} else {
					for (TableItem item : tableViewer.getTable().getItems()) {

						FileModel fileModel = (FileModel) item.getData();

						if (isDir) {
							if (fileModel.isIsDir()) {
								if (newText.equals(fileModel.getName())) {
									return "The folder name already exists.";
								}
							}
						} else {
							if (fileModel.isIsFile()) {
								if (newText.equals(fileModel.getName())) {
									return "The file name already exists.";
								}
							}
						}
					}
				}
				return null;
			}
		};

		CommonInputDialog dialog = new CommonInputDialog(Display.getCurrent().getActiveShell(), "Rename",
				String.join(" ", isDir ? "File" : "Folder", "Rename:"), fileModel.getName(), validator);

		if (dialog.open() == Window.OK) {

			String source = fileModel.getPath();
			String target = LOCAL_PATH + File.separator + dialog.getName();

			if (isDir) {
				Activator.getFileService().renameDir(source, target);
			} else {
				Activator.getFileService().renameFile(source, target);
			}
		}

		setTableDataBind(LOCAL_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoCopy(@UIEventTopic(Constants.FILE_TABLE_COPY_EVENT_BUS_NAME) String uid) {

		STORE_COPY_PATH = null;
		STORE_COPY_PATH = new ArrayList<FileModel>();

		for (TableItem item : tableViewer.getTable().getSelection()) {
			FileModel fileModel = (FileModel) item.getData();
			STORE_COPY_PATH.add(fileModel);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoPaste(@UIEventTopic(Constants.FILE_TABLE_PASTE_EVENT_BUS_NAME) String uid) {

		String copyTarget = LOCAL_PATH;

		FILE_ITEMS = null;
		FILE_ITEMS = new ArrayList<String>();

		for (TableItem item : tableViewer.getTable().getItems()) {
			FileModel fileModel = (FileModel) item.getData();
			FILE_ITEMS.add(fileModel.getName());
		}

		int count = 0;

		for (FileModel fileModel : STORE_COPY_PATH) {
			if (FILE_ITEMS.contains(fileModel.getName())) {
				count = count + 1;
			}
		}

		boolean res = count > 0;
		boolean isConfirm = true;

		if (res) {
			isConfirm = MessageDialog.openConfirm(parent.getShell(), "File Confirm",
					"The file name already exists. Do you want to overwrite?");
		}

		if (isConfirm) {
			MPart mPart = ePartService.findPart(Constants.PROGRESS_VIEW_ID);

			ePartService.showPart(mPart, PartState.ACTIVATE);

			Job job = new Job("Start file transfer.") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					// TODO Auto-generated method stub

					monitor.beginTask("File transfer in progress.", STORE_COPY_PATH.size());

					for (FileModel source : STORE_COPY_PATH) {

						monitor.worked(1);
						monitor.setTaskName(source.getPath());

						if (source.isIsFile()) {
							System.out.println(source.getPath() + "==>" + LOCAL_PATH + "/" + source.getName());
//							Activator.getFileService().copyFile(source.getPath(), LOCAL_PATH + "/" + source.getName());
							Activator.getFileService().copyFile(source.getPath(), copyTarget + "/" + source.getName());
						} else {
//							Activator.getFileService().copySubdirectories(source.getPath(),
//									LOCAL_PATH + "/" + source.getName());
							Activator.getFileService().copySubdirectories(source.getPath(),
									copyTarget + "/" + source.getName());
						}
					}

//					setTableDataBind(LOCAL_PATH);

					if (LOCAL_PATH.equals(copyTarget)) {
						setTableDataBind(LOCAL_PATH);
					}

					monitor.done();

					return Status.OK_STATUS;
				}
			};
			job.schedule();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		LOCAL_PATH = Constants.USER_LOCAL_HOME;

		LOCAL_HISTORY_PATH = new ArrayList<String>();
		LOCAL_HISTORY_PATH.add(LOCAL_PATH);

		bind();
		event();
	}

	@Override
	public void bind() {
		// TODO Auto-generated method stub

		setTableDataBind(Constants.USER_LOCAL_HOME);

		sync.asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String[] strings = LOCAL_HISTORY_PATH.stream().toArray(String[]::new);
				Arrays.sort(strings);
				pathCombo.setItems(strings);
				pathCombo.setText(LOCAL_PATH);
			}
		});
	}

	@Override
	public void event() {
		// TODO Auto-generated method stub

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});

		pathCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				LOCAL_PATH = pathCombo.getText();
				setTableDataBind(LOCAL_PATH);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();

				FileModel file = (FileModel) selection.getFirstElement();

				if (file.isIsDir()) {

					LOCAL_PATH = file.getPath();

//					if (!LOCAL_HISTORY_PATH.contains(LOCAL_PATH)) {
//
//						LOCAL_HISTORY_PATH.add(LOCAL_PATH);
//
//						String[] strings = LOCAL_HISTORY_PATH.stream().toArray(String[]::new);
//						Arrays.sort(strings);
//						pathCombo.setItems(strings);
//					}

					LOCAL_HISTORY_PATH.add(LOCAL_PATH);
					IDX = LOCAL_HISTORY_PATH.size() - 1;

					String[] strings = LOCAL_HISTORY_PATH.stream().distinct().collect(Collectors.toList()).stream()
							.toArray(String[]::new);
					Arrays.sort(strings);
					pathCombo.setItems(strings);

					setComboDataBind();
					setTableDataBind(LOCAL_PATH);
				}
			}
		});

		/**
		 * Drag and Drop Event
		 */

		table = tableViewer.getTable();

		DragSource source = new DragSource(table, DND.DROP_COPY);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				event.detail = DND.DROP_COPY;
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub

				List<FileModel> files = new ArrayList<FileModel>();

				for (TableItem tableItem : table.getSelection()) {
					FileModel fileModel = (FileModel) tableItem.getData();
					files.add(fileModel);
				}

				Gson gson = new Gson();
				String json = gson.toJson(files);
				event.data = json;
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if (event.detail == DND.DROP_COPY) {
				}
			}
		});

		DropTarget target = new DropTarget(table, DND.DROP_COPY);
		target.setTransfer(new Transfer[] { TextTransfer.getInstance() });

		target.addDropListener(new DropTargetListener() {

			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub
				event.detail = DND.DROP_COPY;
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void drop(DropTargetEvent event) {
				// TODO Auto-generated method stub

				if (!ePartService.getActivePart().getElementId().equals(Constants.FILE_TABLE_VIEW_ID)) {
					if (Activator.getRapidantService().isTransfer()) {
						MessageDialog.openInformation(parent.getShell(), "File Information",
								"File transfer in progress. Please try again after the transfer is complete.");
					} else {

						int count = 0;

						if (Activator.getRapidantService().getSessionInfo() == null) {
							count = 0;
						} else {
							count = Activator.getRapidantService().getSessionInfo().getNowTransferCount();
						}

						if (count < Constants.RAPIDANT_MAX_TRANSFER_COUNT) {
							String json = (String) event.data;
							download(json);
						} else {
							MessageDialog.openWarning(parent.getShell(), "File Warning",
									"The upload service is currently unavailable due to a many users.");
						}
					}
				} else {
					MessageDialog.openWarning(parent.getShell(), "File Warning", "Incorrectly select path.");
				}
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void download(Object object) {

		source = null;

		if (object instanceof String) {

			String json = (String) object;

			TypeToken<List<FileModel>> token = new TypeToken<List<FileModel>>() {
			};

			Gson gson = new Gson();
			source = gson.fromJson(json, token.getType());

		} else if (object instanceof List) {
			source = (List<FileModel>) object;
		}

		int total_count = tableViewer.getTable().getItemCount() + source.size();

		boolean isConfirm = true;

		if (total_count > 100) {
			isConfirm = MessageDialog.openConfirm(parent.getShell(), "Transfer Confirm",
					"It is not recommended to store more than 100 files in one folder. Do you want to continue?");
		}

		if (isConfirm) {

			MPart mPart = ePartService.findPart(Constants.PROGRESS_VIEW_ID);

			ePartService.showPart(mPart, PartState.ACTIVATE);

			Job job = new Job("Starts preprocessing of the transfer file.") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					// TODO Auto-generated method stub

					monitor.beginTask("Searching for transfer data.", source.size());

					sync.asyncExec(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							String target = LOCAL_PATH;
							String type = org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_TRANSFER_RECEIVE;

							if (Activator.getRapidantService().isTransfer()) {
								MessageDialog.openInformation(parent.getShell(), "Transfer Information",
										"Transferring.");
							} else {

								Job j = new Job("GBox High-speed data transfer in progress.") {

									@Override
									protected IStatus run(IProgressMonitor monitor) {
										// TODO Auto-generated method stub

										System.out.println(source.toString() + "\t" + target);

										GBoxTransferHandler handler = new GBoxTransferHandler(parent, iEventBroker,
												monitor, source, target, type);
										handler.run();

										return Status.OK_STATUS;
									}
								};

								j.schedule();
							}
						}
					});

					return Status.OK_STATUS;
				}
			};

			job.schedule();
		}
	}

	private void setComboDataBind() {
		sync.asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pathCombo.setText(LOCAL_PATH);
			}
		});
	}

	private void setTableDataBind(String path) {

		Job job = new Job("Binding local data files") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("Local data file.", IProgressMonitor.UNKNOWN);

				List<FileModel> files = Activator.getFileService().getFiles(path);

				monitor.beginTask("Loading Local data file list.", files.size());

				for (FileModel file : files) {
					monitor.worked(1);
					monitor.setTaskName(file.getPath());
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

						tableViewer.setInput(files);
					}
				});

				monitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}
