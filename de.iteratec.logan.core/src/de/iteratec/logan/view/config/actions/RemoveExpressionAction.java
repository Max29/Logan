package de.iteratec.logan.view.config.actions;

import java.util.List;

import de.iteratec.logan.Messages;
import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.view.SearchView;

import org.eclipse.jface.action.Action;


public final class RemoveExpressionAction extends Action {
  private final SearchView       searchView;
  private final List<Expression> expressionsToRemove;

  public RemoveExpressionAction(SearchView searchView, List<Expression> expressionsToRemove) {
    super(Messages.RemoveExpressionAction_removeExpression);
    this.searchView = searchView;
    this.expressionsToRemove = expressionsToRemove;
  }

  @Override
  public void run() {
    for (Expression expression : expressionsToRemove) {
      searchView.removeExpression(expression);
    }
  }

}