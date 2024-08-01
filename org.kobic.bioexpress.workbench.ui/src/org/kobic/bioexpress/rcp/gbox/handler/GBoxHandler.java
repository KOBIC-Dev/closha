package org.kobic.bioexpress.rcp.gbox.handler;

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

public class GBoxHandler {

	@Inject
	@Optional
	private EModelService eModelService;

	@Inject
	@Optional
	private MApplication mApplication;

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void swichGBoxPerspective(IWorkbench iWorkbench, Shell shell, MWindow window) {
		// MPerspective per = eModelService.getActivePerspective(window);
		List<MPerspectiveStack> pStacks = eModelService.findElements(window, null, MPerspectiveStack.class);
		if (pStacks.size() == 1) {
			List<MPerspective> pers = pStacks.get(0).getChildren();
			for (MPerspective per : pers) {
				System.out.println(per.getElementId());
				if (per.getElementId().contains(Constants.GBOX_PERSPECTIVE_ID)) {
					ePartService.switchPerspective(per);
					break;
				}
			}
			// return perspective;
		}
//		MPerspective element = (MPerspective) eModelService.find(Constants.GBOX_PERSPECTIVE_ID, mApplication);
//		ePartService.switchPerspective(element);
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
