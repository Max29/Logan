package de.iteratec.logan.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.search.FileMatch;
import de.iteratec.logan.search.LoganalyserSearchEngine;
import de.iteratec.logan.search.TextSearchResult;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.core.resources.IFile;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;

import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;


public class MarkOccurenciesHandler extends AbstractOccurenciesHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    final String styleParameter = event.getParameter(PARAMETER_ID);
    final String annotationType = getAnnotationType(styleParameter);

    try {
      final ITextEditor textEditor = AppUtils.getEditor();
      final TextSelection selection = AppUtils.getTextSelection();
      final String searchTerm = selection.getText();

      Job job = new Job("Adding Style") { //$NON-NLS-1$
        @Override
        protected IStatus run(IProgressMonitor monitor) {
          final IFile file = (IFile) textEditor.getEditorInput().getAdapter(IFile.class);
          final TextSearchResult textSearchResult = LoganalyserSearchEngine.search(file, searchTerm, monitor);

          Map<Annotation, Position> annotationMap = new HashMap<Annotation, Position>();
          final List<FileMatch> searchResult = LoganalyserSearchEngine.getFileMatches(textSearchResult);
          for (FileMatch fileMatch : searchResult) {
            Position position = new Position(fileMatch.getOffset(), fileMatch.getLength());
            Annotation annotation = new Annotation(false);
            annotation.setType(annotationType);
            annotationMap.put(annotation, position);
          }

          IDocumentProvider idp = textEditor.getDocumentProvider();
          final IAnnotationModel annotationModel = idp.getAnnotationModel(textEditor.getEditorInput());
          ((IAnnotationModelExtension) annotationModel).replaceAnnotations(null, annotationMap);

          return Status.OK_STATUS;
        }
      };

      job.setPriority(Job.INTERACTIVE);
      job.setUser(true);
      job.schedule();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

}
