package com.andframe.annotation.interpreter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.annotation.pager.BindSoftInputMode;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.model.constants.SoftInputAdjust;
import com.andframe.model.constants.SoftInputMode;
import com.andframe.model.constants.SoftInputState;
import com.andframe.util.java.AfReflecter;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面属性绑定器
 * @author 树朾
 */
public class PageBinder {

    protected static String TAG(Object obj,String tag) {
        if (obj == null) {
            return "PageBinder." + tag;
        }
        return "PageBinder(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doBind(@NonNull AfActivity activity) {
        try{
            Class<? extends Activity> clazz = activity.getClass();
            BindSoftInputMode mode = cache.get(clazz);
            if (!cache.containsKey(clazz)) {
                mode = AfReflecter.getAnnotation(clazz, AfActivity.class, BindSoftInputMode.class);
                cache.put(clazz, mode);
            }
            if (mode != null) {
                if (mode.state() != SoftInputState.None || mode.adjust() != SoftInputAdjust.None) {
                    activity.getWindow().setSoftInputMode(mode.state().value | mode.adjust().value);
                } else if (mode.value() != SoftInputMode.None) {
                    activity.getWindow().setSoftInputMode(mode.value().value);
                }
            }
        } catch (Throwable ex) {
            $.error().handle(ex, TAG(activity, "doBind(activity)"));
        }
    }



    //<editor-fold desc="反射缓存">
    private static Map<Class, BindSoftInputMode> cache = new HashMap<>();
    //</editor-fold>
}
