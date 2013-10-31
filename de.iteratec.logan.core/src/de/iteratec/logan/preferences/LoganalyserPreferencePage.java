package de.iteratec.logan.preferences;

import de.iteratec.logan.LoganPlugin;
import de.iteratec.logan.Messages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class LoganalyserPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public LoganalyserPreferencePage() {
    super(GRID);
  }

  @Override
  public void createFieldEditors() {
    String[][] labelAndValues = new String[][] {
        { Messages.searchResultCreationModeOnce, SearchResultsCreation.ONCE.name() },
        { Messages.searchResultCreationModeEditor, SearchResultsCreation.EDITOR.name() },
        { Messages.searchResultCreationModeSearch, SearchResultsCreation.SEARCH.name() } };
    addField(new RadioGroupFieldEditor(PreferenceConstants.SEARCH_RESULT_CREATION, Messages.searchResultCreationMode,
        1, labelAndValues, getFieldEditorParent()));
  }

  @Override
  public void init(IWorkbench workbench) {
    setPreferenceStore(LoganPlugin.getDefault().getPreferenceStore());
    setDescription(Messages.preferencesHeader);
  }

}