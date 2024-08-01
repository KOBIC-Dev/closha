package org.kobic.bioexpress.rcp.member.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.rcp.constant.Constants;

public class MemberTableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	final static Logger logger = Logger.getLogger(MemberTableViewLabelProvider.class);

	@Override
	public String getColumnText(Object object, int index) {

		if (object instanceof Member) {

			Member item = (Member) object;

			String value = null;

			switch (index) {
			case 0:
				value = item.getMemberId();
				break;
			case 1:
				value = item.getMemberNm();
				break;
			case 2:
				value = item.getMemberEmail();
				break;
			default:
				value = Constants.DEFAULT_VALUE;
				break;
			}

			return value;
		}

		return Constants.DEFAULT_VALUE;
	}

	@Override
	public Image getColumnImage(Object object, int index) {

		if (index == 0) {
			return getImage(object);
		}

		return null;
	}

	@Override
	public Image getImage(Object object) {

		return ResourceManager.getPluginImage(Constants.SYMBOLIC_NAME, Constants.MEMBER_ICON);
	}
}