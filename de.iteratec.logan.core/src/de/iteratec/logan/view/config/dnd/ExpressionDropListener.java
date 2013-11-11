package de.iteratec.logan.view.config.dnd;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.view.SearchView;
import de.iteratec.logan.view.config.ProfilesContentProvider;

import org.eclipse.swt.dnd.TransferData;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;


public class ExpressionDropListener extends ViewerDropAdapter {

  private final CheckboxTreeViewer viewer;
  private final SearchView         searchView;

  public ExpressionDropListener(CheckboxTreeViewer viewer, SearchView searchView) {
    super(viewer);
    this.viewer = viewer;
    this.searchView = searchView;
  }

  @Override
  public boolean performDrop(Object data) {
    if (data instanceof String) {
      if (getCurrentTarget() instanceof Profile) {
        SourceExpression source = getSource(data);
        if (source != null) {
          Profile targetProfile = (Profile) getCurrentTarget();
          if (targetProfile == source.getProfile()) {
            return false;
          }

          int expressionIndex = targetProfile.getExpressions().size();
          searchView.moveExpression(source.getExpression(), source.getProfile(), targetProfile, expressionIndex);
          viewer.refresh();
        }
      }
      else if (getCurrentTarget() instanceof Expression) {
        SourceExpression source = getSource(data);
        if (source != null) {
          Expression targetExpression = (Expression) getCurrentTarget();
          if (targetExpression == source.getExpression()) {
            return false;
          }

          Profile targetProfile = (Profile) getContentProvider().getParent(targetExpression);
          int expressionIndex = targetProfile.getExpressions().indexOf(targetExpression);

          switch (getCurrentLocation()) {
            case ViewerDropAdapter.LOCATION_AFTER:
              ++expressionIndex;
              break;
            default:
              break;
          }

          searchView.moveExpression(source.getExpression(), source.getProfile(), targetProfile, expressionIndex);
          viewer.refresh();
        }
      }

      return true;
    }
    else {
      return false;
    }
  }

  private SourceExpression getSource(Object data) {
    int hashCode = Integer.parseInt((String) data);
    ProfilesContentProvider contentProvider = getContentProvider();
    for (Profile profile : contentProvider.getProfiles()) {
      for (Expression expression : profile.getExpressions()) {
        if (expression.hashCode() == hashCode) {
          return new SourceExpression(profile, expression);
        }
      }
    }

    return null;
  }

  private ProfilesContentProvider getContentProvider() {
    ProfilesContentProvider contentProvider = (ProfilesContentProvider) viewer.getContentProvider();
    return contentProvider;
  }

  @Override
  public boolean validateDrop(Object target, int operation, TransferData transferType) {
    if (target instanceof Profile) {
      return true;
    }
    if (target instanceof Expression) {
      return true;
    }
    else {
      return false;
    }
  }

}