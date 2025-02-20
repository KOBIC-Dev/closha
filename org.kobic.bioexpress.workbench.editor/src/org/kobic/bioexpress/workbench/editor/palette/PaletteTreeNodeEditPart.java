/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.WorkflowImageProvider;

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
			children.addAll(pc.getChildren());
		}
		return children;
	}
	
	@Override
	protected Image getDefaultImage(){
		Object obj = getModel();
		String path = WorkflowImageProvider.get().getImageFilePath(WorkflowImageProvider.IMG_PALETTE_ROOT);	
		if(obj instanceof PaletteDrawer) {
			path = WorkflowImageProvider.get().getImageFilePath(WorkflowImageProvider.IMG_PALETTE_SUB);
		}		
		ImageDescriptor descriptor = WorkflowEditorPlugin.getImageDescriptor(path);
		return descriptor.createImage();
		//return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
	}
	
	@Override
	public int hashCode() {		
		return getText() == null ? 0 : getText().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PaletteTreeNodeEditPart) &&
				Objects.equals(((PaletteTreeNodeEditPart) obj).getText(), getText());			
		
	}
}
