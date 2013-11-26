package de.iteratec.logan.view.config.dnd;

import java.util.List;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.view.SearchView;
import de.iteratec.logan.view.config.ProfilesContentProvider;

import org.eclipse.swt.dnd.TransferData;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
    if (data instanceof IStructuredSelection) {
      IStructuredSelection selection = (IStructuredSelection) data;
      Object draggedObject = selection.getFirstElement();
      if (draggedObject instanceof Expression) {
        Expression draggedExpression = (Expression) draggedObject;

        return performExpressionDrop(draggedExpression);
      }
      if (draggedObject instanceof Profile) {
        Profile draggedProfile = (Profile) draggedObject;
        return performProfileDrop(draggedProfile);
      }

      return false;
    }
    else {
      return false;
    }
  }

  private boolean performProfileDrop(Profile draggedProfile) {
    Profile targetProfile = (Profile) getCurrentTarget();

    ProfilesContentProvider contentProvider = getContentProvider();
    List<Profile> profiles = contentProvider.getProfiles();
    if (profiles.remove(draggedProfile)) {
      int index = profiles.indexOf(targetProfile) + 1;

      switch (getCurrentLocation()) {
        case ViewerDropAdapter.LOCATION_AFTER:
          ++index;
          break;
        case ViewerDropAdapter.LOCATION_BEFORE:
          --index;
          break;
        default:
          break;
      }

      if (index >= profiles.size()) {
        profiles.add(draggedProfile);
      }
      else {
        profiles.add(index, draggedProfile);
      }
      viewer.refresh();
      return true;
    }

    return false;
  }

  private boolean performExpressionDrop(Expression draggedExpression) {
    if (getCurrentTarget() instanceof Profile) {
      SourceExpression source = getSource(draggedExpression);
      Profile targetProfile = (Profile) getCurrentTarget();
      int expressionIndex = targetProfile.getExpressions().size();
      searchView.moveExpression(source.getExpression(), source.getProfile(), targetProfile, expressionIndex);
      viewer.refresh();
      return true;
    }
    else if (getCurrentTarget() instanceof Expression) {
      SourceExpression source = getSource(draggedExpression);
      Expression targetExpression = (Expression) getCurrentTarget();
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
      return true;
    }

    return false;
  }

  private SourceExpression getSource(Expression draggedExpression) {
    ProfilesContentProvider contentProvider = getContentProvider();
    Profile profile = (Profile) contentProvider.getParent(draggedExpression);

    return new SourceExpression(profile, draggedExpression);
  }

  private ProfilesContentProvider getContentProvider() {
    ProfilesContentProvider contentProvider = (ProfilesContentProvider) viewer.getContentProvider();
    return contentProvider;
  }

  @Override
  public boolean validateDrop(Object target, int operation, TransferData transferType) {
    if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
      ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
      if (selection instanceof IStructuredSelection) {
        if (selection.isEmpty()) {
          return false;
        }

        Object draggedObject = ((IStructuredSelection) selection).getFirstElement();
        return isValidDrop(draggedObject, target);
      }
    }

    return false;
  }

  private boolean isValidDrop(Object draggedObject, Object target) {
    if (draggedObject instanceof Expression && (target instanceof Profile || target instanceof Expression)) {
      return true;
    }
    if (draggedObject instanceof Profile && target instanceof Profile) {
      return true;
    }

    return false;
  }

}