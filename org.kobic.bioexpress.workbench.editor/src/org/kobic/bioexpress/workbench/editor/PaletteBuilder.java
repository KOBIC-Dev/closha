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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.IToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu.CascadingMenu;
import org.eclipse.jface.viewers.LabelProvider;
import org.kobic.bioexpress.channel.client.category.CategoryClient;
import org.kobic.bioexpress.channel.client.category.CategoryClientImpl;
import org.kobic.bioexpress.channel.client.program.ProgramClient;
import org.kobic.bioexpress.channel.client.program.ProgramClientImpl;
import org.kobic.bioexpress.model.category.CategoryModel;
import org.kobic.bioexpress.model.program.ProgramDataModel;
import org.kobic.bioexpress.model.program.ProgramModel;
import org.kobic.bioexpress.workbench.editor.configuration.NodeType;
import org.kobic.bioexpress.workbench.editor.configuration.WorkflowTypesManager;
import org.kobic.bioexpress.workbench.editor.extensions.IWorkflowEditorPaletteContributor;
import org.kobic.bioexpress.workbench.editor.features.CreateImageFeature;
import org.kobic.bioexpress.workbench.editor.features.CreateNoteFeature;
import org.kobic.bioexpress.workbench.editor.features.CreateWFNodeFeature;
import org.kobic.bioexpress.workbench.editor.library.UserCustomNodeLibrary;

public class PaletteBuilder {

	private static final String WORKFLOW_ORGANIZATION = "Workflow Organization";
	//private static final String USER_INTERFACE = "User Interface";

	public static final String USER_DEFINED = "User Defined";

	public IPaletteCompartmentEntry[] createPaletteEntries(IFeatureProvider featureProvider, URI diagramFile) {
		
		Map<String, PaletteCompartmentEntry> compartmentsByName = new HashMap<>();
		List<IPaletteCompartmentEntry> compartments = new ArrayList<>();
		//PaletteCompartmentEntry workflowOrganization = getCompartment(WORKFLOW_ORGANIZATION, compartments, compartmentsByName);
		//PaletteCompartmentEntry userInterface = getCompartment(USER_INTERFACE, compartments, compartmentsByName);

		ICreateFeature noteFeature = new CreateNoteFeature(featureProvider);   
		ObjectCreationToolEntry noteToolEntry = new ObjectCreationToolEntry(
				"note", "A descriptive note",
				null,
				null,
				noteFeature);
		//.addToolEntry(noteToolEntry);	
		//userInterface.addToolEntry(noteToolEntry);
		
		ICreateFeature imageFeature = new CreateImageFeature(featureProvider);   
		ObjectCreationToolEntry imageToolEntry = new ObjectCreationToolEntry(
				"image", "An image on the workflow canvas",
				null,
				null,
				imageFeature);
		//workflowOrganization.addToolEntry(imageToolEntry);	
		//userInterface.addToolEntry(imageToolEntry);
		
		WorkflowTypesManager manager = WorkflowTypesManager.get();
		//Map<String, NodeType> nodeTypes = manager.getNodeTypes();
		//get programs..
		
		String memberID = WorkflowEditorPlugin.getDefault().getMember().getMemberId();
		
		ProgramClient programClient =  new ProgramClientImpl();
		Map<String, Map<String, List<ProgramDataModel>>> rootMap = programClient.getProgramDataListForPallet(memberID);
		
		for( Map.Entry<String, Map<String, List<ProgramDataModel>>> elem1 : rootMap.entrySet() ){
			String cname = elem1.getKey();
			PaletteCompartmentEntry compartment = new PaletteCompartmentEntry(cname, null);
			//sort sub category..
			List<PaletteCompartmentEntry> subs = new ArrayList<PaletteCompartmentEntry>();
			for( Map.Entry<String, List<ProgramDataModel>> elem2 : elem1.getValue().entrySet() ){				
				String subCname = elem2.getKey();
				//System.out.println("====== "+subCname);
				PaletteCompartmentEntry subCompartment = new PaletteCompartmentEntry(subCname, null);
				List<ProgramDataModel> elem2s = elem2.getValue();
				elem2s.sort(new Comparator<ProgramDataModel>() {
					@Override
					public int compare(ProgramDataModel p1, ProgramDataModel p2) {
						try {
							return p1.getProgramName().compareToIgnoreCase(p2.getProgramName());
						} catch (RuntimeException e) {
							return 0;
						}
					}				
				});
				for (ProgramDataModel program : elem2s) {
					//System.out.println(program.getScriptType());
					String sType = "python";
					if(program.getScriptType().equals("Bash"))
						sType = "bash";
					if(program.getScriptType().equals("R"))
						sType = "rscript";
					NodeType node = new NodeType(program.getProgramName(), program.getRawID(), memberID, sType);
					if(program.isIsPublic()) {
						sType = sType+"_p";
					}
					ICreateFeature functionFeature = new CreateWFNodeFeature(featureProvider, node);   
					ObjectCreationToolEntry functionToolEntry = new ObjectCreationToolEntry(
							program.getProgramName(), program.getProgramDesc(),
							WorkflowImageProvider.PREFIX+sType,
							WorkflowImageProvider.PREFIX+sType,
							functionFeature);
					subCompartment.addToolEntry(functionToolEntry);
				}				
				//compartment.addSubEntry(subCompartment);
				subs.add(subCompartment);
			}
			subs.sort(new Comparator<PaletteCompartmentEntry>() {
				@Override
				public int compare(PaletteCompartmentEntry p1, PaletteCompartmentEntry p2) {
					try {
						return p1.getLabel().compareToIgnoreCase(p2.getLabel());
					} catch (RuntimeException e) {
						return 0;
					}
				}				
			});
			for(PaletteCompartmentEntry sub:subs)
				compartment.addSubEntry(sub);
			compartments.add(compartment);
			compartmentsByName.put(cname, compartment);
        }
		
		for (NodeType nodeType: UserCustomNodeLibrary.getNodes()) {
			List<String> categories = nodeType.getCategories();
			for (String category : categories) {
				PaletteCompartmentEntry compartment = getCompartment(category, compartments, compartmentsByName);
				String name = nodeType.getLabel();
				if (StringUtils.isEmpty(name))
					name = nodeType.getName();
				ICreateFeature functionFeature = new CreateWFNodeFeature(featureProvider, nodeType);   	    	
				// Using the description as a backdoor to allow us to find and delete the nodeType later. This is an ugly kludge, but not
				// Graphiti makes this very hard; this tool entry is pulled apart and discarded rather than actually being used on the palette.
				ObjectCreationToolEntry functionToolEntry = new ObjectCreationToolEntry(
						name, String.valueOf(System.identityHashCode(nodeType)),
						functionFeature.getCreateImageId(),
						functionFeature.getCreateLargeImageId(),
						functionFeature);
				compartment.addToolEntry(functionToolEntry);
			}
		}
		
		
		List<IWorkflowEditorPaletteContributor> extensions = getExtensions();
//		extensions.sort(new Comparator<IWorkflowEditorPaletteContributor>() {
//			@Override
//			public int compare(IWorkflowEditorPaletteContributor p1, IWorkflowEditorPaletteContributor p2) {
//				try {
//					IPaletteCompartmentEntry pp1 = p1.getPaletteEntry(featureProvider, diagramFile);
//					IPaletteCompartmentEntry pp2 = p2.getPaletteEntry(featureProvider, diagramFile);
//					return pp1.getLabel().compareToIgnoreCase(pp2.getLabel());
//				} catch (RuntimeException e) {
//					return 0;
//				}
//			}
//			
//		});
		for (IWorkflowEditorPaletteContributor extension: extensions) {
			IPaletteCompartmentEntry paletteEntry = extension.getPaletteEntry(featureProvider, diagramFile);			
			if (paletteEntry != null) {
				compartments.add(paletteEntry);
			}
		}
		//sort compartments..
		compartments.sort(new Comparator<IPaletteCompartmentEntry>() {
			@Override
			public int compare(IPaletteCompartmentEntry p1, IPaletteCompartmentEntry p2) {
				try {
					return p1.getLabel().compareToIgnoreCase(p2.getLabel());
				} catch (RuntimeException e) {
					return 0;
				}
			}				
		});
		for (IPaletteCompartmentEntry entry : compartments) {
			List<IToolEntry> tools = entry.getToolEntries();
			tools.sort(new Comparator<IToolEntry>() {
				@Override
				public int compare(IToolEntry p1, IToolEntry p2) {
					try {
						return p1.getLabel().compareToIgnoreCase(p2.getLabel());
					} catch (RuntimeException e) {
						return 0;
					}
				}				
			});
		}
				
		IPaletteCompartmentEntry[] entries = compartments.toArray(new IPaletteCompartmentEntry[compartments.size()]);
		Arrays.sort(entries, new Comparator<IPaletteCompartmentEntry>() {
			@Override
			public int compare(IPaletteCompartmentEntry o1,
					IPaletteCompartmentEntry o2) {
				return o1.getLabel().compareToIgnoreCase(o2.getLabel());
			}
		});
		return entries;
	}

	private PaletteCompartmentEntry getCompartment(String category,
			List<IPaletteCompartmentEntry> compartments, Map<String, PaletteCompartmentEntry> compartmentsByName) {
		PaletteCompartmentEntry entry = compartmentsByName.get(category);
		if (entry == null) {
			entry = new PaletteCompartmentEntry(category, null);
			compartments.add(entry);
			compartmentsByName.put(category, entry);
			entry.setInitiallyOpen("Engineering".equals(category));
		}
		return entry;		
	}
	
	private List <IWorkflowEditorPaletteContributor> getExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(WorkflowEditorPlugin.PLUGIN_ID, "paletteContributor");
		IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
		 List <IWorkflowEditorPaletteContributor> contributors = new ArrayList<>();
		for (int el = 0; el < elements.length; el++) {
			IConfigurationElement config = elements[el];
			if (config.getName().equals("contributor")) {				
				try {
					IWorkflowEditorPaletteContributor contributor = (IWorkflowEditorPaletteContributor) config.createExecutableExtension("class");
					contributors.add(contributor);
				} catch (Exception e) {
					WorkflowEditorPlugin.getDefault().logError("Can't create paletteContributor", e);
				}
			}
		}
		return contributors;
	}

	@SuppressWarnings("restriction")
	public static ArrayList<Object> buildMenu(Set<String> nodeTypeNames) {
		WorkflowTypesManager manager = WorkflowTypesManager.get();
		Map<String, NodeType> nodeTypes = manager.getNodeTypes();
		Map<String, Set<String>> compartments = new TreeMap<>();

		for (NodeType nodeType: nodeTypes.values()) {
			// These are added back elsewhere
			List<String> categories = nodeType.getCategories();
			if (categories.size() == 1 && "Uncategorized".equals(categories.get(0))) {
				continue;
			}
			for (String category : categories) {
				Set<String> compartment = getSubmenu(category, compartments);
				String name = nodeType.getName();
				compartment.add(name);
			}
		}
		compartments.get(WORKFLOW_ORGANIZATION).add("note");
		compartments.get(WORKFLOW_ORGANIZATION).add("image");
		compartments.get(WORKFLOW_ORGANIZATION).add("response");
		
		//compartments.get(USER_INTERFACE).add("note");
		//compartments.get(USER_INTERFACE).add("image");

		
		ArrayList<Object> aContent = new ArrayList<>();
		for (String cName: compartments.keySet()) {
			Set<String> compartment = compartments.get(cName);
			PopupMenu pm = new PopupMenu(new ArrayList<>(compartment), new LabelProvider()); 
			CascadingMenu cm = new CascadingMenu(cName, pm);
			aContent.add(cm);
		}
		
		return aContent;
	}

	private static Set<String> getSubmenu(String category, Map<String, Set<String>> compartments) {
		Set<String> set = compartments.get(category);
		if (set == null) {
			set = new TreeSet<>();
			compartments.put(category, set);
		}
		return set;
	}
}
