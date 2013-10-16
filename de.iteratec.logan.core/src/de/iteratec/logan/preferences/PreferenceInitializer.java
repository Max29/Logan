package de.iteratec.logan.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.ui.PlatformUI;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = PlatformUI.getPreferenceStore();
    store.setDefault(PreferenceConstants.P_BOOLEAN, true);
    store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
    store.setDefault(PreferenceConstants.P_STRING, "Default value");
  }

}
