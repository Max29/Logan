package de.iteratec.logan.view.config.actions;

import java.util.List;

import de.iteratec.logan.Messages;
import de.iteratec.logan.model.Profile;
import de.iteratec.logan.view.SearchView;

import org.eclipse.jface.action.Action;


public final class RemoveProfileAction extends Action {
  private final SearchView    searchView;
  private final List<Profile> profiles;

  public RemoveProfileAction(SearchView searchView, List<Profile> profiles) {
    super(Messages.RemoveProfileAction_closeProfile);
    this.searchView = searchView;
    this.profiles = profiles;
  }

  @Override
  public void run() {
    for (Profile profile : profiles) {
      searchView.removeProvile(profile);
    }
  }
}