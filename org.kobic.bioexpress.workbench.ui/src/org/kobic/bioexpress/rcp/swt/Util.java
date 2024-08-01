package org.kobic.bioexpress.rcp.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Util {

	private static Util instance;

	public static Util getInstance() {

		if (instance == null) {
			instance = new Util();
		} else {
			return instance;
		}

		return instance;
	}

	public Point getMonitorCenter(Shell shell) {
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		return new Point(x, y);
	}

}
