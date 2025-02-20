/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.kobic.bioexpress.workbench.editor.preferences.IWorkflowEditorPreferences;

public class KeyboardStateListener implements Listener {
	private static volatile boolean isKeyDown = false;
	Display display;
	KeyboardStateListener(Display display) {
		this.display = display;
	}
	@Override
	public void handleEvent(Event event) {
		if (event.keyCode == SWT.CTRL || event.keyCode == SWT.SHIFT) {
			if (event.type == SWT.KeyDown)
				setKeyDown(true);
			else
				setKeyDown(false);
		}
	}
	public static boolean isKeyDown() {
		IPreferenceStore store = WorkflowEditorPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(IWorkflowEditorPreferences.REQUIRE_CTRL_FOR_POPUP) ? isKeyDown : true;
	}
	public static void setKeyDown(boolean isNDown) {
		KeyboardStateListener.isKeyDown = isNDown;
	}
	
}
