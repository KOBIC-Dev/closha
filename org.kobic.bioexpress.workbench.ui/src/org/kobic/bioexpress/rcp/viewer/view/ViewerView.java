package org.kobic.bioexpress.rcp.viewer.view;

import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefFocusHandlerAdapter;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.kobic.bioexpress.channel.client.task.TaskClient;
import org.kobic.bioexpress.channel.client.task.TaskClientImpl;
import org.kobic.bioexpress.model.task.ScriptTaskModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.JCefUtil;

import gov.sandia.dart.common.core.env.OS;

public class ViewerView {

	final static Logger logger = Logger.getLogger(ViewerView.class);

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
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private MPartStack mPartStack;

	@Inject
	@Optional
	private IWorkbench iworkbench;

	@SuppressWarnings("unused")
	@Inject
	private ESelectionService selectionService;

	private CefClient client_;
	private CefBrowser cbrowser;
	private boolean browserFocus_ = true;
	private String webURL = "";
	
	private Browser ibrowser;

	private Composite composite;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		webURL = "";

		composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		init();
		//
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {

		setTableViewerDataBind();
	}

	@SuppressWarnings("unused")
	private void setTableViewerDataBind() {

		String memberID = Activator.getMember().getMemberId();

		TaskClient client = new TaskClientImpl();
		List<ScriptTaskModel> list = client.getScriptTaskByMember(memberID);

	}

	private void event() {

	}

	@Inject
	@Optional
	private void wandiscoReloadsubscribeTopicTodoUpdated(
			@UIEventTopic(Constants.SCRIPT_TASK_HISTORY_REFRESH_EVENT_BUS_NAME) String uid) {

		setTableViewerDataBind();

		logger.info(uid + " : script task history view refresh");
	}

	private void initCef(Composite comp) {

		CefApp cefApp_ = JCefUtil.getCefApp();

		client_ = cefApp_.createClient();

		CefMessageRouter msgRouter = CefMessageRouter.create();
		client_.addMessageRouter(msgRouter);

		client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
			@Override
			public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
				// address_.setText(url);
			}
		});

		// Clear focus from the address field when the browser gains focus.
		client_.addFocusHandler(new CefFocusHandlerAdapter() {
			@Override
			public void onGotFocus(CefBrowser browser) {
				if (browserFocus_)
					return;
				browserFocus_ = true;
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
				browser.setFocus(true);
			}

			@Override
			public void onTakeFocus(CefBrowser browser, boolean next) {
				browserFocus_ = false;
			}
		});
		// new Thread().
		System.setProperty("java.awt.headless", "false");
		cbrowser = client_.createBrowser(webURL, false, false);
		Frame frame = SWT_AWT.new_Frame(comp);
		frame.add(cbrowser.getUIComponent());
		frame.pack();
	}
	
	private void initBrowser(Composite comp) {
		ibrowser = new Browser(comp, SWT.NONE);
		ibrowser.addTitleListener(new TitleListener() {
			
			@Override
			public void changed(TitleEvent event) {
				// TODO Auto-generated method stub
				
			}
		});

		ibrowser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void changed(ProgressEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		//String webURL = "";//props.getProperty("closha.workbench.url");
		ibrowser.setUrl(webURL);
	}

	@Focus
	public void setFocus() {
		if(OS.isWindows()) {
			if (cbrowser == null) {
				initCef(composite);
			}
		}else {
			if(ibrowser == null) {
				initBrowser(composite);
			}
		}
		composite.setFocus();
	}

	public void loadURL(String path) {
		//
		if(OS.isWindows()) {
			cbrowser.loadURL(path);
		}else {
			ibrowser.setUrl(path);
		}
	}
}
