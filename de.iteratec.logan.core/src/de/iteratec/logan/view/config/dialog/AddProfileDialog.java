package de.iteratec.logan.view.config.dialog;

import de.iteratec.logan.Messages;
import de.iteratec.logan.common.model.Profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;

import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;


public class AddProfileDialog extends TitleAreaDialog {
  private boolean dirty;
  private Text    profileNameText;
  private Text    profileDescriptionText;
  private Profile profile;

  public AddProfileDialog(Shell parentShell) {
    super(parentShell);
    this.profile = new Profile();
  }

  public AddProfileDialog(Shell parentShell, Profile profile) {
    super(parentShell);
    this.profile = profile;
  }

  @Override
  public void create() {
    super.create();
    setTitle(Messages.AddProfileDialog_title);
    setMessage(Messages.AddProfileDialog_captionMessage, IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(final Composite parent) {
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    parent.setLayout(layout);

    // The text fields will grow with the size of the dialog
    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;

    Label profileNameLabel = new Label(parent, SWT.NONE);
    profileNameLabel.setText(Messages.AddProfileDialog_name);
    profileNameText = new Text(parent, SWT.BORDER);
    profileNameText.setLayoutData(gridData);

    Label profileDescriptionLabel = new Label(parent, SWT.NONE);
    profileDescriptionLabel.setText(Messages.AddProfileDialog_description);
    profileDescriptionText = new Text(parent, SWT.BORDER);
    profileDescriptionText.setLayoutData(gridData);

    DataBindingContext bindingContext = new DataBindingContext();
    bindingContext.bindValue(SWTObservables.observeText(profileNameText, SWT.Modify),
        PojoObservables.observeValue(profile, Profile.NAME_PROP));
    bindingContext.bindValue(SWTObservables.observeText(profileDescriptionText, SWT.Modify),
        PojoObservables.observeValue(profile, Profile.DESCRIPTION_PROP));

    bindingContext.updateTargets();

    IObservableList bindings = bindingContext.getValidationStatusProviders();
    for (Object o : bindings) {
      Binding b = (Binding) o;
      b.getTarget().addChangeListener(listener);
    }

    return parent;
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  @Override
  protected void okPressed() {
    if (profileNameText.getText().length() != 0) {
      if (profile == null) {
        profile = new Profile();
      }

      profile.setName(profileNameText.getText());
      profile.setDescription(profileDescriptionText.getText());
      super.okPressed();
    }
    else {
      setErrorMessage(Messages.AddProfileDialog_errorRequiredData);
    }
  }

  public Profile getProfile() {
    return profile;
  }

  public boolean isDirty() {
    return dirty;
  }

  IChangeListener listener = new IChangeListener() {
                             @Override
                             public void handleChange(ChangeEvent event) {
                               dirty = true;
                             }
                           };
}
