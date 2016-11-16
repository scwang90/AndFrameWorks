package com.andframe.annotation.interpreter;

import android.app.Activity;
import android.app.Dialog;

import com.andframe.annotation.pager.BindLayout;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;

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

    public static void doBind(Activity activity) throws Throwable {
        try{
            Class<? extends Activity> clazz = activity.getClass();
            BindLayout layout = AfReflecter.getAnnotation(clazz, Activity.class, BindLayout.class);
            if (layout != null) {
                activity.setContentView(layout.value());
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, TAG(activity, "doBind(activity)"));
            throw ex;
        }
    }

    public static void doBind(Dialog dialog) {
        try{
            Class<? extends Dialog> clazz = dialog.getClass();
            BindLayout layout = AfReflecter.getAnnotation(clazz, Dialog.class, BindLayout.class);
            if (layout != null) {
                dialog.setContentView(layout.value());
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, TAG(dialog, "doBind(dialog)"));
        }
    }
}
