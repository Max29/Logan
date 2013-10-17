package de.iteratec.logan.view.config;

import java.util.List;

import com.google.common.collect.Iterables;

import de.iteratec.logan.common.collections.EnabledExpressionPredicate;
import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;


public class CheckStateListener implements ICheckStateListener, ICheckStateProvider {

  @Override
  public void checkStateChanged(CheckStateChangedEvent event) {
    CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) event.getSource();
    Object selectedElement = event.getElement();

    if (selectedElement instanceof Profile) {
      Profile profile = (Profile) selectedElement;
      profileStateChanged(profile, checkboxTreeViewer, event.getChecked());
    }
    else if (selectedElement instanceof Expression) {
      Expression expression = (Expression) selectedElement;
      expressionStateChanged(expression, checkboxTreeViewer, event.getChecked());
    }
  }

  public static void profileStateChanged(Profile profile, CheckboxTreeViewer checkboxTreeViewer, boolean state) {
    checkboxTreeViewer.setSubtreeChecked(profile, state);
    checkboxTreeViewer.setGrayed(profile, false);
    List<Expression> expressions = profile.getExpressions();
    for (Expression expression : expressions) {
      expression.setEnabled(state);
    }
  }

  public static void expressionStateChanged(Expression expression, CheckboxTreeViewer checkboxTreeViewer, boolean state) {
    checkboxTreeViewer.setChecked(expression, state);
    expression.setEnabled(state);

    ProfilesContentProvider contentProvider = (ProfilesContentProvider) checkboxTreeViewer.getContentProvider();
    Profile profile = (Profile) contentProvider.getParent(expression);
    if (Iterables.all(profile.getExpressions(), new EnabledExpressionPredicate())) {
      checkboxTreeViewer.setGrayed(profile, false);
      checkboxTreeViewer.setChecked(profile, true);
    }
    else if (Iterables.any(profile.getExpressions(), new EnabledExpressionPredicate())) {
      checkboxTreeViewer.setGrayChecked(profile, true);
    }
    else {
      checkboxTreeViewer.setChecked(profile, false);
    }
  }

  @Override
  public boolean isChecked(Object element) {
    if (element instanceof Expression) {
      Expression expression = (Expression) element;
      return expression.isEnabled();
    }
    else {
      Profile profile = (Profile) element;
      if (Iterables.any(profile.getExpressions(), new EnabledExpressionPredicate())) {
        return true;
      }
      else {
        return false;
      }
    }
  }

  @Override
  public boolean isGrayed(Object element) {
    if (element instanceof Profile) {
      Profile profile = (Profile) element;
      if (Iterables.all(profile.getExpressions(), new EnabledExpressionPredicate())) {
        return false;
      }
      else if (Iterables.any(profile.getExpressions(), new EnabledExpressionPredicate())) {
        return true;
      }
      else {
        return false;
      }
    }

    return false;
  }

}
