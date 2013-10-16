package de.iteratec.logan.view.config;

import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.iteratec.logan.collections.EnabledExpressionPredicate;
import de.iteratec.logan.model.Expression;
import de.iteratec.logan.model.Profile;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;


public class TreeSelectionUtils {
  public static Profile getSelectedProfile(CheckboxTreeViewer treeViewer) {
    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
    if (!selection.isEmpty()) {
      Object selectedElement = selection.getFirstElement();
      if (selectedElement instanceof Profile) {
        return (Profile) selectedElement;
      }
    }

    return null;
  }

  public static Expression getSelectedExpression(CheckboxTreeViewer treeViewer) {
    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
    if (!selection.isEmpty()) {
      Object selectedElement = selection.getFirstElement();
      if (selectedElement instanceof Expression) {
        return (Expression) selectedElement;
      }
    }

    return null;
  }

  public static List<Profile> getSelectedProfiles(CheckboxTreeViewer treeViewer) {
    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
    List<Profile> result = Lists.newArrayList();
    for (Object element : selection.toList()) {
      if (element instanceof Profile) {
        result.add((Profile) element);
      }
    }

    return result;
  }

  public static List<Expression> getSelectedExpressions(CheckboxTreeViewer treeViewer) {
    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
    List<Expression> result = Lists.newArrayList();
    for (Object element : selection.toList()) {
      if (element instanceof Expression) {
        result.add((Expression) element);
      }
    }

    return result;
  }

  public static boolean allExpressionsEnabled(Profile profile) {
    List<Expression> expressions = profile.getExpressions();
    return Iterables.all(expressions, new EnabledExpressionPredicate());
  }
}
