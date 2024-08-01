package org.kobic.bioexpress.rcp.program.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent2;
import org.kobic.bioexpress.rcp.script.dialog.ViewScriptDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;

public class NewProgramWizardPage2 extends WizardPage {

	private boolean isMulti = false;
	private ProgramWizardComponent2 programComponent2;
	private Map<String, ProgramDataModel> map;
	private List<Text> textList;

	public NewProgramWizardPage2() {

		super("wizardPage");

		setTitle("Create New Analysis Program");
		setDescription("Setting Program Execution Information");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Constants.SYMBOLIC_NAME,
				Constants.PROGRAM_NEW_DIALOG_TITLE_ICON));

		map = new HashMap<String, ProgramDataModel>();
	}

	@Override
	public void createControl(Composite parent) {

		programComponent2 = new ProgramWizardComponent2();

		Composite executeComposite = programComponent2.getProgramWizardComponent2(parent);

		setControl(executeComposite);
		setPageComplete(false);

		textList = new ArrayList<Text>();
		textList.add(programComponent2.scriptPathText);

		init();
	}

	private void init() {
		bind();
		event();
	}

	private void bind() {

		programComponent2.scriptTypeCombo.select(0);

		ProgramClient programClient = new ProgramClientImpl();
		List<ProgramDataModel> programs = programClient.getPublicProgram();

		programComponent2.treeViewer.setInput(programs);

	}

	public void event() {

		programComponent2.selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ViewScriptDialog dialog = new ViewScriptDialog(getShell());
				if (dialog.open() == Window.OK) {
					programComponent2.scriptPathText.setText(dialog.getScriptPath());
				}
			}
		});

		programComponent2.singleCoreRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isMulti = false;
			}
		});

		programComponent2.multiCoreRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isMulti = true;
			}
		});

		programComponent2.treeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				TreeItem item = (TreeItem) e.item;

				Object obj = item.getData();

				if (e.detail == SWT.CHECK) {

					if (obj instanceof ProgramDataModel) {

						ProgramDataModel programData = (ProgramDataModel) obj;

						if (item.getChecked()) {
							System.out.println("+++++" + obj);
							map.put(programData.getRawID(), programData);
						} else {
							System.out.println("-----" + obj);
							map.remove(programData.getRawID());
						}
					} else {
						item.setChecked(false);
						MessageDialog.openError(getShell(), "Select Error", "Only program units can be selected.");
					}
				}
			}
		});

		programComponent2.treeViewer.getTree().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub

				if (getScriptPath().length() == 0) {

					setMessage("Please select a script Path");
					setPageComplete(false);

				} else {

					setMessage(null);
					setPageComplete(true);
				}
			}
		};

		programComponent2.scriptPathText.addListener(SWT.Modify, listener);

	}

	public String getScriptPath() {
		return programComponent2.scriptPathText.getText();
	}

	public String getScriptType() {
		return programComponent2.scriptTypeCombo.getText();
	}

	public boolean getIsMulti() {
		return isMulti;
	}

	public TreeViewer getTreeViewer() {
		return programComponent2.treeViewer;
	}

	public ProgramWizardComponent2 getProgramComponent2() {
		return programComponent2;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.programComponent2.treeViewer = treeViewer;
	}

	public Map<String, ProgramDataModel> getMap() {
		return map;
	}

	public void setMap(Map<String, ProgramDataModel> map) {
		this.map = map;
	}

}