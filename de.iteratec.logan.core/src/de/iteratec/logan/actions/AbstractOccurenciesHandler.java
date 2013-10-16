package de.iteratec.logan.actions;

import de.iteratec.logan.AppUtils;

import org.eclipse.core.commands.AbstractHandler;


public abstract class AbstractOccurenciesHandler extends AbstractHandler {
  public static final String PARAMETER_ID = "de.iteratec.logan.commands.OccurenciesParameter"; //$NON-NLS-1$

  protected String getAnnotationType(String style) {
    if (style == null) {
      // TODO agu add logging
      return AppUtils.ANN_STYLE1;
    }

    if (style.endsWith("1")) { //$NON-NLS-1$
      return AppUtils.ANN_STYLE1;
    }
    else if (style.endsWith("2")) { //$NON-NLS-1$
      return AppUtils.ANN_STYLE2;
    }
    else if (style.endsWith("3")) { //$NON-NLS-1$
      return AppUtils.ANN_STYLE3;
    }
    else if (style.endsWith("4")) { //$NON-NLS-1$
      return AppUtils.ANN_STYLE4;
    }
    else if (style.endsWith("5")) { //$NON-NLS-1$
      return AppUtils.ANN_STYLE5;
    }

    return AppUtils.ANN_STYLE1;
  }
}
