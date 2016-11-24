package com.andframe.annotation.interpreter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.andframe.annotation.view.BindLayout;
import com.andframe.annotation.view.BindLayout$;
import com.andframe.application.AfExceptionHandler;
import com.andframe.layoutbind.framework.AfViewWrapper;
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

    public static void doBind(Activity activity) {
        try{
            int layoutId = getBindLayoutId(activity, activity, Activity.class);
            if (layoutId > 0) {
                activity.setContentView(layoutId);
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, TAG(activity, "doBind(activity)"));
        }
    }

    public static void doBind(Dialog dialog) {
        try{
            int layoutId = getBindLayoutId(dialog, dialog.getContext(), Dialog.class);
            if (layoutId > 0) {
                dialog.setContentView(layoutId);
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, TAG(dialog, "doBind(dialog)"));
        }
    }

    public static int getBindLayoutId(Context context) {
        return getBindLayoutId(context, context);
    }

    public static int getBindLayoutId(Object handler, Context context) {
        return getBindLayoutId(handler, context, AfViewWrapper.class);
    }

    public static int getBindLayoutId(Object handler, Context context, Class<?> stop) {
        return getBindLayoutId(handler.getClass(), context, stop);
    }

    public static int getBindLayoutId(Class<?> clazz, Context context, Class<?> stop) {
        BindLayout layout = AfReflecter.getAnnotation(clazz, stop, BindLayout.class);
        if (layout != null) {
            return layout.value();
        } else {
            BindLayout$ layout$ = AfReflecter.getAnnotation(clazz, stop, BindLayout$.class);
            if (layout$ != null) {
                return context.getResources().getIdentifier(layout$.value(), "layout", context.getPackageName());
            }
        }
        return 0;
    }
}
