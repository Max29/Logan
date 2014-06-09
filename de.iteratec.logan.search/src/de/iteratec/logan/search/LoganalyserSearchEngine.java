package de.iteratec.logan.search;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.ui.text.Match;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.iteratec.logan.common.Logger;
import de.iteratec.logan.common.collections.EnabledExpressionPredicate;
import de.iteratec.logan.common.collections.ProfileExpressionsFunction;
import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;


public class LoganalyserSearchEngine {

  private static LoganalyserTextSearchEngine plainSearchEngine = new LoganalyserTextSearchEngine();

  public static TextSearchResult search(IFile file, String searchTerm, IProgressMonitor monitor) {
    monitor.beginTask("Searching for expressions", IProgressMonitor.UNKNOWN);

    TextSearchResult textSearchResult = new TextSearchResult();
    Expression expression = new Expression(searchTerm);
    searchForExpression(file, monitor, textSearchResult, expression);

    monitor.done();
    return textSearchResult;
  }

  public static TextSearchResult searchRegexp(IFile file, String regexp, IProgressMonitor monitor) {
    monitor.beginTask("Searching for regexp expression", IProgressMonitor.UNKNOWN);

    TextSearchResult textSearchResult = new TextSearchResult();
    Expression expression = new Expression(regexp);
    expression.setRegexp(true);
    searchForExpression(file, monitor, textSearchResult, expression);

    monitor.done();
    return textSearchResult;
  }

  public static TextSearchResult searchProfiles(IFile file, List<Profile> profiles, IProgressMonitor monitor) {
    Iterable<Iterable<Expression>> allProfileExpressions = Iterables.transform(profiles,
        new ProfileExpressionsFunction());
    Iterable<Expression> allExpressions = Iterables.concat(allProfileExpressions);
    Iterable<Expression> enabledExpressions = Iterables.filter(allExpressions, new EnabledExpressionPredicate());
    int totalEnabledExpressions = Iterables.size(enabledExpressions);
    monitor.beginTask("Searching for expressions", totalEnabledExpressions);

    TextSearchResult textSearchResult = new TextSearchResult();

    for (Expression expression : enabledExpressions) {
      if (monitor.isCanceled()) {
        throw new OperationCanceledException();
      }

      searchForExpression(file, monitor, textSearchResult, expression);
    }

    monitor.done();
    return textSearchResult;
  }

  private static void searchForExpression(IFile file, IProgressMonitor monitor, TextSearchResult textSearchResult,
                                          Expression expression) {
    long start = System.nanoTime();
    String searchExpression = expression.getSearchExpression();
    monitor.subTask("Searching for expression: " + searchExpression); //$NON-NLS-1$
    PropertyFileSearchRequestor tsr = new PropertyFileSearchRequestor(textSearchResult, expression);
    if (expression.isRegexp()) {
      Pattern pattern = createPattern(expression);
      plainSearchEngine.search(new IFile[] { file }, tsr, pattern, null);
    }
    else {
      plainSearchEngine.search(new IFile[] { file }, tsr, searchExpression, !expression.isCaseSensitive(), null);
    }

    long end = System.nanoTime();
    long time = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    Logger.getInstance().log("Search expression time: " + time); //$NON-NLS-1$

    monitor.worked(1);
  }

  private static Pattern createPattern(Expression expression) {
    Pattern pattern = TextSearchEngine.createPattern(expression.getSearchExpression(), expression.isCaseSensitive(),
        expression.isRegexp());
    return pattern;
  }

  public static List<FileMatch> getFileMatches(TextSearchResult textSearchResult) {
    List<FileMatch> result = Lists.newArrayList();

    Object[] elements = textSearchResult.getElements();
    for (Object object : elements) {
      Match[] matches = textSearchResult.getMatches(object);
      for (Match match : matches) {
        FileMatch fileMatch = (FileMatch) match;

        result.add(fileMatch);
      }
    }

    return result;
  }
}
