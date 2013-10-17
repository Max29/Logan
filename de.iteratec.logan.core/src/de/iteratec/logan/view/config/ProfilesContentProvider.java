package de.iteratec.logan.view.config;

import java.util.List;

import com.google.common.collect.Lists;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class ProfilesContentProvider implements ITreeContentProvider {

  private List<Profile> profiles = Lists.newArrayList();

  @Override
  public void dispose() {
  }

  @Override
  @SuppressWarnings("unchecked")
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.profiles = (List<Profile>) newInput;
  }

  @Override
  public Object[] getElements(Object inputElement) {
    return profiles.toArray();
  }

  @Override
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof Profile) {
      Profile profile = (Profile) parentElement;
      return profile.getExpressions().toArray();
    }

    return null;
  }

  @Override
  public Object getParent(Object element) {
    if (element instanceof Expression) {
      Expression expression = (Expression) element;

      for (Profile profile : profiles) {
        if (profile.getExpressions().contains(expression)) {
          return profile;
        }
      }
    }

    return null;
  }

  @Override
  public boolean hasChildren(Object element) {
    if (element instanceof Profile) {
      return true;
    }
    return false;
  }

  public List<Profile> getProfiles() {
    return profiles;
  }

}
