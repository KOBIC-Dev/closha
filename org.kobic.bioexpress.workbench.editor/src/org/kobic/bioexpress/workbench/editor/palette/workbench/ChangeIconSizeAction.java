package org.kobic.bioexpress.workbench.editor.palette.workbench;

import org.eclipse.jface.action.Action;

/**
 * This action toggles the "Use Large Icons" option for the current layout mode
 * of the palette.
 * 
 * @author Pratik Shah
 */
public class ChangeIconSizeAction extends Action {

	private PaletteViewerPreferences prefs;

	/**
	 * Constructor
	 * 
	 * @param prefs
	 *            The <code>PaletteViewerPreferences</code> object that this
	 *            action is manipulating
	 */
	public ChangeIconSizeAction(PaletteViewerPreferences prefs) {
		super(PaletteMessages.SETTINGS_USE_LARGE_ICONS_LABEL_CAPS);
		this.prefs = prefs;
		setChecked(prefs.useLargeIcons());
	}

	/**
	 * Toggles the "Use Large Icons" option for the current layout mode.
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		prefs.setCurrentUseLargeIcons(!prefs.useLargeIcons());
	}

}
