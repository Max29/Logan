package de.iteratec.logan.view.config;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;


public class CustomColorSelector extends ColorSelector {

  public CustomColorSelector(Composite parent) {
    super(parent);
  }

  public void addPropertyChangeListener(IPropertyChangeListener listener) {
    addListener(listener);
  }

  public void removePropertyChangeListener(IPropertyChangeListener listener) {
    removeListener(listener);
  }

}
