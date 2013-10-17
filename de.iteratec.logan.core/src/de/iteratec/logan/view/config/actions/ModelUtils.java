package de.iteratec.logan.view.config.actions;

import java.util.List;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;


public class ModelUtils {
  public static Profile getselectedProfile(List<Profile> profiles, Expression selectedExpression) {
    for (Profile profile : profiles) {
      List<Expression> expressions = profile.getExpressions();
      for (Expression expression : expressions) {
        if (selectedExpression.equals(expression)) {
          return profile;
        }
      }
    }

    return null;
  }
}
