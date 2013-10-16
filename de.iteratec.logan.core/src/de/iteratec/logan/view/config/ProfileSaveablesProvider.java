package de.iteratec.logan.view.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.iteratec.logan.model.Profile;


public class ProfileSaveablesProvider {
  private Map<Profile, ProfileSaveable>         mapping  = new HashMap<Profile, ProfileSaveable>();
  private static final ProfileSaveablesProvider INSTANCE = new ProfileSaveablesProvider();

  public static ProfileSaveablesProvider getInstance() {
    return INSTANCE;
  }

  public ProfileSaveable addMapping(Profile profile) {
    ProfileSaveable profileSaveable = new ProfileSaveable(profile);
    mapping.put(profile, profileSaveable);

    return profileSaveable;
  }

  public void removeMapping(Profile profile) {
    mapping.remove(profile);
  }

  public ProfileSaveable get(Profile profile) {
    return mapping.get(profile);
  }

  public ProfileSaveable[] getSaveables() {
    Collection<ProfileSaveable> values = mapping.values();

    return values.toArray(new ProfileSaveable[values.size()]);
  }

  public boolean isAnyDirty() {
    for (ProfileSaveable profileSaveable : getSaveables()) {
      if (profileSaveable.isDirty()) {
        return true;
      }
    }

    return false;
  }

  public boolean isDirty(Profile profile) {
    ProfileSaveable profileSaveable = mapping.get(profile);

    if (profileSaveable != null) {
      return profileSaveable.isDirty();
    }

    return false;
  }
}
