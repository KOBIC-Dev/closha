/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddBendpointFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICopyFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveBendpointFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IPasteFeature;
import org.eclipse.graphiti.features.IPrintFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IRemoveBendpointFeature;
import org.eclipse.graphiti.features.ISaveImageFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddBendpointContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveBendpointContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.IRemoveBendpointContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.DefaultAddBendpointFeature;
import org.eclipse.graphiti.features.impl.DefaultMoveBendpointFeature;
import org.eclipse.graphiti.features.impl.DefaultRemoveBendpointFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

import gov.sandia.dart.workflow.domain.Conductor;
import gov.sandia.dart.workflow.domain.Image;
import gov.sandia.dart.workflow.domain.Note;
import gov.sandia.dart.workflow.domain.Port;
import gov.sandia.dart.workflow.domain.Response;
import gov.sandia.dart.workflow.domain.ResponseArc;
import gov.sandia.dart.workflow.domain.WFArc;
import gov.sandia.dart.workflow.domain.WFNode;
import org.kobic.bioexpress.workbench.editor.features.AddImageFeature;
import org.kobic.bioexpress.workbench.editor.features.AddNoteFeature;
import org.kobic.bioexpress.workbench.editor.features.AddResponseArcFeature;
import org.kobic.bioexpress.workbench.editor.features.AddResponseFeature;
import org.kobic.bioexpress.workbench.editor.features.AddWFArcFeature;
import org.kobic.bioexpress.workbench.editor.features.AddWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.CopyWorkflowObjectsFeature;
import org.kobic.bioexpress.workbench.editor.features.CreateArcFeature;
import org.kobic.bioexpress.workbench.editor.features.DeleteConductorFeature;
import org.kobic.bioexpress.workbench.editor.features.DeletePortFeature;
import org.kobic.bioexpress.workbench.editor.features.DeleteWFArcFeature;
import org.kobic.bioexpress.workbench.editor.features.DeleteWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.DirectEditNoteFeature;
import org.kobic.bioexpress.workbench.editor.features.DirectEditParameterNameFeature;
import org.kobic.bioexpress.workbench.editor.features.DirectEditParameterValueFeature;
import org.kobic.bioexpress.workbench.editor.features.DirectEditResponseFeature;
import org.kobic.bioexpress.workbench.editor.features.DropResourceFeature;
import org.kobic.bioexpress.workbench.editor.features.LayoutImageFeature;
import org.kobic.bioexpress.workbench.editor.features.LayoutNoteFeature;
import org.kobic.bioexpress.workbench.editor.features.LayoutResponseFeature;
import org.kobic.bioexpress.workbench.editor.features.LayoutWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.MoveThingsOntoNotesFeature;
import org.kobic.bioexpress.workbench.editor.features.PasteWorkflowObjectFeature;
import org.kobic.bioexpress.workbench.editor.features.UpdateImageFeature;
import org.kobic.bioexpress.workbench.editor.features.UpdateNoteFeature;
import org.kobic.bioexpress.workbench.editor.features.UpdateResponseFeature;
import org.kobic.bioexpress.workbench.editor.features.UpdateWFArcFeature;
import org.kobic.bioexpress.workbench.editor.features.UpdateWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.features.WorkflowEditorSaveImageFeature;
import org.kobic.bioexpress.workbench.editor.features.WorkflowReconnectionFeature;
import gov.sandia.dart.workflow.util.ParameterUtils;

public class WorkflowFeatureProvider extends DefaultFeatureProvider {

	public WorkflowFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public IFeature[] getDragAndDropFeatures(IPictogramElementContext context) {
		// simply return all create connection features
		return getCreateConnectionFeatures();
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		Object newObject = context.getNewObject();
		if (newObject instanceof WFNode) {
			return new AddWFNodeFeature(this);
		} else if (newObject instanceof WFArc) {
			return new AddWFArcFeature(this);
		} else if (newObject instanceof ResponseArc) {
			return new AddResponseArcFeature(this);
		} else if (newObject instanceof Note) {
			return new AddNoteFeature(this);
		} else if (newObject instanceof Image) {
			return new AddImageFeature(this);
		} else if (newObject instanceof Response) {
			return new AddResponseFeature(this);
		} else {
			IResource resource = Platform.getAdapterManager().getAdapter(newObject, IResource.class);
			if (resource != null) {
				return new DropResourceFeature(resource, context.getTargetContainer(), this);
			}
		}
		return super.getAddFeature(context);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pictogramElement);
		if (pictogramElement instanceof ContainerShape) {
			if (bo instanceof WFNode) {
				return new UpdateWFNodeFeature(this);
			} else if (bo instanceof Response) {
				return new UpdateResponseFeature(this);
			} else if (bo instanceof Note) {
				return new UpdateNoteFeature(this);
			} else if (bo instanceof Image) {
				return new UpdateImageFeature(this);
			}
		} else if (pictogramElement instanceof Connection) {
			if (bo instanceof WFArc) {
				return new UpdateWFArcFeature(this);
			}
		}
		return super.getUpdateFeature(context);
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pictogramElement);
		if (bo instanceof WFNode) {
			return new LayoutWFNodeFeature(this);
		} else if (bo instanceof Note) {
			return new LayoutNoteFeature(this);
		} else if (bo instanceof Image) {
			return new LayoutImageFeature(this);
		} else if (bo instanceof Response) {
			return new LayoutResponseFeature(this);
		}
		return super.getLayoutFeature(context);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] { new CreateArcFeature(this) };
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IReconnectionContext context) {
		return new WorkflowReconnectionFeature(this);
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof Note) {
			return new DirectEditNoteFeature(this, (Note) bo);
		} else if (bo instanceof Response) {
			return new DirectEditResponseFeature(this);
		} else if (bo instanceof WFNode && ParameterUtils.isParameter((WFNode) bo)) {
			WFNode p = (WFNode) bo;
			if (isBrandNew(p)) {
				return new DirectEditParameterNameFeature(this);
			} else {
				return new DirectEditParameterValueFeature(this);
			}
		}
		return super.getDirectEditingFeature(context);
	}

	private boolean isBrandNew(WFNode p) {
		return "parameter".equals(ParameterUtils.getName(p)) && StringUtils.isEmpty(ParameterUtils.getValue(p));
	}

	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		Diagram dr = getDiagramTypeProvider().getDiagram();
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode) {
			//if(dr.getPstatus().startsWith("run") || dr.getPstatus().contains("PI-REG-0020")
			//		|| dr.getPstatus().contains("PI-REG-0030")) {
			if (dr.getPstatus().equals("run") || dr.getPstatus().equals("exec")) {
				return null;
			}
			return new DeleteWFNodeFeature(this);
		} else if (bo instanceof WFArc) {
			return new DeleteWFArcFeature(this);
		} else if (bo instanceof Port) {
			return new DeletePortFeature(this);
		} else if (bo instanceof Conductor) {
			return new DeleteConductorFeature(this);
		}
		return super.getDeleteFeature(context);
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof WFNode || bo instanceof Response || bo instanceof Note || bo instanceof Image) {
			return new MoveThingsOntoNotesFeature(this);
		}
		return super.getMoveShapeFeature(context);
	}

	@Override
	public ICopyFeature getCopyFeature(ICopyContext context) {
		CopyWorkflowObjectsFeature feature = new CopyWorkflowObjectsFeature(this);
		return feature.canCopy(context) ? feature : super.getCopyFeature(context);
	}

	@Override
	public IPasteFeature getPasteFeature(IPasteContext context) {
		PasteWorkflowObjectFeature feature = new PasteWorkflowObjectFeature(this);
		return feature.canPaste(context) ? feature : super.getPasteFeature(context);
	}

	@Override
	public IMoveBendpointFeature getMoveBendpointFeature(IMoveBendpointContext context) {
		return new DefaultMoveBendpointFeature(this);
	}

	@Override
	public IAddBendpointFeature getAddBendpointFeature(IAddBendpointContext context) {
		return new DefaultAddBendpointFeature(this);
	}

	@Override
	public IRemoveBendpointFeature getRemoveBendpointFeature(IRemoveBendpointContext context) {
		return new DefaultRemoveBendpointFeature(this);
	}

	@Override
	public ISaveImageFeature getSaveImageFeature() {
		return new WorkflowEditorSaveImageFeature(this);
	}

	@Override
	public IPrintFeature getPrintFeature() {
		return null;
	}

}
