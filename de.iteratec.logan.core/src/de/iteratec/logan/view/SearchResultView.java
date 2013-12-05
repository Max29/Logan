package de.iteratec.logan.view;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.editor.results.CustomTextSourceViewerConfiguration;
import de.iteratec.logan.editor.results.SearchResultViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.source.VerticalRuler;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.part.ViewPart;


public class SearchResultView extends ViewPart {

  public static final String ID = "de.iteratec.logan.view.SearchResultView"; //$NON-NLS-1$

  private SearchResultViewer textViewer;
  private IDocument          document;
  private IFindReplaceTarget fFindReplaceTarget;

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
    IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
    Action addItemAction = new Action("Copy All to clipboard") { //$NON-NLS-1$
      @Override
      public void run() {
        // a bit hacky...
        StyledText textWidget = textViewer.getTextWidget();
        Point selection = textWidget.getSelection();
        textWidget.setSelectionRange(0, Math.max(textWidget.getCharCount(), 0));
        textWidget.copy();
        textWidget.setSelection(selection);
      }
    };
    ISharedImages sharedImages = AppUtils.getActiveWorkbenchWindow().getWorkbench().getSharedImages();
    addItemAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));

    toolbarManager.add(addItemAction);
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

  @Override
  @SuppressWarnings("rawtypes")
  public Object getAdapter(Class adapter) {
    if (IFindReplaceTarget.class.equals(adapter)) {
      if (fFindReplaceTarget == null) {
        fFindReplaceTarget = (textViewer == null ? null : textViewer.getFindReplaceTarget());
      }
      return fFindReplaceTarget;
    }

    return super.getAdapter(adapter);
  }
}
