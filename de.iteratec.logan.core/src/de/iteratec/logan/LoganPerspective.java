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

import de.iteratec.logan.view.SearchResultView;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class LoganPerspective implements IPerspectiveFactory {

  private static final String MULTI_VIEW = ":*"; //$NON-NLS-1$

  public LoganPerspective() {
  }

  @Override
  public void createInitialLayout(IPageLayout layout) {
    String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible(true);

    IFolderLayout f1 = layout.createFolder("SearchResultsFolder", IPageLayout.BOTTOM, 0.75f, editorArea); //$NON-NLS-1$
    f1.addPlaceholder(SearchResultView.ID + MULTI_VIEW);
  }
}
