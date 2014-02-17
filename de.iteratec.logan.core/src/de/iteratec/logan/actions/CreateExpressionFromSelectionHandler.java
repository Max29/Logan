package de.iteratec.logan.actions;

import java.util.List;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.common.utils.ModelUtils;
import de.iteratec.logan.view.ProfileTreeViewer;
import de.iteratec.logan.view.SearchView;
import de.iteratec.logan.view.config.ProfilesContentProvider;
import de.iteratec.logan.view.config.dialog.AddExpressionDialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
    newExpression.setCaseSensitive(true);
    Shell parentShell = Display.getDefault().getActiveShell();
    IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    SearchView searchView = (SearchView) activeWorkbenchWindow.getActivePage().findView(SearchView.ID);
    ProfileTreeViewer profileTreeViewer = searchView.getTreeViewer();
    ProfilesContentProvider contentProvider = (ProfilesContentProvider) profileTreeViewer.getContentProvider();
    List<Profile> profiles = contentProvider.getProfiles();
    AddExpressionDialog dialog = new AddExpressionDialog(parentShell, newExpression, profiles);
    dialog.open();
    if (dialog.getReturnCode() == Window.OK) {
      Expression expression = dialog.getExpression();
      String profileName = dialog.getProfileName();
      
      Profile profile = ModelUtils.getProfile(profiles, profileName);
      searchView.addProfileExpression(profile, expression);
    }

    return null;
  }
}
