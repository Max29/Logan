package de.iteratec.logan.search;

import java.util.List;

import com.google.common.collect.Lists;

import de.iteratec.logan.common.model.Expression;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IFile;

import org.eclipse.search.core.text.TextSearchMatchAccess;
import org.eclipse.search.core.text.TextSearchRequestor;


public class PropertyFileSearchRequestor extends TextSearchRequestor {
  private List<FileMatch>        fCachedMatches;
  private final TextSearchResult textSearchResult;
  private final Expression       expression;

  public PropertyFileSearchRequestor(TextSearchResult textSearchResult, Expression expression) {
    this.textSearchResult = textSearchResult;
    this.expression = expression;
  }

  @Override
  public boolean acceptPatternMatch(TextSearchMatchAccess matchRequestor) throws CoreException {
    int matchOffset = matchRequestor.getMatchOffset();
    int matchLength = matchRequestor.getMatchLength();
    IFile file = matchRequestor.getFile();

    FileMatch fileMatch = new FileMatch(file, matchOffset, matchLength, expression);
    fCachedMatches.add(fileMatch);
    return true;
  }

  @Override
  public void beginReporting() {
    fCachedMatches = Lists.newArrayList();
  }

  @Override
  public void endReporting() {
    flushMatches();
    fCachedMatches = null;
  }

  private void flushMatches() {
    if (!fCachedMatches.isEmpty()) {
      textSearchResult.addMatches(fCachedMatches);
      fCachedMatches.clear();
    }
  }
}
