package org.kobic.bioexpress.rcp.gbox.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
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
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
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
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.console.MessageConsoleStream;
import org.kobic.bioexpress.gbox.Constant;
import org.kobic.bioexpress.gbox.local.FileService;
import org.kobic.bioexpress.gbox.local.FileServiceImpl;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.common.dialog.CommonInputDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.gbox.handler.GBoxTransferHandler;
import org.kobic.bioexpress.rcp.gbox.provider.GBoxTableViewCompator;
import org.kobic.bioexpress.rcp.gbox.provider.GBoxTableViewLabelProvider;
import org.kobic.bioexpress.rcp.presenter.Presenter;
import org.kobic.bioexpress.rcp.swt.listener.TableViewColumnSelectionListener;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GBoxTableView implements Presenter {

	final static Logger logger = Logger.getLogger(GBoxTableView.class);

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

	private String GBOX_PATH;
	private String GBOX_REAL_PATH;

	private String message;

	private List<String> GBOX_HISTORY_PATH;
	private List<String> SHOW_GBOX_HISTORY_PATH;
	private List<FileModel> STORE_COPY_PATH;
	private List<String> FILE_ITEMS;

	private List<String> files;
	private List<String> exclude;

	private List<FileModel> source;

//	private Pattern PATTERN_1 = Pattern.compile("^[ㄱ-ㅎ가-힣]*$");
	private Pattern PATTERN_1 = Pattern.compile(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
	private Pattern PATTERN_2 = Pattern.compile("[ !@#$%^&*(),?\\\"{}|<>]");
//	private Pattern PATTERN_2 = Pattern.compile("[ !@#$%^&*(),?\\\":{}|<>]");

	private int IDX = 0;

	public GBoxTableView() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void createComposite(Composite parent, EMenuService eMenuService) {

		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		pathComboViewer = new ComboViewer(parent, SWT.NONE | SWT.READ_ONLY);
		pathCombo = pathComboViewer.getCombo();
		pathCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new GBoxTableViewLabelProvider());
		tableViewer.setComparator(new GBoxTableViewCompator());

		table = tableViewer.getTable();
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

		eMenuService.registerContextMenu(tableViewer.getControl(), Constants.GBOX_REMOTE_EXPLORER_POPUP_MENU_ID);

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
	private void subscribeTopicTodoUpload(
			@UIEventTopic(Constants.FILE_TABLE_RAPIDANT_UPLOAD_EVENT_BUS_NAME) List<FileModel> files) {
		upload(files);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoDownload(@UIEventTopic(Constants.GBOX_DOWNLOAD_EVENT_BUS_NAME) String uid) {

		List<FileModel> files = new ArrayList<FileModel>();

		for (TableItem tableItem : table.getSelection()) {
			FileModel fileModel = (FileModel) tableItem.getData();
			files.add(fileModel);
		}

		iEventBroker.send(Constants.GBOX_RAPIDANT_DOWNLOAD_EVENT_BUS_NAME, files);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(Constants.GBOX_TREE_SELECT_EVENT_BUS_NAME) FileModel file) {

		logger.info("tree view select file name: " + file.getName() + "\tdir: [" + file.isIsDir() + "]");

		GBOX_PATH = file.getPath();

//		if (!GBOX_HISTORY_PATH.contains(GBOX_PATH)) {
//			GBOX_HISTORY_PATH.add(GBOX_PATH);
//		}

		GBOX_HISTORY_PATH.add(GBOX_PATH);
		IDX = GBOX_HISTORY_PATH.size() - 1;

		if (!SHOW_GBOX_HISTORY_PATH.contains(fake(GBOX_PATH))) {

			SHOW_GBOX_HISTORY_PATH.add(fake(GBOX_PATH));

			String[] strings = SHOW_GBOX_HISTORY_PATH.stream().toArray(String[]::new);
			Arrays.sort(strings);
			pathCombo.setItems(strings);

		}

		setComboDataBind();
		setTableDataBind(GBOX_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoHome(@UIEventTopic(Constants.GBOX_REMOTE_EXPLORER_HOME_EVENT_BUS_NAME) String uid) {

		GBOX_PATH = org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT + Activator.getMember().getMemberId();

//		if (!GBOX_HISTORY_PATH.contains(GBOX_PATH)) {
//			GBOX_HISTORY_PATH.add(GBOX_PATH);
//		}

		GBOX_HISTORY_PATH.add(GBOX_PATH);
		IDX = GBOX_HISTORY_PATH.size() - 1;

		if (!SHOW_GBOX_HISTORY_PATH.contains(fake(GBOX_PATH))) {

			SHOW_GBOX_HISTORY_PATH.add(fake(GBOX_PATH));

			String[] strings = SHOW_GBOX_HISTORY_PATH.stream().toArray(String[]::new);
			Arrays.sort(strings);
			pathCombo.setItems(strings);

		}

		setComboDataBind();
		setTableDataBind(GBOX_PATH);
	}

	@Inject
	@Optional
	private void gboxReloadSubscribeTopicTodoUpdated(
			@UIEventTopic(Constants.GBOX_DATA_RELOAD_EVENT_BUS_NAME) String uid) {

		logger.info("tree view reload job id: " + uid + "\tpath: [" + GBOX_PATH + "]");
		setTableDataBind(GBOX_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoForwardHistory(
			@UIEventTopic(Constants.GBOX_HISTORY_FORWARD_EVENT_BUS_NAME) String uid) {

		IDX = IDX + 1;

		if (IDX > GBOX_HISTORY_PATH.size() - 1) {
			IDX = GBOX_HISTORY_PATH.size() - 1;
			MessageDialog.openInformation(parent.getShell(), "Browser Information", "No more pages can be loaded.");
		} else {

			System.out.println(GBOX_HISTORY_PATH.toString());
			System.out.println(IDX + ":" + GBOX_HISTORY_PATH.get(IDX));

			GBOX_PATH = GBOX_HISTORY_PATH.get(IDX);

			setComboDataBind();
			setTableDataBind(GBOX_PATH);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoPreviousHistory(
			@UIEventTopic(Constants.GBOX_HISTORY_PREVIOUS_EVENT_BUS_NAME) String uid) {

		IDX = IDX - 1;

		if (IDX < 0) {
			IDX = 0;
			MessageDialog.openInformation(parent.getShell(), "Browser Information", "No more pages can be loaded.");
		} else {

			System.out.println(GBOX_HISTORY_PATH.toString());
			System.out.println(IDX + ":" + GBOX_HISTORY_PATH.get(IDX));

			GBOX_PATH = GBOX_HISTORY_PATH.get(IDX);

			setComboDataBind();
			setTableDataBind(GBOX_PATH);

			GBOX_HISTORY_PATH.add(GBOX_PATH);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpHistory(@UIEventTopic(Constants.GBOX_HISTORY_UP_EVENT_BUS_NAME) String uid) {

		File file = new File(GBOX_PATH);

		System.out.println(file.getParent().replace("\\", "/") + "\t"
				+ org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT + "\t"
				+ file.getParent().replace("\\", "/").equals(org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT));

		String _parent = file.getParent().replace("\\", "/") + "/";

		if (_parent.equals(org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT)) {
			MessageDialog.openInformation(parent.getShell(), "Browser Information", "No more pages can be loaded.");
		} else {

			GBOX_PATH = file.getParent().replace("\\", "/");

			GBOX_HISTORY_PATH.add(GBOX_PATH);
			IDX = GBOX_HISTORY_PATH.size() - 1;

			if (!SHOW_GBOX_HISTORY_PATH.contains(fake(GBOX_PATH))) {

				SHOW_GBOX_HISTORY_PATH.add(fake(GBOX_PATH));

				String[] strings = SHOW_GBOX_HISTORY_PATH.stream().toArray(String[]::new);
				Arrays.sort(strings);
				pathCombo.setItems(strings);
			}

			setComboDataBind();
			setTableDataBind(GBOX_PATH);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoDelete(@UIEventTopic(Constants.GBOX_DELETE_EVENT_BUS_NAME) String uid) {

		if (!tableViewer.getStructuredSelection().isEmpty()) {

			TableItem[] items = tableViewer.getTable().getSelection();

			boolean res = MessageDialog.openConfirm(parent.getShell(), "Delete",
					"Are you sure you want to delete " + items.length + " files?");

			if (res) {

				sync.asyncExec(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						List<String> path = new ArrayList<String>();

						for (TableItem item : items) {
							FileModel fileModel = (FileModel) item.getData();
							path.add(fileModel.getPath());
						}

						Activator.getRapidantService().delete(path);

						setTableDataBind(GBOX_PATH);
					}
				});
			}
		}
	}

	@Inject
	@Optional
	private void subscribeTopicTodoNewFile(@UIEventTopic(Constants.GBOX_NEW_FILE_EVENT_BUS_NAME) String uid) {

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

			String path = GBOX_PATH + "/" + dialog.getName();

			boolean res = Activator.getRapidantService().touch(path);

			System.out.println("touch:[" + String.valueOf(res) + "] -> path:[" + path + "]");
		}

		setTableDataBind(GBOX_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoNewFolder(@UIEventTopic(Constants.GBOX_NEW_FOLDER_EVENT_BUS_NAME) String uid) {

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
			System.out.println(String.join("/", GBOX_PATH, dialog.getName()));
			Activator.getRapidantService().mkdir(String.join("/", GBOX_PATH, dialog.getName()));
		}

		setTableDataBind(GBOX_PATH);
	}

	@Inject
	@Optional
	private void subscribeTopicTodoRename(@UIEventTopic(Constants.GBOX_RENAME_EVENT_BUS_NAME) FileModel fileModel) {

		if (fileModel.isIsSymbol()) {

			MessageDialog.openWarning(parent.getShell(), "GBox Warning",
					"This data cannot be edited. Only can be downloaded.");

		} else {

			boolean isDir = fileModel.isIsDir();

			IInputValidator validator = new IInputValidator() {

				@Override
				public String isValid(String newText) {

					if (newText.isEmpty()) {
						return "Please enter a new name.";
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
					String.join(" ", isDir ? "File" : "Folder", "rename:"), fileModel.getName(), validator);

			if (dialog.open() == Window.OK) {

				String source = fileModel.getPath();
				String target = GBOX_PATH + "/" + dialog.getName();

				if (PATTERN_1.matcher(dialog.getName()).find() || PATTERN_2.matcher(dialog.getName()).find()) {
					MessageDialog.openError(parent.getShell(), "Rename Error", "Cannot contain special characters.");
				} else {
					boolean res = Activator.getRapidantService().rename(source, target);

					System.out.println("rename:[" + res + "] => source:[" + source + "], target:[" + target + "]");

					if (res) {
						setTableDataBind(GBOX_PATH);
						MessageDialog.openInformation(parent.getShell(), "Rename Complete", "Change is complete.");
					}
				}
			}
		}
	}

	@Inject
	@Optional
	private void fileReloadsubscribeTopicTodoCopy(@UIEventTopic(Constants.GBOX_COPY_EVENT_BUS_NAME) String uid) {

		STORE_COPY_PATH = null;
		STORE_COPY_PATH = new ArrayList<FileModel>();

		for (TableItem item : tableViewer.getTable().getSelection()) {
			FileModel fileModel = (FileModel) item.getData();
			STORE_COPY_PATH.add(fileModel);
		}
	}

	@Inject
	@Optional
	private void fileReloadsubscribeTopicTodoPaste(@UIEventTopic(Constants.GBOX_PASTE_EVENT_BUS_NAME) String uid) {

		String copyTarget = GBOX_PATH;

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

			Job job = new Job("Start transfer files.") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					// TODO Auto-generated method stub

					monitor.beginTask("File transfer in progress.", STORE_COPY_PATH.size());

					for (FileModel source : STORE_COPY_PATH) {

						monitor.worked(1);
						monitor.setTaskName(source.getPath());

						System.out.println("copy => soruce: [" + source.getPath() + "], target: [" + GBOX_PATH + "/]");

//						Activator.getRapidantService().copy(source.getPath(), GBOX_PATH + "/");
						Activator.getRapidantService().copy(source.getPath(), copyTarget + "/");
					}

//					setTableDataBind(GBOX_PATH);

					if (GBOX_PATH.equals(copyTarget)) {
						setTableDataBind(GBOX_PATH);
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

		GBOX_PATH = org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT + Activator.getMember().getMemberId();

		GBOX_HISTORY_PATH = new ArrayList<String>();
		SHOW_GBOX_HISTORY_PATH = new ArrayList<String>();

		GBOX_HISTORY_PATH.add(GBOX_PATH);
		SHOW_GBOX_HISTORY_PATH.add(fake(GBOX_PATH));

		bind();
		event();
	}

	@Override
	public void bind() {
		// TODO Auto-generated method stub

		String user = Activator.getMember().getMemberId();
		String root = Constant.GBOX_RAPIDANT_ROOT + user;

		setTableDataBind(root);

		sync.asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String[] strings = SHOW_GBOX_HISTORY_PATH.stream().toArray(String[]::new);
				Arrays.sort(strings);
				pathCombo.setItems(strings);
				pathCombo.setText(fake(GBOX_PATH));
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
				GBOX_REAL_PATH = org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT
						+ pathCombo.getText().substring(1);

				GBOX_PATH = GBOX_REAL_PATH;

				GBOX_HISTORY_PATH.add(GBOX_REAL_PATH);
				IDX = GBOX_HISTORY_PATH.size() - 1;

				setTableDataBind(GBOX_REAL_PATH);
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

				if (file.isIsSymbol()) {

					MessageDialog.openWarning(parent.getShell(), "GBox Warning",
							"This data cannot be edited.Only can be downloaded.");

				} else {

					if (file.isIsDir()) {

						GBOX_PATH = file.getPath();

//						if (!GBOX_HISTORY_PATH.contains(GBOX_PATH)) {
//							GBOX_HISTORY_PATH.add(GBOX_PATH);
//						}

						GBOX_HISTORY_PATH.add(GBOX_PATH);
						IDX = GBOX_HISTORY_PATH.size() - 1;

						if (!SHOW_GBOX_HISTORY_PATH.contains(fake(GBOX_PATH))) {

							SHOW_GBOX_HISTORY_PATH.add(fake(GBOX_PATH));

							String[] strings = SHOW_GBOX_HISTORY_PATH.stream().toArray(String[]::new);
							Arrays.sort(strings);
							pathCombo.setItems(strings);
						}

						setComboDataBind();
						setTableDataBind(GBOX_PATH);
					}
				}

			}
		});

		/**
		 * Drag and Drop
		 */
		Table table = tableViewer.getTable();

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
				// TODO Auto-generated method stubSystem.out.println(event.doit);
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

				boolean isSupportedType = FileTransfer.getInstance().isSupportedType(event.currentDataType);

				if (!ePartService.getActivePart().getElementId().equals(Constants.GBOX_TABLE_VIEW_ID) || isSupportedType) {

					if (Activator.getRapidantService().isTransfer()) {
						MessageDialog.openInformation(parent.getShell(), "GBox Information",
								"File transfer in progress. Please try again after the transfer is complete.");
					} else {

						System.out.println(Activator.getRapidantService());

						int count = 0;

						if (Activator.getRapidantService().getSessionInfo() == null) {
							count = 0;
						} else {
							count = Activator.getRapidantService().getSessionInfo().getNowTransferCount();
						}

						if (count < Constants.RAPIDANT_MAX_TRANSFER_COUNT) {
							
							String json = null;

							if (isSupportedType) {

								String[] files = (String[]) event.data;

								FileService fileService = new FileServiceImpl();

								List<FileModel> fileList = new ArrayList<FileModel>();

								for (String file : files) {
									FileModel fileModel = fileService.getFile(file);
									fileList.add(fileModel);
								}

								Gson gson = new Gson();
								json = gson.toJson(fileList);

							} else {
								json = (String) event.data;
							}

							upload(json);
						} else {
							MessageDialog.openWarning(parent.getShell(), "GBox Warning",
									"The upload service is currently unavailable due to a many users.");
						}
					}
				} else {
					MessageDialog.openWarning(parent.getShell(), "Warning", "Incorrectly select path.");
				}
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	@SuppressWarnings("unchecked")
	private void upload(Object object) {

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

		FileService fileService = new FileServiceImpl();

		int total_count = tableViewer.getTable().getItemCount() + source.size();

		boolean isConfirm = true;

		if (total_count > 100) {
			isConfirm = MessageDialog.openConfirm(parent.getShell(), "Tranfer Confirm",
					"It is not recommended to store more than 100 files in one folder. Do you want to continue?");
		}

		if (isConfirm) {

			files = null;
			files = new ArrayList<String>();

			exclude = null;
			exclude = new ArrayList<String>();

			MPart mPart = ePartService.findPart(Constants.PROGRESS_VIEW_ID);

			ePartService.showPart(mPart, PartState.ACTIVATE);

			Job job = new Job("Starts preprocessing of the transfer file.") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					// TODO Auto-generated method stub

					monitor.beginTask("Searching for transfer data.", source.size());

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (FileModel fileModel : source) {

						monitor.worked(1);
						monitor.setTaskName(fileModel.getPath());

						files = fileService.getSubFiles(files, fileModel.getPath());
					}

					monitor.done();

					SubMonitor subMonitor = SubMonitor.convert(monitor, files.size());
					subMonitor.beginTask("Starts preprocessing of the transfer file.", files.size());

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int i = 0; i < files.size(); i++) {

						if (PATTERN_1.matcher(files.get(i)).find() || PATTERN_2.matcher(files.get(i)).find()) {
							exclude.add(files.get(i));
						}

						subMonitor.worked(1);
						subMonitor.setTaskName(files.get(i));
						subMonitor.subTask("(" + (i + 1) + "/" + files.size() + ")");

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					subMonitor.done();

					sync.asyncExec(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							if (exclude.size() == 0) {

								String target = GBOX_PATH;
								String type = org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_TRANSFER_SEND;

								if (Activator.getRapidantService().isTransfer()) {
									MessageDialog.openInformation(parent.getShell(), "GBox Information",
											"The data file is being transferred. Please try again after transferring the data file.");
								} else {

									Job j = new Job("GBox High-speed data transfer in progress.") {

										@Override
										protected IStatus run(IProgressMonitor monitor) {
											// TODO Auto-generated method stub

											GBoxTransferHandler handler = new GBoxTransferHandler(parent, iEventBroker,
													monitor, source, target, type);
											handler.run();

											return Status.OK_STATUS;
										}
									};

									j.schedule();
								}
							} else {

								if (exclude.size() <= 3) {
									message = String.join(",", exclude);
								} else {
									message = String.join(",", exclude.subList(0, 3));
								}

								MessageDialog.openWarning(parent.getShell(), "GBox Warning",
										message + "\r\n" + "more " + exclude.size() + " files cannot be transferred.\n"
												+ "Check if Korean or special characters are included and try again.");

								MessageConsoleStream msgConsoleStream = Activator.getMessageConsole()
										.newMessageStream();

								msgConsoleStream.println(message);
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
				pathCombo.setText(fake(GBOX_PATH));
			}
		});
	}

	private void setTableDataBind(String path) {

		Job job = new Job("Binding GBox data files") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub

				monitor.beginTask("GBox data file.", IProgressMonitor.UNKNOWN);

				List<FileModel> files = Activator.getRapidantService().getFiles(path);

				monitor.beginTask("Loading GBox data file list..", files.size());

				for (FileModel file : files) {

					monitor.worked(1);
					monitor.setTaskName(file.getPath().replace(Constants.GBOX_RAPIDANT_ROOT, ""));
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
						tableViewer.refresh(true, true);
					}
				});

				monitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();

	}

	private String fake(String path) {
		return path.replace(org.kobic.bioexpress.gbox.Constant.GBOX_RAPIDANT_ROOT, "/");
	}
}