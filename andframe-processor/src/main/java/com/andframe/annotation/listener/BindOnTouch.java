package com.andframe.annotation.listener;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
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
    int[] value() default {-1};
	String[] idname() default {""};
}
