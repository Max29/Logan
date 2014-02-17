package de.iteratec.logan.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.viewers.CheckboxTreeViewer;


public class ProfileTreeViewer extends CheckboxTreeViewer {

  public ProfileTreeViewer(Composite parent) {
    super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
  }
}
