package org.kobic.bioexpress.rcp.gbox.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.kobic.bioexpress.model.file.FileModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class ClipboardHandler {

	final static Logger logger = Logger.getLogger(ClipboardHandler.class);

	@Inject
	@Optional
	private EPartService ePartService;

	@Execute
	public void execute(IWorkbench iWorkbench, Shell shell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {

		if (activePart.getElementId().equals(Constants.CLOSHA_GBOX_TREE_VIEW_ID)
				|| activePart.getElementId().equals(Constants.GBOX_TABLE_VIEW_ID)
				|| activePart.getElementId().equals(Constants.GBOX_BROWSER_PART_ID)) {

			if (object instanceof FileModel) {

				FileModel fileModel = (FileModel) object;

				String text = fileModel.getPath().replace(Constants.GBOX_RAPIDANT_ROOT, "");

				final Clipboard clipboard = new Clipboard(Display.getDefault());

				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { text }, new Transfer[] { textTransfer });
			}
		}
	}

	@CanExecute
	public boolean enable(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		return true;
	}
}
