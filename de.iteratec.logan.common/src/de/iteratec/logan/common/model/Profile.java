package de.iteratec.logan.common.model;

import java.util.List;

import com.google.common.collect.Lists;


@SuppressWarnings("nls")
public class Profile extends AbstractModelObject {
  public static final String NAME_PROP        = "name";
  public static final String DESCRIPTION_PROP = "description";

  private String             name;
  private String             description;
  private List<Expression>   expressions      = Lists.newArrayList();

  public Profile() {
  }

  public Profile(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    firePropertyChange(NAME_PROP, this.name, this.name = name);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    firePropertyChange(DESCRIPTION_PROP, this.description, this.description = description);
  }

  public List<Expression> getExpressions() {
    return expressions;
  }

  public void setExpressions(List<Expression> expressions) {
    firePropertyChange("expressions", this.expressions, this.expressions = expressions);
  }

  @Override
  public String toString() {
    return "Profile [name=" + name + ", description=" + description + ", expressions=" + expressions + "]";
  }

}
