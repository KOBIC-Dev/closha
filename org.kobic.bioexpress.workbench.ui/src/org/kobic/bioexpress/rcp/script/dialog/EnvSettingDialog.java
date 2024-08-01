package org.kobic.bioexpress.rcp.script.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClient;
import org.kobic.bioexpress.channel.client.podman.PodmanAPIClientImpl;
import org.kobic.bioexpress.model.podman.PodmanModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.script.provider.ScriptTableComboViewProvider;
import org.eclipse.swt.layout.GridLayout;

public class EnvSettingDialog extends TitleAreaDialog {

	@SuppressWarnings("unused")
	private Shell parent;

	private String name = null;
	private String podmanName = null;
	private String podmanRawID = null;

	private Label podmanImageLabel;
	private TableCombo combo;
	private TableComboViewer comboTableView;

	public EnvSettingDialog(Shell parent, String name, String podmanName) {
		// TODO Auto-generated constructor stub
		super(parent);
		this.parent = parent;
		this.name = name;
		this.podmanName = podmanName;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		init();
	}

	@Override
	public void create() {
		super.create();

		setTitle("Podman Setting");
		setMessage(Constants.EDIT_SCRIPT_ENV_DIALOG_TITLE_MESSAGE, IMessageProvider.INFORMATION);
		setTitleImage(
				ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PODMAN_REGISTER_DIALOG_TITLE_ICON));
		this.getShell().setText("Podman Setting");
		this.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite body = (Composite) super.createDialogArea(parent);
		setName("");
		GridLayout gl_body = new GridLayout(1, true);
		gl_body.marginWidth = 0;
		gl_body.marginHeight = 0;
		body.setLayout(gl_body);

		Composite container = new Composite(body, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.marginWidth = 11;
		gl_container.marginHeight = 13;
		container.setLayout(gl_container);

		podmanImageLabel = new Label(container, SWT.SHADOW_NONE);
		podmanImageLabel.setText("Select specified a podman container image for running user scripts.");

		combo = new TableCombo(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		combo.setEditable(false);
		combo.setEnabled(false);
		combo.setShowTableHeader(true);
		combo.setShowTableLines(true);
		combo.setTableWidthPercentage(100);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		comboTableView = new TableComboViewer(combo);
		comboTableView.setContentProvider(ArrayContentProvider.getInstance());
		comboTableView.setLabelProvider(new ScriptTableComboViewProvider());
		combo.setEnabled(true);
		comboTableView.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				PodmanModel t1 = (PodmanModel) e1;
				PodmanModel t2 = (PodmanModel) e2;
				return t1.getCreateDate().compareTo(t2.getCreateDate());
			};
		});

		comboTableView.getTableCombo().defineColumns(new String[] { "Name", "Tag", "Created" },
				new int[] { 250, 100, 100 });

		init();

		return body;
	}

	public void init() {
		event();
		bind();
	}

	public void event() {

		comboTableView.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

				if (!event.getSelection().isEmpty()) {
					PodmanModel podmanModel = (PodmanModel) event.getStructuredSelection().getFirstElement();
					System.out.println(podmanModel);
					podmanRawID = podmanModel.getRawID();
					setPodmanRawID(podmanRawID);
				}
			}
		});
	}

	public void bind() {

		PodmanAPIClient api = new PodmanAPIClientImpl();

		List<PodmanModel> podmanData = null;

		if (Activator.isAdmin()) {
			podmanData = api.getAllPodman();
		} else {
			podmanData = api.getOfficialPodman();
		}

		comboTableView.setInput(podmanData);

		String names[] = comboTableView.getTableCombo().getItems();

		for (int i = 0; i < names.length; i++) {

			System.out.println(names[i]);

			if (names[i].equalsIgnoreCase(podmanName)) {
				comboTableView.setSelection(new StructuredSelection(comboTableView.getElementAt(i)));
			}
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 350);
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
	}

	@Override
	protected void okPressed() {

		super.okPressed();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPodmanRawID() {
		return podmanRawID;
	}

	public void setPodmanRawID(String podmanRawID) {
		this.podmanRawID = podmanRawID;
	}

}
