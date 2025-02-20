package org.kobic.bioexpress.workbench.editor.palette.workbench;

import org.eclipse.jface.action.IMenuManager;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.editparts.IPinnableEditPart;

/**
 * Provides the context menu for a palette.
 * 
 * @author Pratik Shah
 */
public class PaletteContextMenuProvider extends ContextMenuProvider {

	/**
	 * Constructor
	 * 
	 * @param palette
	 *            the palette viewer for which the context menu has to be
	 *            created
	 */
	public PaletteContextMenuProvider(PaletteViewer palette) {
		super(palette);
	}

	/**
	 * @return the palette viewer
	 */
	protected PaletteViewer getPaletteViewer() {
		return (PaletteViewer) getViewer();
	}

	/**
	 * This is the method that builds the context menu.
	 * 
	 * @param menu
	 *            The IMenuManager to which actions for the palette's context
	 *            menu can be added.
	 * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);

		EditPart selectedPart = (EditPart) getPaletteViewer()
				.getSelectedEditParts().get(0);
		IPinnableEditPart pinnablePart = (IPinnableEditPart) selectedPart
				.getAdapter(IPinnableEditPart.class);
		if (pinnablePart != null && pinnablePart.canBePinned()) {
			menu.appendToGroup(GEFActionConstants.MB_ADDITIONS,
					new PinDrawerAction(pinnablePart));
		}
		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new LayoutAction(
				getPaletteViewer().getPaletteViewerPreferences()));
		menu.appendToGroup(GEFActionConstants.GROUP_VIEW,
				new ChangeIconSizeAction(getPaletteViewer()
						.getPaletteViewerPreferences()));
		if (getPaletteViewer().getCustomizer() != null) {
			menu.appendToGroup(GEFActionConstants.GROUP_REST,
					new CustomizeAction(getPaletteViewer()));
		}
		menu.appendToGroup(GEFActionConstants.GROUP_REST, new SettingsAction(
				getPaletteViewer()));
	}

}
