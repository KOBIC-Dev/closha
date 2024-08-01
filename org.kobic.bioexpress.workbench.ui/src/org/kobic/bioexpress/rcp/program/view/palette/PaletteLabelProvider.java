package org.kobic.bioexpress.rcp.program.view.palette;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PaletteLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		return ((PaletteEntryEditPart) element).getImage();
	}

	@Override
	public String getText(Object element) {
		if (element instanceof PaletteEntryEditPart)
			return ((PaletteEntryEditPart) element).getText();
		else
			return String.valueOf(element);
	}
}
