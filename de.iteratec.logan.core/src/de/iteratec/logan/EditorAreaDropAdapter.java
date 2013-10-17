package de.iteratec.logan;

import java.io.File;

import de.iteratec.logan.actions.OpenFileAction;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;


public class EditorAreaDropAdapter extends DropTargetAdapter {
  private IWorkbenchWindow window;

  public EditorAreaDropAdapter(IWorkbenchWindow window) {
    this.window = window;
  }

  @Override
  public void dragEnter(DropTargetEvent event) {
    event.detail = DND.DROP_COPY;
  }

  @Override
  public void dragOperationChanged(DropTargetEvent event) {
    event.detail = DND.DROP_COPY;
  }

  @Override
  public void drop(final DropTargetEvent event) {
    Display d = window.getShell().getDisplay();
    final IWorkbenchPage page = window.getActivePage();
    if (page != null) {
      d.asyncExec(new Runnable() {
        @Override
        public void run() {
          asyncDrop(event, page);
        }
      });
    }
  }

  private void asyncDrop(DropTargetEvent event, IWorkbenchPage page) {
    if (event.data == null) {
      event.detail = DND.DROP_NONE;
      return;
    }
    if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
      String[] fileNames = (String[]) event.data;
      if (fileNames.length == 0) {
        event.detail = DND.DROP_NONE;
        return;
      }
      File file = new File(fileNames[0]);
      if (!file.exists()) {
        return;
      }

      OpenFileAction action = new OpenFileAction();
      action.init(page.getWorkbenchWindow());
      action.openEditor(file.getAbsolutePath());
      action.dispose();
    }
  }
}
