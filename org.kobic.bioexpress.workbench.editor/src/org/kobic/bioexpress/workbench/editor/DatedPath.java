/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor;

public class DatedPath implements Comparable<DatedPath> {
	String path;
	long date;
	public DatedPath(String path, long date) {
		this.path = path;
		this.date = date;
	}
	@Override
	public int compareTo(DatedPath arg0) {
		long diff = arg0.date - date;
		return diff > 0 ? 1 : diff == 0 ? 0 : -1;
	}
}
