package de.iteratec.logan.editor.results;

import java.util.List;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.editor.CustomTextEditor;
import de.iteratec.logan.search.FileMatch;
import de.iteratec.logan.search.LineMatch;
import de.iteratec.logan.search.TextSearchResult;

import org.eclipse.core.resources.IFile;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.ui.texteditor.AbstractTextEditor;

import org.eclipse.search.ui.text.Match;


public class CustomTextDoubleClickStrategy extends DefaultTextDoubleClickStrategy {

  @Override
  public void doubleClicked(ITextViewer text) {
    super.doubleClicked(text);

    int offset = text.getSelectedRange().x;
    if (offset < 0)
      return;

    IDocument document = text.getDocument();
    int line = -1;
    try {
      line = document.getLineOfOffset(offset);
    } catch (BadLocationException e) {
    }

    SearchResultViewer searchResultViewer = (SearchResultViewer) text;
    TextSearchResult textSearchResult = searchResultViewer.getTextSearchResult();

    Object[] elements = textSearchResult.getElements();
    Match[] matches = textSearchResult.getMatches(elements[0]);

    int currentLine = 0;
    IFile editorFile = null;
    LineMatch lineInfo = null;
    for (Match match : matches) {
      FileMatch fileMatch = (FileMatch) match;
      List<LineMatch> lineMatches = fileMatch.getLineMatches();
      for (LineMatch lineMatch : lineMatches) {
        if (currentLine == line) {
          editorFile = fileMatch.getFile();
          lineInfo = lineMatch;
          break;
        }
        else {
          currentLine++;
        }
      }

      if (lineInfo != null) {
        break;
      }
    }

    try {
      IWorkbenchPage activePage = AppUtils.getActiveWorkbenchWindow().getActivePage();
      IEditorPart editor = activePage.openEditor(new FileEditorInput(editorFile), CustomTextEditor.ID);
      if (editor != null && editor instanceof AbstractTextEditor) {
        AbstractTextEditor textEditor = (AbstractTextEditor) editor;
        IRegion lineInformation = lineInfo.getLineInformation();
        textEditor.selectAndReveal(lineInformation.getOffset(), lineInformation.getLength());
      }
    } catch (PartInitException e) {
      e.printStackTrace();
    }
  }
}
