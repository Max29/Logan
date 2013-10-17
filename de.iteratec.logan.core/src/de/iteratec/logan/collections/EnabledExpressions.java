package de.iteratec.logan.collections;

import com.google.common.base.Predicate;

import de.iteratec.logan.common.model.Expression;


public class EnabledExpressions implements Predicate<Expression> {
  @Override
  public boolean apply(Expression expression) {
    return expression.isEnabled();
  }
}