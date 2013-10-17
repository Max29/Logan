package de.iteratec.logan.search;

import org.eclipse.jface.text.IRegion;


public class LineMatch {
  private final int     lineNumber;
  private final IRegion lineInformation;

  public LineMatch(int lineNumber, IRegion lineInformation) {
    this.lineNumber = lineNumber;
    this.lineInformation = lineInformation;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public IRegion getLineInformation() {
    return lineInformation;
  }

}
