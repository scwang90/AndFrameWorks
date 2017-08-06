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
    int[] value() default {-1};
	String[] idname() default {""};
}
