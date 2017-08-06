package com.andframe.annotation.resource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified color resource ID. Type can be {@code int} or
 * <pre><code>
 * {@literal @}BindColor(R.color.background_green) int green;
 * {@literal @}BindColor(R.color.background_green_selector) ColorStateList greenSelector;
 * </code></pre>
 */
@Retention(CLASS)
@Target(FIELD)
public @interface BindColor {
    /**
     * Color resource ID to which the field will be bound.
     */
    int value();
}
