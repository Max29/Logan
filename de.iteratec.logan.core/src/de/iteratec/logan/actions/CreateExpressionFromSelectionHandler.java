package de.iteratec.logan.actions;

import java.util.List;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.view.ProfileTreeViewer;
import de.iteratec.logan.view.SearchView;
import de.iteratec.logan.view.config.ProfilesContentProvider;
import de.iteratec.logan.view.config.dialog.AddExpressionDialog;

import org.eclipse.swt.widgets.Display;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;

import org.eclipse.jface.text.TextSelection;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * Creates the new {@link Expression} from the editor text selection. The new expression will be added
 * to the default profile in the profiles {@link TreeViewer}.
 * 
 * @author agu
 */
public class CreateExpressionFromSelectionHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    final TextSelection selection = AppUtils.getTextSelection();
    final String searchTerm = selection.getText();

    Expression newExpression = new Expression(searchTerm);
    newExpression.setName(searchTerm);
    newExpression.setEnabled(true);
    AddExpressionDialog dialog = new AddExpressionDialog(Display.getDefault().getActiveShell(), newExpression);
    dialog.open();
    if (dialog.getReturnCode() == Window.OK) {
      Expression expression = dialog.getExpression();

      IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      SearchView searchView = (SearchView) activeWorkbenchWindow.getActivePage().findView(SearchView.ID);
      ProfileTreeViewer profileTreeViewer = searchView.getTreeViewer();
      ProfilesContentProvider contentProvider = (ProfilesContentProvider) profileTreeViewer.getContentProvider();
      List<Profile> profiles = contentProvider.getProfiles();
      Profile defaultProfile = getDefaultProfile(profiles);
      if (defaultProfile == null) {
        defaultProfile = profileTreeViewer.getDefaultProfile();
        defaultProfile.getExpressions().clear();
        searchView.addProfile(defaultProfile, null, false);
      }

      searchView.addProfileExpression(defaultProfile, expression);
    }

    return null;
  }

  private Profile getDefaultProfile(List<Profile> profiles) {
    for (Profile profile : profiles) {
      if (ProfileTreeViewer.DEFAULT_PROFILE.equals(profile.getName())) {
        return profile;
      }
    }

    return null;
  }
}
