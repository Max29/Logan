package de.iteratec.logan.preferences;

import java.util.HashMap;
import java.util.Map;

import de.iteratec.logan.view.SearchResultView;


/**
 * Defines the {@link SearchResultView} creation modes. Used in preferences.
 * 
 * @author agusevas
 */
public enum SearchResultsCreation {
  ONCE, EDITOR, SEARCH;

  private static final Map<String, SearchResultsCreation> LOOKUP = new HashMap<String, SearchResultsCreation>();
  static {
    for (SearchResultsCreation c : values()) {
      LOOKUP.put(c.name(), c);
    }
  }

  public static SearchResultsCreation toEnum(String mode) {
    if (!LOOKUP.containsKey(mode)) {
      return ONCE;
    }

    return LOOKUP.get(mode);
  }
}
