package de.iteratec.logan.view.config.actions;

import de.iteratec.logan.Messages;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.view.SearchView;

import org.eclipse.jface.action.Action;


public final class AddProfileExpressionAction extends Action {
  private final SearchView searchView;
  private final Profile    profile;

  public AddProfileExpressionAction(SearchView searchView, Profile profile) {
    super(Messages.AddProfileExpressionAction_addNewExpression);
    this.profile = profile;
    this.searchView = searchView;
  }

  @Override
  public void run() {
    searchView.addProfileExpression(profile);
  }
}