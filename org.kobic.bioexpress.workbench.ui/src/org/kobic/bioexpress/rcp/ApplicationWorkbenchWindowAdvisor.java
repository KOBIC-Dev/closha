package org.kobic.bioexpress.rcp;

import java.util.Properties;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
//import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Monitor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.login.view.LoginDialog;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	private Properties props = Activator.getProperties(Constants.BIO_EXPRESS_PROPERTIES);

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {

		String VERSION = props.getProperty("closha.workbench.version");

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
//		configurer.setInitialSize(new Point(1600, 950));
		configurer.setInitialSize(new Point(2500, 1500));
		configurer.setShowProgressIndicator(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle("CLOSHA " + VERSION);

		IWorkbenchWindow window = configurer.getWindow();
		window.getShell().setMaximized(true);
		window.getShell()
				.setImage(ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.BI_CLOSHA_LARGE_ICON));

//		Monitor primary = window.getShell().getDisplay().getPrimaryMonitor();
//		Rectangle bounds = primary.getBounds();
//		Rectangle rect = window.getShell().getBounds();
//		int x = bounds.x + (bounds.width - rect.width) / 2;
//		int y = bounds.y + (bounds.height - rect.height) / 2;	
//		window.getShell().setLocation(x, y);

		LoginDialog ld = new LoginDialog(window.getShell(), SWT.NONE);
		Object obj = ld.open();
		if (obj == null) {
			System.exit(-1);
		}
	}

	@Override
	public void postWindowCreate() {

		System.out.println("posetWindowCreate.................");

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		IWorkbenchWindow window = configurer.getWindow();
		// window.getShell().setMaximized(true);
		PreferenceManager pm = window.getWorkbench().getPreferenceManager();

//		IPreferenceNode[] nodes = pm.getRootSubNodes();
//		for (IPreferenceNode node : nodes) {
//			 System.out.println(node.getId());
//		}

		pm.remove("com.strikewire.snl.apc.Common.hostName");
		pm.remove("com.strikewire.snl.apc.ApcCommon.preferences.pages.system");
		pm.remove("org.eclipse.team.ui.TeamPreferences");
		pm.remove("org.eclipse.graphiti.examples.common.preference.GraphicsTestPreferencePage");

		IPerspectiveRegistry reg = window.getWorkbench().getPerspectiveRegistry();
		IPerspectiveDescriptor[] descs = reg.getPerspectives();
		// reg.setDefaultPerspective("org.kobic.bioexpress.rcp.closha.perspective.<CLOSHA>");
		reg.setDefaultPerspective("org.kobic.bioexpress.rcp.closha.perspective.<CLOSHA>");
		for (IPerspectiveDescriptor desc : descs) {
			// System.out.println("desc " + desc.getId());
			if (desc.getId().startsWith("org.eclipse."))
				reg.deletePerspective(desc);
			if (desc.getId().startsWith("org.python."))
				reg.deletePerspective(desc);
		}
		descs = reg.getPerspectives();
		for (IPerspectiveDescriptor desc : descs) {
			System.out.println("desc " + desc.getId());
		}
		IPerspectiveDescriptor desc1 = reg
				.findPerspectiveWithId("org.kobic.bioexpress.rcp.closha.perspective.<CLOSHA>");
		System.out.println("desc1 " + desc1);
		IPerspectiveDescriptor desc2 = reg.findPerspectiveWithId("org.kobic.bioexpress.rcp.gbox.perspective.<GBOX>");
		System.out.println("desc2 " + desc2);
		System.out.println("part " + ePartService);
	}
}
