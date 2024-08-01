package org.kobic.bioexpress.rcp.program.view.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class PaletteTreeNodeEditPart extends PaletteEntryEditPart {

	public PaletteTreeNodeEditPart(PaletteContainer model) {
		super(model);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getModelChildren() {
		List children = new ArrayList();
		PaletteEntry entry = (PaletteEntry) getModel();
		if (entry instanceof PaletteContainer) {
			PaletteContainer pc = (PaletteContainer) entry;
			List list = pc.getChildren();
			children.addAll(list);
		}
		return children;
	}

	@Override
	protected Image getDefaultImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	}

	@Override
	public int hashCode() {
		return getText() == null ? 0 : getText().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PaletteTreeNodeEditPart)
				&& Objects.equals(((PaletteTreeNodeEditPart) obj).getText(), getText());

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getChildren() {
		if (children == null)
			children = new ArrayList<PaletteEntryEditPart>();
		return children;
	}
}
