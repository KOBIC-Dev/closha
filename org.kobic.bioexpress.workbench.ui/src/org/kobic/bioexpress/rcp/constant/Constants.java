package org.kobic.bioexpress.rcp.constant;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;

public interface Constants {

	/**
	 * global setting
	 */
	public static final String DEFAULT_VALUE = "NA";
	public static final String DEFAULT_SPACE_VALUE = " ";
	public static final String DEFAULT_NULL_VALUE = "";
	public static final int DEFAULT_DELAY_TIME = 1;
	public static final int DEFAULT_COUNT = 0;
	public static final int DEFAULT_DELAY_MILLISECONDS_TIME = 0;
	public static final int DEFAULT_CONSOLE_LOG_LINE = 1000;
	public static final String DEFAULT_OWNER = "kobic";

	public static final int ENTER_KEY_CODE = 13;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";
	public static final String TRANSFER_SPEED_MONITOR_FORMAT = "%s / %s ( %s/S )";
	public static final String USER_LOCAL_HOME = System.getProperty("user.dir");
	public static final String BIO_EXPRESS_PROPERTIES = "/config/closha.properties";
	public static final String BIO_EXPRESS_MESSAGE_PROPERTIES = "/config/messages.properties";
	public static final String BIO_INFORMATICS_KEYWORD_DATA = "/config/keywords-all.tab";
	public static final String TABLE_WIDGET_TYPE = "table";
	public static final String TREE_WIDGET_TYPE = "tree";
	public static final String KEYWORD_FORMAT = "#%s";
	public static final String SCRIPT_ROOT_PATH = "/BiO/K-BDS/BiO-EXPRESS/script/%s";
	public static final String GBOX_RAPIDANT_ROOT = "/BiO/K-BDS/USER";
	public static final String INSTALLED_PROGRAM_ROOT = "";

	public static final String DEFAULT_INFO_TYPE = "info";

	public static final String PIPELINE_STATUS_REGISTER_READY = "PI-REG-0010";
	public static final String PIPELINE_STATUS_REGISTER_REQUEST = "PI-REG-0020";
	public static final String PIPELINE_STATUS_REGISTER_COMPLETE = "PI-REG-0030";
	public static final String PIPELINE_STATUS_REGISTER_REJECT = "PI-REG-0400";

	public static final String PROGRAM_STATUS_REGISTER_READY = "PR-REG-0010";
	public static final String PROGRAM_STATUS_REGISTER_REQUEST = "PR-REG-0020";
	public static final String PROGRAM_STATUS_REGISTER_COMPLETE = "PR-REG-0030";
	public static final String PROGRAM_STATUS_REGISTER_REJECT = "PR-REG-0400";

	public static final String RAONWIZ_STATE_STABLE = "Stable";
	public static final String RAONWIZ_STATE_CONFUSION = "Confusion";

	public static final String RAONWIZ_STATE_CONNECTED = "Stable";
	public static final String RAONWIZ_STATE_DISCONNECTED = "Confusion";

	public static final int RAPIDANT_MAX_TRANSFER_COUNT = 50;

	public static final String CLOSHA = "CLOSHA";
	public static final String GBox = "GBox";

	public static final String RAONWIZ_SERVER_ID = "KBDS-1";
	public static final String RAONWIZ_SERVER_NAME = "Raonwiz-Server";

	/**
	 * Podman
	 */
	public static final String PODMAN_BUILD_FILE_TYPE = "build";
	public static final String PODMAN_IMAGE_TYPE = "load";

	public static final String PODMAN_BASE_IMAGE_NAME = "bx.official";

	/**
	 * Gbox
	 */
	public static String GBOX_RAPIDANT_TRANSFER_SEND = "send";
	public static String GBOX_RAPIDANT_TRANSFER_RECEIVE = "receive";
	
	public static String GBOX_MAX_SPEED_VALUE = "100";
	public static String GBOX_MIN_SPEED_VALUE = "5";
	

	/**
	 * perspective
	 */
	// symbolic name
	public static final String SYMBOLIC_NAME = "org.kobic.bioexpress.workbench.ui";

	// window id
	public static final String LOGIN_M_WINDOW_ID = "org.eclipse.e4.window.login";
	public static final String MAIN_M_WINDOW_ID = "org.eclipse.e4.window.main";

	// perspective id
	public static final String GBOX_PERSPECTIVE_ID = "org.kobic.bioexpress.rcp.gbox.perspective";
	public static final String CLOSHA_PERSPECTIVE_ID = "org.kobic.bioexpress.rcp.closha.perspective";
	public static final String LOGIN_PERSPECTIVE_ID = "org.kobic.bioexpress.rcp.login.perspective";

	// view id
	public static final String GBOX_TABLE_VIEW_ID = "org.kobic.bioexpress.rcp.gbox.explorer";
	public static final String FILE_TABLE_VIEW_ID = "org.kobic.bioexpress.rcp.file.explorer";
	public static final String FILE_TREE_VIEW_ID = "org.kobic.bioexpress.rcp.file.view.file.tree.view";
	public static final String CLOSHA_FILE_TREE_VIEW_ID = "org.kobic.bioexpress.rcp.closha.file.browser.part";
	public static final String CLOSHA_GBOX_TREE_VIEW_ID = "org.kobic.bioexpress.rcp.closha.gbox.view";
	public static final String INTRO_VIEW_ID = "org.kobic.bioexpress.rcp.intro.view.intro.view";
	public static final String LOGIN_VIEW_ID = "org.kobic.bioexpress.rcp.login.view.login.view";
	public static final String HOST_MONITOR_VIEW_ID = "org.kobic.bioexpress.rcp.monitor.view.cluster.host.monitor.view";
	public static final String PIPELINE_VIEW_ID = "org.kobic.bioexpress.rcp.pipeline.view.pipeline.view";
	public static final String PROGRAM_VIEW_ID = "org.kobic.bioexpress.rcp.program.view.program.view";
	public static final String WORKSPACE_VIEW_ID = "org.kobic.bioexpress.rcp.workspace.view.workspace.view";
	public static final String DETAIL_DATA_VIEW_ID = "org.kobic.bioexpress.rcp.info.detail.data.view";
	public static final String GBOX_TRANSFER_VIEW_ID = "org.kobic.bioexpress.rcp.gbox.transfer.monitor.view";
	public static final String WORKBENCH_MONITOR_VIEW_ID = "org.kobic.bioexpress.rcp.monitor.view";
	public static final String PIPELINE_DETAIL_VIEW_ID = "org.kobic.bioexpress.rcp.pipeline.view.pipeline.detail";
	public static final String LOG_VIEW_ID = "org.kobic.bioexpress.workbench.ui.part.log.view";
	public static final String RAONWIZ_VIEW_ID = "org.kobic.bioexpress.rcp.raonwiz.view";
	public static final String SCRIPT_VIEW_ID = "org.kobic.bioexpress.workbench.ui.part.script";
	public static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";
	public static final String PROGRESS_VIEW_ID = "org.eclipse.ui.views.ProgressView";
	public static final String SCRIPT_TASK_HISTORY_VIEW_ID = "org.eclipse.ui.console.ScriptTaskHistory";
	public static final String INSTALLED_PROGRAM_VIEW_ID = "org.kobic.bioexpress.rcp.installed.program.view.installed.program.view";
	public static final String PODMAN_VIEW_ID = "org.kobic.bioexpress.workbench.ui.part.podman.view";

	// part id

	public static final String GBOX_BROWSER_PART_ID = "org.kobic.bioexpress.rcp.gbox.browser.part";
	public static final String GBOX_LOCAL_TREE_PART_ID = "org.kobic.bioexpress.rcp.file.browser.part";

	// popup menu(contextmenu) id
	public static final String PODMAN_POPUP_MENU_ID = "org.kobic.bioexpress.workbench.ui.popupmenu.podman";
	public static final String SCRIPT_POPUP_MENU_ID = "org.kobic.bioexpress.rcp.script.popupmenu";
	public static final String WORKSPACE_POPUP_MENU_ID = "org.kobic.bioexpress.rcp.workpace.popupmenu";
	public static final String PROGRAM_POPUP_MENU_ID = "org.kobic.bioexpress.rcp.program.popupmenu";
	public static final String INSTALLED_PROGRAM_POPUP_MENU_ID = "org.kobic.bioexpress.rcp.installed.program.popupmenu";
	public static final String PIPELINE_POPUP_MENU_ID = "org.kobic.bioexpress.rcp.pipeline.popupmenu";
	public static final String CLOSHA_REMOTE_POPUP_MENU_ID = "org.kobic.bioexpress.rcp.browser.popupmenu";
	public static final String GBOX_LOCAL_BROWSER_POPUP_MENU_ID = "org.kobic.bioexpress.workbench.gbox.local.browser.popupmenu";
	public static final String GBOX_LOCAL_EXPLORER_POPUP_MENU_ID = "org.kobic.bioexpress.workbench.gbox.local.explorer.popupmenu";
	public static final String GBOX_REMOTE_BROWSER_POPUP_MENU_ID = "org.kobic.bioexpress.workbench.gbox.remote.browser.popupmenu";
	public static final String GBOX_REMOTE_EXPLORER_POPUP_MENU_ID = "org.kobic.bioexpress.workbench.gbox.remote.exploer.popupmenu";

	/**
	 * event broker
	 */

	public static final String CLOSHA_FILE_TREE_DATA_RELOAD_EVENT_BUS_NAME = "CLOSHA_FILE_TREE_DATA_RELOAD";
	public static final String CLOSHA_GBOX_TREE_DATA_RELOAD_EVENT_BUS_NAME = "CLOSHA_GBOX_TREE_DATA_RELOAD";

	public static final String FILE_TREE_DATA_RELOAD_EVENT_BUS_NAME = "FILE_TREE_DATA_RELOAD";
	public static final String FILE_TREE_SELECT_EVENT_BUS_NAME = "FILE_TREE_SELECT_EVENT";
	public static final String FILE_DATA_RELOAD_EVENT_BUS_NAME = "FILE_DATA_RELOAD";
	public static final String FILE_HISTORY_FORWARD_EVENT_BUS_NAME = "FILE_HISTORY_FORWARD";
	public static final String FILE_HISTORY_PREVIOUS_EVENT_BUS_NAME = "FILE_HISTORY_PREVIOUS";
	public static final String FILE_HISTORY_UP_EVENT_BUS_NAME = "FILE_HISTORY_UP";
	public static final String FILE_TABLE_DELETE_EVENT_BUS_NAME = "FILE_TABLE_DELETE_EVENT";
	public static final String FILE_TABLE_NEW_FILE_EVENT_BUS_NAME = "FILE_TABLE_NEW_FILE_EVENT";
	public static final String FILE_TABLE_NEW_FOLDER_EVENT_BUS_NAME = "FILE_TABLE_NEW_FOLDER_EVENT";
	public static final String FILE_TABLE_UPLOAD_EVENT_BUS_NAME = "FILE_TABLE_UPLOAD_EVENT";
	public static final String FILE_TABLE_RAPIDANT_UPLOAD_EVENT_BUS_NAME = "FILE_TABLE_RAPIDANT_UPLOAD_EVENT";
	public static final String FILE_TABLE_COPY_EVENT_BUS_NAME = "FILE_TABLE_COPY_EVENT";
	public static final String FILE_TABLE_PASTE_EVENT_BUS_NAME = "FILE_TABLE_PASTE_EVENT";
	public static final String FILE_TABLE_RENAME_EVENT_BUS_NAME = "FILE_TABLE_RENAME_EVENT";

	public static final String GBOX_DATA_LOAD_EVENT_BUS_NAME = "GBOX_DATA_LOAD";
	public static final String GBOX_DATA_RELOAD_EVENT_BUS_NAME = "GBOX_DATA_RELOAD";
	public static final String GBOX_TREE_DATA_RELOAD_EVENT_BUS_NAME = "GBOX_TREE_DATA_RELOAD";
	public static final String GBOX_REMOTE_EXPLORER_HOME_EVENT_BUS_NAME = "GBOX_REMOTE_EXPLORER_GO_HOME";
	public static final String GBOX_LOCAL_EXPLORER_HOME_EVENT_BUS_NAME = "GBOX_LOCAL_EXPLORER_GO_HOME";
	public static final String GBOX_TREE_SELECT_EVENT_BUS_NAME = "GBOX_TREE_SELECT_EVENT";
	public static final String GBOX_TRANSFER_MONITOR_INIT_EVENT_BUS_NAME = "GBOX_TRANSFER_INIT_EVENT";
	public static final String GBOX_TRANSFER_MONITOR_STATUS_EVENT_BUS_NAME = "GBOX_TRANSFER_STATUS_EVENT";
	public static final String GBOX_HISTORY_FORWARD_EVENT_BUS_NAME = "GBOX_HISTORY_FORWARD";
	public static final String GBOX_HISTORY_PREVIOUS_EVENT_BUS_NAME = "GBOX_HISTORY_PREVIOUS";
	public static final String GBOX_HISTORY_UP_EVENT_BUS_NAME = "GBOX_HISTORY_UP";
	public static final String CLUSTER_DATA_RELOAD_EVENT_BUS_NAME = "CLUSTER_DATA_RELOAD";
	public static final String LOG_DATA_RELOAD_EVENT_BUS_NAME = "LOG_DATA_RELOAD";
	public static final String GBOX_DELETE_EVENT_BUS_NAME = "GBOX_DELETE_EVENT";
	public static final String GBOX_DOWNLOAD_EVENT_BUS_NAME = "GBOX_DOWNLOAD_EVENT";
	public static final String GBOX_RAPIDANT_DOWNLOAD_EVENT_BUS_NAME = "GBOX_RAPIDANT_DOWNLOAD_EVENT";
	public static final String GBOX_NEW_FILE_EVENT_BUS_NAME = "GBOX_NEW_FILE_EVENT";
	public static final String GBOX_NEW_FOLDER_EVENT_BUS_NAME = "GBOX_NEW_FOLDER_EVENT";
	public static final String GBOX_RENAME_EVENT_BUS_NAME = "GBOX_RENAME_EVENT";
	public static final String GBOX_COPY_EVENT_BUS_NAME = "GBOX_COPY_EVENT";
	public static final String GBOX_PASTE_EVENT_BUS_NAME = "GBOX_PASTE_EVENT";

	public static final String WORKSPACE_REFRESH_EVENT_BUS_NAME = "WORKSPACE_NEW_EVENT";
	public static final String PIPELINE_DETAIL_REFRESH_EVENT_BUS_NAME = "PIPELINE_DETAIL_REFRESH_EVENT";
	public static final String PROGRAM_CATEGORY_REFRESH_EVENT_BUS_NAME = "PROGRAM_CATEGORY_REFRESH_EVENT";
	public static final String INSTALLED_PROGRAM_REFRESH_EVENT_BUS_NAME = "INSTALLED_PROGRAM_REFRESH_EVENT";
	public static final String PIPELINE_CATEGORY_REFRESH_EVENT_BUS_NAME = "PIPELINE_CATEGORY_REFRESH_EVENT";

	public static final String CATEGORY_SELECT_EVENT_BUS_NAME = "CATEGORY_SELECT_EVENT";

	public static final String SCRIPT_REFRESH_EVENT_BUS_NAME = "SCRIPT_REFRESH_EVENT";
	public static final String SCRIPT_DELETE_EVENT_BUS_NAME = "SCRIPT_DELETE_EVENT";

	public static final String RAONWIZ_REFRESH_EVENT_BUS_NAME = "RAONWIZ_REFRESH_EVENT";
	public static final String SCRIPT_TASK_HISTORY_REFRESH_EVENT_BUS_NAME = "SCRIPT_TASK_HISTORY_REFRESH_EVENT";
	public static final String DETAIL_DATA_REFRESH_EVENT_BUS_NAME = "DETAIL_DATA_REFRESH_EVENT";

	public static final String PODMAN_DATA_RELOAD_EVENT_BUS_NAME = "PODMAN_DATA_RELOAD_EVENT";

	/**
	 * thread
	 */
	public static final String GBOX_TRANSFER_THREAD_JOB_01 = "G0001"; // GBox 데이터 전송 시작
	public static final String GBOX_TRANSFER_THREAD_JOB_02 = "G0002"; // GBox 데이터 전송 시작 로그 전달
	public static final String GBOX_TRANSFER_THREAD_JOB_03 = "G0003"; // GBox 데이터 전송 중 로그 전달
	public static final String GBOX_TRANSFER_THREAD_JOB_04 = "G0004"; // GBox 데이터 전송 완료 로그 전달
	public static final String GBOX_TRANSFER_THREAD_JOB_05 = "G0005"; // GBox 데이터 전송 로그 모니터 출력

	public static final String PIPELINE_LABEL = "pipeline";
	public static final String PROGRAM_LABEL = "program";
	public static final String WORKSPACE_LABEL = "workspace";
	public static final String SCRIPT_LABEL = "script";

	public static final String PYTHON_EXT = ".py";
	public static final String BASH_EXT = ".sh";
	public static final String R_EXT = ".r";

	public static final String PYTHON = "python";
	public static final String BASH = "bash";
	public static final String R = "r";

	public static final String FILE_VALUE_TYPE = "File";
	public static final String FOLDER_VALUE_TYPE = "Folder";

	/**
	 * Dialog button id
	 */
	public static final int EDTICODE_BUTTON_ID = IDialogConstants.NO_TO_ALL_ID + 1;

	/**
	 * image
	 */
	public static final String LOGIN_BACKGROUND_IMAGE = "imgs/login_bg_1.jpg";

	/**
	 * icon
	 */
	public static final String FILE_VIEW_FOLDER_ICON = "icons/folder2.png";
	public static final String FILE_VIEW_FILE_ICON = "icons/file.png";
	public static final String GBOX_VIEW_SYMBOLIC_FOLDER_ICON = "icons/folder_linked.png";
	public static final String INFO_VIEW_NAME_ICON = "icons/information_name_dot.png";
	public static final String FILE_DELETE_ICON = "icons/delete.gif";
	public static final String CLUSTER_VIEW_HOST_ICON = "icons/cluster_host.png";
	public static final String CLUSTER_VIEW_HOST_WARNIG_ICON = "icons/cluster_host_warn.png";
	public static final String CLUSTER_VIEW_HOST_RISK_ICON = "icons/cluster_host_risk.png";
	public static final String CATEGORY_VIEW_CATEGORY_ICON = "icons/workspace_obj.png";
	public static final String PIPELINE_VIEW_ROOT_CATEGORY_ICON = "icons/pipeline_root_category.png";
	public static final String PIPELINE_VIEW_SUB_CATEGORY_ICON = "icons/pipeline_sub_category.png";
	public static final String PIPELINE_VIEW_PIPELINE_ICON = "icons/pipeline.png";
	public static final String PIPELINE_VIEW_RUN_PIPELINE_ICON = "icons/pipeline_run.png";
	public static final String PIPELINE_VIEW_PUBLIC_PIPELINE_ICON = "icons/pipeline_public.png";
	public static final String PIPELINE_VIEW_PUBLIC_ROOT_CATEGORY_ICON = "icons/pipeline_root_category_public.png";
	public static final String PIPELINE_VIEW_PUBLIC_SUB_CATEGORY_ICON = "icons/pipeline_sub_category_public.png";
	public static final String PIPELINE_SMALL_ICON = "icons/pipeline_small.png";
	public static final String PROGRAM_SMALL_ICON = "icons/program_small.png";
	public static final String PROGRAM_VIEW_ROOT_CATEGORY_ICON = "icons/program_root_category.png";
	public static final String PROGRAM_VIEW_SUB_CATEGORY_ICON = "icons/program_sub_category.png";
	public static final String PROGRAM_VIEW_PROGRAM_ICON = "icons/program.png";
	public static final String PROGRAM_VIEW_PUBLIC_PROGRAM_ICON = "icons/program_public.png";
	public static final String PROGRAM_VIEW_PUBLIC_ROOT_CATEGORY_ICON = "icons/program_root_category_public.png";
	public static final String PROGRAM_VIEW_PUBLIC_SUB_CATEGORY_ICON = "icons/program_sub_category_public.png";
	public static final String WORKSPACE_VIEW_WORKSPACE_ICON = "icons/workspace.png";
	public static final String TRANSFER_MONITOR_UPLOAD_ICON = "icons/upload.png";
	public static final String TRANSFER_MONITOR_DOWNLOAD_ICON = "icons/down.gif";
	public static final String TRANSFER_MONITOR_PUASE_ICON = "icons/pause.png";
	public static final String TRANSFER_MONITOR_CLOSE_ICON = "icons/actionengine_stop.gif";
	public static final String PARAMETER_DIALOG_TAB_ICON = "icons/flatLayout.png";
	public static final String PARAMETER_DIALOG_INPUT_TAB_ICON = "icons/info_parameter_input.png";
	public static final String PARAMETER_DIALOG_OUTPUT_TAB_ICON = "icons/info_parameter_output.png";
	public static final String PARAMETER_DIALOG_OPTION_TAB_ICON = "icons/info_parameter_option.png";
	public static final String PARAMETER_DIALOG_ADD_ICON = "icons/parameter_new.png";
	public static final String PARAMETER_DIALOG_EDIT_ICON = "icons/parameter_edit.png";
	public static final String PARAMETER_DIALOG_REMOVE_ICON = "icons/parameter_delete.png";
	public static final String PARAMETER_DIALOG_STRING_ICON = "icons/parameter_string.png";
	public static final String PARAMETER_DIALOG_INTEGER_ICON = "icons/parameter_integer.png";
	public static final String PARAMETER_DIALOG_FLOAT_ICON = "icons/parameter_float.png";
	public static final String PARAMETER_DIALOG_BOOLEAN_ICON = "icons/parameter_boolean.png";
	public static final String PARAMETER_DIALOG_FILE_ICON = "icons/parameter_file.png";
	public static final String PARAMETER_DIALOG_FOLDER_ICON = "icons/parameter_folder.png";
	public static final String PARAMETER_DIALOG_SCRIPT_ICON = "icons/script.gif";
	public static final String PARAMETER_DIALOG_REFRESH_ICON = "icons/launch_restart.gif";
	public static final String PARAMETER_BASIC_TAB_ICON = "icons/info_basic.png";
	public static final String PARAMETER_EXCUTE_TAB_ICON = "icons/info_excute.png";
	public static final String PARAMETER_PARAMETER_TAB_ICON = "icons/info_parameter.png";
	public static final String PROGRAM_PYTHON_ICON = "icons/python.png";
	public static final String PROGRAM_BASH_ICON = "icons/bash.png";
	public static final String PROGRAM_R_ICON = "icons/rscript.png";
	public static final String BOOLEAN_TRUE_ICON = "icons/boolean_true.png";
	public static final String BOOLEAN_FALSE_ICON = "icons/boolean_false.png";
	public static final String STATUS_WAIT_ICON = "icons/status_wait.png";
	public static final String STATUS_RUN_ICON = "icons/status_run.png";
	public static final String STATUS_COMPLETE_ICON = "icons/status_complete.png";
	public static final String STATUS_ERROR_ICON = "icons/status_error.png";
	public static final String STATUS_STOP_ICON = "icons/status_stop.png";
	public static final String SEARCH_ICON = "icons/search.png";
	public static final String KEYWORD_ICON = "icons/keyword.png";
	public static final String PASTE_ICON = "icons/paste.png";
	public static final String MEMBER_ICON = "icons/member.png";
	public static final String CHECK_ICON = "icons/check.png";
	public static final String SYNCHRONIZED_ICON = "icons/status_sync.png";
	public static final String WARNING_ICON = "icons/status_warning.png";
	public static final String UNSYNCHRONIZED_ICON = "icons/status_un_sync.png";
	public static final String FEEDBACK_ICON = "icons/feedback.png";
	public static final String COMMUNITY_ICON = "icons/community.png";
	public static final String MESSAGE_INFORMATION_ICON = "icons/message_info.png";
	public static final String MESSAGE_INFORMATION_ORI_ICON = "icons/message_info_ori.png";
	public static final String HELP_ICON = "/icons/help.png";
	public static final String PODMAN_ICON = "icons/podman.png";

	public static final String REQUIRED_ICON = "icons/reqired.png";
	public static final String OPTIONAL_ICON = "icons/optional.png";

	public static final String PYTHON_ICON = "icons/python.png";
	public static final String PYTHON_PUBLIC_ICON = "icons/python_public.png";
	public static final String BASH_ICON = "icons/bash.png";
	public static final String BASH_PUBLIC_ICON = "icons/bash_public.png";
	public static final String R_ICON = "icons/rscript.png";
	public static final String R_PUBLIC_ICON = "icons/rscript_public.png";
	public static final String DEFAULT_LANGUAGE_ICON = "icons/user.png";

	public static final String BI_BIO_EXPRESS_LARGE_ICON = "icons/bio_64.png";
	public static final String BI_BIO_EXPRESS_MIDIUM_ICON = "icons/bio_32.png";
	public static final String BI_BIO_EXPRESS_ICON = "icons/bio.png";

	public static final String BI_CLOSHA_LARGE_ICON = "icons/closha_64.png";
	public static final String BI_CLOSHA_MIDIUM_ICON = "icons/closha_32.png";
	public static final String BI_CLOSHA_ICON = "icons/closha.png";

	public static final String BI_GBOX_LARGE_ICON = "icons/gbox_64.png";
	public static final String BI_GBOX_MIDIUM_ICON = "icons/gbox_32.png";
	public static final String BI_GBOX_ICON = "icons/gbox.png";

	public static final String PODMAN_BUILD_FILE = "icons/podman.png";
	public static final String PODMAN_SAVE_IMAGE_FILE = "icons/podman.png";

	// dialog title
	public static final String CATEGORY_DIALOG_TITLE_ICON = "icons/workspace_new_dialog.png";
	public static final String CATEGORY_NEW_DIALOG_TITLE_ICON = "icons/category_new_dialog.png";
	public static final String CATEGORY_EDIT_DIALOG_TITLE_ICON = "icons/category_edit_dialog.png";
	public static final String WORKSPACE_NEW_DIALOG_TITLE_ICON = "icons/workspace_new_dialog.png";
	public static final String WORKSPACE_EDIT_DIALOG_TITLE_ICON = "icons/workspace_edit_dialog.png";
	public static final String PIPELINE_NEW_DIALOG_TITLE_ICON = "icons/pipeline_new_dialog.png";
	public static final String PIPELINE_EDIT_DIALOG_TITLE_ICON = "icons/pipeline_edit_dialog.png";
	public static final String PIPELINE_SHARE_DIALOG_TITLE_ICON = "icons/pipeline_share_dialog.png";
	public static final String PIPELINE_REGISTER_DIALOG_TITLE_ICON = "icons/pipeline_register_dialog.png";
	public static final String PROGRAM_NEW_DIALOG_TITLE_ICON = "icons/program_new_dialog.png";
	public static final String PROGRAM_EDIT_DIALOG_TITLE_ICON = "icons/program_edit_dialog.png";
	public static final String PROGRAM_REGISTER_DIALOG_TITLE_ICON = "icons/program_register_dialog.png";
	public static final String PODMAN_REGISTER_DIALOG_TITLE_ICON = "icons/podman_register_dialog.png";

	// file ext zip
	public static final String FILE_EXTENSION_ZIP_BASIC = "compressed";
	public static final String FILE_EXTENSION_ZIP = "zip";
	public static final String FILE_EXTENSION_SEVEN_ZIP = "7z";
	public static final String FILE_EXTENSION_TAR = "tar";
	public static final String FILE_EXTENSION_GZ = "gz";
	public static final String FILE_EXTENSION_BZ = "bz";
	public static final String FILE_EXTENSION_BZ_TWO = "bz2";
	public static final String FILE_EXTENSION_JAR = "jar";
	public static final String FILE_EXTENSION_WAR = "war";
	public static final String FILE_EXTENSION_ISO = "iso";
	public static final String FILE_EXTENSION_BIN = "bin";
	// file ext image
	public static final String FILE_EXTENSION_IMAGE_BASIC = "image";
	public static final String FILE_EXTENSION_PNG = "png";
	public static final String FILE_EXTENSION_BMP = "bmp";
	public static final String FILE_EXTENSION_GIF = "gif";
	public static final String FILE_EXTENSION_JPG = "jpg";
	// file ext logo
	public static final String FILE_EXTENSION_HTML = "html";
	public static final String FILE_EXTENSION_JAVA = "java";
	public static final String FILE_EXTENSION_JS = "js";
	public static final String FILE_EXTENSION_CSS = "css";
	public static final String FILE_EXTENSION_HWP = "hwp";
	public static final String FILE_EXTENSION_WORD = "word";
	public static final String FILE_EXTENSION_EXCEL = "xlsx";
	public static final String FILE_EXTENSION_PPT = "pptx";
	public static final String FILE_EXTENSION_PDF = "pdf";
	public static final String FILE_EXTENSION_JSON = "json";
	public static final String FILE_EXTENSION_CSV = "csv";
	public static final String FILE_EXTENSION_TSV = "tsv";
	public static final String FILE_EXTENSION_SVG = "svg";
	// file ext txt
	public static final String FILE_EXTENSION_TEXT_BASIC = "text";
	public static final String FILE_EXTENSION_TXT = "txt";
	public static final String FILE_EXTENSION_EXE = "exe";
	public static final String FILE_EXTENSION_DMG = "dmg";
	public static final String FILE_EXTENSION_MSI = "msi";
	public static final String FILE_EXTENSION_SQL = "sql";
	public static final String FILE_EXTENSION_DB = "db";
	public static final String FILE_EXTENSION_BAK = "bak";
	public static final String FILE_EXTENSION_XML = "xml";
	// file ext bioinfo
	public static final String FILE_EXTENSION_BIO_ICON = "icons/extension_bio.png";
	public static final String FILE_EXTENSION_FQ = "fastq";
	public static final String FILE_EXTENSION_FA = "fa";
	public static final String FILE_EXTENSION_SAM = "sam";
	public static final String FILE_EXTENSION_BAM = "bam";
	public static final String FILE_EXTENSION_VCF = "vcf";
	public static final String FILE_EXTENSION_GVCF = "gvcf";
	public static final String FILE_EXTENSION_REF = "ref";
	// other
	public static final String FILE_EXTENSION_BASIC = "other";
	public static final String FILE_EXTENSION_BINARY = "binary";
	public static final String FILE_EXTENSION_PYTHON = "py";
	public static final String FILE_EXTENSION_BASH = "sh";
	public static final String FILE_EXTENSION_R = "r";

	public static final Map<String, String> EXTENSION_MAP = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{
			// zip
			put(FILE_EXTENSION_ZIP_BASIC, "icons/extension_zip_title.png");
			put(FILE_EXTENSION_ZIP, "icons/extension_zip.png");
			put(FILE_EXTENSION_SEVEN_ZIP, "icons/extension_7z.png");
			put(FILE_EXTENSION_TAR, "icons/extension_tar.png");
			put(FILE_EXTENSION_GZ, "icons/extension_gz.png");
			put(FILE_EXTENSION_BZ, "icons/extension_bz.png");
			put(FILE_EXTENSION_BZ_TWO, "icons/extension_bz2.png");
			put(FILE_EXTENSION_WAR, "icons/extension_war.png");
			put(FILE_EXTENSION_ISO, "icons/extension_iso.png");
			put(FILE_EXTENSION_BIN, "icons/extension_bin.png");

			// image
			put(FILE_EXTENSION_IMAGE_BASIC, "icons/extension_img_title.png");
			put(FILE_EXTENSION_PNG, "icons/extension_png.png");
			put(FILE_EXTENSION_BMP, "icons/extension_bmp.png");
			put(FILE_EXTENSION_GIF, "icons/extension_gif.png");
			put(FILE_EXTENSION_JPG, "icons/extension_jpg.png");

			// logo
			put(FILE_EXTENSION_HTML, "icons/extension_html.png");
			put(FILE_EXTENSION_JAVA, "icons/extension_java.png");
			put(FILE_EXTENSION_JAR, "icons/extension_java.png");
			put(FILE_EXTENSION_JS, "icons/extension_js.png");
			put(FILE_EXTENSION_CSS, "icons/extension_css.png");
			put(FILE_EXTENSION_HWP, "icons/extension_hwp.png");
			put(FILE_EXTENSION_WORD, "icons/extension_word.png");
			put(FILE_EXTENSION_EXCEL, "icons/extension_excel.png");
			put(FILE_EXTENSION_PPT, "icons/extension_ppt.png");
			put(FILE_EXTENSION_PDF, "icons/extension_pdf.png");
			put(FILE_EXTENSION_SVG, "icons/extension_svg.png");

			// txt
			put(FILE_EXTENSION_TEXT_BASIC, "icons/extension_file_title.png");
			put(FILE_EXTENSION_TXT, "icons/extension_txt.png");
			put(FILE_EXTENSION_EXE, "icons/extension_exe.png");
			put(FILE_EXTENSION_DMG, "icons/extension_dmg.png");
			put(FILE_EXTENSION_MSI, "icons/extension_msi.png");
			put(FILE_EXTENSION_SQL, "icons/extension_sql.png");
			put(FILE_EXTENSION_DB, "icons/extension_db.png");
			put(FILE_EXTENSION_BAK, "icons/extension_bak.png");
			put(FILE_EXTENSION_XML, "icons/extension_xml.png");
			put(FILE_EXTENSION_JSON, "icons/extension_json.png");
			put(FILE_EXTENSION_CSV, "icons/extension_csv.png");
			put(FILE_EXTENSION_TSV, "icons/extension_tsv.png");

			// bioinfo
			put(FILE_EXTENSION_FQ, FILE_EXTENSION_BIO_ICON);
			put(FILE_EXTENSION_FA, FILE_EXTENSION_BIO_ICON);
			put(FILE_EXTENSION_SAM, FILE_EXTENSION_BIO_ICON);
			put(FILE_EXTENSION_VCF, FILE_EXTENSION_BIO_ICON);
			put(FILE_EXTENSION_GVCF, FILE_EXTENSION_BIO_ICON);
			put(FILE_EXTENSION_REF, FILE_EXTENSION_BIO_ICON);

			// other
			put(FILE_EXTENSION_BAM, "icons/extension_bam.png");
			put(FILE_EXTENSION_BASIC, "icons/extension_other_title.png");
			put(FILE_EXTENSION_BINARY, "icons/extension_binary.png");
			put(FILE_EXTENSION_PYTHON, "icons/python.png");
			put(FILE_EXTENSION_BASH, "icons/bash.png");
			put(FILE_EXTENSION_R, "icons/rscript.png");

		}
	};

	/**
	 * dialog Title message
	 */
	public static final String NEW_CATEGORY_DIALOG_TITLE_MESSAGE = "Create New Category";
	public static final String EDIT_CATEGORY_DIALOG_TITLE_MESSAGE = "Edit Category Information";
	public static final String NEW_WORKSPACE_DIALOG_TITLE_MESSAGE = "Create New Workspace";
	public static final String EDIT_WORKSPACE_DIALOG_TITLE_MESSAGE = "Edit Workspace Information";
	public static final String NEW_PIPELINE_DIALOG_TITLE_MESSAGE = "Create New Pipeline";
	public static final String EDIT_PIPELINE_DIALOG_TITLE_MESSAGE = "Edit Pipeline Information";
	public static final String REGISTER_PIPELINE_DIALOG_TITLE_MESSAGE = "Register Pipeline as Public";
	public static final String SHARE_PIPELINE_DIALOG_TITLE_MESSAGE = "Edit Pipeline Information";
	public static final String NEW_PROGRAM_DIALOG_TITLE_MESSAGE = "Create New Analysis Program";
	public static final String EDIT_PROGRAM_DIALOG_TITLE_MESSAGE = "Edit Analysis Program Information";
	public static final String REGISTER_PROGRAM_DIALOG_TITLE_MESSAGE = "Register Analysis Program as Public";
	public static final String REGISTER_PODMAN_IMAGE_DIALOG_TITLE_MESSAGE = "Regist a Podman Image for your Application";
	public static final String EDIT_SCRIPT_ENV_DIALOG_TITLE_MESSAGE = "Edit Podman Run Setting";

	/**
	 * dialog type
	 */
	public static final String EDIT_NODE_PARAMETER = "NODE_PARAMETER";
	public static final String EDIT_PROGRAM_PARAMETER = "PROGRAM_PARAMETER";

	public static final String INPUT_PARAMETER = "Input parameter";
	public static final String OUTPUT_PARAMETER = "Output parameter";

	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/**
	 * tree type
	 */
	public static final String PIPELINE_DETAIL_TREE_TABLE_VIEW_ID = "PIPELINE_DETAIL_TREE_TABLE_VIEW_ID";
	public static final String LOG_TREE_TABLE_VIEW_ID = "LOG_TREE_TABLE_VIEW_ID";

	/**
	 * message dialog
	 */
	// validation
	public static final String NAMING_RULE = "The name can only contain alphanumeric characters and some special characters(-_.). However, only a alphabet can be entered for the first letter.";
	public static final String NEW_NAMING_RULE = "Only contain alphanumeric characters and some special characters(-_.).";
	public static final String PROGRAM_VERSION_NAMING = "Only numbers and decimal points can be entered for the version.";
	public static final String READY_MESSAGE = "Service is being prepared.";

	// common
	public static final String CANCEL_LABEL = "Cancel";
	public static final String ADD_LABEL = "Add";
	public static final String REGISTER_LABLE = "Register";

	// parameter type
	public static final String PARAMETER_TYPE_INPUT = "Input";
	public static final String PARAMETER_TYPE_OUTPUT = "Output";
	public static final String PARAMETER_TYPE_OPTION = "Option";

	public static final String PARAMETER_REQUIRED = "Required";
	public static final String PARAMETER_OPTIONAL = "Optional";

	// excute
	public static final String RUN_SINGLE_NODE = "Run Program";
	public static final String RUN_PIPELINE = "Run Pipeline";

	// parameter value type
	public static final String PARAMETER_VALUE_TYPE_STRING = "String";
	public static final String PARAMETER_VALUE_TYPE_INTEGER = "Integer";
	public static final String PARAMETER_VALUE_TYPE_FLOAT = "Float";
	public static final String PARAMETER_VALUE_TYPE_BOOLEAN = "Boolean";
	public static final String PARAMETER_VALUE_TYPE_FOLDER = "Folder";
	public static final String PARAMETER_VALUE_TYPE_FILE = "File";

	// node status
	public static final String STATUS_WAIT = "wait";
	public static final String STATUS_RUN = "run";
	public static final String STATUS_EXEC = "exec";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_ERROR = "error";
	public static final String STATUS_STOP = "stop";

	// login
	public static final String ID_LABEL = "ID: ";
	public static final String CONFIRM_DIALOG_TITLE = "Confirmation";
	public static final String ADD_BUTTON_TEXT = "ADD";
	public static final String CLOSHA_EXIST_MSG = "Are you sure you want to quit the CLOSHA?";
	public static final String INIT_LOGIN_MESSAGE = "Starts the CLOSHA.";

	// local
	public static final String DELTE_TITLE = "Delete Data";
	public static final String DELTE_MSG = "Are you sure you want to delete\" + %s + \"  items ?";

	public static final String DEFAULT_MESSAGE = "Preparing to work.";
}
