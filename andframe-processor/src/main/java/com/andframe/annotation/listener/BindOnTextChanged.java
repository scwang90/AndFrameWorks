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
        targetType = "android.widget.TextView",
        setter = "addTextChangedListener",
        remover = "removeTextChangedListener",
        type = "android.text.TextWatcher",
        callbacks = BindOnTextChanged.Callback.class
)
public @interface BindOnTextChanged {
    /**
     * View IDs to which the method will be bound.
     */
    int[] value() default {-1};
	String[] idname() default {""};

    /**
     * Listener callback to which the method will be bound.
     */
    Callback callback() default Callback.TEXT_CHANGED;

    enum Callback {
        @ListenerMethod(
                name = "onTextChanged",
                parameters = {
                        "java.lang.CharSequence",
                        "int",
                        "int",
                        "int"
                }
        )
        TEXT_CHANGED,

        @ListenerMethod(
                name = "beforeTextChanged",
                parameters = {
                        "java.lang.CharSequence",
                        "int",
                        "int",
                        "int"
                }
        )
        BEFORE_TEXT_CHANGED,
        @ListenerMethod(
                name = "afterTextChanged",
                parameters = "android.text.Editable"
        )
        AFTER_TEXT_CHANGED,
    }
}
