package org.kobic.bioexpress.rcp.main.handler;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.rcp.Activator;
import org.kobic.bioexpress.rcp.constant.Constants;

public class MainHandler {

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Inject
	@Optional
	private MWindow window;

	@Execute
	public void swichCloshaPerspective(IWorkbench iWorkbench, Shell shell, MWindow window) {
		List<MPerspectiveStack> pStacks = eModelService.findElements(window, null, MPerspectiveStack.class);
		if (pStacks.size() == 1) {
			List<MPerspective> pers = pStacks.get(0).getChildren();
			for (MPerspective per : pers) {
				System.out.println(per.getElementId());
				if (per.getElementId().contains(Constants.CLOSHA_PERSPECTIVE_ID)) {
//	        		ePartService.switchPerspective(per);
//	        		break;
				}
			}
		}
		MPerspective element = (MPerspective) eModelService.find(Constants.CLOSHA_PERSPECTIVE_ID + ".<CLOSHA>",
				mApplication);
		if (element == null)
			element = (MPerspective) eModelService.find(Constants.CLOSHA_PERSPECTIVE_ID, mApplication);
		ePartService.switchPerspective(element);

//		MPart mPart = ePartService.findPart(Constants.WORKBENCH_MONITOR_VIEW_ID);
//		ePartService.activate(mPart);

	}

	@CanExecute
	public boolean enable() {

		// login 이후 활성화
		if (Activator.getMember() != null) {
			return true;
		} else {
			return false;
		}
	}
}
