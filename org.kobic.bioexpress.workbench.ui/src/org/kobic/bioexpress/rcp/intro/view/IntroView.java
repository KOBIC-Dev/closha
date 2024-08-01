package org.kobic.bioexpress.rcp.intro.view;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class IntroView {

	final static Logger logger = Logger.getLogger(IntroView.class);

	@Inject
	private MDirtyable dirty;

	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_PROPERTIES);

	@PostConstruct
	public void createComposite(Composite parent) {

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

		String webURL = props.getProperty("closha.workbench.url");

		browser.setUrl(webURL);
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}
