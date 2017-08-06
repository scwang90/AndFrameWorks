package com.andframe.annotation.listener;

import android.support.annotation.IdRes;
import android.text.TextWatcher;
import android.view.View;

import com.andframe.annotation.listener.internal.ListenerClass;
import com.andframe.annotation.listener.internal.ListenerMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@link TextWatcher TextWatcher} on the view for each ID specified.
 * <pre><code>
 * {@literal @}BindOnTextChanged(R.id.example) void onTextChanged(CharSequence text) {
 *   Toast.makeText(this, "Text changed: " + text, Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 * Any number of parameters from {@link TextWatcher#onTextChanged(CharSequence, int, int, int)
 * onTextChanged} may be used on the method.
 * <p>
 * To bind to methods other than {@code onTextChanged}, specify a different {@code callback}.
 * <pre><code>
 * {@literal @}BindOnTextChanged(value = R.id.example, callback = BEFORE_TEXT_CHANGED)
 * void onBeforeTextChanged(CharSequence text) {
 *   Toast.makeText(this, "Before text changed: " + text, Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 *
 * @see TextWatcher
 */
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
    @IdRes int[] value() default {View.NO_ID};
	String[] idname() default {""};

    /**
     * Listener callback to which the method will be bound.
     */
    Callback callback() default Callback.TEXT_CHANGED;

    /**
     * {@link TextWatcher} callback methods.
     */
    enum Callback {
        /**
         * {@link TextWatcher#onTextChanged(CharSequence, int, int, int)}
         */
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

        /**
         * {@link TextWatcher#beforeTextChanged(CharSequence, int, int, int)}
         */
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

        /**
         * {@link TextWatcher#afterTextChanged(android.text.Editable)}
         */
        @ListenerMethod(
                name = "afterTextChanged",
                parameters = "android.text.Editable"
        )
        AFTER_TEXT_CHANGED,
    }
}
