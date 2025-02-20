/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.custom.ICustomFeature;

public class AutoEditParameterFeatureProvider implements ICustomFeatureProvider {

	public AutoEditParameterFeatureProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ICustomFeature createFeature(IFeatureProvider fp, String nodeType, String property) {
		// TODO Auto-generated method stub
		return new AutoEditParameterFeature(fp, nodeType, property);
	}

}
