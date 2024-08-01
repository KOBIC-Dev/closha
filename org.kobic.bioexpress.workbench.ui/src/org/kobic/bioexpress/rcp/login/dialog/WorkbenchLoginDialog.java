package org.kobic.bioexpress.rcp.login.dialog;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
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
import org.kobic.bioexpress.rcp.swt.Util;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;

public class WorkbenchLoginDialog extends Dialog {

	final static Logger logger = Logger.getLogger(WorkbenchLoginDialog.class);

	protected Object result;
	protected Shell shell;

	@SuppressWarnings("unused")
	private MApplication mApplication;

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
	private Link bioexpressWebLink;
	private Link findIDPasswordLink;
	private Link registerLink;
	private Label messageLabel;

	private Member member;
	private boolean isAdmin;
	private Label verticallabel;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */

	public WorkbenchLoginDialog(Shell parent, int style) {
		super(parent, style);
		setText("");
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
		shell.setBackgroundMode(SWT.INHERIT_FORCE);

		final Label IdLabel = new Label(shell, SWT.NONE);
		IdLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		IdLabel.setText(Constants.ID_LABEL);

		idText = new Text(shell, SWT.BORDER);
		idText.setBackground(SWTResourceManager.getColor(245, 245, 245));

		final Label passwordLabel = new Label(shell, SWT.NONE);
		passwordLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		passwordLabel.setText("Password :");

		passwdText = new Text(shell, SWT.PASSWORD | SWT.BORDER);
		passwdText.setBackground(SWTResourceManager.getColor(245, 245, 245));
		
		combo = new Combo(shell, SWT.READ_ONLY);
		combo.setBackground(SWTResourceManager.getColor(245, 245, 245));
		combo.setItems(new String[] {Constants.CLOSHA, Constants.GBox});
		combo.select(0);

		final Label horizontalLabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		horizontalLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		horizontalLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));

		cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		cancelButton.setBackground(SWTResourceManager.getColor(11, 193, 246));

		loginButton = new Button(shell, SWT.NONE);
		loginButton.setText("Login");
		loginButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		loginButton.setBackground(SWTResourceManager.getColor(11, 193, 246));

		bioexpressWebLink = new Link(shell, SWT.NONE);
		bioexpressWebLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		bioexpressWebLink.setLinkForeground(SWTResourceManager.getColor(245, 245, 245));
		bioexpressWebLink.setText("<a>Go Bio-Express Service</a>");

		findIDPasswordLink = new Link(shell, SWT.NONE);
		findIDPasswordLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		findIDPasswordLink.setLinkForeground(SWTResourceManager.getColor(245, 245, 245));
		findIDPasswordLink.setText("<a>Find ID/Password</a>");

		verticallabel = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		verticallabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		verticallabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));

		registerLink = new Link(shell, 0);
		registerLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		registerLink.setText("<a>Register</a>");
		registerLink.setLinkForeground(SWTResourceManager.getColor(245, 245, 245));

		messageLabel = new Label(shell, SWT.NONE);
		messageLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		messageLabel.setText(Constants.INIT_LOGIN_MESSAGE);

		FormData fdIdLabel = new FormData();
		fdIdLabel.top = new FormAttachment(idText, 3, SWT.TOP);
		fdIdLabel.right = new FormAttachment(idText, -6);

		FormData fdIdText = new FormData();
		fdIdText.top = new FormAttachment(0, 77);
		fdIdText.right = new FormAttachment(100, -10);
		fdIdText.left = new FormAttachment(0, 82);

		FormData fdPasswordLabel = new FormData();
		fdPasswordLabel.top = new FormAttachment(passwdText, 3, SWT.TOP);
		fdPasswordLabel.right = new FormAttachment(passwdText, -6);

		FormData fdPasswdText = new FormData();
		fdPasswdText.top = new FormAttachment(idText, 6);
		fdPasswdText.left = new FormAttachment(idText, 0, SWT.LEFT);
		fdPasswdText.right = new FormAttachment(100, -10);

		FormData fdCombo = new FormData();
		fdCombo.left = new FormAttachment(100, -122);
		fdCombo.top = new FormAttachment(passwdText, 6);
		fdCombo.right = new FormAttachment(100, -10);

		FormData fdTitleLabel = new FormData();
		fdTitleLabel.right = new FormAttachment(idText, 10, SWT.RIGHT);
		fdTitleLabel.bottom = new FormAttachment(0, 53);
		fdTitleLabel.top = new FormAttachment(0);
		fdTitleLabel.left = new FormAttachment(0);

		FormData fdBtnLogin = new FormData();
		fdBtnLogin.left = new FormAttachment(cancelButton, -98, SWT.LEFT);
		fdBtnLogin.bottom = new FormAttachment(100, -10);
		fdBtnLogin.right = new FormAttachment(cancelButton, -6);

		FormData fdBtnCancel = new FormData();
		fdBtnCancel.bottom = new FormAttachment(100, -10);
		fdBtnCancel.right = new FormAttachment(100, -10);
		fdBtnCancel.left = new FormAttachment(100, -102);

		FormData fdBiolink = new FormData();
		fdBiolink.bottom = new FormAttachment(100, -10);
		fdBiolink.left = new FormAttachment(0, 10);

		FormData fdHorizontalLabel = new FormData();
		fdHorizontalLabel.left = new FormAttachment(0, 13);
		fdHorizontalLabel.right = new FormAttachment(100, -7);
		fdHorizontalLabel.top = new FormAttachment(passwdText, 36);

		FormData fdfindIdPwdlink = new FormData();
		fdfindIdPwdlink.bottom = new FormAttachment(bioexpressWebLink, -6);
		fdfindIdPwdlink.left = new FormAttachment(0, 10);

		FormData fdverticalLabel = new FormData();
		fdverticalLabel.bottom = new FormAttachment(bioexpressWebLink, -6);
		fdverticalLabel.top = new FormAttachment(findIDPasswordLink, 0, SWT.TOP);
		fdverticalLabel.right = new FormAttachment(findIDPasswordLink, 8, SWT.RIGHT);
		fdverticalLabel.left = new FormAttachment(findIDPasswordLink, 6);

		FormData fdregisterLink = new FormData();
		fdregisterLink.top = new FormAttachment(findIDPasswordLink, 0, SWT.TOP);
		fdregisterLink.left = new FormAttachment(verticallabel, 6);

		FormData fdMessageLabel = new FormData();
		fdMessageLabel.left = new FormAttachment(0, 13);
		fdMessageLabel.top = new FormAttachment(horizontalLabel, 6);

		IdLabel.setLayoutData(fdIdLabel);
		passwordLabel.setLayoutData(fdPasswordLabel);
		horizontalLabel.setLayoutData(fdHorizontalLabel);
		idText.setLayoutData(fdIdText);
		passwdText.setLayoutData(fdPasswdText);
		combo.setLayoutData(fdCombo);
		cancelButton.setLayoutData(fdBtnCancel);
		loginButton.setLayoutData(fdBtnLogin);
		bioexpressWebLink.setLayoutData(fdBiolink);
		findIDPasswordLink.setLayoutData(fdfindIdPwdlink);
		verticallabel.setLayoutData(fdverticalLabel);
		registerLink.setLayoutData(fdregisterLink);
		messageLabel.setLayoutData(fdMessageLabel);

		shell.setLayout(new FormLayout());
		shell.setBackgroundImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.LOGIN_BACKGROUND_IMAGE));
		shell.setSize(420, 280);
		shell.setLocation(Util.getInstance().getMonitorCenter(shell));

		FontDescriptor descriptor1 = FontDescriptor.createFrom(IdLabel.getFont());
		descriptor1 = descriptor1.setStyle(SWT.BOLD);
		IdLabel.setFont(descriptor1.createFont(IdLabel.getDisplay()));

		FontDescriptor descriptor2 = FontDescriptor.createFrom(passwordLabel.getFont());
		descriptor2 = descriptor2.setStyle(SWT.BOLD);
		passwordLabel.setFont(descriptor2.createFont(passwordLabel.getDisplay()));

		FontDescriptor descriptor3 = FontDescriptor.createFrom(findIDPasswordLink.getFont());
		descriptor3 = descriptor3.setStyle(SWT.BOLD);
		findIDPasswordLink.setFont(descriptor3.createFont(findIDPasswordLink.getDisplay()));

		FontDescriptor descriptor4 = FontDescriptor.createFrom(bioexpressWebLink.getFont());
		descriptor4 = descriptor4.setStyle(SWT.BOLD);
		bioexpressWebLink.setFont(descriptor4.createFont(bioexpressWebLink.getDisplay()));

		FontDescriptor descriptor5 = FontDescriptor.createFrom(registerLink.getFont());
		descriptor5 = descriptor5.setStyle(SWT.BOLD);
		registerLink.setFont(descriptor5.createFont(registerLink.getDisplay()));

		FontDescriptor descriptor6 = FontDescriptor.createFrom(messageLabel.getFont());
		descriptor6 = descriptor6.setStyle(SWT.BOLD);
		messageLabel.setFont(descriptor6.createFont(messageLabel.getDisplay()));

		FontDescriptor descriptor7 = FontDescriptor.createFrom(loginButton.getFont());
		descriptor7 = descriptor7.setStyle(SWT.BOLD);
		loginButton.setFont(descriptor7.createFont(loginButton.getDisplay()));

		FontDescriptor descriptor8 = FontDescriptor.createFrom(cancelButton.getFont());
		descriptor8 = descriptor8.setStyle(SWT.BOLD);
		cancelButton.setFont(descriptor8.createFont(cancelButton.getDisplay()));

		init();
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

		bioexpressWebLink.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://www.kobic.re.kr/closha2/");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		findIDPasswordLink.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://kobic.re.kr/sso/go_find_id");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		registerLink.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Program.launch("https://kobic.re.kr/sso/go_agree");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
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
					messageLabel.setSize(300, 100);
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
					shell.close();
				}
			});
		} else {
			setLoginProcessMessage("It was wrong user id or password.");
		}
	}
}