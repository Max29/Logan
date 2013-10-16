package de.iteratec.logan.view.config;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import de.iteratec.logan.model.Expression;
import de.iteratec.logan.model.Profile;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


public class ProfileLabelProvider extends StyledCellLabelProvider {

  @Override
  public void update(ViewerCell cell) {
    Object element = cell.getElement();
    StyledString text = new StyledString();

    if (element instanceof Profile) {
      Profile profile = (Profile) element;

      boolean dirty = ProfileSaveablesProvider.getInstance().isDirty(profile);
      if (dirty) {
        text.append("*"); //$NON-NLS-1$
      }

      text.append(profile.getName());
      cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
      text.append(" (" + profile.getExpressions().size() + ") ", StyledString.COUNTER_STYLER); //$NON-NLS-1$//$NON-NLS-2$
    }
    else {
      Expression expression = (Expression) element;
      String name = Objects.firstNonNull(expression.getName(), expression.getSearchExpression());
      text.append(name, new ExpressionStyler(expression));
      cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
    }
    cell.setText(text.toString());
    cell.setStyleRanges(text.getStyleRanges());
    super.update(cell);
  }

  @Override
  public String getToolTipText(Object element) {
    if (element instanceof Profile) {
      Profile profile = (Profile) element;
      return Strings.emptyToNull(profile.getDescription());
    }
    else {
      Expression expression = (Expression) element;
      return expression.getSearchExpression();
    }
  }

  @Override
  public int getToolTipDisplayDelayTime(Object object) {
    return 0;
  }

  @Override
  public int getToolTipTimeDisplayed(Object object) {
    return 0;
  }
}
