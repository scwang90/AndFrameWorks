package com.andframe.processor.model;

import com.squareup.javapoet.CodeBlock;

/**
 * 资源绑定抽象类
 */
public abstract class ResourceBinding {
  public abstract Id id();
  /** True if the code for this binding requires a 'res' variable for {@code Resources} access. */
  public abstract boolean requiresResources(int sdk);
  public abstract CodeBlock render(int sdk);
}
