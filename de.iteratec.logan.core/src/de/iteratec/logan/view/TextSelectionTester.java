/**
 * 
 */
package de.iteratec.logan.view;

import org.eclipse.core.expressions.PropertyTester;

import org.eclipse.jface.text.ITextSelection;


/**
 * @author agu
 */
public class TextSelectionTester extends PropertyTester {

  private static final String SELECTED = "selected"; //$NON-NLS-1$

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    ITextSelection selection = (ITextSelection) receiver;

    if (property.equals(SELECTED)) {
      return selection.getLength() > 0;
    }

    return false;
  }

}
