/*******************************************************************************
 * Sandia Analysis Workbench Integration Framework (SAW)
 * Copyright 2019 National Technology & Engineering Solutions of Sandia, LLC (NTESS).
 * Under the terms of Contract DE-NA0003525 with NTESS, the U.S. Government retains
 * certain rights in this software.
 *  
 * This software is distributed under the Eclipse Public License.  For more
 * information see the files copyright.txt and license.txt included with the software.
 ******************************************************************************/
package org.kobic.bioexpress.workbench.editor.settings;

import java.util.Arrays;

import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import gov.sandia.dart.workflow.domain.Property;
import org.kobic.bioexpress.workbench.editor.WorkflowEditorPlugin;
import org.kobic.bioexpress.workbench.editor.configuration.Prop;

public class PropertiesEditingSupport extends EditingSupport {

        public static final int EDITED = 9999;
		private static final String[] booleanValues = {"false", "true"};
		private TableViewer viewer;
        private CellEditor editor1, editor2;
		private int column;

        public PropertiesEditingSupport(TableViewer viewer, int column) {
                super(viewer);
                this.viewer = viewer;
				switch (column) {
					case 0:
						this.editor1 = new TextCellEditor(viewer.getTable());
						break;
					case 1:						
						this.editor1 = new ComboBoxCellEditor(viewer.getTable(), Prop.availableTypes());
						break;
					case 2:
						this.editor1 = new TextCellEditor(viewer.getTable());
						this.editor2 = new ComboBoxCellEditor(viewer.getTable(), booleanValues);
						break;
					case 3:
						this.editor1 = new ComboBoxCellEditor(viewer.getTable(), booleanValues);
						break;


				}
                this.column = column;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
        	if (column == 2) {
        		String type = ((Property) element).getType();
        		if ("boolean".equals(type))
        			return editor2;
        	}            
        	return editor1;
        }

        @Override
        protected boolean canEdit(Object element) {
                return true;
        }

        @Override
        protected Object getValue(Object element) {
        	Object result = null;	
        	String type = ((Property) element).getType();
        	switch (column) {
        		case 0:        		
                    result = ((Property) element).getName();
                    break;
        		case 1:
        			String[] labels = Prop.availableTypes();
        			result = Arrays.binarySearch(labels, ((Property) element).getType());
                    break;
        		case 2:  
        			String value = ((Property) element).getValue();
                    if ("boolean".equals(type)) {
        				result = "true".equals(value) ? 1 : 0;
        			} else {
        				result = value;
        			}
        			break;
           		case 3:        			
        			result = ((Property) element).isAdvanced() ? 1 : 0;
                    break;

        		}
        		return result == null ? "" : result;        			
        }

        @Override
        protected void setValue(Object element, Object userInputValue) {          
        	try {
        		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(element);
        		domain.getCommandStack().execute(new RecordingCommand(domain) {

        			@Override
        			protected void doExecute() {
        				String type = ((Property) element).getType();
        				switch (column) {
        				case 0:					
        					((Property) element).setName(String.valueOf(userInputValue));
        					((Property) element).getNode().eNotify(new NotificationImpl(EDITED, 0, 0));
        					break;
        				case 1: {
                			String[] labels = Prop.availableTypes();
                			int index = (Integer) userInputValue;
                			if (index > -1 && index < labels.length) {
                				((Property) element).setType(labels[index]);
            					((Property) element).getNode().eNotify(new NotificationImpl(EDITED, 0, 0));
                			}
        					break;
        				}
        				case 2:			
                            if ("boolean".equals(type)) {
        						int index = (Integer) userInputValue;
        						((Property) element).setValue(Boolean.toString(index == 1));
        					} else {
        						((Property) element).setValue(String.valueOf(userInputValue));
        						((Property) element).getNode().eNotify(new NotificationImpl(EDITED, 0, 0));
        					}
        					break;
        				case 3: {	
                			int index = (Integer) userInputValue;
        					((Property) element).setAdvanced(index == 1);
        					((Property) element).getNode().eNotify(new NotificationImpl(EDITED, 0, 0));
        					break;
        				}
        				}                    
        			}
        		});

        		viewer.update(element, null);
        	} catch (Exception ex) {
        		WorkflowEditorPlugin.getDefault().logError("Error setting object property", ex);
        	}
        }
}
