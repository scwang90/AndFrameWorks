package com.andframe;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;

import com.andframe.util.AfReflecter;

/**
 * 框架入口
 * Created by SCWANG on 2017/8/6.
 */

public class AndFrame {
    public static void init(Application app) {
        // 整个框架的核心
        // 反射获取mMainThread
        // getBaseContext()返回的是ContextImpl对象 ContextImpl中包含ActivityThread mMainThread这个对象
        AfReflecter.setMemberNoException(app.getBaseContext(), "mMainThread.mInstrumentation", new InstrumentationIoc());
    }

    private static class InstrumentationIoc extends Instrumentation {
        @Override
        public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
            try {
                return (Activity)cl.loadClass(className+"$").newInstance();
            } catch (ClassNotFoundException e) {
                return super.newActivity(cl, className, intent);
            }
        }
    }
}
