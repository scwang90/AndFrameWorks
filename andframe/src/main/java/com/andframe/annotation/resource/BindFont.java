package com.andframe.annotation.resource;

import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.support.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified font resource ID.
 * <pre><code>
 * {@literal @}BindFont(R.font.comic_sans) Typeface comicSans;
 * </code></pre>
 */
@Retention(CLASS)
@Target(FIELD)
public @interface BindFont {
    /**
     * Font resource ID to which the field will be bound.
     */
    int value() default -1;
    String idname() default "";

    @TypefaceStyle int style() default Typeface.NORMAL;

    @IntDef({
            Typeface.NORMAL,
            Typeface.BOLD,
            Typeface.ITALIC,
            Typeface.BOLD_ITALIC
    })
    @RestrictTo(LIBRARY)
    @interface TypefaceStyle {
    }
}
