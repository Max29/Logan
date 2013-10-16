package de.iteratec.logan.search;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;


public class SearchUtils {

  public static List<LineMatch> getLineInformations(IDocument document, FileMatch fileMatch) {
    try {
      int numberOfLines = document.getNumberOfLines(fileMatch.getOffset(), fileMatch.getLength());
      int startLine = document.getLineOfOffset(fileMatch.getOffset());
      int endLine = startLine + numberOfLines - 1;

      List<LineMatch> lineMatches = Lists.newArrayList();
      for (int i = startLine; i <= endLine; i++) {
        IRegion lineInformation = document.getLineInformation(i);
        LineMatch lineMatch = new LineMatch(i, lineInformation);
        lineMatches.add(lineMatch);
      }

      return lineMatches;
    } catch (BadLocationException e) {
      e.printStackTrace();
    }

    return Collections.emptyList();
  }
}
