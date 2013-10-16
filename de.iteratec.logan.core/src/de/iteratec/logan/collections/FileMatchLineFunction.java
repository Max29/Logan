package de.iteratec.logan.collections;

import java.util.List;

import com.google.common.base.Function;

import de.iteratec.logan.search.FileMatch;
import de.iteratec.logan.search.LineMatch;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;


public class FileMatchLineFunction implements Function<FileMatch, String> {

  private final IDocument document;

  public FileMatchLineFunction(IDocument document) {
    this.document = document;
  }

  @Override
  public String apply(FileMatch fileMatch) {
    try {
      List<LineMatch> lineMatches = fileMatch.getLineMatches();
      StringBuilder sb = new StringBuilder();
      for (LineMatch lineMatch : lineMatches) {
        IRegion lineInformation = lineMatch.getLineInformation();
        String lineContent = document.get(lineInformation.getOffset(), lineInformation.getLength());
        sb.append(lineContent);
        String lineDelimiter = document.getLineDelimiter(lineMatch.getLineNumber());
        if (lineDelimiter != null) {
          sb.append(lineDelimiter);
        }
      }

      return sb.toString();
    } catch (BadLocationException e) {
      e.printStackTrace();
    }

    return ""; //$NON-NLS-1$
  }
}
