package de.iteratec.logan.view.config.dnd;

import de.iteratec.logan.common.model.Expression;

import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;

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
    if (firstElement instanceof Expression) {
      event.doit = true;
    }
    else {
      event.doit = false;
    }
  }

  @Override
  public void dragSetData(DragSourceEvent event) {
    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
    Object firstElement = selection.getFirstElement();
    if (firstElement instanceof Expression) {
      Expression expression = (Expression) firstElement;
      if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
        event.data = String.valueOf(expression.hashCode());
      }
    }
    else {
      event.doit = false;
    }
  }

}