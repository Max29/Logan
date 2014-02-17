package de.iteratec.logan.common.utils;

import java.util.List;

import de.iteratec.logan.common.model.Expression;
import de.iteratec.logan.common.model.Profile;

public class ModelUtils {
	public static Profile getselectedProfile(List<Profile> profiles,
			Expression selectedExpression) {
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

	public static Profile getProfile(List<Profile> profiles, String name) {
		for (Profile profile : profiles) {
			if (profile.getName().equals(name)) {
				return profile;
			}
		}

		return null;
	}
}
