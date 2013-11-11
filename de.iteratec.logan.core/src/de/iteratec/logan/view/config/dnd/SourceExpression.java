/**
 * 
 */
package de.iteratec.logan.view.config.dnd;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;


/**
 * Helper class to identify the source profile and expression.
 * 
 * @author agu
 */
public class SourceExpression {

  private final Profile    profile;
  private final Expression expression;

  public SourceExpression(Profile profile, Expression expression) {
    super();
    this.profile = profile;
    this.expression = expression;
  }

  public Profile getProfile() {
    return profile;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  @SuppressWarnings("nls")
  public String toString() {
    return "SourceExpression [profile=" + profile + ", expression=" + expression + "]";
  }

}
