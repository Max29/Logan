package de.iteratec.logan.view.config.actions;

import de.iteratec.logan.Messages;
import de.iteratec.logan.view.SearchView;

import org.eclipse.jface.action.Action;


public final class AddProfileAction extends Action {
  private final SearchView searchView;

  public AddProfileAction(SearchView searchView) {
    super(Messages.AddProfileAction_addNewProfile);
    this.searchView = searchView;
  }

  @Override
  public void run() {
    searchView.addNewProfile();
  }
}