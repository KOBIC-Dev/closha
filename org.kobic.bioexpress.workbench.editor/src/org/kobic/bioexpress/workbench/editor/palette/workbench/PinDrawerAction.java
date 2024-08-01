package org.kobic.bioexpress.workbench.editor.palette.workbench;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.ui.palette.editparts.IPinnableEditPart;

/**
 * An action that can be used to pin the given pinnable palette editpart (drawer
 * or stack) open.
 * 
 * @author Pratik Shah
 */
public class PinDrawerAction extends Action {

	private IPinnableEditPart pinnableEditPart;

	/**
	 * Constructor
	 * 
	 * @param drawer
	 *            The EditPart for the drawer that this action pins/unpins
	 */
	public PinDrawerAction(DrawerEditPart drawer) {
		this.pinnableEditPart = drawer;
		setChecked(drawer.isPinnedOpen());
		setEnabled(drawer.isExpanded());
		setText(PaletteMessages.PINNED);
	}

	/**
	 * Constructor
	 * 
	 * @param pinnableEditPart
	 *            the pinnable palette editpart
	 * @since 3.4
	 */
	public PinDrawerAction(IPinnableEditPart pinnableEditPart) {
		this.pinnableEditPart = pinnableEditPart;
		setChecked(pinnableEditPart.isPinnedOpen());
		setEnabled(pinnableEditPart.isExpanded());
		setText(PaletteMessages.PINNED);
	}

	/**
	 * Toggles the pinned open status of the pinnable palette editpart.
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		pinnableEditPart.setPinnedOpen(!pinnableEditPart.isPinnedOpen());
	}

}
