package org.kobic.bioexpress.workbench.editor.utils;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.kobic.bioexpress.channel.client.file.FileUtilsClient;
import org.kobic.bioexpress.channel.client.file.FileUtilsClientImpl;
import org.kobic.bioexpress.model.file.FileModel;

public class CanvasUtil {
	public static void openEditor(FileModel fileModel) {

		if (fileModel.isIsFile()) {

			FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();

			System.out.println(fileModel.getPath());

			List<String> code = fileUtilsClient.readFile(fileModel.getPath());

			String script = null;
			for (String str : code) {
				if (script == null)
					script = str;
				else
					script = script + "\n" + str;
			}
			// open sh or python
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject("temp");
			String path = fileModel.getParentPath();
			String name = fileModel.getName();
			String[] strs = name.split("/");
			name = strs[strs.length - 1];
			IFolder parentF = createFolders(path, project);
//			
			IFile file = parentF.getFile(name);
			if (file.exists()) {
				try {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(script.getBytes());
					file.setContents(inputStream, true, true, null);
					inputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				try {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(script.getBytes());
					file.create(inputStream, true, null);
					inputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IEditorPart part = IDE.openEditor(page, file, false);
				part.addPropertyListener(new IPropertyListener() {
					boolean c = false;

					@Override
					public void propertyChanged(Object source, int propId) {
						// TODO Auto-generated method stub
						if (propId == 257) {
							if (c) {
								// save editor to server..
								TextEditor editor = (TextEditor) source;
								IDocumentProvider provider = editor.getDocumentProvider();
								IEditorInput input = editor.getEditorInput();
								IDocument document = provider.getDocument(input);
								String text = document.get();
								// System.out.println(text);
								FileUtilsClient fileUtilsClient = new FileUtilsClientImpl();
								System.out.println(fileModel.getPath());
								fileUtilsClient.writeFile(fileModel.getPath(), text);// readFile(fileModel.getPath());
								c = false;
							} else {
								c = true;
							}
						}
						System.out.println(source + " " + propId);
					}
				});

				page.addPartListener(new IPartListener() {

					@Override
					public void partOpened(IWorkbenchPart part) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partDeactivated(IWorkbenchPart part) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partClosed(IWorkbenchPart part) {
						// TODO Auto-generated method stub
						System.out.println("close " + part);
					}

					@Override
					public void partBroughtToTop(IWorkbenchPart part) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partActivated(IWorkbenchPart part) {
						// TODO Auto-generated method stub

					}
				});
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static IFolder createFolders(String path, IProject project) {
		String[] strs = path.split("/");
		System.out.println(path + " " + strs.length);
		IFolder parent = null;// project.getFolder(strs[1]);
		int idx = 0;
		for (String str : strs) {
			if (str.length() == 0) {
				idx++;
				continue;
			}
			if (parent == null) {
				try {
					parent = project.getFolder(str);
					if (!parent.exists())
						parent.create(true, true, null);
					idx++;
					continue;
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			IFolder subF = parent.getFolder(new Path(str));
			if (!subF.exists()) {
				try {
					subF.create(true, true, null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			parent = subF;
			idx++;
		}
		return parent;
	}

}
