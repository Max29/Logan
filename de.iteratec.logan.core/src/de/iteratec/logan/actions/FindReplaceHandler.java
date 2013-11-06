/**
 * 
 */
package de.iteratec.logan.actions;

import java.io.InputStream;
import java.util.PropertyResourceBundle;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.jface.commands.ActionHandler;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.ui.texteditor.FindReplaceAction;


/**
 * A handler for the {@code org.eclipse.ui.edit.findReplace} command.
 * 
 * @author agusevas
 */
public class FindReplaceHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    try {
      InputStream in = null;
      String resource = "plugin.properties"; //$NON-NLS-1$
      ClassLoader loader = this.getClass().getClassLoader();
      if (loader != null) {
        in = loader.getResourceAsStream(resource);
      }
      else {
        in = ClassLoader.getSystemResourceAsStream(resource);
      }

      IWorkbenchPart part = HandlerUtil.getActivePart(event);
      FindReplaceAction findReplaceAction = new FindReplaceAction(new PropertyResourceBundle(in), null, part);
      ActionHandler actionHandler = new ActionHandler(findReplaceAction);

      return actionHandler.execute(event);
    } catch (Exception e) {
      throw new ExecutionException("Could not execute command!", e); //$NON-NLS-1$
    }
  }

}
