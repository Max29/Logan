package de.iteratec.logan.search;

import java.util.List;

import com.google.common.collect.Lists;

import de.iteratec.logan.model.Expression;

import org.eclipse.core.resources.IFile;

import org.eclipse.search.ui.text.Match;


public class FileMatch extends Match {
  private final Expression      expression;
  private final List<LineMatch> lineMatches = Lists.newArrayList();

  public FileMatch(IFile element, int offset, int length, Expression expression) {
    super(element, offset, length);
    this.expression = expression;
  }

  public IFile getFile() {
    return (IFile) getElement();
  }

  public Expression getExpression() {
    return expression;
  }

  public List<LineMatch> getLineMatches() {
    return lineMatches;
  }

  public int getLinesCount() {
    return lineMatches.size();
  }
}
