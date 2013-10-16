package de.iteratec.logan.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.Messages;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;

import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;


public class RemoveOccurenciesHandler extends AbstractOccurenciesHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    final String styleParameter = event.getParameter(PARAMETER_ID);
    final String annotationType = getAnnotationType(styleParameter);

    try {
      final ITextEditor textEditor = AppUtils.getEditor();

      Job job = new Job(Messages.RemoveOccurenciesHandler_addStyleJobName) {
        @SuppressWarnings("unchecked")
        @Override
        protected IStatus run(IProgressMonitor monitor) {
          IDocumentProvider idp = textEditor.getDocumentProvider();
          final IAnnotationModel annotationModel = idp.getAnnotationModel(textEditor.getEditorInput());

          List<Annotation> removeAnnotations = new LinkedList<Annotation>();
          Iterator<Annotation> annotationIterator = annotationModel.getAnnotationIterator();
          while (annotationIterator.hasNext()) {
            Annotation annotation = annotationIterator.next();
            if (annotation.getType().equals(annotationType)) {
              removeAnnotations.add(annotation);
            }
          }

          Annotation[] annotations = removeAnnotations.toArray(new Annotation[removeAnnotations.size()]);
          ((IAnnotationModelExtension) annotationModel).replaceAnnotations(annotations, null);

          return Status.OK_STATUS;
        }
      };

      job.schedule();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

}
