package de.iteratec.logan.common.model;

import com.thoughtworks.xstream.annotations.XStreamOmitField;


@SuppressWarnings("nls")
public class Expression extends AbstractModelObject {
  public final static String NAME             = "name";
  public final static String SEARCH           = "searchExpression";
  public static final String ENABLED_PROP     = "enabled";
  public static final String REGEXP_PROP      = "regexp";
  public static final String CASE_SENSITIVE   = "caseSensitive";
  public static final String BACKGROUND_COLOR = "backgroundColor";
  public static final String TEXT_COLOR       = "textColor";

  private String             name;
  private String             searchExpression;
  private boolean            caseSensitive    = false;
  private boolean            regexp           = false;
  @XStreamOmitField
  private boolean            enabled          = false;
  private String             backgroundColor  = "255,255,255";     //$NON-NLS-1$
  private String             textColor        = "0,0,0";           //$NON-NLS-1$

  public Expression() {
  }

  public Expression(String searchExpression) {
    this.searchExpression = searchExpression;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    firePropertyChange(NAME, this.name, this.name = name);
  }

  public String getSearchExpression() {
    return searchExpression;
  }

  public void setSearchExpression(String searchExpression) {
    firePropertyChange(SEARCH, this.searchExpression, this.searchExpression = searchExpression);
  }

  public boolean match(String value) {
    return value.contains(searchExpression);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    firePropertyChange(ENABLED_PROP, this.enabled, this.enabled = enabled);
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    firePropertyChange(BACKGROUND_COLOR, this.backgroundColor, this.backgroundColor = backgroundColor);
  }

  public String getTextColor() {
    return textColor;
  }

  public void setTextColor(String textColor) {
    firePropertyChange(TEXT_COLOR, this.textColor, this.textColor = textColor);
  }

  public boolean isRegexp() {
    return regexp;
  }

  public void setRegexp(boolean regexp) {
    firePropertyChange(REGEXP_PROP, this.regexp, this.regexp = regexp);
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public void setCaseSensitive(boolean caseSensitive) {
    firePropertyChange(CASE_SENSITIVE, this.caseSensitive, this.caseSensitive = caseSensitive);
  }

  @Override
  public String toString() {
    return "Expression [name=" + name + ", searchExpression=" + searchExpression + ", caseSensitive=" + caseSensitive
        + ", regexp=" + regexp + ", enabled=" + enabled + ", backgroundColor=" + backgroundColor + ", textColor="
        + textColor + "]";
  }

  public Expression clone() {
    Expression clone = new Expression(searchExpression);
    clone.name = name;
    clone.caseSensitive = caseSensitive;
    clone.regexp = regexp;
    clone.enabled = enabled;
    clone.backgroundColor = backgroundColor;
    clone.textColor = textColor;

    return clone;
  }

}
