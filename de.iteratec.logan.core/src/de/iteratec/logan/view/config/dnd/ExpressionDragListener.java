package de.iteratec.logan.view.config.dnd;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;

import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;


public class ExpressionDragListener extends DragSourceAdapter {

  private final CheckboxTreeViewer viewer;

  public ExpressionDragListener(CheckboxTreeViewer viewer) {
    this.viewer = viewer;
  }

  @Override
  public void dragStart(DragSourceEvent event) {
    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
    Object firstElement = selection.getFirstElement();
    if (firstElement instanceof Expression || firstElement instanceof Profile) {
      LocalSelectionTransfer.getTransfer().setSelection(viewer.getSelection());
      LocalSelectionTransfer.getTransfer().setSelectionSetTime(event.time & 0xFFFFFFFFL);
      event.doit = true;
    }
    else {
      event.doit = false;
    }
  }

  @Override
  public void dragSetData(DragSourceEvent event) {
    if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
      event.data = LocalSelectionTransfer.getTransfer().getSelection();
    }
    else {
      event.doit = false;
    }
  }

  @Override
  public void dragFinished(DragSourceEvent event) {
    LocalSelectionTransfer.getTransfer().setSelection(null);
    LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
  }

}