package com.andframe.annotation.resource;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * <pre><code>
 * {@literal @}BindBitmap(R.drawable.logo) Bitmap logo;
 * </code></pre>
 */
@Retention(CLASS)
@Target(FIELD)
public @interface BindBitmap {
    /**
     */
    int value();
}
