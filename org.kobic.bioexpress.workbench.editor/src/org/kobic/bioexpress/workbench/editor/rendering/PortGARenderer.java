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

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.ga.IGraphicsAlgorithmRenderer;
import org.eclipse.graphiti.platform.ga.IRendererContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import gov.sandia.dart.workflow.domain.InputPort;
import gov.sandia.dart.workflow.domain.OutputPort;
import gov.sandia.dart.workflow.domain.Port;
import gov.sandia.dart.workflow.domain.Response;
import gov.sandia.dart.workflow.domain.ResponseArc;
import gov.sandia.dart.workflow.domain.WFArc;
import gov.sandia.dart.workflow.domain.WFNode;
import org.kobic.bioexpress.workbench.editor.WorkflowDiagramEditor;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.WorkflowImageProvider;
import org.kobic.bioexpress.workbench.editor.preferences.IWorkflowEditorPreferences;
import org.kobic.bioexpress.workbench.editor.settings.NOWPSettingsEditorUtils;
import gov.sandia.dart.workflow.util.ParameterUtils;

public class PortGARenderer extends AbstractGARenderer implements IGraphicsAlgorithmRenderer {

	public static final String ID = "wfport";
	private static final int PORT_HEIGHT = 10;
	
	static Map<String, Image> images = new ConcurrentHashMap<>();
	
	public PortGARenderer(IRendererContext rc, IFeatureProvider fp) {
		setRc(rc);
		setFp(fp);
	}

	@Override
	protected void fillShape(Graphics g) {
		Rectangle r = getInnerBounds();
		r.height = 8;
		r.width = 8;
		//r.y = r.y+5;
		int[] poly = { r.x, r.y, r.x + r.width, r.y + r.height / 2, r.x, r.y + r.height, r.x, r.y };
		PictogramElement pe = rc.getPlatformGraphicsAlgorithm().getPictogramElement();
		Object bo = fp.getBusinessObjectForPictogramElement(pe);
		Image image = getIcon("input");
		if (bo instanceof OutputPort) {
			if (hasSelectedConnection(bo))
				g.setBackgroundColor(ColorConstants.green);
			else
				g.setBackgroundColor(ColorConstants.black);
			OutputPort op = (OutputPort)bo;
			if(op.getProperties().size() > 0) {
				String type = op.getProperties().get(0).getType();
				image = getIcon("input_"+type.toLowerCase());
			}			
			g.drawImage(image, new Point(r.x-6, r.y-3));
		} else {
			if (hasSelectedConnection(bo))
				g.setBackgroundColor(ColorConstants.red);
			else
				g.setBackgroundColor(ColorConstants.white);
			InputPort ip = (InputPort)bo;
			if(ip.getProperties().size() > 0) {
				String type = ip.getProperties().get(0).getType();
				image = getIcon("input_"+type.toLowerCase());
			}
			g.drawImage(image, new Point(r.x-4, r.y-3));
		}
		//g.fillPolygon(poly);
		
	}

	private boolean hasSelectedConnection(Object bo) {
		if (bo instanceof OutputPort) {
			OutputPort port = (OutputPort) bo;
			if (port.getArcs().size() > 0) {
				WorkflowDiagramEditor editor = NOWPSettingsEditorUtils.getDiagramEditor(port);
				if (editor != null) {
					PictogramElement[] elements = editor.getSelectedPictogramElements();				
					for (WFArc arc: port.getArcs()) {
						PictogramElement element = fp.getPictogramElementForBusinessObject(arc);
						if (Arrays.asList(elements).contains(element))
							return true;
					}
				}
			}	
			if (port.getResponseArcs().size() > 0) {
				WorkflowDiagramEditor editor = NOWPSettingsEditorUtils.getDiagramEditor(port);
				if (editor != null) {

					PictogramElement[] elements = editor.getSelectedPictogramElements();
					for (ResponseArc arc: port.getResponseArcs()) {
						PictogramElement element = fp.getPictogramElementForBusinessObject(arc);
						if (Arrays.asList(elements).contains(element))
							return true;
					}				
				}
			}	
		} else if (bo instanceof InputPort) {
			InputPort port = (InputPort) bo;
			if (port.getArcs().size() > 0) {	
				WorkflowDiagramEditor editor = NOWPSettingsEditorUtils.getDiagramEditor(port);
				if (editor != null) {			
					WFArc arc = port.getArcs().get(0);
					PictogramElement element = fp.getPictogramElementForBusinessObject(arc);
					PictogramElement[] elements = editor.getSelectedPictogramElements();
					return Arrays.asList(elements).contains(element);
				}
			}
		}  else if (bo instanceof Response) {
			Response response = (Response) bo;
			if (response.getSource().size() > 0) {
				WorkflowDiagramEditor editor = NOWPSettingsEditorUtils.getDiagramEditor(response);
				if (editor != null) {
					PictogramElement[] elements = editor.getSelectedPictogramElements();
					for (ResponseArc arc: response.getSource()) {
						PictogramElement element = fp.getPictogramElementForBusinessObject(arc);
						if (Arrays.asList(elements).contains(element))
							return true;
					}		
				}
			}
		}
		return false;
	}

	@Override
	protected void outlineShape(Graphics g) {	
		Rectangle r = getInnerBounds();
		r.height = 10;
		r.width = 10;
		// TODO Better way to do this?
		g.setClip(new Rectangle(r.x-200, r.y-200,  1000, 1000)); 
		g.setForegroundColor(ColorConstants.blue);

		PictogramElement pe = rc.getPlatformGraphicsAlgorithm().getPictogramElement();
		Object bo = fp.getBusinessObjectForPictogramElement(pe);
		if (!hasSelectedConnection(bo)) {
			//r.y = r.y+5;
			int[] poly = { r.x, r.y, r.x + r.width, r.y + r.height/2, r.x, r.y + r.height, r.x, r.y    };
			//g.drawPolygon(poly);
		}

		if (bo instanceof Port && showPortLabels(bo)) {
			if (bo instanceof Port) {
				Port p = (Port) bo;
				WFNode node = getNode(p);				
				if (!ParameterUtils.isParameter(node)) {
//					if (skipSingletonPortLabels()) {
//						if (bo instanceof InputPort && node.getInputPorts().size() == 1) {
//							return;
//						} else if (bo instanceof OutputPort && node.getOutputPorts().size() == 1) {
//							return;
//						}
//					}
					Font f = WorkflowEditorPlugin.getDefault().getDiagramFont();
					g.setFont(f);
					String text = ((Port)bo).getName();	
					try {
						TextLayout tl = new TextLayout(null);
						tl.setFont(g.getFont());
						tl.setText(text);
						int h = g.getFontMetrics().getHeight()-2;
						if (bo instanceof InputPort) {
							g.drawTextLayout(tl, r.x + r.width + 3, r.y + PORT_HEIGHT - h);
							tl.setAlignment(SWT.LEFT);
	
						} else {
							tl.setAlignment(SWT.RIGHT);
	
							int width = tl.getLineBounds(0).width;
							g.drawTextLayout(tl, r.x - 5 - width, r.y + PORT_HEIGHT - h);
						}
					}catch(Exception ex) {
						
					}
				}
			}
		}
	}

	private boolean skipSingletonPortLabels() {
		return WorkflowEditorPlugin.getDefault().getPreferenceStore().getBoolean(IWorkflowEditorPreferences.SKIP_SINGLETON_PORT_LABELS);
	}
	
	private boolean showPortLabels(Object bo) {
		Port p = (Port) bo;
		WFNode node = getNode(p);
		if (OnOffSwitchGARenderer.hideLabels(node))
			return false;
		else
			return WorkflowEditorPlugin.getDefault().getPreferenceStore().getBoolean(IWorkflowEditorPreferences.PORT_LABELS);
	}	

	private WFNode getNode(Port p) {
		WFNode node = p instanceof InputPort ? ((InputPort) p).getNode() : ((OutputPort) p).getNode();
		return node;
	}
	
	public static Image getIcon(String type) {
		String id = WorkflowImageProvider.PREFIX + type;
		Image image = images.get(id);
		if (image != null) {
			return image;
		}
		
		String path = WorkflowImageProvider.get().getImageFilePath(id);
		if (path == null) {
			if (images.get(WorkflowImageProvider.IMG_INPUT) != null)
				return images.get(WorkflowImageProvider.IMG_INPUT);
			path = WorkflowImageProvider.get().getImageFilePath(WorkflowImageProvider.IMG_INPUT);
		}		
		
		ImageDescriptor descriptor = WorkflowEditorPlugin.getImageDescriptor(path);
		image = descriptor.createImage();
		images.put(id, image);
		return image;
	}
}
