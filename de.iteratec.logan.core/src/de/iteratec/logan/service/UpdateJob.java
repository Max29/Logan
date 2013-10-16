package de.iteratec.logan.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;


/**
 * A Basic Update Job.
 * 
 * @author agu
 */
public class UpdateJob extends Job {

  public UpdateJob() {
    super("Checking for updates");
  }

  @Override
  protected IStatus run(IProgressMonitor progressMonitor) {
    return new UpdateService().update(progressMonitor);
  }

}
