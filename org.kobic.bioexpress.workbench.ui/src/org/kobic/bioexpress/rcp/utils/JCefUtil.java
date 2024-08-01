package org.kobic.bioexpress.rcp.utils;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;

public class JCefUtil {
	private static CefApp cefApp_;	
	
	
	/**
	 * Chromium Embedded Framework (CEF)
	 * 
	 */
	private static void initCef() {

		CefAppBuilder builder = new CefAppBuilder();
		// windowless_rendering_enabled must be set to false if not wanted.

		builder.getCefSettings().windowless_rendering_enabled = false;
		// USE builder.setAppHandler INSTEAD OF CefApp.addAppHandler!
		// Fixes compatibility issues with MacOSX

		builder.setAppHandler(new MavenCefAppHandlerAdapter() {
			@Override
			public void stateHasChanged(org.cef.CefApp.CefAppState state) {
				// Shutdown the app if the native CEF part is terminated
				if (state == CefAppState.TERMINATED)
					System.exit(0);
			}
		});
		
		try {
			cefApp_ = builder.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CefApp getCefApp() {
		if(cefApp_ == null)
			initCef();
		return cefApp_;
	}

}
