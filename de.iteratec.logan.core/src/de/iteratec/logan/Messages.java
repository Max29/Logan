package de.iteratec.logan;

import org.eclipse.osgi.util.NLS;


public class Messages extends NLS {
  private static final String BUNDLE_NAME = "de.iteratec.logan.messages"; //$NON-NLS-1$
  public static String AddExpressionDialog_backgroundColor;
  public static String AddExpressionDialog_captionMessage;
  public static String AddExpressionDialog_caseSensitive;
  public static String AddExpressionDialog_enabled;
  public static String AddExpressionDialog_errorEnterRequiredData;
  public static String AddExpressionDialog_name;
  public static String AddExpressionDialog_regularExpression;
  public static String AddExpressionDialog_searchExpression;
  public static String AddExpressionDialog_textColor;
  public static String AddExpressionDialog_title;
  public static String AddProfileAction_addNewProfile;
  public static String AddProfileDialog_captionMessage;
  public static String AddProfileDialog_description;
  public static String AddProfileDialog_errorRequiredData;
  public static String AddProfileDialog_name;
  public static String AddProfileDialog_title;
  public static String AddProfileExpressionAction_addNewExpression;
  public static String preferencesHeader;
  public static String RemoveExpressionAction_removeExpression;
  public static String RemoveOccurenciesHandler_addStyleJobName;
  public static String RemoveProfileAction_closeProfile;
  public static String searchResultCreationModeEditor;
  public static String searchResultCreationMode;
  public static String searchResultCreationModeOnce;
  public static String searchResultCreationModeSearch;
  public static String SearchView_deselectAll;
  public static String SearchView_dublicateProfile;
  public static String SearchView_loadProfiles;
  public static String SearchView_newProfile;
  public static String SearchView_profileAlreadyOpen;
  public static String SearchView_saveAll;
  public static String SearchView_saveProfile;
  public static String        SearchView_search;
  public static String        SearchView_searchJobName;
  public static String        SearchView_searchJobTitle;
  public static String SearchView_selectAll;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
