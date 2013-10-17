package de.iteratec.logan.search;

import java.util.regex.Pattern;

import de.iteratec.logan.search.visitor.PatternTextSearchVisitor;
import de.iteratec.logan.search.visitor.PlainTextSearchVisitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.core.resources.IFile;

import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.core.text.TextSearchScope;


public class LoganalyserTextSearchEngine extends TextSearchEngine {

  @Override
  public IStatus search(IFile[] scope, TextSearchRequestor requestor, Pattern searchPattern, IProgressMonitor monitor) {
    return new PatternTextSearchVisitor(requestor, searchPattern).search(scope, monitor);
  }

  @Override
  public IStatus search(TextSearchScope scope, TextSearchRequestor requestor, Pattern searchPattern,
                        IProgressMonitor monitor) {
    throw new IllegalStateException();
  }

  public IStatus search(IFile[] scope, TextSearchRequestor requestor, String searchPattern, boolean ignoreCase,
                        IProgressMonitor monitor) {
    return new PlainTextSearchVisitor(requestor, searchPattern, ignoreCase).search(scope, monitor);
  }
}
