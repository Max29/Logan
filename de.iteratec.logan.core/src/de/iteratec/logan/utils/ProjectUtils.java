package de.iteratec.logan.utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;


/**
 * An Utils class for various project related tasks.
 * 
 * @author agusevas
 */
public class ProjectUtils {
  public static final String VIRTUAL_PROJECT_NAME = "External Files"; //$NON-NLS-1$

  public static void clearVirtualLinks() throws CoreException {
    IWorkspace ws = ResourcesPlugin.getWorkspace();
    IProject project = ws.getRoot().getProject(VIRTUAL_PROJECT_NAME);
    if (!project.exists()) {
      return;
    }

    IResource[] members = project.members();
    for (IResource iResource : members) {
      IPath location = iResource.getLocation();
      boolean exists = location != null && location.toFile().exists();
      if (iResource.isLinked() && !exists) {
        iResource.delete(true, null);
      }
    }
  }

  public static IProject openProject() throws CoreException {
    IWorkspace ws = ResourcesPlugin.getWorkspace();
    IProject project = ws.getRoot().getProject(VIRTUAL_PROJECT_NAME);
    if (!project.exists()) {
      project.create(null);
    }
    if (!project.isOpen()) {
      project.open(null);
    }

    return project;
  }

  public static IFile addFileToProject(IProject project, String filePath) throws CoreException {
    IPath location = new Path(filePath);
    IFile file = project.getFile(location.lastSegment());
    file.createLink(location, IResource.REPLACE, null);

    return file;
  }
}
