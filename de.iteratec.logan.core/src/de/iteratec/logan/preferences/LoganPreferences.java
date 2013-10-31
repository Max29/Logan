package de.iteratec.logan.preferences;

import de.iteratec.logan.LoganPlugin;
import de.iteratec.logan.view.SearchResultView;

import org.eclipse.jface.preference.IPreferenceStore;


/**
 * A helper class to get the Logan preferences.
 * 
 * @author agusevas
 */
public class LoganPreferences {

  /**
   * Returns the {@link SearchResultView} creation mode.
   * 
   * @return the creation mode. Never returns {@code null}
   */
  public static SearchResultsCreation getSearchResultsCreation() {
    IPreferenceStore preferenceStore = LoganPlugin.getDefault().getPreferenceStore();
    String mode = preferenceStore.getString(PreferenceConstants.SEARCH_RESULT_CREATION);

    return SearchResultsCreation.toEnum(mode);
  }
}
