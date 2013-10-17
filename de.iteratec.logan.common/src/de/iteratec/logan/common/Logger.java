package de.iteratec.logan.common;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;


public class Logger {
  private static final String ID     = "de.iteratec.logan"; //$NON-NLS-1$
  private static Logger       LOGGER = new Logger();
  private final ILog          log;

  private Logger() {
    Bundle bundle = Activator.getContext().getBundle();
    log = Platform.getLog(bundle);
  }

  public static Logger getInstance() {
    return LOGGER;
  }

  public void log(String message) {
    log.log(new Status(IStatus.INFO, ID, message));
  }
}
