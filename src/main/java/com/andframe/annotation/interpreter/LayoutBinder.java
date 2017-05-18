package com.andframe.annotation.interpreter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.andframe.annotation.pager.BindLayout;
import com.andframe.annotation.pager.idname.BindLayout$;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;

import java.util.HashMap;
import java.util.Map;

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
            int layoutId = getBindLayoutId(activity, activity);
            if (layoutId > 0) {
                activity.setContentView(layoutId);
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, TAG(activity, "doBind(activity)"));
        }
    }

    public static void doBind(Dialog dialog) {
        try{
            int layoutId = getBindLayoutId(dialog, dialog.getContext());
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
        return getBindLayoutId(handler.getClass(), context);
    }

    public static int getBindLayoutId(Class<?> clazz, Context context) {
        Integer integer = idCache.get(clazz);
        if (integer == null) {
            idCache.put(clazz, integer = reflectLayoutId(clazz, context));
        }
        return integer;
    }

    private static int reflectLayoutId(Class<?> clazz, Context context) {
        Class<?> stop = ReflecterCacher.getStopType(clazz);
        BindLayout layout = AfReflecter.getAnnotation(clazz, stop, BindLayout.class);
        if (layout != null) {
            return layout.value();
        } else {
            BindLayout$ layout$ = AfReflecter.getAnnotation(clazz, stop, BindLayout$.class);
            if (layout$ != null) {
                int id = context.getResources().getIdentifier(layout$.value(), "layout", context.getPackageName());
                if (id <= 0) {
                    id = context.getResources().getIdentifier(layout$.value(), "id", context.getPackageName());
                }
                return id;
            }
        }
        return 0;
    }

    //<editor-fold desc="反射缓存">
    private static Map<Class, Integer> idCache = new HashMap<>();
    //</editor-fold>
}
