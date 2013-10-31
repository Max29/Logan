package de.iteratec.logan.preferences;

import de.iteratec.logan.LoganPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore preferenceStore = LoganPlugin.getDefault().getPreferenceStore();
    preferenceStore.setDefault(PreferenceConstants.SEARCH_RESULT_CREATION, SearchResultsCreation.ONCE.name());
  }

}
