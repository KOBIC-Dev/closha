package org.kobic.bioexpress.rcp.gbox.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class SpeedLimitDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Composite container;
	private FormLayout fl_container;
	private Label imgLabel;
	private Button enableSpeedLimitsButton;
	private Label downloadSpeedLimitLabel;
	private Label uploadSpeedLimitLabel;
	private Text downloadSpeedText;
	private Text uploadSpeedText;
	private Label downloadUnitLabel;
	private Label uploadUnitLabel;

	public SpeedLimitDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent.getShell().setText("GBox Transfer Speed Limits");

		container = (Composite) super.createDialogArea(parent);

		fl_container = new FormLayout();
		fl_container.marginBottom = 10;

		imgLabel = formToolkit.createLabel(container, "", SWT.NONE);
		imgLabel.setBackground(null);
		imgLabel.setImage(
				ResourceManager.getPluginImage("org.kobic.bioexpress.workbench.ui", "icons/gauge_dialog_48_a.png"));

		enableSpeedLimitsButton = new Button(container, SWT.CHECK);
		enableSpeedLimitsButton.setText("Enable speed limits");
		enableSpeedLimitsButton.setSelection(Activator.isEnableLimit());

		downloadSpeedLimitLabel = new Label(container, SWT.NONE);
		downloadSpeedLimitLabel.setText("Download speed limit:");

		uploadSpeedLimitLabel = new Label(container, SWT.NONE);
		uploadSpeedLimitLabel.setText("Upload speed limit:");

		downloadSpeedText = new Text(container, SWT.BORDER);
		downloadSpeedText.setText(Constants.GBOX_MIN_SPEED_VALUE);
		downloadSpeedText.setEnabled(false);

		uploadSpeedText = new Text(container, SWT.BORDER);
		uploadSpeedText.setText(Constants.GBOX_MIN_SPEED_VALUE);
		uploadSpeedText.setEnabled(false);

		downloadUnitLabel = new Label(container, SWT.NONE);
		downloadUnitLabel.setText("MB/s");

		uploadUnitLabel = new Label(container, SWT.NONE);
		uploadUnitLabel.setText("MB/s");

		FormData fd_imgLabel = new FormData();
		fd_imgLabel.top = new FormAttachment(0, 31);
		fd_imgLabel.left = new FormAttachment(0, 15);

		FormData fd_enableSpeedLimitsButton = new FormData();
		fd_enableSpeedLimitsButton.left = new FormAttachment(imgLabel, 20);
		fd_enableSpeedLimitsButton.top = new FormAttachment(0, 20);

		FormData fd_downloadSpeedLimitLabel = new FormData();
		fd_downloadSpeedLimitLabel.top = new FormAttachment(enableSpeedLimitsButton, 14);
		fd_downloadSpeedLimitLabel.left = new FormAttachment(enableSpeedLimitsButton, 0, SWT.LEFT);

		FormData fd_uploadSpeedLimitLabel = new FormData();
		fd_uploadSpeedLimitLabel.top = new FormAttachment(downloadSpeedLimitLabel, 16);
		fd_uploadSpeedLimitLabel.left = new FormAttachment(enableSpeedLimitsButton, 0, SWT.LEFT);

		FormData fd_downloadSpeedText = new FormData();
		fd_downloadSpeedText.width = 90;
		fd_downloadSpeedText.left = new FormAttachment(downloadSpeedLimitLabel, 10);
		fd_downloadSpeedText.top = new FormAttachment(downloadSpeedLimitLabel, -3, SWT.TOP);

		FormData fd_uploadSpeedText = new FormData();
		fd_uploadSpeedText.width = 90;
		fd_uploadSpeedText.left = new FormAttachment(downloadSpeedText, 0, SWT.LEFT);
		fd_uploadSpeedText.right = new FormAttachment(downloadSpeedText, 0, SWT.RIGHT);
		fd_uploadSpeedText.top = new FormAttachment(uploadSpeedLimitLabel, 0, SWT.TOP);
		fd_downloadSpeedText.right = new FormAttachment(downloadUnitLabel, -6);

		FormData fd_downloadUnitLabel = new FormData();
		fd_downloadUnitLabel.top = new FormAttachment(downloadSpeedLimitLabel, 0, SWT.TOP);
		fd_downloadUnitLabel.right = new FormAttachment(100, -15);

		FormData fd_uploadUnitLabel = new FormData();
		fd_uploadUnitLabel.top = new FormAttachment(uploadSpeedLimitLabel, 0, SWT.TOP);
		fd_uploadUnitLabel.right = new FormAttachment(downloadUnitLabel, 0, SWT.RIGHT);

		container.setLayout(fl_container);
		imgLabel.setLayoutData(fd_imgLabel);
		enableSpeedLimitsButton.setLayoutData(fd_enableSpeedLimitsButton);
		downloadSpeedLimitLabel.setLayoutData(fd_downloadSpeedLimitLabel);
		uploadSpeedLimitLabel.setLayoutData(fd_uploadSpeedLimitLabel);
		downloadSpeedText.setLayoutData(fd_downloadSpeedText);
		uploadSpeedText.setLayoutData(fd_uploadSpeedText);
		downloadUnitLabel.setLayoutData(fd_downloadUnitLabel);
		uploadUnitLabel.setLayoutData(fd_uploadUnitLabel);

		init();

		return container;
	}

	public void init() {
		bind();
		event();
	}

	private void bind() {
		if (enableSpeedLimitsButton.getSelection()) {
			downloadSpeedText.setText(Constants.GBOX_MIN_SPEED_VALUE);
			uploadSpeedText.setText(Constants.GBOX_MIN_SPEED_VALUE);
		}
	}

	private void event() {

		enableSpeedLimitsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (enableSpeedLimitsButton.getSelection()) {
					downloadSpeedText.setText(Constants.GBOX_MIN_SPEED_VALUE);
					uploadSpeedText.setText(Constants.GBOX_MIN_SPEED_VALUE);
				} else {
					downloadSpeedText.setText(Constants.GBOX_MAX_SPEED_VALUE);
					uploadSpeedText.setText(Constants.GBOX_MAX_SPEED_VALUE);
				}
			}
		});
	}

	@Override
	protected void okPressed() {

//		if (enableSpeedLimitsButton.getSelection()) {
//			Activator.setEnableLimit(enableSpeedLimitsButton.getSelection());
//			Activator.setDOWN_MAX_VALUE(Integer.parseInt(downloadSpeedText.getText()));
//			Activator.setUP_MAX_VALUE(Integer.parseInt(uploadSpeedText.getText()));
//		}
		
		Activator.setEnableLimit(enableSpeedLimitsButton.getSelection());
		Activator.setDOWN_MAX_VALUE(Integer.parseInt(downloadSpeedText.getText()));
		Activator.setUP_MAX_VALUE(Integer.parseInt(uploadSpeedText.getText()));

		super.okPressed();
	}
}