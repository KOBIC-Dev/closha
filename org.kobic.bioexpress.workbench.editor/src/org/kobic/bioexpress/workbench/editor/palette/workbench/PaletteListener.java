package org.kobic.bioexpress.workbench.editor.palette.workbench;

import java.util.EventListener;

import org.eclipse.gef.palette.ToolEntry;

/**
 * Listens to changes in the palette.
 */
public interface PaletteListener extends EventListener {

	/**
	 * A new tool was activated in the palette.
	 * 
	 * @param palette
	 *            the source of the change
	 * @param tool
	 *            the new tool that was activated
	 */
	void activeToolChanged(PaletteViewer palette, ToolEntry tool);

}
