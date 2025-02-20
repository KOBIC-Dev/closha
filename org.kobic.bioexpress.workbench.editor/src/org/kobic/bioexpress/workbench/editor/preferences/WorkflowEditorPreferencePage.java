/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.strikewire.snl.apc.GUIs.DefaultFieldEditorPreferencePage;

import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;

public class WorkflowEditorPreferencePage extends DefaultFieldEditorPreferencePage implements
		IWorkbenchPreferencePage {


	public WorkflowEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(WorkflowEditorPlugin.getDefault().getPreferenceStore());
		setDescription("Preferences for the Bioexpress Workbench Editor");
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing
	}

	@Override
	protected void createFieldEditors() {
		 addField(new FontFieldEditor(IWorkflowEditorPreferences.FONT,
                 "Bioexpress diagram font",
                 getFieldEditorParent())); 	
		 addField(new FontFieldEditor(IWorkflowEditorPreferences.NOTES_FONT,
                 "Bioexpress notes font",
                 getFieldEditorParent())); 		
		 addField(new FontFieldEditor(IWorkflowEditorPreferences.EDITOR_FONT,
                 "Bioexpress settings view text editor font",
                 getFieldEditorParent())); 		

		 addField(new DirectoryFieldEditor(IWorkflowEditorPreferences.PALETTE_FILE_DIR,
                 "Location for user-defined component file",
                 getFieldEditorParent())); 		
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.MANHATTAN_CONNECTIONS,
                 "Use 'Manhattan-style' connectors",
                 getFieldEditorParent())); 		
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.NODE_TYPE_HEADERS,
                 "Display types (instead of names) at top of nodes",
                 getFieldEditorParent())); 	
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.PORT_LABELS,
                 "Display port names",
                 getFieldEditorParent())); 	
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.SKIP_SINGLETON_PORT_LABELS,
                 "Skip display of singleton port names",
                 getFieldEditorParent())); 	
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.REQUIRE_CTRL_FOR_POPUP,
		         "Don't show 'Add New' popup on new connection unless Ctrl or Shift key is down",
                 getFieldEditorParent())); 	
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.SHOW_MIN_MAX_ICON,
                 "Display minimize icon in node header",
                 getFieldEditorParent())); 	

		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.CONNECTIONS_BEHIND,
                 "Draw connections behind nodes (after close/reopen diagram)",
                 getFieldEditorParent())); 		
		 addField(new BooleanFieldEditor(IWorkflowEditorPreferences.TRANSLUCENT_COMPONENTS,
                 "Draw translucent nodes",
                 getFieldEditorParent())); 		
		BooleanFieldEditor renderNested;
		addField(renderNested = new BooleanFieldEditor(IWorkflowEditorPreferences.RENDER_NESTED,
                 "Draw content in nested Bioexpress nodes",
                 getFieldEditorParent())); 				
		addField(new BooleanFieldEditor(IWorkflowEditorPreferences.RENDER_NESTED_INDENTED,
                 "Avoid nested content overlap with port labels",
                 getFieldEditorParent())); 		

	}

}
