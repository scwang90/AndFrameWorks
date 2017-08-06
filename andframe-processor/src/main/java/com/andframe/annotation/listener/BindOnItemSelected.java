package com.andframe.annotation.listener;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
        targetType = "android.widget.AdapterView<?>",
        setter = "setOnItemSelectedListener",
        type = "android.widget.AdapterView.OnItemSelectedListener",
        callbacks = BindOnItemSelected.Callback.class
)
public @interface BindOnItemSelected {
    /**
     * View IDs to which the method will be bound.
     */
    int[] value() default {-1};
	String[] idname() default {""};

    /**
     * Listener callback to which the method will be bound.
     */
    Callback callback() default Callback.ITEM_SELECTED;

    enum Callback {
        @ListenerMethod(
                name = "onItemSelected",
                parameters = {
                        "android.widget.AdapterView<?>",
                        "android.view.View",
                        "int",
                        "long"
                }
        )
        ITEM_SELECTED,

        @ListenerMethod(
                name = "onNothingSelected",
                parameters = "android.widget.AdapterView<?>"
        )
        NOTHING_SELECTED
    }
}
