package com.andframe.annotation.listener;

import android.support.annotation.IdRes;
import android.view.View;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static android.view.View.OnClickListener;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@link OnClickListener OnClickListener} on the view for each ID specified.
 * <pre><code>
 * {@literal @}BindOnClick(R.id.example) void onClick() {
 *   Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 * Any number of parameters from
 * {@link OnClickListener#onClick(View) onClick} may be used on the
 * method.
 *
 * @see OnClickListener
 */
@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
        targetType = "android.view.View",
        setter = "setOnClickListener",
        type = "com.andframe.annotation.listener.internal.DebouncingOnClickListener",
        method = @ListenerMethod(
                name = "doClick",
                parameters = "android.view.View"
        )
)
public @interface BindOnClick {
    /**
     * View IDs to which the method will be bound.
     */
    @IdRes int[] value() default {View.NO_ID};
	String[] idname() default {""};

    int intervalTime() default 1000;
}
