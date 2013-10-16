package de.iteratec.logan.service;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.eclipse.swt.graphics.Image;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import org.eclipse.jface.resource.ImageDescriptor;


public class ImageUtils {
  private static final String ICONS_FOLDER = "icons/"; //$NON-NLS-1$

  public static Image getImage(String file) {
    Bundle bundle = FrameworkUtil.getBundle(ImageUtils.class);
    URL url = FileLocator.find(bundle, new Path(ICONS_FOLDER + file), null);
    ImageDescriptor image = ImageDescriptor.createFromURL(url);
    return image.createImage();
  }
}
