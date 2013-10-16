/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package de.iteratec.logan.actions;

import java.io.File;

import de.iteratec.logan.editor.CustomTextEditor;
import de.iteratec.logan.service.IOUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.part.FileEditorInput;


public class OpenFileAction extends Action implements IWorkbenchWindowActionDelegate {

  private static final String VIRTUAL_PROJECT_NAME = "External Files"; //$NON-NLS-1$
  private IWorkbenchWindow    fWindow;
  private String              filterPath;

  public OpenFileAction() {
    setEnabled(true);
  }

  @Override
  public void dispose() {
    fWindow = null;
  }

  @Override
  public void init(IWorkbenchWindow window) {
    fWindow = window;
  }

  @Override
  public void run(IAction action) {
    run();
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
  }

  @Override
  public void run() {
    IFile file = getFile();
    if (file != null) {
      IWorkbenchPage page = fWindow.getActivePage();
      try {
        page.openEditor(new FileEditorInput(file), CustomTextEditor.ID);
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
  }

  private IFile getFile() {
    try {
      IWorkspace ws = ResourcesPlugin.getWorkspace();
      IProject project = ws.getRoot().getProject(VIRTUAL_PROJECT_NAME);
      if (!project.exists()) {
        project.create(null);
      }
      if (!project.isOpen()) {
        project.open(null);
      }

      FileDialog fileDialog = new FileDialog(fWindow.getShell(), SWT.OPEN);
      fileDialog.setFilterPath(filterPath);
      String selectedFileName = fileDialog.open();
      if (selectedFileName == null) {
        return null;
      }

      if (IOUtils.isZipFile(selectedFileName)) {
        File combinedFile = IOUtils.combineZipFileEntries(selectedFileName);
        selectedFileName = combinedFile.getAbsolutePath();
      }

      filterPath = new File(selectedFileName).getParent();
      IPath location = new Path(selectedFileName);
      IFile file = project.getFile(location.lastSegment());
      file.createLink(location, IResource.REPLACE, null);

      return file;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
