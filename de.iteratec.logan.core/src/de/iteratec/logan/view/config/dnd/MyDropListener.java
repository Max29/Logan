package de.iteratec.logan.view.config.dnd;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.view.SearchView;
import de.iteratec.logan.view.config.ProfilesContentProvider;

import org.eclipse.swt.dnd.TransferData;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;


public class MyDropListener extends ViewerDropAdapter {

  private final CheckboxTreeViewer viewer;
  private final SearchView         searchView;
  private Profile                  targetProfile;

  public MyDropListener(CheckboxTreeViewer viewer, SearchView searchView) {
    super(viewer);
    this.viewer = viewer;
    this.searchView = searchView;
  }

  @Override
  public boolean performDrop(Object data) {
    if (data instanceof String) {
      int hashCode = Integer.parseInt((String) data);
      ProfilesContentProvider contentProvider = (ProfilesContentProvider) viewer.getContentProvider();
      Expression sourceExpression = null;
      Profile sourceProfile = null;

      for (Profile profile : contentProvider.getProfiles()) {
        for (Expression expression : profile.getExpressions()) {
          if (expression.hashCode() == hashCode) {
            sourceExpression = expression;
            sourceProfile = profile;
          }
        }
      }

      searchView.moveExpression(sourceExpression, sourceProfile, targetProfile);
      viewer.refresh();

      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean validateDrop(Object target, int operation, TransferData transferType) {
    if (target instanceof Profile) {
      this.targetProfile = (Profile) target;
      return true;
    }
    else {
      this.targetProfile = null;
      return false;
    }
  }

}