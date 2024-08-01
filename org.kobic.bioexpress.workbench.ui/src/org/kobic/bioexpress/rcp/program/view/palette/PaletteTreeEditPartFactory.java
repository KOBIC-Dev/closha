package org.kobic.bioexpress.rcp.program.view.palette;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;

public class PaletteTreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart parentEditPart, Object model) {
		if (model instanceof PaletteContainer)
			return new PaletteTreeNodeEditPart((PaletteContainer) model);
		if (model instanceof PaletteEntry)
			return new PaletteEntryEditPart((PaletteEntry) model);
		return null;
	}

}
