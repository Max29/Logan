package de.iteratec.logan.view.config;

import de.iteratec.logan.model.Expression;
import de.iteratec.logan.model.Profile;
import de.iteratec.logan.view.config.actions.ModelUtils;
import de.iteratec.logan.view.config.dialog.AddExpressionDialog;
import de.iteratec.logan.view.config.dialog.AddProfileDialog;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.window.Window;


public class DoubleClickListener implements IDoubleClickListener {

  private final Shell                   shell;
  private final ProfilesContentProvider contentProvider;

  public DoubleClickListener(Shell shell, ProfilesContentProvider contentProvider) {
    this.shell = shell;
    this.contentProvider = contentProvider;
  }

  @Override
  public void doubleClick(DoubleClickEvent event) {
    CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) event.getViewer();
    Expression selectedExpression = TreeSelectionUtils.getSelectedExpression(checkboxTreeViewer);
    if (selectedExpression != null) {
      AddExpressionDialog dialog = new AddExpressionDialog(shell, selectedExpression);
      dialog.open();
      if (dialog.getReturnCode() == Window.OK && dialog.isDirty()) {
        Profile profile = ModelUtils.getselectedProfile(contentProvider.getProfiles(), selectedExpression);
        ProfileSaveable profileSaveable = ProfileSaveablesProvider.getInstance().get(profile);
        profileSaveable.setDirty(true);

        checkboxTreeViewer.refresh();
      }
    }

    Profile selectedProfile = TreeSelectionUtils.getSelectedProfile(checkboxTreeViewer);
    if (selectedProfile != null) {
      AddProfileDialog dialog = new AddProfileDialog(shell, selectedProfile);
      dialog.open();
      if (dialog.getReturnCode() == Window.OK && dialog.isDirty()) {
        ProfileSaveable profileSaveable = ProfileSaveablesProvider.getInstance().get(selectedProfile);
        profileSaveable.setDirty(true);

        checkboxTreeViewer.refresh();
      }
    }
  }

}
