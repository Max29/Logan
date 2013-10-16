package de.iteratec.logan.view.config;

import de.iteratec.logan.model.Expression;
import de.iteratec.logan.service.ColorManager;

import org.eclipse.swt.graphics.TextStyle;

import org.eclipse.jface.viewers.StyledString.Styler;


public class ExpressionStyler extends Styler {
  private Expression expression;

  public ExpressionStyler(Expression expression) {
    this.expression = expression;
  }

  @Override
  public void applyStyles(TextStyle textStyle) {
    textStyle.background = ColorManager.getInstance().getColor(expression.getBackgroundColor());
    textStyle.foreground = ColorManager.getInstance().getColor(expression.getTextColor());
  }
}
