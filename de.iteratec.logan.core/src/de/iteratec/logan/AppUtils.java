package de.iteratec.logan;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ui.texteditor.ITextEditor;


public class AppUtils {
  public static final String MARKER     = "de.iteratec.logan.markers.style1"; //$NON-NLS-1$
  public static final String ANN_STYLE1 = "de.iteratec.logan.markers.style1"; //$NON-NLS-1$
  public static final String ANN_STYLE2 = "de.iteratec.logan.markers.style2"; //$NON-NLS-1$
  public static final String ANN_STYLE3 = "de.iteratec.logan.markers.style3"; //$NON-NLS-1$
  public static final String ANN_STYLE4 = "de.iteratec.logan.markers.style4"; //$NON-NLS-1$
  public static final String ANN_STYLE5 = "de.iteratec.logan.markers.style5"; //$NON-NLS-1$

  public static TextSelection getTextSelection() {
    ISelection selection = getActiveWorkbenchWindow().getSelectionService().getSelection();
    if (selection instanceof TextSelection) {
      return (TextSelection) selection;
    }
    return null;
  }

  public static IWorkbenchWindow getActiveWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  public static ITextEditor getEditor() {
    return (ITextEditor) getActiveWorkbenchWindow().getActivePage().getActiveEditor();
  }

  public static void restartAfterUpdate() {
    Display.getDefault().syncExec(new Runnable() {

      @Override
      public void run() {
        boolean restart = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
            "Updates installed, restart?", "Updates have been installed successfully, do you want to restart?");
        if (restart) {
          PlatformUI.getWorkbench().restart();
        }
      }
    });

  }

  public static IMarker createMarker(IResource res, Position position, String name) throws CoreException {
    IMarker marker = res.createMarker(MARKER);
    marker.setAttribute(IMarker.MESSAGE, name);
    int start = position.getOffset();
    int end = position.getOffset() + position.getLength();
    marker.setAttribute(IMarker.CHAR_START, start);
    marker.setAttribute(IMarker.CHAR_END, end);

    return marker;
  }

  public static void createAnnotation(IAnnotationModel annotationModel, String annotationType, Position position) {
    Annotation annotation = new Annotation(annotationType, false, null);
    annotationModel.addAnnotation(annotation, position);
  }

  /**
   * Opens the file chooser.
   * 
   * @param shell the current shell
   * @param filterPath the path, where the files are located. Can be {@code null}
   * @return the list selected files
   */
  public static List<File> queryFile(Shell shell, String filterPath) {
    List<File> result = Lists.newArrayList();

    FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
    dialog.setText("Open File"); //$NON-NLS-1$
    dialog.setFilterPath(filterPath);
    String[] filterExt = { "*.xml" }; //$NON-NLS-1$
    dialog.setFilterExtensions(filterExt);
    dialog.open();
    String path = dialog.getFilterPath();
    for (String fileName : dialog.getFileNames()) {
      result.add(new File(path, fileName));
    }

    return result;
  }

  public static File saveFile(Shell shell, String title, String fileName, String openDialogFilterPath) {
    FileDialog fd = new FileDialog(shell, SWT.SAVE);
    fd.setText(title);
    fd.setFileName(fileName);
    String[] filterExt = { "*.xml" }; //$NON-NLS-1$
    fd.setFilterExtensions(filterExt);
    fd.setFilterPath(openDialogFilterPath);
    String path = fd.open();
    if (path != null && path.length() > 0) {
      return new File(path);
    }

    return null;
  }
}
