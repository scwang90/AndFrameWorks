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
        targetType = "android.widget.CompoundButton",
        setter = "setOnCheckedChangeListener",
        type = "android.widget.CompoundButton.OnCheckedChangeListener",
        method = @ListenerMethod(
                name = "onCheckedChanged",
                parameters = {
                        "android.widget.CompoundButton",
                        "boolean"
                }
        )
)
public @interface BindOnCheckedChanged {
    /**
     * View IDs to which the method will be bound.
     */
    int[] value() default {-1};
    String[] idname() default {""};
}
