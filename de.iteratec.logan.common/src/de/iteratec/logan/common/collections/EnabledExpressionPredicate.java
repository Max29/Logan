package de.iteratec.logan.common.collections;

import com.google.common.base.Predicate;

import de.iteratec.logan.common.model.Expression;


public class EnabledExpressionPredicate implements Predicate<Expression> {
  @Override
  public boolean apply(Expression expression) {
    return expression.isEnabled();
  }
}