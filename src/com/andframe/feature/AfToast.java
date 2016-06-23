package com.andframe.feature;

import android.content.Context;
import android.widget.Toast;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;

/**
 * Created by Administrator on 2015/8/17.
 */
public class AfToast {

    private static Context getContext() {
        return AfApplication.getAppContext();
    }

    /**
     * 以下是静态方法
     */
    public static void makeToastLong(String tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    public static void makeToastShort(String tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
    }

    public static void makeToastLong(int resid) {
        Toast.makeText(getContext(), resid, Toast.LENGTH_LONG).show();
    }

    public static void makeToastShort(int resid) {
        Toast.makeText(getContext(), resid, Toast.LENGTH_SHORT).show();
    }

    public static void makeToastLong(String tip,Throwable e) {
        tip = AfExceptionHandler.tip(e, tip);
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    public static void makeToastDuration(String tip,int duration) {
        Toast.makeText(getContext(), tip, duration).show();
    }

    public static void makeToastDuration(int resid,int duration) {
        Toast.makeText(getContext(), resid, duration).show();
    }
}
