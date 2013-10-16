/**
 * 
 */
package de.iteratec.logan.view.config;

import java.io.File;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.Messages;
import de.iteratec.logan.model.Profile;
import de.iteratec.logan.service.ProfilePersister;
import de.iteratec.logan.view.SearchView;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.Saveable;


/**
 * @author Arturas
 */
public class ProfileSaveable extends Saveable {
  private boolean       dirty;
  private final Profile profile;
  private File          profileFile;

  public ProfileSaveable(Profile profile) {
    this.profile = profile;
  }

  public Profile getProfile() {
    return profile;
  }

  public File getProfileFile() {
    return profileFile;
  }

  public void setProfileFile(File profileFile) {
    this.profileFile = profileFile;
  }

  @Override
  public String getName() {
    return profile.getName();
  }

  @Override
  public String getToolTipText() {
    return profile.getDescription();
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return null;
  }

  @Override
  public void doSave(IProgressMonitor monitor) throws CoreException {
    String profileName = profile.getName();
    File file = profileFile;
    if (file != null) {
      ProfilePersister.getInstance().write(profile, file);
      setDirty(false);
    }
    else {
      String title = Messages.SearchView_saveProfile + profileName;
      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      file = AppUtils.saveFile(shell, title, profileName, SearchView.getProfilesDirectoryPath());
      if (file != null) {
        ProfilePersister.getInstance().write(profile, file);
        setProfileFile(file);
        setDirty(false);
      }
    }
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean equals(Object object) {
    return this == object;
  }

  @Override
  public int hashCode() {
    return profile.hashCode();
  }

}
