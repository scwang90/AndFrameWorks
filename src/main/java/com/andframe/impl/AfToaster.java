package com.andframe.impl;

import android.widget.Toast;

import com.andframe.api.Toaster;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;

/**
 * Toast 气泡提示
 * Created by SCWANG on 2017/3/21.
 */

public class AfToaster implements Toaster {

    //<editor-fold desc="气泡封装">
    @Override
    public void makeToastLong(CharSequence tip) {
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(CharSequence tip) {
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(int resid) {
        Toast.makeText(AfApp.get(), resid, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastLong(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(int resid) {
        Toast.makeText(AfApp.get(), resid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastShort(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_SHORT).show();
    }

    protected static Toaster instance = null;

    public static Toaster getInstance() {
        if (instance == null) {
            instance = AfApp.get().newToaster();
        }
        return instance;
    }

    //</editor-fold>

}
