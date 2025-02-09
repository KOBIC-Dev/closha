/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.rendering;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.Graphics;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import com.strikewire.snl.apc.util.ResourceUtils;

import gov.sandia.dart.workflow.domain.WFNode;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import gov.sandia.dart.workflow.util.PropertyUtils;

public class NestedInternalWorkflowGARenderer extends AbstractNestedWorkflowGARenderer {

	@Override
	protected void fillShape(Graphics g) {
		super.fillShape(g);
		PictogramElement pe = rc.getPlatformGraphicsAlgorithm().getPictogramElement();
		WFNode node = (WFNode) fp.getBusinessObjectForPictogramElement(pe);
		String contents = PropertyUtils.getProperty(node, "fileContents");
		// TODO Avoid loops!
		if (!StringUtils.isBlank(contents)) {
			try {
				IFile file = ResourceUtils.getTempDir().getFile(new Path(System.currentTimeMillis() + ".iwf"));
				if (file.exists())
					file.setContents(new ByteArrayInputStream(contents.getBytes()), IFile.FORCE, null);
				else
					file.create(new ByteArrayInputStream(contents.getBytes()), IFile.FORCE, null);
				renderWorkflow(node, file, g);
				file.delete(true, null);
			} catch (CoreException e) {
				WorkflowEditorPlugin.getDefault().logError("Error rendering nestedInternalWorkflow node", e);
			}
			
		}
	}

}
