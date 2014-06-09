package de.iteratec.logan.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import de.iteratec.logan.AppUtils;
import de.iteratec.logan.Messages;
import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;
import de.iteratec.logan.common.service.ProfilePersister;
import de.iteratec.logan.common.utils.ModelUtils;
import de.iteratec.logan.editor.results.SearchResultViewer;
import de.iteratec.logan.preferences.LoganPreferences;
import de.iteratec.logan.preferences.SearchResultsCreation;
import de.iteratec.logan.search.FileMatch;
import de.iteratec.logan.search.LoganalyserSearchEngine;
import de.iteratec.logan.search.SearchUtils;
import de.iteratec.logan.search.TextSearchResult;
import de.iteratec.logan.service.ColorManager;
import de.iteratec.logan.service.ImageUtils;
import de.iteratec.logan.service.Images;
import de.iteratec.logan.view.config.CheckStateListener;
import de.iteratec.logan.view.config.DoubleClickListener;
import de.iteratec.logan.view.config.ProfileLabelProvider;
import de.iteratec.logan.view.config.ProfileSaveable;
import de.iteratec.logan.view.config.ProfileSaveablesProvider;
import de.iteratec.logan.view.config.ProfilesContentProvider;
import de.iteratec.logan.view.config.TreeSelectionUtils;
import de.iteratec.logan.view.config.actions.AddProfileAction;
import de.iteratec.logan.view.config.actions.AddProfileExpressionAction;
import de.iteratec.logan.view.config.actions.RemoveExpressionAction;
import de.iteratec.logan.view.config.actions.RemoveProfileAction;
import de.iteratec.logan.view.config.dialog.AddExpressionDialog;
import de.iteratec.logan.view.config.dialog.AddProfileDialog;
import de.iteratec.logan.view.config.dnd.ExpressionDragListener;
import de.iteratec.logan.view.config.dnd.ExpressionDropListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.core.resources.IFile;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.window.Window;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISaveableFilter;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.ISaveablesLifecycleListener;
import org.eclipse.ui.ISaveablesSource;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.SaveablesLifecycleEvent;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;


public class SearchView extends ViewPart implements ISaveablesSource, ISaveablePart {
  public static final String      ID                      = "de.iteratec.logan.view.SearchView"; //$NON-NLS-1$
  public static final String      LOADED_PROFILES         = "SearchView.loadedProfiles";        //$NON-NLS-1$
  private static String           profilesDirectoryPath;

  private ProfileTreeViewer       treeViewer;
  private ProfilesContentProvider profilesContentProvider = new ProfilesContentProvider();
  private IMemento                memento;

  @Override
  public void init(IViewSite site, IMemento mementoParam) throws PartInitException {
    super.init(site, mementoParam);
    this.memento = mementoParam;
  }

  @Override
  public void createPartControl(final Composite parent) {
    Composite top = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    top.setLayout(layout);

    Button searchButton = new Button(top, SWT.PUSH);
    searchButton.setText(Messages.SearchView_search);
    Image image = ImageUtils.getImage(Images.FIND);
    searchButton.setImage(image);

    searchButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        SearchJob searchJob = new SearchJob();
        searchJob.setUser(true);
        searchJob.setPriority(Job.INTERACTIVE);
        searchJob.schedule();
      }
    });

    addPersistenceButtons(top);
    addTreeActionsView(top);

    treeViewer = new ProfileTreeViewer(top);
    treeViewer.setContentProvider(profilesContentProvider);
    treeViewer.setLabelProvider(new ProfileLabelProvider());
    treeViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
    treeViewer.setInput(Lists.newArrayList());
    CheckStateListener checkStateListener = new CheckStateListener();
    treeViewer.addCheckStateListener(checkStateListener);
    treeViewer.setCheckStateProvider(checkStateListener);
    ColumnViewerToolTipSupport.enableFor(treeViewer);

    int operations = DND.DROP_COPY | DND.DROP_MOVE;
    Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    treeViewer.addDragSupport(operations, transferTypes, new ExpressionDragListener(treeViewer));
    treeViewer.addDropSupport(operations, transferTypes, new ExpressionDropListener(treeViewer, this));

    final MenuManager mgr = new MenuManager();
    mgr.setRemoveAllWhenShown(true);
    mgr.addMenuListener(new IMenuListener() {
      @Override
      public void menuAboutToShow(IMenuManager manager) {
        ITreeSelection selection = (ITreeSelection) treeViewer.getSelection();
        if (!selection.isEmpty()) {
          Object firstElement = selection.getFirstElement();
          if (firstElement instanceof Profile) {
            Profile profile = (Profile) firstElement;

            mgr.add(new AddProfileExpressionAction(SearchView.this, profile));

            List<Profile> selectedProfiles = TreeSelectionUtils.getSelectedProfiles(treeViewer);
            mgr.add(new RemoveProfileAction(SearchView.this, selectedProfiles));
          }
          else if (firstElement instanceof Expression) {
            List<Expression> selectedExpressions = TreeSelectionUtils.getSelectedExpressions(treeViewer);
            mgr.add(new RemoveExpressionAction(SearchView.this, selectedExpressions));
          }
        }
        else {
          mgr.add(new AddProfileAction(SearchView.this));
        }
      }
    });
    treeViewer.getControl().setMenu(mgr.createContextMenu(treeViewer.getControl()));
    treeViewer.addDoubleClickListener(new DoubleClickListener(getViewSite().getShell(), profilesContentProvider));

    getSite().setSelectionProvider(treeViewer);

    GridData gridData = new GridData();
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalSpan = 2;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;
    treeViewer.getControl().setLayoutData(gridData);

    restoreState();
  }

  private void addTreeActionsView(Composite top) {
    Composite treeActionsView = new Composite(top, SWT.NONE);
    GridLayout layout = new GridLayout(3, false);
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    treeActionsView.setLayout(layout);

    Button selectAllButton = new Button(treeActionsView, SWT.FLAT | SWT.PUSH);
    selectAllButton.setText(Messages.SearchView_selectAll);
    selectAllButton.setImage(ImageUtils.getImage(Images.CHECKED));
    selectAllButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        List<Profile> profiles = profilesContentProvider.getProfiles();
        for (Profile profile : profiles) {
          CheckStateListener.profileStateChanged(profile, treeViewer, true);
        }
      }
    });

    Button deselectAllButton = new Button(treeActionsView, SWT.FLAT | SWT.PUSH);
    deselectAllButton.setText(Messages.SearchView_deselectAll);
    deselectAllButton.setImage(ImageUtils.getImage(Images.UNCHECKED));
    deselectAllButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        List<Profile> profiles = profilesContentProvider.getProfiles();
        for (Profile profile : profiles) {
          CheckStateListener.profileStateChanged(profile, treeViewer, false);
        }
      }
    });
  }

  private void restoreState() {
    if (memento == null) {
      return;
    }

    String value = memento.getString(LOADED_PROFILES);
    if (value == null) {
      return;
    }

    Iterable<String> fileNames = Splitter.on(',').split(value);
    for (String filePath : fileNames) {
      File file = new File(filePath);
      if (file.exists()) {
        Profile profile = ProfilePersister.getInstance().read(file);
        addProfile(profile, file, true);
        profilesDirectoryPath = file.getParent();
      }
    }
  }

  private void addPersistenceButtons(Composite top) {
    Composite expressionComposite = new Composite(top, SWT.NONE);
    GridLayout expressionLayout = new GridLayout(3, false);
    expressionLayout.marginHeight = 0;
    expressionLayout.marginWidth = 0;
    expressionComposite.setLayout(expressionLayout);

    Button addButton = new Button(expressionComposite, SWT.PUSH);
    addButton.setText(Messages.SearchView_loadProfiles);
    Image loadProfileImage = ImageUtils.getImage(Images.LOAD_PROFILES);
    addButton.setImage(loadProfileImage);
    addButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        List<File> files = AppUtils.queryFile(getSite().getShell(), profilesDirectoryPath);
        if (files.isEmpty()) {
          return;
        }

        for (File file : files) {
          Profile profile = ProfilePersister.getInstance().read(file);
          addProfile(profile, file, false);
          profilesDirectoryPath = file.getParent();
        }
      }
    });

    Button saveButton = new Button(expressionComposite, SWT.PUSH);
    saveButton.setText(Messages.SearchView_saveAll);
    Image image = ImageUtils.getImage(Images.SAVE_ALL);
    saveButton.setImage(image);
    saveButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        ISaveableFilter filter = new ISaveableFilter() {
          @Override
          public boolean select(Saveable saveable, IWorkbenchPart[] containingParts) {
            return saveable instanceof ProfileSaveable;
          }
        };

        IWorkbenchWindow workbenchWindow = getViewSite().getWorkbenchWindow();
        IWorkbench workbench = workbenchWindow.getWorkbench();
        workbench.saveAll(workbenchWindow, workbenchWindow, filter, false);
        treeViewer.refresh();
      }
    });

    Button newProfileButton = new Button(expressionComposite, SWT.PUSH);
    newProfileButton.setText(Messages.SearchView_newProfile);
    Image newProfileImage = ImageUtils.getImage(Images.NEW_PROFILE);
    newProfileButton.setImage(newProfileImage);
    newProfileButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        AddProfileAction addProfileAction = new AddProfileAction(SearchView.this);
        addProfileAction.run();
      }
    });
  }

  @Override
  public void setFocus() {

  }

  @Override
  public void saveState(IMemento mementoParam) {
    super.saveState(mementoParam);

    List<String> files = Lists.newArrayList();
    List<Profile> profiles = profilesContentProvider.getProfiles();
    for (Profile profile : profiles) {
      ProfileSaveable profileSaveable = ProfileSaveablesProvider.getInstance().get(profile);
      File file = profileSaveable.getProfileFile();
      if (file != null) {
        files.add(file.getAbsolutePath());
      }
    }

    mementoParam.putString(LOADED_PROFILES, Joiner.on(',').join(files));
  }

  public void addProfile(Profile profile, File file, boolean restore) {
    List<Profile> profiles = profilesContentProvider.getProfiles();
    if (containsProfile(profile, profiles)) {
      String msg = String.format(Messages.SearchView_profileAlreadyOpen, profile.getName());
      MessageDialog.openError(getViewSite().getShell(), Messages.SearchView_dublicateProfile, msg);

      return;
    }

    profiles.add(profile);

    ProfileSaveable saveable = ProfileSaveablesProvider.getInstance().addMapping(profile);
    saveable.setProfileFile(file);
    saveable.setDirty(file == null);

    if (!restore) {
      saveablesEvent(saveable, SaveablesLifecycleEvent.POST_OPEN);
    }

    treeViewer.refresh();
  }

  private boolean containsProfile(Profile profileToAdd, List<Profile> profiles) {
    for (Profile profile : profiles) {
      if (profile.getName().equals(profileToAdd.getName())) {
        return true;
      }
    }

    return false;
  }

  public void removeProvile(Profile profile) {
    ProfileSaveablesProvider profileSaveablesProvider = ProfileSaveablesProvider.getInstance();
    ProfileSaveable toRemove = profileSaveablesProvider.get(profile);

    SaveablesLifecycleEvent event = saveablesEvent(toRemove, SaveablesLifecycleEvent.PRE_CLOSE);
    if (!event.isVeto()) {
      profileSaveablesProvider.removeMapping(profile);
      profilesContentProvider.getProfiles().remove(profile);
      treeViewer.refresh();

      saveablesEvent(toRemove, SaveablesLifecycleEvent.POST_CLOSE);
    }
  }

  public void addProfileExpression(Profile profile) {
    AddExpressionDialog dialog = new AddExpressionDialog(getViewSite().getShell());
    dialog.open();
    if (dialog.getReturnCode() == Window.OK) {
      Expression expression = dialog.getExpression();
      addProfileExpression(profile, expression);
    }
  }

  public void addProfileExpression(Profile profile, Expression expression) {
    profile.getExpressions().add(expression);

    ProfileSaveable profileSaveable = ProfileSaveablesProvider.getInstance().get(profile);
    profileSaveable.setDirty(true);
    saveablesEvent(profileSaveable, SaveablesLifecycleEvent.DIRTY_CHANGED);

    treeViewer.refresh();
    treeViewer.setExpandedState(profile, true);
  }

  public void addNewProfile() {
    AddProfileDialog dialog = new AddProfileDialog(getViewSite().getShell());
    dialog.open();
    if (dialog.getReturnCode() == Window.OK) {
      Profile profile = dialog.getProfile();
      addProfile(profile, null, false);
    }
  }

  public void moveExpression(Expression expression, Profile sourceProfile, Profile targetProfile, int expressionIndex) {
    sourceProfile.getExpressions().remove(expression);
    if (expressionIndex > targetProfile.getExpressions().size() - 1) {
      targetProfile.getExpressions().add(expression);
    }
    else {
      targetProfile.getExpressions().add(expressionIndex, expression);
    }

    ProfileSaveable profileSaveable = ProfileSaveablesProvider.getInstance().get(sourceProfile);
    profileSaveable.setDirty(true);
    saveablesEvent(profileSaveable, SaveablesLifecycleEvent.DIRTY_CHANGED);

    profileSaveable = ProfileSaveablesProvider.getInstance().get(targetProfile);
    profileSaveable.setDirty(true);
    saveablesEvent(profileSaveable, SaveablesLifecycleEvent.DIRTY_CHANGED);
  }

  public void removeExpression(Expression expression) {
    Profile selectedProfile = ModelUtils.getselectedProfile(profilesContentProvider.getProfiles(), expression);
    if (selectedProfile != null) {
      selectedProfile.getExpressions().remove(expression);

      ProfileSaveable profileSaveable = ProfileSaveablesProvider.getInstance().get(selectedProfile);
      profileSaveable.setDirty(true);
      saveablesEvent(profileSaveable, SaveablesLifecycleEvent.DIRTY_CHANGED);

      treeViewer.refresh();
    }
  }

  private SaveablesLifecycleEvent saveablesEvent(ProfileSaveable profileSaveable, int flag) {
    ISaveablesLifecycleListener lifecycleListener = (ISaveablesLifecycleListener) getSite().getService(
        ISaveablesLifecycleListener.class);
    ProfileSaveable[] removeArray = new ProfileSaveable[] { profileSaveable };
    SaveablesLifecycleEvent event = new SaveablesLifecycleEvent(this, flag, removeArray, false);
    lifecycleListener.handleLifecycleEvent(event);

    return event;
  }

  @Override
  public void dispose() {
    super.dispose();
    ColorManager.getInstance().dispose();
  }

  private void doSearch(final IProgressMonitor monitor) {
    monitor.beginTask(Messages.SearchView_searchJobTitle, 10);

    final ITextEditor activeEditor = (ITextEditor) getViewSite().getPage().getActiveEditor();
    if (activeEditor != null) {
      final IFile file = (IFile) activeEditor.getEditorInput().getAdapter(IFile.class);
      SubProgressMonitor searchMonitor = new SubProgressMonitor(monitor, 10);
      final TextSearchResult textSearchResult = LoganalyserSearchEngine.searchProfiles(file,
          profilesContentProvider.getProfiles(), searchMonitor);
      final List<FileMatch> searchResult = LoganalyserSearchEngine.getFileMatches(textSearchResult);

      Display.getDefault().asyncExec(new Runnable() {
        @Override
        public void run() {
          showSearchResults(textSearchResult, searchResult, activeEditor);
        }
      });
    }
  }

  private void showSearchResults(TextSearchResult textSearchResult, List<FileMatch> fileMatches,
                                 ITextEditor activeEditor) {

    IWorkbenchWindow workbenchWindow = getSite().getWorkbenchWindow();
    final IWorkbenchPage page = workbenchWindow.getActivePage();

    try {
      IDocumentProvider documentProvider = activeEditor.getDocumentProvider();
      IDocument document = documentProvider.getDocument(activeEditor.getEditorInput());
      SearchUtils.addLineInformation(document, fileMatches);
      Iterable<String> matches = SearchUtils.getMatchineLines(document, fileMatches);
      final String searchResultText = searchResultText(matches);

      SearchResultView searchResultView = createResultsView(activeEditor, page);
      SearchResultViewer searchResultViewer = searchResultView.getTextViewer();
      searchResultViewer.setTextSearchResult(textSearchResult);
      searchResultViewer.setDocument(new Document(searchResultText));

      int offset = 0;
      int count = 0;
      TextPresentation presentation = new TextPresentation(fileMatches.size() + 1);
      List<String> matchesList = Lists.newArrayList(matches);
      for (FileMatch fileMatch : fileMatches) {
        Expression expression = fileMatch.getExpression();
        String backgroundColorValue = expression.getBackgroundColor();
        String textColorValue = expression.getTextColor();
        Color foreground = ColorManager.getInstance().getColor(textColorValue);
        Color backgroundColor = ColorManager.getInstance().getColor(backgroundColorValue);

        TextAttribute attr = new TextAttribute(foreground, backgroundColor, 0);
        int length = matchesList.get(count).length();
        StyleRange styleRange = new StyleRange(offset, length, attr.getForeground(), attr.getBackground());
        presentation.addStyleRange(styleRange);

        offset = offset + length;
        count++;
      }
      searchResultViewer.changeTextPresentation(presentation, true);
    } catch (PartInitException e2) {
      e2.printStackTrace();
    }
  }

  private SearchResultView createResultsView(ITextEditor activeEditor, final IWorkbenchPage page)
      throws PartInitException {
    String secondaryId = null;
    SearchResultView searchResultView = null;
    SearchResultsCreation resultsMode = LoganPreferences.getSearchResultsCreation();
    switch (resultsMode) {
      case ONCE:
        secondaryId = "once"; //$NON-NLS-1$
        break;
      case EDITOR:
        secondaryId = activeEditor.getTitle();
        break;
      case SEARCH:
        secondaryId = UUID.randomUUID().toString();
        break;
      default:
        break;
    }

    page.showView(SearchResultView.ID, secondaryId, IWorkbenchPage.VIEW_CREATE);
    searchResultView = (SearchResultView) page.showView(SearchResultView.ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE);

    return searchResultView;
  }

  private String searchResultText(Iterable<String> matches) {
    String result = ""; //$NON-NLS-1$
    int size = 0;
    for (String string : matches) {
      size = size + string.length();
    }

    // concanate small strings in memory
    if (size < 10000000) {
      StringBuilder sb = new StringBuilder(size);
      for (String string : matches) {
        sb.append(string);
      }

      result = sb.toString();
    }
    else {
      // concanate small strings in file
      // TODO maybe this can be improved?
      try {
        File createTempFile = File.createTempFile("searchResult", null); //$NON-NLS-1$
        BufferedWriter writer = Files.newWriter(createTempFile, Charset.defaultCharset());
        for (String string : matches) {
          writer.write(string);
        }
        writer.close();

        result = Files.toString(createTempFile, Charset.defaultCharset());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return result;
  }

  private class SearchJob extends Job {

    public SearchJob() {
      super(Messages.SearchView_searchJobName);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
      try {
        doSearch(monitor);
      } finally {
        monitor.done();
      }
      return Status.OK_STATUS;
    }
  }

  public ProfileTreeViewer getTreeViewer() {
    return treeViewer;
  }

  public static String getProfilesDirectoryPath() {
    return profilesDirectoryPath;
  }

  @Override
  public void doSave(IProgressMonitor monitor) {
    // ignore
  }

  @Override
  public void doSaveAs() {
    // ignore
  }

  @Override
  public boolean isDirty() {
    return ProfileSaveablesProvider.getInstance().isAnyDirty();
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  @Override
  public boolean isSaveOnCloseNeeded() {
    return true;
  }

  @Override
  public Saveable[] getSaveables() {
    return ProfileSaveablesProvider.getInstance().getSaveables();
  }

  @Override
  public Saveable[] getActiveSaveables() {
    // TODO agu fix this
    return ProfileSaveablesProvider.getInstance().getSaveables();
  }

}
