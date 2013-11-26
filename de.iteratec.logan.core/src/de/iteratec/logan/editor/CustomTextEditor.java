package de.iteratec.logan.editor;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IFile;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;


public class CustomTextEditor extends TextEditor {
  public static final String ID = "de.iteratec.logan.editor.CustomTextEditor"; //$NON-NLS-1$

  public CustomTextEditor() {
    String lineNumbers = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER;
    EditorsUI.getPreferenceStore().setValue(lineNumbers, true);
  }

  @Override
  protected void editorContextMenuAboutToShow(IMenuManager menu) {
    menu.add(new Separator(ITextEditorActionConstants.GROUP_UNDO));
    menu.add(new GroupMarker(ITextEditorActionConstants.GROUP_SAVE));
    menu.add(new Separator(ITextEditorActionConstants.GROUP_COPY));
    menu.add(new Separator(ITextEditorActionConstants.GROUP_PRINT));
    menu.add(new Separator(ITextEditorActionConstants.GROUP_EDIT));
    menu.add(new Separator(ITextEditorActionConstants.GROUP_FIND));
    menu.add(new Separator(IWorkbenchActionConstants.GROUP_ADD));
    menu.add(new Separator(ITextEditorActionConstants.GROUP_REST));
    menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

    if (isEditable()) {
      addAction(menu, ITextEditorActionConstants.GROUP_UNDO, ITextEditorActionConstants.UNDO);
      addAction(menu, ITextEditorActionConstants.GROUP_UNDO, ITextEditorActionConstants.REVERT_TO_SAVED);
      addAction(menu, ITextEditorActionConstants.GROUP_SAVE, ITextEditorActionConstants.SAVE);
      addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.CUT);
      addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.COPY);
      addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.PASTE);
      IAction action = getAction(ITextEditorActionConstants.QUICK_ASSIST);
      if (action != null && action.isEnabled())
        addAction(menu, ITextEditorActionConstants.GROUP_EDIT, ITextEditorActionConstants.QUICK_ASSIST);
    }
    else {
      addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.COPY);
    }
  }

  @Override
  public void dispose() {
    super.dispose();

    IEditorInput editorInput = getEditorInput();
    final IFile file = (IFile) editorInput.getAdapter(IFile.class);
    try {
      file.delete(true, null);
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
}
