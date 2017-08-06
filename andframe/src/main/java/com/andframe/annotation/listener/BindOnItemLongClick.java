package com.andframe.annotation.listener;

import android.support.annotation.IdRes;
import android.view.View;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static android.widget.AdapterView.OnItemLongClickListener;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@link OnItemLongClickListener OnItemLongClickListener} on the view for each
 * ID specified.
 * <pre><code>
 * {@literal @}BindOnItemLongClick(R.id.example_list) boolean onItemLongClick(int position) {
 *   Toast.makeText(this, "Long clicked position " + position + "!", Toast.LENGTH_SHORT).show();
 *   return true;
 * }
 * </code></pre>
 * Any number of parameters from
 * {@link OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, View,
 * int, long) onItemLongClick} may be used on the method.
 *
 * @see OnItemLongClickListener
 */
@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
        targetType = "android.widget.AdapterView<?>",
        setter = "setOnItemLongClickListener",
        type = "android.widget.AdapterView.OnItemLongClickListener",
        method = @ListenerMethod(
                name = "onItemLongClick",
                parameters = {
                        "android.widget.AdapterView<?>",
                        "android.view.View",
                        "int",
                        "long"
                },
                returnType = "boolean",
                defaultReturn = "false"
        )
)
public @interface BindOnItemLongClick {
    /**
     * View IDs to which the method will be bound.
     */
    @IdRes int[] value() default {View.NO_ID};
	String[] idname() default {""};
}
