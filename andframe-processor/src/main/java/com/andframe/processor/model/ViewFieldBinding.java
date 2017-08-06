package com.andframe.processor.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class ViewFieldBinding {
  private final String name;
  private final TypeName type;
  private final boolean required;

  public ViewFieldBinding(String name, TypeName type, boolean required) {
    this.name = name;
    this.type = type;
    this.required = required;
  }

  public String getName() {
    return name;
  }

  public TypeName getType() {
    return type;
  }

  public ClassName getRawType() {
    if (type instanceof ParameterizedTypeName) {
      return ((ParameterizedTypeName) type).rawType;
    }
    return (ClassName) type;
  }

  @Override public String toString() {
    return "field '" + name + "'";
  }

  public boolean isRequired() {
    return required;
  }
}
