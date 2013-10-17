package de.iteratec.logan.common.model;

import java.util.List;

import com.google.common.collect.Lists;


public class ViewModel extends AbstractModelObject {
  private List<Profile> profiles = Lists.newArrayList();

  public List<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    firePropertyChange("profiles", this.profiles, this.profiles = profiles); //$NON-NLS-1$
  }
}
