package com.andframe.processor.model;

import com.squareup.javapoet.TypeName;

/**
 * 描述参数类型及其在侦听器方法中的位置。
 * Represents a parameter type and its position in the listener method.
 */
public class Parameter {
  public static final Parameter[] NONE = new Parameter[0];

  public final int index;
  public final TypeName type;

  public Parameter(int index, TypeName type) {
    this.index = index;
    this.type = type;
  }

  public boolean requiresCast(String toType) {
    return !type.toString().equals(toType);
  }
}
