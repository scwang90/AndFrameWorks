package com.andframe.annotation.listener;

import android.support.annotation.IdRes;
import android.view.View;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static android.view.View.OnTouchListener;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@link OnTouchListener OnTouchListener} on the view for each ID specified.
 * <pre><code>
 * {@literal @}BindOnTouch(R.id.example) boolean onTouch() {
 *   Toast.makeText(this, "Touched!", Toast.LENGTH_SHORT).show();
 *   return false;
 * }
 * </code></pre>
 * Any number of parameters from
 * {@link OnTouchListener#onTouch(View, android.view.MotionEvent) onTouch} may be used
 * on the method.
 *
 * @see OnTouchListener
 */
@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
        targetType = "android.view.View",
        setter = "setOnTouchListener",
        type = "android.view.View.OnTouchListener",
        method = @ListenerMethod(
                name = "onTouch",
                parameters = {
                        "android.view.View",
                        "android.view.MotionEvent"
                },
                returnType = "boolean",
                defaultReturn = "false"
        )
)
public @interface BindOnTouch {
    /**
     * View IDs to which the method will be bound.
     */
    @IdRes int[] value() default {View.NO_ID};
	String[] idname() default {""};
}
