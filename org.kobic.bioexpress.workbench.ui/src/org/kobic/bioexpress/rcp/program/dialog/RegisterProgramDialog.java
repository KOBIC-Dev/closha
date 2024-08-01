package org.kobic.bioexpress.rcp.program.dialog;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.regist.RegistClient;
import org.kobic.bioexpress.channel.client.regist.RegistClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.dialog.ViewPublicCategoryDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RegisterProgramDialog extends TitleAreaDialog {

	private ProgramDataModel programData;

	private Text programNameText;
	private Text categoryNameText;
	private Text categoryIdText;
	private Text versionText;
	private Text descriptionText;
	private Text urlText;
	private Text keywordText;
	private Text requestMsgText;

	private Button selectButton;

	private boolean isRegistComplete;

	public boolean isRegistComplete() {
		return isRegistComplete;
	}

	private String parentCategoryID;

	@Override
	public void create() {

		super.create();

		setTitle("Register Program");
		setMessage(Constants.REGISTER_PROGRAM_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_REGISTER_DIALOG_TITLE_ICON));

		this.getShell().setText("Register Program");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	public RegisterProgramDialog(Shell parentShell, ProgramDataModel programData) {
		super(parentShell);
		this.programData = programData;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);

		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label programNameLabel = new Label(container, SWT.NONE);
		programNameLabel.setText("Program Name:");

		programNameText = new Text(container, SWT.BORDER);
		programNameText.setEditable(true);
		programNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label separatorLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separatorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Group categoryGroup = new Group(composite, SWT.NONE);
		categoryGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		categoryGroup.setText("Category");
		categoryGroup.setLayout(new GridLayout(3, false));

		Label categoryNameLabel = new Label(categoryGroup, SWT.NONE);
		categoryNameLabel.setText("Name:");

		categoryNameText = new Text(categoryGroup, SWT.BORDER);
		categoryNameText.setEditable(false);
		categoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		selectButton = new Button(categoryGroup, SWT.NONE);
		GridData gd_selectButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_selectButton.heightHint = 23;
		selectButton.setLayoutData(gd_selectButton);
		selectButton.setText("Select...");

		Label categoryLabel = new Label(categoryGroup, SWT.NONE);
		categoryLabel.setText("ID:");

		categoryIdText = new Text(categoryGroup, SWT.BORDER);
		categoryIdText.setEditable(false);
		categoryIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Group basicGroup = new Group(composite, SWT.NONE);
		basicGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		basicGroup.setLayout(new GridLayout(2, false));
		basicGroup.setText("Basic Information");

		Label versionLabel = new Label(basicGroup, SWT.NONE);
		versionLabel.setText("Version:");

		versionText = new Text(basicGroup, SWT.BORDER);
		versionText.setEditable(false);
		versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label keywordLabel = new Label(basicGroup, SWT.NONE);
		keywordLabel.setText("Keyword:");

		keywordText = new Text(basicGroup, SWT.BORDER);
		keywordText.setEditable(false);
		keywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label urlLabel = new Label(basicGroup, SWT.NONE);
		urlLabel.setText("URL:");

		urlText = new Text(basicGroup, SWT.BORDER);
		urlText.setEditable(false);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group descriptionGroup = new Group(composite, SWT.NONE);
		descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout descriptionGroupGridLayout = new GridLayout(1, false);
		descriptionGroupGridLayout.marginTop = 5;
		descriptionGroup.setLayout(descriptionGroupGridLayout);
		descriptionGroup.setText("Description");

		descriptionText = new Text(descriptionGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label requestMsgLabel = new Label(container, SWT.NONE);
		requestMsgLabel.setText("Request Message:");
		new Label(container, SWT.NONE);

		requestMsgText = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		GridData requestMsgTextGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		requestMsgTextGridData.heightHint = 48;
		requestMsgText.setLayoutData(requestMsgTextGridData);

		init();

		return area;
	}

	public void init() {
		bind();
		event();
	}

	public void bind() {

		programNameText.setText(programData.getProgramName());
		keywordText.setText(programData.getKeyword());
		urlText.setText(programData.getUrl());
		versionText.setText(programData.getVersion());
		descriptionText.setText(programData.getProgramDesc());

	}

	public void event() {

		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ViewPublicCategoryDialog dialog = new ViewPublicCategoryDialog(getShell(), Constants.PROGRAM_LABEL);

				if (dialog.open() == Window.OK) {
					categoryNameText.setText(dialog.getCategoryName());
					categoryIdText.setText(dialog.getCategoryID());
					parentCategoryID = dialog.getParentID();
				}
			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Constants.REGISTER_LABLE, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {

		String programName = programNameText.getText();
		String subCategoryName = categoryNameText.getText();
		String subCategoryID = categoryIdText.getText();
		String message = requestMsgText.getText();
		String description = descriptionText.getText();
		String registrant = Activator.getMember().getMemberId();
		String rawID = programData.getRawID();

		if (programName.length() == 0) {

			setErrorMessage("Please enter a program name");
			programNameText.setFocus();

		} else if (subCategoryName.length() == 0 && subCategoryID.length() == 0) {

			setErrorMessage("Please select a category.");

		} else if (description.length() == 0) {

			setErrorMessage("Please enter a description.");
			descriptionText.setFocus();

		} else if (message.length() == 0) {

			setErrorMessage("Please write a program registration request message.");
			requestMsgText.setFocus();

		} else {

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

			try {
				progressDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {

						monitor.beginTask("Starting to request regist program.", IProgressMonitor.UNKNOWN);

						monitor.subTask("Connecting to database.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						monitor.subTask("Requesting program registration.");

						TimeUnit.SECONDS.sleep(Constants.DEFAULT_DELAY_TIME);

						CategoryClient categoryClient = new CategoryClientImpl();
						CategoryModel rootCategoryModel = categoryClient.getCategoryWithID(parentCategoryID);

						String rootID = rootCategoryModel.getCategoryID();
						String rootName = rootCategoryModel.getCategoryName();
						String subID = subCategoryID;
						String subName = subCategoryName;

						RegistClient registClient = new RegistClientImpl();
						isRegistComplete = registClient.requestRegistProgram(rootID, rootName, subID, subName,
								registrant, message, rawID, programName, description);

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

			super.okPressed();
		}
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(456, 659);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public Text getCategoryNameText() {
		return categoryNameText;
	}

	public Text getCategoryIdText() {
		return categoryIdText;
	}

}
