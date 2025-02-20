package org.kobic.bioexpress.workbench.editor.palette.workbench;

import java.util.List;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;

/**
 * This action launches the PaletteCustomizerDialog for the given palette.
 * 
 * @author Pratik Shah
 */
public class CustomizeAction extends Action {

	private PaletteViewer paletteViewer;

	/**
	 * Constructor
	 * 
	 * @param palette
	 *            the palette which has to be customized when this action is run
	 */
	public CustomizeAction(PaletteViewer palette) {
		super();
		setText(PaletteMessages.MENU_OPEN_CUSTOMIZE_DIALOG);
		paletteViewer = palette;
	}

	/**
	 * Opens the Customizer Dialog for the palette
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		PaletteCustomizerDialog dialog = paletteViewer.getCustomizerDialog();
		List list = paletteViewer.getSelectedEditParts();
		if (!list.isEmpty()) {
			PaletteEntry selection = (PaletteEntry) ((EditPart) list.get(0))
					.getModel();
			if (!(selection instanceof PaletteRoot)) {
				dialog.setDefaultSelection(selection);
			}
		}
		dialog.open();
	}

}
