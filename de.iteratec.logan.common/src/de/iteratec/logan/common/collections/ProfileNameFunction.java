/**
 * 
 */
package de.iteratec.logan.common.collections;

import com.google.common.base.Function;

import de.iteratec.logan.common.model.Profile;

/**
 * Converts the profile to its name.
 * 
 * @author agusevas
 */
public class ProfileNameFunction implements Function<Profile, String> {
	@Override
	public String apply(Profile profile) {
		return profile.getName();
	}
}
