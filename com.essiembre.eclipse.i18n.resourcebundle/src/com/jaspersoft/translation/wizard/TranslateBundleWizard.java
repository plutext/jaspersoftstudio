/*******************************************************************************
 * Copyright (C) 2010 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, 
 * the following license terms apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jaspersoft Studio Team - initial API and implementation
 ******************************************************************************/
package com.jaspersoft.translation.wizard;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.CreateFolderOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import com.essiembre.eclipse.rbe.RBEPlugin;
import com.essiembre.eclipse.rbe.model.bundle.PropertiesGenerator;
import com.essiembre.eclipse.rbe.model.workbench.RBEPreferences;
import com.essiembre.eclipse.rbe.ui.wizards.ResourceBundleNewWizardPage;
import com.jaspersoft.translation.resources.AbstractResourceDefinition;
import com.jaspersoft.translation.resources.TranslationProjectNature;

/**
 * This wizard allow to start a translation of one or more properties resources
 * provided by extension point and selected by the user. The selected file are copied
 * and can be opened to be edited.
 * 
 * @author Orlandin Marco
 */
public class TranslateBundleWizard extends Wizard implements INewWizard {
	
	/**
	 * Page where the file that can be imported are shown
	 */
	private GetinputNewWizardPage page0;
	
	/**
	 * Page where the user choose the translation languages, the project folder and so on
	 */
    private TranslationBundleNewWizardPage page1;
    
    /**
     * Eventually can contain the selected project
     */
    private ISelection selection;
    
    /**
     * The reference to the first file created, that will be opened in the editor
     */
    private IFile firstFileCreated;

    /**
     * Constructor for ResourceBundleWizard.
     */
    public TranslateBundleWizard() {
        super();
        setNeedsProgressMonitor(true);
		setWindowTitle(RBEPlugin.getString("editor.wiz.window.title"));
    }
    
    /**
     * Adding the page to the wizard.
     */
    public void addPages() {
        page0= new GetinputNewWizardPage("Resource slection");
        addPage(page0);
        
        page1 = new TranslationBundleNewWizardPage(selection);
        addPage(page1);
    }
    
    /**
     * Fill a just created properties file with all the element inside the properties
     * source. In short it mirror the original source
     * 
     * @param file the just created properties file
     * @param sourceResource the source of all the properties
     */
    private void fillFile(IFile file, AbstractResourceDefinition sourceResource){
    	Properties languageFile = new Properties();
    	URL propertiesFile;
		try {
			propertiesFile = file.getLocation().toFile().toURI().toURL();
	    	languageFile.load(propertiesFile.openStream());
	    	String[] keys = sourceResource.getKeys();
	    	for(String key : keys){
	    		languageFile.put(key, sourceResource.getValue(key));
	    	}
			String packageName = sourceResource.getPackageName() != null ? sourceResource.getPackageName() + ":" : ""; 
	    	String comment = "source="+packageName+sourceResource.getFileName();
	    	languageFile.store(new FileOutputStream(file.getLocation().toFile()), comment);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
	/**
	 * Creates a folder resource handle for the folder with the given workspace
	 * path. This method does not create the folder resource; this is the
	 * responsibility of <code>createFolder</code>.
	 * 
	 * @param folderPath
	 *            the path of the folder resource to create a handle for
	 * @return the new folder resource handle
	 * @see #createFolder
	 */
	protected IFolder createFolderHandle(IPath folderPath) {
		return IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFolder(
				folderPath);
	}
    
	/**
	 * Creates a new folder resource in the selected container and with the
	 * selected name. Creates any missing resource containers along the path;
	 * does nothing if the container resources already exist.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on this page currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this page caches the new folder once it has been successfully
	 * created; subsequent invocations of this method will answer the same
	 * folder resource without attempting to create it again.
	 * </p>
	 * <p>
	 * This method should be called within a workspace modify operation since it
	 * creates resources.
	 * </p>
	 * 
	 * @return the created folder resource, or <code>null</code> if the folder
	 *         was not created
	 */
	public IFolder createNewFolder(String containerName, String folderName, IProgressMonitor monitor) {
		// create the new folder and cache it if successful
		final IPath containerPath = new Path(containerName);
		IPath newFolderPath = containerPath.append(folderName);
		final IFolder newFolderHandle = createFolderHandle(newFolderPath);
			AbstractOperation op;
			op = new CreateFolderOperation(newFolderHandle, null, false, null, IDEWorkbenchMessages.WizardNewFolderCreationPage_title);
			try {
				op.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
			} catch (final ExecutionException e) {
				getContainer().getShell().getDisplay().syncExec(
						new Runnable() {
							public void run() {
								if (e.getCause() instanceof CoreException) {
									ErrorDialog.openError(getContainer().getShell(), IDEWorkbenchMessages.WizardNewFolderCreationPage_errorTitle, null, ((CoreException) e.getCause()).getStatus());
								} else {
									IDEWorkbenchPlugin.log(getClass(), "createNewFolder()", e.getCause()); //$NON-NLS-1$
									MessageDialog.openError(getContainer().getShell(), IDEWorkbenchMessages.WizardNewFolderCreationPage_internalErrorTitle, NLS.bind(IDEWorkbenchMessages.WizardNewFolder_internalError, e.getCause().getMessage()));
								}
							}
						});
			}
		return newFolderHandle;
	}
	
	/**
	 * Take a project name and if it is note present then it is created. the new project
	 * will have the translation project nature
	 * 
	 * @param projectName name of the project
	 * @param monitor
	 */
	private void checkAndCreatePrject(String projectName, IProgressMonitor monitor){
		IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!prj.exists()){
			IProjectDescription desc = prj.getWorkspace().newProjectDescription(prj.getName());
			try {
				prj.create(desc, null);
				if (!prj.isOpen()) prj.open(null);
				TranslationProjectNature.createJRProject(monitor, prj);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * This method is called when 'Finish' button is pressed in
     * the wizard. For each resource selected in the first step it creates
     * a properties file in the specified project (the project is created if it dosen't exist)
     * Every new properties file will be filled with all the properties that are also in its source
     */
    public boolean performFinish() {
        final String containerName = page1.getContainerName();
        //final String baseName = page1.getFileName();
        final String[] locales = page1.getLocaleStrings();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    monitor.worked(1);
                    monitor.setTaskName(RBEPlugin.getString("editor.wiz.creating")); //$NON-NLS-1$
                    //Check if the selected project exist, otherwise it is created
                    checkAndCreatePrject(containerName, monitor);
                    firstFileCreated = null;
                    for(AbstractResourceDefinition resource : getSelectedResource()){
                        IFile file = null;
                        for (int i = 0; i <  locales.length; i++) {
                            String fileName = resource.getFileNameWithoutExtension();
                            if (locales[i].equals(ResourceBundleNewWizardPage.DEFAULT_LOCALE)) {
                                fileName += ".properties"; //$NON-NLS-1$
                            } else {
                                fileName += "_" + locales[i] + ".properties";  //$NON-NLS-1$
                            } 
                            if (page1.needToCreateFolder()){
                            	String pluginName = resource.getPluginName();
                            	//create a folder with the plugin name
                            	IFolder folder = createNewFolder(containerName, pluginName, monitor);
                            	//create a folder with the package name, if it is defined
                            	if (resource.getPackageName() != null){
                            		folder = createNewFolder(folder.getFullPath().toString(), resource.getPackageName(), monitor);
                            	}
                            	file = createFile(folder.getFullPath(), fileName, resource, monitor);
                            } else file = createFile(containerName, fileName, resource, monitor);
                            if (firstFileCreated == null) firstFileCreated = file;
                        }
                    }
                    /*getShell().getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            IWorkbenchPage wbPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            try {
                            	IDE.openEditor(wbPage, firstFileCreated, "com.essiembre.eclipse.rbe.ui.editor.ResourceBundleEditor",true);
                            } catch (PartInitException e) {
                            }
                        }
                    });*/
                    monitor.worked(1);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } catch (Exception e){
                	e.printStackTrace();
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), 
                    "Error", realException.getMessage()); //$NON-NLS-1$
            return false;
        }
        return true;
    }
    
    /**
     * The worker method. It will find the container, create the
     * file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */
    protected IFile createFile(IPath parentPath, String fileName, AbstractResourceDefinition sourceResource, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask(RBEPlugin.getString("editor.wiz.creating") + fileName, 2); //$NON-NLS-1$
        IResource resource =  ResourcesPlugin.getWorkspace().getRoot().findMember(parentPath);
        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + parentPath.toString() + "\" does not exist."); //$NON-NLS-1$
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        try {
            InputStream stream = openContentStream();
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
            fillFile(file, sourceResource);
        } catch (IOException e) {
        }
        return file;
    }
    
    /**
     * The worker method. It will find the container, create the
     * file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */
    protected IFile createFile(String containerName, String fileName, AbstractResourceDefinition sourceResource, IProgressMonitor monitor) throws CoreException {     
        monitor.beginTask(RBEPlugin.getString("editor.wiz.creating") + fileName, 2); //$NON-NLS-1$
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(containerName));
        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + containerName  + "\" does not exist."); //$NON-NLS-1$ //$NON-NLS-1$
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        try {
            InputStream stream = openContentStream();
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
            fillFile(file, sourceResource);
        } catch (IOException e) {
        }
        return file;
    }
    
    /**
     * We will initialize file contents with a sample text.
     */
    private InputStream openContentStream() {
        String contents = ""; //$NON-NLS-1$
        if (RBEPreferences.getShowGenerator()) {
            contents = PropertiesGenerator.GENERATED_BY;
        }
        return new ByteArrayInputStream(contents.getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, 
                "com.jaspersoft.studio.resourceBundleEditor.i18n.resourcebundle",  //$NON-NLS-1$
                IStatus.OK, message, null);
        throw new CoreException(status);
    }
    
    /**
     * Return the resources selected in the first step
     * 
     * @return a not null array of all the selected resources at the end of this wizard step
     */
    public List<AbstractResourceDefinition> getSelectedResource(){
    	return page0.getSelectedResource();
    }

    /**
     * We will accept the selection in the workbench to see if
     * we can initialize from it.
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(
            IWorkbench workbench, IStructuredSelection structSelection) {
        this.selection = structSelection;
    }
}