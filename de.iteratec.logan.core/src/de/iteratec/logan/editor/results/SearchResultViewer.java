package de.iteratec.logan.editor.results;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.search.TextSearchResult;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.internal.WorkbenchMessages;


@SuppressWarnings("restriction")
public class SearchResultViewer extends SourceViewer {
  /** Menu id for the editor context menu. */
  public static final String CONTEXT_MENU_ID = "#SearchResultEditorContext"; //$NON-NLS-1$

  private TextSearchResult   textSearchResult;

  public SearchResultViewer(Composite parent, IVerticalRuler ruler, int styles) {
    super(parent, ruler, styles);
  }

  public void setTextSearchResult(TextSearchResult textSearchResult) {
    this.textSearchResult = textSearchResult;
  }

  public TextSearchResult getTextSearchResult() {
    return textSearchResult;
  }

  @Override
  protected void createControl(Composite parent, int styles) {
    super.createControl(parent, styles);
    final StyledText styledText = getTextWidget();

    IMenuListener menuListener = new IMenuListener() {
      @Override
      public void menuAboutToShow(IMenuManager menu) {
        Action copyAction = createAction(ITextOperationTarget.COPY, IWorkbenchCommandConstants.EDIT_COPY);
        copyAction.setText(WorkbenchMessages.Workbench_copy);
        copyAction.setToolTipText(WorkbenchMessages.Workbench_copyToolTip);
        ISharedImages sharedImages = AppUtils.getActiveWorkbenchWindow().getWorkbench().getSharedImages();
        copyAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        copyAction.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
        menu.add(copyAction);
      }
    };

    MenuManager manager = new MenuManager(CONTEXT_MENU_ID, CONTEXT_MENU_ID);
    manager.setRemoveAllWhenShown(true);
    Menu contextMenu = manager.createContextMenu(styledText);
    styledText.setMenu(contextMenu);
    manager.addMenuListener(menuListener);
  }

  private Action createAction(final int operation, String actionDefinitionId) {
    Action action = new Action() {
      @Override
      public void run() {
        if (canDoOperation(operation)) {
          doOperation(operation);
        }
      }
    };

    action.setEnabled(canDoOperation(operation));
    action.setActionDefinitionId(actionDefinitionId);
    return action;
  }

}
