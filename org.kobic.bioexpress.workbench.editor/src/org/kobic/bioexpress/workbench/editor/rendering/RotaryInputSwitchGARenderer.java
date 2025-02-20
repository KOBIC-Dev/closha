/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.rendering;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.ga.IGraphicsAlgorithmRenderer;

import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.WFNode;
import gov.sandia.dart.workflow.util.PropertyUtils;

public class RotaryInputSwitchGARenderer extends AbstractGARenderer implements IGraphicsAlgorithmRenderer {

	@Override
	protected void fillShape(Graphics g) {		
	}

	@Override
	protected void outlineShape(Graphics g) {		
		Rectangle ib = getInnerBounds();
		g.setForegroundColor(ColorConstants.black);
		g.setBackgroundColor(ColorConstants.white);
		g.setLineStyle(Graphics.LINE_SOLID);
		g.setLineWidth(2);
		
		Rectangle clip = new Rectangle(ib.x,
				ib.y-GenericWFNodeGARenderer.CLEARANCE,
				ib.width,
				ib.height+GenericWFNodeGARenderer.CLEARANCE + GenericWFNodeGARenderer.MORE_CLEARANCE);
		g.setClip(clip);

		WFNode theNode = getNode();

		// Find center point
		Point center = getCenter(theNode);
		
		// right end
		Point right = new Point(ib.x + ib.width, ib.y + 30);

		Rectangle r = new Rectangle(ib.x + 10, ib.y, ib.width - 15, ib.height);
		if (!StringUtils.isEmpty(theNode.getLabel()))
			renderTextAbove(g, r, theNode.getLabel());

		String selector = PropertyUtils.getProperty(theNode, "selector");	
		InputPort port = null;
		if (selector != null) {
			port = findNamedPort(theNode, selector);			
		}

		if (port != null) {
			PictogramElement pe = fp.getPictogramElementForBusinessObject(port);
			org.eclipse.graphiti.mm.algorithms.styles.Point loc = ((FixPointAnchor) pe).getLocation();
			int x = loc.getX() + ib.x;
			int y = loc.getY() + ib.y;
			g.drawLine(right, center);
			g.drawOval(center.x-2, center.y-2, 5, 5);
			g.drawLine(center, new Point(x, y));

			Rectangle text = new Rectangle(ib.getTopLeft(), center);
			centerText(g, text, selector);
		} else {
			g.setForegroundColor(ColorConstants.red);
			g.drawOval(center.x-2, center.y-2, 5, 5);
			Rectangle text = new Rectangle(ib.getTopLeft(), center);
			centerText(g, text, StringUtils.isEmpty(selector) ? "ERROR" : selector);
		}
			
	}

	public static InputPort findNamedPort(WFNode theNode, String selector) {		
		return theNode.getInputPorts().stream().filter(p -> p.getName().equals(selector)).findFirst().orElse(null);
	}
	
	private Point getCenter(WFNode theNode) {
		Rectangle ib = getInnerBounds();
		Point center = ib.getCenter();

		EList<InputPort> ip = theNode.getInputPorts();
		if (ip.size() == 0) {
			center.y = center.y + 30;
			return center;
		}
		
		PictogramElement pe = fp.getPictogramElementForBusinessObject(ip.get(0));
		org.eclipse.graphiti.mm.algorithms.styles.Point loc = ((FixPointAnchor) pe).getLocation();
		int y1 = loc.getY() + ib.y;
		
		pe = fp.getPictogramElementForBusinessObject(ip.get(ip.size()-1));
		loc = ((FixPointAnchor) pe).getLocation();
		int y2 = loc.getY() + ib.y;
		
		center.y = (y1 + y2) / 2;
		return center;
		
	}

	private WFNode getNode() {
		PictogramElement pe = rc.getPlatformGraphicsAlgorithm().getPictogramElement();
		Object bo = fp.getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			WFNode s = (WFNode) bo;
			if (s.getType().equals("rotaryInputSwitch")) {
				return s;
			}
		}
		return null;
	}
}
