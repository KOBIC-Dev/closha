package org.kobic.bioexpress.rcp.login.view;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.kobic.bioexpress.channel.client.ChannelClient;
import org.kobic.bioexpress.channel.client.ChannelClientImpl;
import org.kobic.bioexpress.channel.service.sso.MemberService;
import org.kobic.bioexpress.channel.service.sso.MemberServiceImpl;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.model.sso.UserAccount;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.osgi.service.event.EventHandler;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.events.HyperlinkEvent;

public class LoginView {

	final static Logger logger = Logger.getLogger(LoginView.class);

	@Inject
	private MDirtyable dirty;

	@SuppressWarnings("unused")
	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_PROPERTIES);

	protected Object result;
	protected Shell shell;

	@SuppressWarnings("unused")
	@Inject
	private UISynchronize sync;

	@SuppressWarnings("unused")
	@Inject
	private IEventBroker iEventBroker;

	private Text idText;
	private Text passwdText;
	private Combo combo;
	private Button cancelButton;
	private Button loginButton;
	private Label messageLabel;
//     private ImageHyperlink imageLink;
	private ImageHyperlink findIDPasswordLink;
	private ImageHyperlink registerLink;
	private ImageHyperlink bioexpressWebLink;

	private Member member;
	private boolean isAdmin;

	private Composite composite;
	private Composite idComposite;
	private Composite pwComposite;
	private Image bg;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private EPartService ePartService;

	public EventHandler startupHandler = new EventHandler() {
		@Override
		public void handleEvent(org.osgi.service.event.Event event) {

		}
	};

	@SuppressWarnings("unused")
	private EventHandler shutdownHandler = new EventHandler() {
		@Override
		public void handleEvent(org.osgi.service.event.Event event) {
			System.out.println("shutdown handler started");
		}
	};
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.getParent().getShell().setMinimized(false);
		parent.getParent().getShell().setMaximized(false);

		parent.setLayout(new GridLayout(1, false));

		GridData gd_composite = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_composite.heightHint = 630;
		gd_composite.widthHint = 430;

		bg = ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/login_bg_3.png");

		composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(gd_composite);
		composite.setBackgroundImage(bg);
		composite.setBounds(430, 630, 430, 630);

		idComposite = new Composite(composite, SWT.NONE);
		idComposite.setBounds(52, 225, 326, 46);
		idComposite.setBackground(SWTResourceManager.getColor(248, 248, 248));

		pwComposite = new Composite(composite, SWT.NONE);
		pwComposite.setBounds(52, 283, 326, 46);
		pwComposite.setBackground(SWTResourceManager.getColor(248, 248, 248));

		idText = new Text(idComposite, SWT.NONE);
		idText.setLocation(4, 12);
		idText.setSize(320, 30);
		idText.setBackground(SWTResourceManager.getColor(248, 248, 248));
		idText.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		idText.setMessage("ID");

		passwdText = new Text(pwComposite, SWT.PASSWORD);
		passwdText.setLocation(4, 12);
		passwdText.setSize(320, 30);
		passwdText.setBackground(SWTResourceManager.getColor(248, 248, 248));
		passwdText.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		passwdText.setMessage("Password");

		combo = new Combo(composite, SWT.READ_ONLY);
		combo.setItems(new String[] { Constants.CLOSHA, Constants.GBox });
		combo.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		combo.setBounds(52, 164, 326, 46);
		combo.select(0);
		combo.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));

		messageLabel = new Label(composite, SWT.NONE);
		messageLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		messageLabel.setBounds(52, 200, 326, 24);
		messageLabel.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		messageLabel.setForeground(SWTResourceManager.getColor(89, 89, 89));
		messageLabel.setText(Constants.INIT_LOGIN_MESSAGE);

		loginButton = new Button(composite, SWT.NONE);
		loginButton.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		loginButton.setBounds(52, 368, 326, 46);
		loginButton.setText("LOGIN");
		loginButton.setBackground(SWTResourceManager.getColor(7, 148, 252));
		loginButton.setForeground(SWTResourceManager.getColor(255, 255, 255));

		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		cancelButton.setBounds(52, 420, 326, 46);
		cancelButton.setText("CANCEL");
		cancelButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		cancelButton.setForeground(SWTResourceManager.getColor(7, 148, 252));

		findIDPasswordLink = formToolkit.createImageHyperlink(composite, SWT.NONE);
		findIDPasswordLink.setUnderlined(false);
		findIDPasswordLink.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/intro/findidpw.png"));
		findIDPasswordLink.setBounds(270, 335, 108, 18);
		formToolkit.paintBordersFor(findIDPasswordLink);

		registerLink = formToolkit.createImageHyperlink(composite, SWT.NONE);
		registerLink.setUnderlined(false);
		registerLink.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/intro/register.png"));
		registerLink.setBounds(191, 520, 51, 18);
		formToolkit.paintBordersFor(registerLink);

		bioexpressWebLink = formToolkit.createImageHyperlink(composite, SWT.NONE);
		bioexpressWebLink.setUnderlined(false);
		bioexpressWebLink.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/intro/gobxservice.png"));
		bioexpressWebLink.setBounds(135, 545, 161, 18);
		formToolkit.paintBordersFor(bioexpressWebLink);

		init();
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	public void event() {

		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

//                              if (combo.getText().equalsIgnoreCase(Constants.CLOSHA)) {
// 
//                                      bg = ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui",
//                                                     "imgs/intro/login_bg_closha.png");
// 
//                              } else if (combo.getText().equalsIgnoreCase(Constants.GBox)) {
//                                      bg = ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui",
//                                                     "imgs/intro/login_bg_gbox.png");
//                              }

				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
//                                              composite.setBackgroundImage(bg);
						composite.setBackgroundMode(SWT.INHERIT_DEFAULT);
					}
				});
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		cancelButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				shell.close();
				System.exit(-1);
			}
		});

		loginButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				doLogin();
			}
		});

		passwdText.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				// TODO Auto-generated method stub

				if (e.keyCode == Constants.ENTER_KEY_CODE) {
					doLogin();
				}
			}
		});

		findIDPasswordLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkActivated(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://kobic.re.kr/sso/go_find_id");
			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}
		});

		registerLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkActivated(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://kobic.re.kr/sso/go_agree");
			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}
		});

		bioexpressWebLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkActivated(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://www.kobic.re.kr/bioexpress/");
			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}
		});

//             imageLink.addHyperlinkListener(new IHyperlinkListener() {
//
//                     public void linkActivated(HyperlinkEvent e) {
//                            // TODO Auto-generated method stub
//                            Program.launch("https://www.kobic.re.kr/bioexpress/");
//                     }
//
//                     public void linkEntered(HyperlinkEvent e) {
//                            // TODO Auto-generated method stub
//                     }
//
//                     public void linkExited(HyperlinkEvent e) {
//                            // TODO Auto-generated method stub
//                     }
//             });

	}

	private void init() {
		bind();
		event();
	}

	private void bind() {

	}

	private void setLoginProcessMessage(final String message) {

		if (messageLabel != null && !messageLabel.isDisposed()) {

			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					messageLabel.setText(message);

					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void doLogin() {

		ChannelClient channelClient = new ChannelClientImpl();
		logger.info(channelClient.isAlive());

		String userID = idText.getText();
		String password = passwdText.getText();

		MemberService service = new MemberServiceImpl();

		UserAccount userAccount = new UserAccount();
		userAccount.setMemberId(userID);
		userAccount.setMemberPassword(password);

		member = service.loginAccount(userAccount);

		if (member != null) {

			setLoginProcessMessage("Attempts login with user information.");

			member = service.getMember(member.getMemberNo(), member.getMemberId());
			isAdmin = service.isAdmin(member.getMemberNo());

			setLoginProcessMessage(String.format("User(%s) login successfuly.", member.getMemberId()));

			Activator.setMember(member);
			Activator.setAdmin(isAdmin);
			Activator.setService(combo.getText());

			// add workflow editor
			WorkflowEditorPlugin.getDefault().setMember(member);
			WorkflowEditorPlugin.setAmdin(isAdmin);
			// iEventBroker.send(Constants.GBOX_DATA_LOAD_EVENT_BUS_NAME, new
			// UID().toString());

			logger.info(String.format("User(%s) login access time: %s", member.getMemberId(),
					Utils.getInstance().getDate()));

			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					IProgressMonitor progressMonitor = new NullProgressMonitor();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					IProject project = root.getProject("temp");

					try {
						if (!project.exists())
							project.create(progressMonitor);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						project.open(progressMonitor);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// shell.close();
				}
			});
			System.out.println("startup handler started");
			if (Activator.getService().equals(Constants.GBox)) {
				MPerspective element = (MPerspective) eModelService.find(Constants.GBOX_PERSPECTIVE_ID + ".<CLOSHA>",
						mApplication);
				if (element == null)
					element = (MPerspective) eModelService.find(Constants.GBOX_PERSPECTIVE_ID, mApplication);
				ePartService.switchPerspective(element);
			} else {
				MPerspective element = (MPerspective) eModelService.find(Constants.CLOSHA_PERSPECTIVE_ID + ".<CLOSHA>",
						mApplication);
				if (element == null)
					element = (MPerspective) eModelService.find(Constants.CLOSHA_PERSPECTIVE_ID, mApplication);
				ePartService.switchPerspective(element);
			}
			// iEventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
			// startupHandler);
			// iEventBroker.subscribe(UIEvents.UILifeCycle.APP_SHUTDOWN_STARTED,
			// shutdownHandler);
		} else {
			setLoginProcessMessage("It was wrong user id or password.");
		}
	}
}