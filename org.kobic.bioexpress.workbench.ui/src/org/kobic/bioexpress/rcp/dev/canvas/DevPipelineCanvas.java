package org.kobic.bioexpress.rcp.dev.canvas;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
//import org.kobic.bioexpress.workspace.WorkspaceModel;

public class DevPipelineCanvas {

	final static Logger logger = Logger.getLogger(DevPipelineCanvas.class);

	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent, MPart part) {

//		WorkspaceModel workspaceModel = (WorkspaceModel) part.getObject();

//		logger.info("workspace name: " + workspaceModel.getWorkspaceName());

		parent.setLayout(new FillLayout());

		final Browser browser = new Browser(parent, SWT.NONE);

		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent event) {
			}
		});

		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
			}
		});

		browser.setUrl("http://eclipse.org/");
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}
