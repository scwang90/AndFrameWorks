package com.andframe.annotation.interpreter;

import android.app.Activity;

import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfExceptionHandler;

/**
 * 布局绑定器
 * @author 树朾
 */
public class LayoutBinder {

    private Object mHandler;

    public LayoutBinder(Object handler) {
        mHandler = handler;
    }

    protected String TAG(String tag) {
        return "LayoutBinder(" + mHandler.getClass().getName() + ")." + tag;
    }

    public void doBind() {
        if (mHandler instanceof Activity) {
            this.doBind((Activity) (mHandler));
        }
    }

    private void doBind(Activity activity) {
        try{
            Class<? extends Activity> clazz = activity.getClass();
            if (clazz.isAnnotationPresent(BindLayout.class)) {
                BindLayout annotation = clazz.getAnnotation(BindLayout.class);
                activity.setContentView(annotation.value());
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handler(ex,TAG("doBind(activity)"));
        }
    }
}
