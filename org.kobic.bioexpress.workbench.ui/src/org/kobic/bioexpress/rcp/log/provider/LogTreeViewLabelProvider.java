package org.kobic.bioexpress.rcp.log.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.pipeline.NodeModel;
import org.kobic.bioexpress.rcp.constant.Constants;

public class LogTreeViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

	final static Logger logger = Logger.getLogger(LogTreeViewLabelProvider.class);

	@Override
	public String getText(Object object) {
		return null;
	}

	@Override
	public Image getImage(Object object) {

		if (object instanceof NodeModel) {
			
			return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.CATEGORY_VIEW_CATEGORY_ICON);
			
		} else {
			
			return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.PROGRAM_VIEW_PROGRAM_ICON);
			
		}
	}

	@Override
	public StyledString getStyledText(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

}