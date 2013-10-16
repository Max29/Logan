/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package de.iteratec.logan;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;


public class LoganApplication implements IApplication {

  @Override
  public Object start(IApplicationContext context) {
    Display display = PlatformUI.createDisplay();
    try {
      int returnCode = PlatformUI.createAndRunWorkbench(display, new LoganWorkbenchAdvisor());
      if (returnCode == PlatformUI.RETURN_RESTART) {
        return IApplication.EXIT_RESTART;
      }
      return IApplication.EXIT_OK;
    } finally {
      display.dispose();
    }
  }

  @Override
  public void stop() {
    if (!PlatformUI.isWorkbenchRunning())
      return;
    final IWorkbench workbench = PlatformUI.getWorkbench();
    final Display display = workbench.getDisplay();
    display.syncExec(new Runnable() {
      @Override
      public void run() {
        if (!display.isDisposed())
          workbench.close();
      }
    });
  }

}
