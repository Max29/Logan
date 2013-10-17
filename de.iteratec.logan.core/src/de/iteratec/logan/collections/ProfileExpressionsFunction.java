package de.iteratec.logan.collections;

import com.google.common.base.Function;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;


public class ProfileExpressionsFunction implements Function<Profile, Iterable<Expression>> {
  @Override
  public Iterable<Expression> apply(Profile profile) {
    return profile.getExpressions();
  }
}
