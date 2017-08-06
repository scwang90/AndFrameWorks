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
        setter = "setOnItemClickListener",
        type = "android.widget.AdapterView.OnItemClickListener",
        method = @ListenerMethod(
                name = "onItemClick",
                parameters = {
                        "android.widget.AdapterView<?>",
                        "android.view.View",
                        "int",
                        "long"
                }
        )
)
public @interface BindOnItemClick {
    /**
     * View IDs to which the method will be bound.
     */
    int[] value() default {-1};
	String[] idname() default {""};

    int intervalTime() default 1000;
}
