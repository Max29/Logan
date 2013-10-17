/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package de.iteratec.logan;

import de.iteratec.logan.service.UpdateJob;

import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Point;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchWindow;


@SuppressWarnings("restriction")
public class LoganWorkbenchAdvisor extends WorkbenchAdvisor {
  public LoganWorkbenchAdvisor() {
  }

  @Override
  public String getInitialWindowPerspectiveId() {
    return "de.iteratec.logan.LoganPerspective"; //$NON-NLS-1$
  }

  @Override
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
    return new WorkbenchWindowAdvisor(configurer) {
      @Override
      public void preWindowOpen() {
        super.preWindowOpen();
        IWorkbenchWindowConfigurer wc = getWindowConfigurer();
        wc.setInitialSize(new Point(600, 450));
        wc.setShowCoolBar(true);
        wc.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);

        configurer.addEditorAreaTransfer(FileTransfer.getInstance());
        configurer.configureEditorAreaDropListener(new EditorAreaDropAdapter(configurer.getWindow()));
      }

      @Override
      public void postWindowOpen() {
        hideQuickAccess();

        UpdateJob updateJob = new UpdateJob();
        updateJob.schedule();
      }

      private void hideQuickAccess() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window instanceof WorkbenchWindow) {
          MWindow model = ((WorkbenchWindow) window).getModel();
          EModelService modelService = model.getContext().get(EModelService.class);
          MToolControl searchField = (MToolControl) modelService.find("SearchField", model); //$NON-NLS-1$
          if (searchField != null) {
            searchField.setToBeRendered(false);
            MTrimBar trimBar = modelService.getTrim((MTrimmedWindow) model, SideValue.TOP);
            // trimBar.getChildren().clear();
            trimBar.getChildren().remove(searchField);
          }
        }
      }

      @Override
      public void postWindowCreate() {
        super.postWindowCreate();
        IWorkbenchWindowConfigurer wc = getWindowConfigurer();
        IWorkbenchPage[] pages = wc.getWindow().getPages();
        for (int i = 0; i < pages.length; i++) {
          pages[i].hideActionSet("org.eclipse.ui.actionSet.openFiles"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.search.searchActionSet"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.cheatsheets.actionSet"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.actionSet.keyBindings"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.WorkingSetActionSet"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.search.searchActionSet"); //$NON-NLS-1$
          pages[i].hideActionSet("org.eclipse.ui.externaltools.ExternalToolsSet"); //$NON-NLS-1$
        }

      }

      @Override
      public boolean preWindowShellClose() {
        try {
          // save the full workspace before quit
          ResourcesPlugin.getWorkspace().save(true, null);
        } catch (final CoreException e) {
          // log exception, if required
        }

        return true;
      }

      @Override
      public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer abConfigurer) {
        return new LoganActionBarAdvisor(abConfigurer);
      }
    };
  }
}
