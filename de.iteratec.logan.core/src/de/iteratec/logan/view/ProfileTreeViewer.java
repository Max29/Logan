package de.iteratec.logan.view;

import de.iteratec.logan.model.Profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.viewers.CheckboxTreeViewer;


public class ProfileTreeViewer extends CheckboxTreeViewer {

  public static final String DEFAULT_PROFILE = "Default Profile"; //$NON-NLS-1$
  private Profile            defaultProfile  = new Profile();

  public ProfileTreeViewer(Composite parent) {
    super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    defaultProfile.setName(DEFAULT_PROFILE);
  }

  public Profile getDefaultProfile() {
    return defaultProfile;
  }
}
