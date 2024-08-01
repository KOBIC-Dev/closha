package org.kobic.bioexpress.rcp.program.wizard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.category.dialog.ViewCategoryDialog;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.program.component.ProgramWizardComponent1;
import org.kobic.bioexpress.rcp.utils.ValidationUtils;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewProgramWizardPage1 extends WizardPage {

	private Text programNameText;
	private List<Text> textList;
	private Map<String, Text> textMap;

	private ProgramWizardComponent1 programComponent1;
	private CategoryModel categoryModel;

	private List<String> programName;

	private String programNameTextValue;

	public NewProgramWizardPage1(CategoryModel categoryModel) {

		super("wizardPage");
		setTitle("Create New Analysis Program");
		setMessage("Setting Program Basic Information");
		
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Constants.SYMBOLIC_NAME, Constants.PROGRAM_NEW_DIALOG_TITLE_ICON));
		
		this.categoryModel = categoryModel;

	}

	@Override
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);

		container.setLayout(new GridLayout(2, false));

		Label programNameLabel = new Label(container, SWT.NONE);
		programNameLabel.setText("Program Name:");

		programNameText = new Text(container, SWT.BORDER);
		programNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		programNameText.setToolTipText(Constants.NAMING_RULE);
		programNameText.setData("Program Name");

		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		programComponent1 = new ProgramWizardComponent1();
		programComponent1.getProgramWizardComponent1(container);

		programComponent1.subCategoryNameText.setText(categoryModel.getCategoryName());
		programComponent1.subCategoryIdText.setText(categoryModel.getCategoryID());

		setControl(container);

		textMap = new LinkedHashMap<String, Text>();
		textMap.put("program name", programNameText);
		textMap.put("category name", programComponent1.subCategoryNameText);
		textMap.put("version", programComponent1.versionText);
		textMap.put("keyword", programComponent1.keywordText);
		textMap.put("URL", programComponent1.urlText);
		textMap.put("description", programComponent1.descriptionText);

		setPageComplete(false);

		init();

	}

	private void init() {

		String categoryID = categoryModel.getCategoryID();
		String memberID = Activator.getMember().getMemberId();

		ProgramClient programClient = new ProgramClientImpl();
		List<ProgramDataModel> programs = programClient.getProgramDataList(categoryID, memberID);

		programName = new ArrayList<String>();

		programs.forEach(program -> programName.add(program.getProgramName()));

		bind();
		event();
	}

	private void bind() {
	}

	private void event() {

		programComponent1.selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ViewCategoryDialog dialog = new ViewCategoryDialog(getShell(), Constants.PROGRAM_LABEL);

				if (dialog.open() == Window.OK) {
					programComponent1.subCategoryIdText.setText(dialog.getCategoryID());
				}
			}
		});

		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub

				programNameTextValue = programNameText.getText();

				if (programName.contains(programNameTextValue)) {

					setErrorMessage("Program name already exists.");
					setPageComplete(false);

				} else if (!ValidationUtils.getInstance().isNameVaildation(programNameTextValue)) {

					setErrorMessage(Constants.NAMING_RULE);
					setPageComplete(false);

				} else if (checkWizardTextEmptyMsg(textMap).length() != 0) {

					setErrorMessage(null);
					setMessage(checkWizardTextEmptyMsg(textMap));
					setPageComplete(false);

				} else {

					setErrorMessage(null);
					setPageComplete(true);
				}

			}
		};

		programNameText.addListener(SWT.Modify, listener);
		programComponent1.subCategoryNameText.addListener(SWT.Modify, listener);
		programComponent1.keywordText.addListener(SWT.Modify, listener);
		programComponent1.urlText.addListener(SWT.Modify, listener);
		programComponent1.descriptionText.addListener(SWT.Modify, listener);

	}

	public boolean checkWizardTextEmpty(List<Text> list) {

		boolean isEmpty = true;

		for (Text text : textList) {

			if (text.getText().length() == 0) {

				isEmpty = true;

				break;

			} else {

				isEmpty = false;
			}
		}

		return isEmpty;
	}


	public String checkWizardTextEmptyMsg(Map<String, Text> map) {
		
		String msg = "";
		
	    for( String key : map.keySet() ){
	    	
	    	Text text = (Text)map.get(key);
	    	
	    	if (text.getText().length() == 0) {
	    		
	    		msg = "Please enter a " + key;
	    		
	    		break;
	    		
	    	}
	    	
	    }

		return msg;

	}

	public String getProgramName() {
		return programNameText.getText();
	}

	public String getSubCategoryID() {
		return programComponent1.subCategoryIdText.getText();
	}

	public String getVersion() {
		return programComponent1.versionText.getText();
	}

	public String getDescription() {
		return programComponent1.descriptionText.getText();
	}

	public String getKeyword() {
		return programComponent1.keywordText.getText();
	}

	public String getSubCategoryName() {
		return programComponent1.subCategoryNameText.getText();
	}

	public String getUrl() {
		return programComponent1.urlText.getText();
	}

}