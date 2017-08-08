package com.andframe;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.andframe.util.AfReflecter;

/**
 * 框架入口
 * Created by SCWANG on 2017/8/6.
 */

public class AndFrame {
    public static void init(Application app) {
        XmlFactory.attach(app);
        AfReflecter.setMemberNoException(app.getBaseContext(), "mMainThread.mInstrumentation", new InstrumentationIoc());
    }

    private static class InstrumentationIoc extends Instrumentation {
        @Override
        public void callActivityOnCreate(Activity activity, Bundle icicle) {
            XmlFactory.attach(activity);
            super.callActivityOnCreate(activity, icicle);
        }

        @Override
        public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
            try {
                return (Activity)cl.loadClass(className+"$").newInstance();
            } catch (ClassNotFoundException e) {
                return super.newActivity(cl, className, intent);
            }
        }
    }

    private static class XmlFactory implements LayoutInflater.Factory {
        XmlFactory() {
        }
        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return null;
        }

        static void attach(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (inflater != null) {
                inflater.setFactory(new XmlFactory());
            }
        }
    }
}
