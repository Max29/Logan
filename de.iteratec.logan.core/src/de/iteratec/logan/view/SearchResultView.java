package de.iteratec.logan.view;

import de.iteratec.logan.editor.results.CustomTextSourceViewerConfiguration;
import de.iteratec.logan.editor.results.SearchResultViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.VerticalRuler;

import org.eclipse.ui.part.ViewPart;


public class SearchResultView extends ViewPart {

  public static final String ID = "de.iteratec.logan.view.SearchResultView"; //$NON-NLS-1$

  private SearchResultViewer textViewer;
  private IDocument          document;

  @Override
  public void createPartControl(Composite parent) {
    this.textViewer = new SearchResultViewer(parent, new VerticalRuler(20), SWT.BORDER | SWT.READ_ONLY | SWT.MULTI
        | SWT.V_SCROLL | SWT.H_SCROLL);
    this.document = new Document();
    textViewer.setDocument(document);
    textViewer.configure(new CustomTextSourceViewerConfiguration());

    createActions();
    initializeToolBar();
    initializeMenu();
  }

  private void createActions() {
    // Create the actions
  }

  private void initializeToolBar() {
    // IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
  }

  private void initializeMenu() {
    // IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
  }

  @Override
  public void setFocus() {
    // Set the focus
  }

  public IDocument getDocument() {
    return document;
  }

  public SearchResultViewer getTextViewer() {
    return textViewer;
  }
}
