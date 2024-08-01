package org.kobic.bioexpress.rcp.script.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
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

public class NewScriptDialog extends InputDialog {

	private String name = null;
	private String podmanRawID = null;

	private Label podmanImageLabel;
	private TableCombo combo;
	private TableComboViewer comboTableView;

	public NewScriptDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
			IInputValidator validator) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);

		newShell.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite body = (Composite) super.createDialogArea(parent);
		setName("");

		podmanImageLabel = new Label(body, SWT.NONE);
		podmanImageLabel.setText("Select specified a podman container image for running user scripts.");

		combo = new TableCombo(body, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
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

			if (names[i].equalsIgnoreCase(Constants.PODMAN_BASE_IMAGE_NAME)) {
				comboTableView.setSelection(new StructuredSelection(comboTableView.getElementAt(i)));
			}
		}
	}

	@Override
	protected void okPressed() {
		name = this.getText().getText();
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
