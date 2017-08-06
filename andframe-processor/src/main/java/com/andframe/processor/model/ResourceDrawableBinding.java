package com.andframe.processor.model;

import com.squareup.javapoet.CodeBlock;

import static com.andframe.processor.constant.ClassNames.ANDFRAME_UTILS;
import static com.andframe.processor.constant.ClassNames.CONTEXT_COMPAT;

public class ResourceDrawableBinding extends ResourceBinding {
  private final Id id;
  private final String name;
  private final Id tintAttributeId;

  public ResourceDrawableBinding(Id id, String name, Id tintAttributeId) {
    this.id = id;
    this.name = name;
    this.tintAttributeId = tintAttributeId;
  }

  @Override public Id id() {
    return id;
  }

  @Override public boolean requiresResources(int sdk) {
    return false;
  }

  @Override public CodeBlock render(int sdk) {
    if (tintAttributeId.value != 0) {
      return CodeBlock.of("target.$L = $T.getTintedDrawable(context, $L, $L)", name, ANDFRAME_UTILS, id.code,
          tintAttributeId.code);
    }
    if (sdk >= 21) {
      return CodeBlock.of("target.$L = context.getDrawable($L)", name, id.code);
    }
    return CodeBlock.of("target.$L = $T.getDrawable(context, $L)", name, CONTEXT_COMPAT, id.code);
  }
}
