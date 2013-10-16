package de.iteratec.logan.service;

import java.lang.reflect.InvocationTargetException;

import de.iteratec.logan.AppUtils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;

import org.eclipse.swt.widgets.Display;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.ui.statushandlers.StatusManager;


public class UpdateService {
  private final IProvisioningAgent agent;
  private ProvisioningSession      session;
  private UpdateOperation          operation;

  public UpdateService() {
    agent = getProvisioningAgent();
    session = new ProvisioningSession(agent);
    operation = new UpdateOperation(session);
  }

  public IStatus update(IProgressMonitor progressMonitor) {
    if (agent == null) {
      return new Status(Status.ERROR, "de.iteratec.logan.core", "Update failed!");
    }

    SubMonitor sub = SubMonitor.convert(progressMonitor, "Checking for application updates...", 200);
    IStatus result = operation.resolveModal(sub.newChild(100));
    if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
      // popUpInformation("Nothing to update!");
      return new Status(Status.OK, "de.iteratec.logan.core", "Nothing to update!");
    }
    else {
      installUpdates();
    }

    return result;
  }

  private void installUpdates() {
    Display.getDefault().asyncExec(new Runnable() {
      @Override
      public void run() {
        StringBuilder updates = new StringBuilder();
        Update[] possibleUpdates = operation.getPossibleUpdates();
        for (Update update : possibleUpdates) {
          updates.append(getUnitDescription(update.toUpdate));
          updates.append(" -> "); //$NON-NLS-1$
          updates.append(getUnitDescription(update.replacement));
          updates.append("\n"); //$NON-NLS-1$
        }

        boolean install = MessageDialog.openQuestion(null, "Updates", "Updates found! install?\n" + updates.toString());
        if (install) {
          ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
          try {
            dialog.run(true, true, new IRunnableWithProgress() {
              @Override
              public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
                SubMonitor sub = SubMonitor.convert(arg0, "Installing updates ...", 200);
                operation.resolveModal(sub.newChild(100));
                ProvisioningJob job = operation.getProvisioningJob(arg0);
                job.runModal(sub.newChild(100));
                AppUtils.restartAfterUpdate();
              }
            });
          } catch (Exception e) {
            e.printStackTrace();
            // TODO agu
            IStatus result = new Status(Status.ERROR, "de.iteratec.logan.core", "Update failed!", e);
            StatusManager.getManager().handle(result);
          }
        }
      }

    });
  }

  private String getUnitDescription(IInstallableUnit unit) {
    String name = unit.getProperty(IInstallableUnit.PROP_NAME);
    Version version = unit.getVersion();
    String versionValue = version.getOriginal();

    return name + " " + versionValue; //$NON-NLS-1$
  }

  public IProvisioningAgent getProvisioningAgent() {
    BundleContext bundleContext = FrameworkUtil.getBundle(UpdateService.class).getBundleContext();
    ServiceReference<IProvisioningAgent> serviceReference = bundleContext.getServiceReference(IProvisioningAgent.class);

    return bundleContext.getService(serviceReference);
  }
}
