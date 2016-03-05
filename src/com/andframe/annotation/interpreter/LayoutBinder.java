package com.andframe.annotation.interpreter;

import android.app.Activity;
import android.app.Dialog;

import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfExceptionHandler;

/**
 * 布局绑定器
 * @author 树朾
 */
public class LayoutBinder {

    protected static String TAG(Object obj,String tag) {
        if (obj == null) {
            return "LayoutBinder." + tag;
        }
        return "LayoutBinder(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doBind(Activity activity) {
        try{
            Class<? extends Activity> clazz = activity.getClass();
            if (clazz.isAnnotationPresent(BindLayout.class)) {
                BindLayout annotation = clazz.getAnnotation(BindLayout.class);
                activity.setContentView(annotation.value());
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handler(ex,TAG(activity,"doBind(activity)"));
        }
    }

    public static void doBind(Dialog dialog) {
        try{
            Class<? extends Dialog> clazz = dialog.getClass();
            if (clazz.isAnnotationPresent(BindLayout.class)) {
                BindLayout annotation = clazz.getAnnotation(BindLayout.class);
                dialog.setContentView(annotation.value());
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handler(ex,TAG(dialog,"doBind(dialog)"));
        }
    }
}
