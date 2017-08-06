package com.andframe.processor.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import static com.andframe.processor.constant.ClassNames.ANDROID_R;

/**
 * 描绘Android资源的ID
 * Represents an ID of an Android resource.
 */
public class Id {

    public static final Id NO_ID = new Id(-1);

    public final int value;
    public final CodeBlock code;
    public final boolean qualifed;

    public Id(int value) {
        this.value = value;
        this.code = CodeBlock.of("$L", value);
        this.qualifed = false;
    }

    public Id(int value, ClassName className, String resourceName) {
        this.value = value;
        this.code = className.topLevelClassName().equals(ANDROID_R)
                ? CodeBlock.of("$L.$N", className, resourceName)
                : CodeBlock.of("$T.$N", className, resourceName);
        this.qualifed = true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Id && value == ((Id) o).value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Please use value or code explicitly");
    }
}
