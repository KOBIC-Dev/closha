package org.kobic.bioexpress.rcp.podman.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RegistPodmanImageDialogComponent {

	private Text imgNameText;
	private Group grpInformation;
	private Combo imgTypeCombo;
	private Text idText;
	private Text repoText;
	private Text tagText;
	private Combo statusCombo;
	private Text descriptionText;
	private Button btnOfficial;

	public RegistPodmanImageDialogComponent(Composite composite) {

		Composite container = new Composite(composite, SWT.NONE);

		Label imgNameLabel = new Label(container, SWT.NONE);
		imgNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		imgNameLabel.setText("Image Name:");

		imgNameText = new Text(container, SWT.BORDER);
		imgNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label emptyLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		emptyLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		grpInformation = new Group(container, SWT.NONE);
		grpInformation.setLayout(new GridLayout(3, false));
		grpInformation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpInformation.setText("Podman Image Information");

		Label typeLabel = new Label(grpInformation, SWT.NONE);
		typeLabel.setText("Type:");

		imgTypeCombo = new Combo(grpInformation, SWT.READ_ONLY);
		imgTypeCombo.setItems(new String[] { "Podmanfile (Podman contains instructions for building a image)",
				"Podman image tar archive file (Podman image save tar file)" });
		imgTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		imgTypeCombo.select(0);

		Label idLabel = new Label(grpInformation, SWT.NONE);
		idLabel.setText("ID:");

		idText = new Text(grpInformation, SWT.BORDER);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label repoLabel = new Label(grpInformation, SWT.NONE);
		repoLabel.setText("Repo:");

		repoText = new Text(grpInformation, SWT.BORDER);
		repoText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label tagLabel = new Label(grpInformation, SWT.NONE);
		tagLabel.setText("Tag:");

		tagText = new Text(grpInformation, SWT.BORDER);
		tagText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label statusLabel = new Label(grpInformation, SWT.NONE);
		statusLabel.setText("Status:");

		statusCombo = new Combo(grpInformation, SWT.READ_ONLY);
		statusCombo.setItems(new String[] { "Complete (Podman image validation complete)",
				"Development (Podman image during development)", "Test (Podman image under test)" });
		statusCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		statusCombo.select(0);

		btnOfficial = new Button(grpInformation, SWT.CHECK);
		btnOfficial.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnOfficial.setText("Officially registered podman image.");

		Group descriptionGroup = new Group(container, SWT.NONE);
		descriptionGroup.setText("Description");
		descriptionText = new Text(descriptionGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		GridLayout descriptionGroupGridLayout = new GridLayout(1, false);
		descriptionGroupGridLayout.marginTop = 5;
		descriptionGroup.setLayout(descriptionGroupGridLayout);

		GridData descriptionGropGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		descriptionGropGridData.grabExcessHorizontalSpace = true;
		descriptionGropGridData.grabExcessVerticalSpace = true;
		descriptionGroup.setLayoutData(descriptionGropGridData);

		GridData descriptionTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		descriptionTextGridData.grabExcessHorizontalSpace = true;
		descriptionTextGridData.grabExcessVerticalSpace = true;
		descriptionText.setLayoutData(descriptionTextGridData);

		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

	}

	public Text getImgNameText() {
		return imgNameText;
	}

	public Combo getImgTypeCombo() {
		return imgTypeCombo;
	}

	public Text getTagText() {
		return tagText;
	}

	public Text getDescriptionText() {
		return descriptionText;
	}

	public Text getIdText() {
		return idText;
	}

	public Text getRepoText() {
		return repoText;
	}

	public Combo getStatusCombo() {
		return statusCombo;
	}

	public Button getBtnOfficial() {
		return btnOfficial;
	}

}