package de.iteratec.logan;

import org.osgi.framework.BundleContext;

import org.eclipse.ui.plugin.AbstractUIPlugin;


public class LoganPlugin extends AbstractUIPlugin {

  private static LoganPlugin plugin;

  public LoganPlugin() {

  }

  public static LoganPlugin getDefault() {
    return plugin;
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
    plugin = null;
  }
}
