package org.kobic.bioexpress.rcp.login.view;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.nebula.widgets.opal.commons.SWTGraphicUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
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

public class LoginDialog extends Dialog {

	final static Logger logger = Logger.getLogger(LoginDialog.class);

	protected Object result = null;
	protected Shell shell;

	private Text idText;
	private Text passwdText;
	private Button cancelButton;
	private Button loginButton;
	private Label messageLabel;
	private Hyperlink findIDPasswordLink;
	private Label registerLabel;
	private Hyperlink registerLink;
	private Hyperlink bioexpressWebLink;
	private ImageHyperlink bioexpressWebImgLink;

	private Member member;
	private boolean isAdmin;

	private Composite composite;
	private Composite idComposite;
	private Composite pwComposite;
	private Image bg;

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Composite bxLinkComposite;
	private Composite linkComposite;
	private Composite registComposite;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public LoginDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {

		shell = new Shell(getParent(), SWT.NONE);
		shell.setBackground(SWTResourceManager.getColor(255, 255, 255));
		shell.setSize(430, 630);
		shell.setText(getText());
		shell.getParent().getShell().setMinimized(false);
		shell.getParent().getShell().setMaximized(false);

		bg = ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/bx_login_bg_closha.png");

		int windows_sub_font_size = 9;
		int windows_sub2_font_size = 10;
		int windows_main_font_size = 11;
		int mac_sub_font_size = 13;
		int mac_sub2_font_size = 14;
		int mac_main_font_size = 15;

		int sub_font_size = windows_sub_font_size;
		int sub2_font_size = windows_sub2_font_size;
		int main_font_size = windows_main_font_size;

		if (SWTGraphicUtil.isMacOS() == true) {
			// 로그인 배경 이미지 수직으로 뒤집히는 현상 조치
			bg = ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/bx_login_bg_closha_flip_vertical.png");
			
			sub_font_size = mac_sub_font_size;
			sub2_font_size = mac_sub2_font_size;
			main_font_size = mac_main_font_size;
		}

		composite = new Composite(shell, SWT.NONE);
		composite.setBackgroundImage(bg);
		composite.setBounds(430, 630, 430, 630);

		messageLabel = new Label(composite, SWT.NONE);
		messageLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		messageLabel.setForeground(SWTResourceManager.getColor(89, 89, 89));
		messageLabel.setFont(SWTResourceManager.getFont("Arial", sub_font_size, SWT.NORMAL));
		messageLabel.setText(Constants.INIT_LOGIN_MESSAGE);

		idComposite = new Composite(composite, SWT.BORDER);
		idComposite.setBackground(SWTResourceManager.getColor(241, 245, 251));

		idText = new Text(idComposite, SWT.NONE);
		idText.setBackground(SWTResourceManager.getColor(241, 245, 251));
		idText.setForeground(SWTResourceManager.getColor(80, 90, 101));
		idText.setFont(SWTResourceManager.getFont("Arial", sub2_font_size, SWT.NORMAL));
		idText.setMessage("ID");

		pwComposite = new Composite(composite, SWT.BORDER);
		pwComposite.setBackground(SWTResourceManager.getColor(241, 245, 251));

		passwdText = new Text(pwComposite, SWT.PASSWORD);
		passwdText.setBackground(SWTResourceManager.getColor(241, 245, 251));
		passwdText.setForeground(SWTResourceManager.getColor(80, 90, 101));
		passwdText.setFont(SWTResourceManager.getFont("Arial", sub2_font_size, SWT.NORMAL));
		passwdText.setMessage("Password");

		findIDPasswordLink = formToolkit.createHyperlink(composite, "Forgot ID/Password?", SWT.NONE);
		findIDPasswordLink.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		findIDPasswordLink.setForeground(SWTResourceManager.getColor(45, 55, 72));
		findIDPasswordLink.setFont(SWTResourceManager.getFont("Arial", sub_font_size, SWT.NORMAL));
		findIDPasswordLink.setUnderlined(false);
		formToolkit.paintBordersFor(findIDPasswordLink);

		loginButton = new Button(composite, SWT.NONE);
		loginButton.setBackground(SWTResourceManager.getColor(7, 148, 252));
		loginButton.setForeground(SWTResourceManager.getColor(255, 255, 255));
		loginButton.setFont(SWTResourceManager.getFont("Arial", main_font_size, SWT.BOLD));
		loginButton.setText("Login");

		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBackground(SWTResourceManager.getColor(45, 55, 72));
		cancelButton.setForeground(SWTResourceManager.getColor(255, 255, 255));
		cancelButton.setFont(SWTResourceManager.getFont("Arial", main_font_size, SWT.BOLD));
		cancelButton.setText("Cancel");

		linkComposite = new Composite(composite, SWT.NONE);
		linkComposite.setForeground(SWTResourceManager.getColor(0, 0, 0));
		linkComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		formToolkit.adapt(linkComposite);
		formToolkit.paintBordersFor(linkComposite);

		registComposite = new Composite(linkComposite, SWT.NONE);
		registComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		registComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(registComposite);
		formToolkit.paintBordersFor(registComposite);

		registerLabel = new Label(registComposite, SWT.NONE);
		registerLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		registerLabel.setForeground(SWTResourceManager.getColor(89, 89, 89));
		registerLabel.setFont(SWTResourceManager.getFont("Arial", sub_font_size, SWT.NORMAL));
		registerLabel.setAlignment(SWT.RIGHT);
		registerLabel.setText("Not registered yet?");

		registerLink = formToolkit.createHyperlink(registComposite, "Create Account", SWT.NONE);
		registerLink.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		registerLink.setFont(SWTResourceManager.getFont("Arial", sub_font_size, SWT.NORMAL));
		registerLink.setUnderlined(true);
		formToolkit.paintBordersFor(registerLink);

		bxLinkComposite = new Composite(linkComposite, SWT.NONE);
		bxLinkComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		bxLinkComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		bxLinkComposite.setSize(164, 28);
		formToolkit.adapt(bxLinkComposite);
		formToolkit.paintBordersFor(bxLinkComposite);

		bioexpressWebLink = formToolkit.createHyperlink(bxLinkComposite, "Go to CLOSHA Service", SWT.NONE);
		bioexpressWebLink.setForeground(SWTResourceManager.getColor(89, 89, 89));
		bioexpressWebLink.setFont(SWTResourceManager.getFont("Arial", sub_font_size, SWT.NORMAL));
		bioexpressWebLink.setUnderlined(false);
		formToolkit.paintBordersFor(bioexpressWebLink);

		bioexpressWebImgLink = formToolkit.createImageHyperlink(bxLinkComposite, SWT.NONE);
		bioexpressWebImgLink.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		bioexpressWebImgLink.setUnderlined(false);
		bioexpressWebImgLink
				.setImage(ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "imgs/intro/icon.png"));
		formToolkit.paintBordersFor(bioexpressWebImgLink);

		GridData gd_composite = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_composite.heightHint = 630;
		gd_composite.widthHint = 430;

		FormData fd_messageLabel = new FormData();
		fd_messageLabel.right = new FormAttachment(idComposite, 0, SWT.RIGHT);
		fd_messageLabel.top = new FormAttachment(0, 168);
		fd_messageLabel.left = new FormAttachment(0, 46);

		FormData fd_idComposite = new FormData();
		fd_idComposite.top = new FormAttachment(messageLabel, 18);
		fd_idComposite.height = 46;
		fd_idComposite.left = new FormAttachment(0, 46);
		fd_idComposite.right = new FormAttachment(100, -46);

		FormData fd_idText = new FormData();
		fd_idText.left = new FormAttachment(0, 14);
		fd_idText.right = new FormAttachment(100, -14);
		fd_idText.top = new FormAttachment(0, 9);
		fd_idText.bottom = new FormAttachment(100);

		FormData fd_pwComposite = new FormData();
		fd_pwComposite.top = new FormAttachment(idComposite, 8);
		fd_pwComposite.height = 46;
		fd_pwComposite.left = new FormAttachment(0, 46);
		fd_pwComposite.right = new FormAttachment(100, -46);

		FormData fd_passwdText = new FormData();
		fd_passwdText.left = new FormAttachment(0, 14);
		fd_passwdText.right = new FormAttachment(100, -14);
		fd_passwdText.top = new FormAttachment(0, 9);
		fd_passwdText.bottom = new FormAttachment(100);

		FormData fd_findIDPasswordLink = new FormData();
		fd_findIDPasswordLink.top = new FormAttachment(pwComposite, 12);
		fd_findIDPasswordLink.right = new FormAttachment(100, -46);

		FormData fd_loginButton = new FormData();
		fd_loginButton.top = new FormAttachment(findIDPasswordLink, 28);
		fd_loginButton.height = 48;
		fd_loginButton.left = new FormAttachment(0, 46);
		fd_loginButton.right = new FormAttachment(100, -46);

		FormData fd_cancelButton = new FormData();
		fd_cancelButton.top = new FormAttachment(loginButton, 8);
		fd_cancelButton.height = 48;
		fd_cancelButton.left = new FormAttachment(0, 46);
		fd_cancelButton.right = new FormAttachment(100, -46);

		GridLayout gl_linkComposite = new GridLayout(1, false);
		gl_linkComposite.marginHeight = 0;
		gl_linkComposite.verticalSpacing = 10;

		FormData fd_linkComposite = new FormData();
		fd_linkComposite.top = new FormAttachment(cancelButton, 36);
		fd_linkComposite.left = new FormAttachment(0, 10);
		fd_linkComposite.right = new FormAttachment(100, -10);

		GridLayout gl_registComposite = new GridLayout(2, false);
		gl_registComposite.marginHeight = 0;

		GridLayout gl_bxLinkComposite = new GridLayout(2, false);
		gl_bxLinkComposite.marginWidth = 0;
		gl_bxLinkComposite.marginHeight = 0;

		shell.setLayout(new GridLayout(1, false));
		composite.setLayout(new FormLayout());
		composite.setLayoutData(gd_composite);
		
		idComposite.setLayout(new FormLayout());
		messageLabel.setLayoutData(fd_messageLabel);
		idComposite.setLayoutData(fd_idComposite);
		idText.setLayoutData(fd_idText);
		pwComposite.setLayoutData(fd_pwComposite);
		passwdText.setLayoutData(fd_passwdText);
		findIDPasswordLink.setLayoutData(fd_findIDPasswordLink);
		loginButton.setLayoutData(fd_loginButton);
		cancelButton.setLayoutData(fd_cancelButton);
		pwComposite.setLayout(new FormLayout());
		linkComposite.setLayout(gl_linkComposite);
		linkComposite.setLayoutData(fd_linkComposite);
		registComposite.setLayout(gl_registComposite);
		bxLinkComposite.setLayout(gl_bxLinkComposite);
		//
		setCenterLocation();
		//
		init();
	}

	@SuppressWarnings("unused")
	public void setCenterLocation() {
		Monitor monitor = Display.getCurrent().getPrimaryMonitor();

		Rectangle monitorClientArea = monitor.getClientArea();

		Rectangle monitorRect = monitor.getBounds();
		int x = (monitorRect.width / 2) - (430 / 2);// - window.width) + 100;
		int y = (monitorRect.height / 2) - (630 / 2);// - shellRect.height) + 100;
		shell.setLocation(x, y);
	}

	public void event() {

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
				Program.launch("https://kobic.re.kr/closha2/");
			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}
		});

		bioexpressWebImgLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkActivated(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://www.kobic.re.kr/closha2/");
			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub
			}
		});

		composite.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					shell.close();
					System.exit(-1);
				}
			}
		});

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
					result = "OK";
					shell.close();
				}
			});
		} else {
			setLoginProcessMessage("It was wrong user id or password.");
		}
	}
}
