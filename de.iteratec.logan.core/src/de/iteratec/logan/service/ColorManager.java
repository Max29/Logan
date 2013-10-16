package de.iteratec.logan.service;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.resource.StringConverter;


public class ColorManager {
  protected Map<RGB, Color>   colorTable = Maps.newHashMap();
  private static ColorManager INSTANCE;

  private ColorManager() {
  }

  public static ColorManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ColorManager();
    }

    return INSTANCE;
  }

  public void dispose() {
    Iterator<Color> e = colorTable.values().iterator();
    while (e.hasNext())
      e.next().dispose();
  }

  public Color getColor(String colorRgb) {
    RGB rgb = StringConverter.asRGB(colorRgb);
    Color color = colorTable.get(rgb);
    if (color == null) {
      color = new Color(Display.getCurrent(), rgb);
      colorTable.put(rgb, color);
    }
    return color;
  }

  public Color getColor(RGB rgb) {
    Color color = colorTable.get(rgb);
    if (color == null) {
      color = new Color(Display.getCurrent(), rgb);
      colorTable.put(rgb, color);
    }
    return color;
  }
}
