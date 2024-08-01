package org.kobic.bioexpress.rcp.swt.component;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class CreateImageDescriptor {

	protected static CreateImageDescriptor instance = null;

	public final static CreateImageDescriptor getInstance() {

		if (instance == null) {
			instance = new CreateImageDescriptor();
		} else {
			return instance;
		}
		return instance;
	}

	public ImageDescriptor getImageDescriptor(String img) {

		Bundle bundle = FrameworkUtil.getBundle(CreateImageDescriptor.class);

		URL url = FileLocator.find(bundle, new Path(img));
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);

		return imageDescriptor;
	}

}
