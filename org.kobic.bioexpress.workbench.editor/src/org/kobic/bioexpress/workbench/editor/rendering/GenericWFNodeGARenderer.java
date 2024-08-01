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

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.ga.IGraphicsAlgorithmRenderer;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import gov.sandia.dart.workflow.domain.WFNode;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.WorkflowImageProvider;
import org.kobic.bioexpress.workbench.editor.WorkflowToolBehaviorProvider;
import org.kobic.bioexpress.workbench.editor.features.MinMaxFeature;
import org.kobic.bioexpress.workbench.editor.monitoring.WorkflowTracker;
import org.kobic.bioexpress.workbench.editor.monitoring.WorkflowTracker.NodeExecutionStatus;
import org.kobic.bioexpress.workbench.editor.preferences.IWorkflowEditorPreferences;
import org.kobic.bioexpress.workbench.editor.settings.NOWPSettingsEditorUtils;

public class GenericWFNodeGARenderer extends AbstractGARenderer implements IGraphicsAlgorithmRenderer {

    public static final String ID = "wfnode";
	static final int CLEARANCE = 100;
	static final int MORE_CLEARANCE = 24;
	
	static Map<String, Image> images = new ConcurrentHashMap<>();
	
	final Color waitColor = new Color(null, 214,214,214);
	final Color runColor = new Color(null, 178,228,195);
	final Color comColor = new Color(null, 189,218,242);
	final Color errorColor = new Color(null, 255,227,139);
	final Color stopColor = new Color(null, 246,199,198);
	
	@Override
	protected void fillShape(Graphics g) {	
//		-투명도�조절�이전�값
//		2. 노드�바디�값
//		 R,G,B (255,255,255) #ffffff
//		3. 간선�색상�값
//		 R,G,B (25,50,158) #19329e
//		대기 / R,G,B (119,119,119) #777777
//		실행 / R,G,B (36,131,211) #2483d3
//		완료 / R,G,B (1,164,55) #01a437
//		에러 / R,G,B (225,195,5) #ffc305
//		중지 / R,G,B (225,70,66) #e14642
		
		
		
		Rectangle innerBounds = getInnerBounds();
		Color fillColor = ColorConstants.white;
		
		PictogramElement pe = rc.getPlatformGraphicsAlgorithm().getPictogramElement();		
		Object bo = fp.getBusinessObjectForPictogramElement(pe);
		WFNode node = (WFNode) bo;			

		IToolBehaviorProvider tbp = fp.getDiagramTypeProvider().getCurrentToolBehaviorProvider();
		if (tbp instanceof WorkflowToolBehaviorProvider) {
			String nodeName = getNodeName(bo); 
			WorkflowDiagramEditor editor = NOWPSettingsEditorUtils.getDiagramEditor(node);
			if (editor != null) {
				
			} 
		}
		
		g.setAntialias(SWT.ON);
		if (WorkflowEditorPlugin.getDefault().getPreferenceStore().getBoolean(IWorkflowEditorPreferences.TRANSLUCENT_COMPONENTS)) {
//			g.setAlpha(150);
		}
		//
//		g.setAlpha(150);
		if(node.getStatus().equals("run"))
			fillColor = runColor;
		else if(node.getStatus().equals("complete"))
			fillColor = comColor;//ColorConstants.yellow;
		else if(node.getStatus().equals("error"))
			fillColor = errorColor;
		else if(node.getStatus().equals("stop"))
			fillColor = stopColor;//ColorConstants.orange;
		else {
//			g.setAlpha(80);
			fillColor = waitColor;//ColorConstants.lightGray;
		}
		g.setBackgroundColor(fillColor);
		Point s2 = innerBounds.getTopRight();
		s2.y = s2.y+24;
		//
		g.fillRectangle(new Rectangle(innerBounds.getTopLeft(), s2));
		Image image = getIcon((WFNode) bo);
		g.drawImage(image, innerBounds.getTopLeft().translate(5, 5));
				
	}

	public static Image getIcon(WFNode bo) {
		String id = WorkflowImageProvider.PREFIX + bo.getType();
		//public check..
		if(bo.isPublic())
			id = id+"_p";
		Image image = images.get(id);
		if (image != null) {
			return image;
		}
		
		String path = WorkflowImageProvider.get().getImageFilePath(id);
		if (path == null) {
			if (images.get(WorkflowImageProvider.IMG_GEAR) != null)
				return images.get(WorkflowImageProvider.IMG_GEAR);
			path = WorkflowImageProvider.get().getImageFilePath(WorkflowImageProvider.IMG_GEAR);
		}		
		
		ImageDescriptor descriptor = WorkflowEditorPlugin.getImageDescriptor(path);
		image = descriptor.createImage();
		images.put(id, image);
		return image;
	}

	@Override
	protected void outlineShape(Graphics g) {
		Font f = WorkflowEditorPlugin.getDefault().getDiagramFont();
		g.setFont(f);
		Rectangle ib = getInnerBounds();
		
		
		//
		PictogramElement pe = rc.getPlatformGraphicsAlgorithm().getPictogramElement();		
		Object bo = fp.getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			//TODO : BIOEXPRESS
			WFNode node = (WFNode) bo;			
			//
			Color lineColor = ColorConstants.black;
			if(node.getStatus().equals("run"))
				lineColor = runColor;
			else if(node.getStatus().equals("complete"))
				lineColor = comColor;//ColorConstants.yellow;
			else if(node.getStatus().equals("error"))
				lineColor = errorColor;
			else if(node.getStatus().equals("stop"))
				lineColor = stopColor;//ColorConstants.orange;
			else {
//				g.setAlpha(80);
				lineColor = waitColor;//ColorConstants.lightGray;
			}			
			g.setAlpha(255);
			g.setBackgroundColor(ColorConstants.white);
			Point t1 = new Point(ib.getTopLeft().x, ib.getTopLeft().y+26);
			Point t2 = new Point(ib.getBottomRight().x, ib.getBottomRight().y);
			g.fillRoundRectangle(new Rectangle(t1, t2), 5, 5);
			//
			g.setForegroundColor(lineColor);
			g.setLineStyle(Graphics.LINE_SOLID);
			g.setLineWidth(1);
			g.drawRoundRectangle(new Rectangle(ib.getTopLeft(), ib.getBottomRight()), 5, 5);			
			//
			Rectangle clip = new Rectangle(ib.x, ib.y-CLEARANCE, ib.width, ib.height+CLEARANCE+MORE_CLEARANCE);
			g.setClip(clip);
			if (shouldDrawMinMaxControl())
				drawMinMaxControl(g, ib);
			
			Rectangle r = new Rectangle(ib.x + 10, ib.y, ib.width - 15, ib.height);
			//
			final Color fontColor = new Color(null, 51, 51, 51);
			g.setForegroundColor(fontColor);
	        if (WorkflowEditorPlugin.getDefault().getPreferenceStore().getBoolean(IWorkflowEditorPreferences.NODE_TYPE_HEADERS))
	        	g.drawText(node.getType(), ib.x + 24, ib.y + 4);
	        else
	        	g.drawText(node.getName(), ib.x + 24, ib.y + 4);
	        
	        //draw line
	        Point s1 = new Point(ib.x, ib.y+24);
	        Point s2 = new Point(ib.getTopRight().x, ib.y+24);
	        g.setForegroundColor(lineColor);
	        g.drawLine(s1, s2);
			
			IToolBehaviorProvider tbp = fp.getDiagramTypeProvider().getCurrentToolBehaviorProvider();

			if (tbp instanceof WorkflowToolBehaviorProvider) {
				String nodeName = getNodeName(bo);
				WorkflowDiagramEditor editor = NOWPSettingsEditorUtils.getDiagramEditor(node);
				if (editor != null) {
					//IFile file = editor.getWorkflowFile();
					//TODO : BIOEXPESS
//					NodeExecutionStatus status = WorkflowTracker.getExecutionStatus(nodeName, file, editor.getRunLocation());	
//					if (status == NodeExecutionStatus.CURRENT) {
//						g.setForegroundColor(ColorConstants.darkGreen);
//						g.setLineStyle(Graphics.LINE_DASHDOT);
//						g.setLineWidth(3);
//						Point br = new Point(ib.getBottomRight().x-2, ib.getBottomRight().y-2);
//						g.drawRectangle(new Rectangle(ib.getTopLeft(), br));
//					}
				}
			}
		}
	}


	private String getNodeName(Object bo) {
		return bo instanceof WFNode ? ((WFNode) bo).getName() : "NONE";
	}

	private void drawMinMaxControl(Graphics g, Rectangle ib) {
		final int SIZE = MinMaxFeature.MIN_MAX_ICON_SIZE;
		final int HALF = MinMaxFeature.MIN_MAX_ICON_SIZE / 2;
		Rectangle control = new Rectangle(ib.x + ib.width - SIZE, ib.y, SIZE, SIZE);
		g.setForegroundColor(ColorConstants.black);
		g.drawRectangle(control);
		control = new Rectangle(ib.x + ib.width - HALF, ib.y, HALF, HALF);
		g.drawRectangle(control);
		
	}

	private boolean shouldDrawMinMaxControl() {
		return MinMaxFeature.isEnabled();
	}
}
