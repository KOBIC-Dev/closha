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
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.ui.platform.AbstractImageProvider;
 
public class WorkflowImageProvider extends AbstractImageProvider {
 
    public static final String PREFIX =  "ngw.";
 
    public static final String IMG_GEAR = PREFIX + "gear";
    public static final String IMG_LOOP = PREFIX + "loop";
    public static final String IMG_PALETTE = PREFIX + "palette";
    public static final String IMG_WORKFLOW = PREFIX + "nestedWorkflow";
    public static final String IMG_INTERNAL_WORKFLOW = PREFIX + "nestedInternalWorkflow";
    public static final String IMG_REMOTE_WORKFLOW = PREFIX + "remoteNestedWorkflow";

    public static final String IMG_DUPLICATE = PREFIX + "duplicate";
	public static final String IMG_PORTS = PREFIX + "ports";
	public static final String IMG_PLUG = PREFIX + "plug";
	public static final String IMG_PACKAGE = PREFIX + "pkg";
	public static final String IMG_GLOBE = PREFIX + "globe";

	public static final String IMG_PORT = PREFIX + "port";
	
	//TODO : BIOEXPRESS
	public static final String IMG_START = PREFIX + "start";
	public static final String IMG_STOP = PREFIX + "stop";
	public static final String IMG_INFO = PREFIX + "info";
	public static final String IMG_EDIT = PREFIX + "edit";
	public static final String IMG_START_DIS = PREFIX + "start_dis";
	public static final String IMG_STOP_DIS = PREFIX + "stop_dis";
	public static final String IMG_INFO_DIS = PREFIX + "info_dis";
	public static final String IMG_EDIT_DIS = PREFIX + "edit_dis";
	
	public static final String IMG_INPUT = PREFIX + "input";
	public static final String IMG_INPUT_FOLDER = PREFIX + "input_folder";
	public static final String IMG_INPUT_FILE = PREFIX + "input_file";
	public static final String IMG_INPUT_STRING = PREFIX + "input_string";
	public static final String IMG_INPUT_INTEGER = PREFIX + "input_integer";
	public static final String IMG_OUTPUT = PREFIX + "output";
	public static final String IMG_OUTPUT_FOLDER = PREFIX + "output_folder";
	public static final String IMG_OUTPUT_FILE = PREFIX + "output_file";
	public static final String IMG_OUTPUT_STRING = PREFIX + "output_string";
	public static final String IMG_OUTPUT_INTEGER = PREFIX + "output_integer";
	
	public static final String IMG_PALETTE_ROOT = PREFIX + "palette_root";
	public static final String IMG_PALETTE_SUB = PREFIX + "palette_sub";
	public static final String IMG_PALETTE_TITLE = PREFIX + "palette_title";
	

	private static WorkflowImageProvider INSTANCE = null;
	
	public WorkflowImageProvider() {
		if(INSTANCE == null) {
			INSTANCE = this;
		}
	}
 
    @Override
    protected void addAvailableImages() {
        addImageFilePath(IMG_GEAR, "icons/gear.gif");
        addImageFilePath(IMG_LOOP, "icons/loop.png");
        addImageFilePath(IMG_PALETTE, "icons/palette.png");
        addImageFilePath(IMG_WORKFLOW, "icons/iwf_file3.png");
        addImageFilePath(IMG_INTERNAL_WORKFLOW, "icons/iwf_file3.png");
        addImageFilePath(IMG_REMOTE_WORKFLOW, "icons/iwf_file3.png");

        addImageFilePath(IMG_DUPLICATE, "icons/duplicate.png");
        addImageFilePath(IMG_PORTS, "icons/ports.gif");
        addImageFilePath(IMG_PORT, "icons/port.gif");
        
        addImageFilePath(IMG_START, "icons/menu_start.png");
        addImageFilePath(IMG_STOP, "icons/menu_stop.png");
        addImageFilePath(IMG_INFO, "icons/menu_program.png");
        addImageFilePath(IMG_EDIT, "icons/menu_script.png");
        addImageFilePath(IMG_START_DIS, "icons/menu_start_dis.png");
        addImageFilePath(IMG_STOP_DIS, "icons/menu_stop_dis.png");
        addImageFilePath(IMG_INFO_DIS, "icons/menu_program_dis.png");
        addImageFilePath(IMG_EDIT_DIS, "icons/menu_script_dis.png");
        
        
        addImageFilePath(IMG_PLUG, "icons/plug.png");
        addImageFilePath(IMG_PACKAGE, "icons/package.png");
        addImageFilePath(IMG_GLOBE, "icons/globe.gif");

        addImageFilePath(PREFIX + "add", "icons/add.png");
        addImageFilePath(PREFIX + "fail", "icons/fail.png");
        addImageFilePath(PREFIX + "subtract", "icons/subtract.png");
        addImageFilePath(PREFIX + "multiply", "icons/multiply.png");
        addImageFilePath(PREFIX + "divide", "icons/divide.png");        
        addImageFilePath(PREFIX + "compare", "icons/compare.png");        
        addImageFilePath(PREFIX + "constant", "icons/constant.png");        
        addImageFilePath(PREFIX + "print", "icons/print.gif");        
        addImageFilePath(PREFIX + "file", "icons/file.png");
        addImageFilePath(PREFIX + "folder", "icons/folder.png");        
        addImageFilePath(PREFIX + "externalProcess", "icons/externalProcess.png");        
        addImageFilePath(PREFIX + "cubit", "icons/cubit.gif");
        addImageFilePath(PREFIX + "bashScript", "icons/shell.png");
        addImageFilePath(PREFIX + "pythonScript", "icons/python.png");
        addImageFilePath(PREFIX + "cubitPythonScript", "icons/cubitpython.png");
        addImageFilePath(PREFIX + "tclScript", "icons/tclScript.png");
        addImageFilePath(PREFIX + "openFile", "icons/file.png");
        addImageFilePath(PREFIX + "prompt", "icons/ask.png");       
        addImageFilePath(PREFIX + "ask_yes_no", "icons/ask.png");
        addImageFilePath(PREFIX + "sierra", "icons/sierra.gif");
        addImageFilePath(PREFIX + "blackHole", "icons/blackHole.png");

        addImageFilePath(PREFIX + "parameterFile", "icons/properties.png");
        addImageFilePath(PREFIX + "or", "icons/or.png");
        
        //TODO : BIOEXPRESS
        addImageFilePath(PREFIX + "python", "icons/python.png");
        addImageFilePath(PREFIX + "bash", "icons/bash.png");
        addImageFilePath(PREFIX + "rscript", "icons/rscript.png");
        //
        addImageFilePath(PREFIX + "python_p", "icons/python_p.png");
        addImageFilePath(PREFIX + "bash_p", "icons/bash_p.png");
        addImageFilePath(PREFIX + "rscript_p", "icons/rscript_p.png");
        
        addImageFilePath(PREFIX + "input", "icons/input.png");
        addImageFilePath(PREFIX + "input_folder", "icons/input_folder.png");
        addImageFilePath(PREFIX + "input_file", "icons/input_file.png");
        addImageFilePath(PREFIX + "input_string", "icons/input_string.png");
        addImageFilePath(PREFIX + "input_integer", "icons/input_integer.png");
        addImageFilePath(PREFIX + "output", "icons/output.png");
        addImageFilePath(PREFIX + "output_folder", "icons/output_folder.png");
        addImageFilePath(PREFIX + "output_file", "icons/output_file.png");
        addImageFilePath(PREFIX + "output_string", "icons/output_string.png");
        addImageFilePath(PREFIX + "output_integer", "icons/output_integer.png");
        //Palette Images.
        addImageFilePath(PREFIX + "palette_root", "icons/palette_root_category.png");
        addImageFilePath(PREFIX + "palette_sub", "icons/palette_sub_category.png");
        addImageFilePath(PREFIX + "palette_title", "icons/palette_title.png");        


   	 	IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor("org.kobic.bioexpress.workbench.editor.nodeIcon" );                      
   	 	for (final IConfigurationElement extension : extensions) {   
   	 		String contributorBundleName = extension.getContributor().getName();
   	 		String nodeType = extension.getAttribute("nodeType" );
   	 		String iconPath = extension.getAttribute("iconPath" );
   	 		String templateIconPath = "platform:/plugin/" + contributorBundleName +"/"+ iconPath;
   	 		addImageFilePath( PREFIX + nodeType, templateIconPath );    
   	 	}
    }

	public static WorkflowImageProvider get() {
		if(INSTANCE == null) {
			new WorkflowImageProvider();
		}
		return INSTANCE;
	}	
}
