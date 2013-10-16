package de.iteratec.logan.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import de.iteratec.logan.model.Expression;
import de.iteratec.logan.model.Profile;


/**
 * @author agu
 */
public class ProfilePersister {
  private XStream                 xstream = new XStream(new StaxDriver());
  private static ProfilePersister persister;

  private ProfilePersister() {
    xstream.alias("profile", Profile.class); //$NON-NLS-1$
    xstream.alias("expression", Expression.class); //$NON-NLS-1$
    xstream.autodetectAnnotations(true);
  }

  public static ProfilePersister getInstance() {
    if (persister == null) {
      persister = new ProfilePersister();
    }

    return persister;
  }

  public void write(Profile profile, File file) {
    try {
      xstream.toXML(profile, new FileOutputStream(file));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
  }

  public Profile read(File file) {
    return (Profile) xstream.fromXML(file);
  }
}
