package com.andframe.impl;

import android.content.Context;
import android.widget.Toast;

import com.andframe.api.Toaster;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;

/**
 * Toast 气泡提示
 * Created by SCWANG on 2017/3/21.
 */

public class AfToaster implements Toaster {

    private Viewer viewer;

    public AfToaster() {
    }

    public AfToaster(Viewer viewer) {
        this.viewer = viewer;
    }

    protected Context getContext() {
        return this.viewer == null ? AfApp.get() : (viewer.getContext() == null ? AfApp.get() : viewer.getContext());
    }

    //<editor-fold desc="气泡封装">
    @Override
    public void makeToastLong(CharSequence tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }
    @Override
    public void makeToastShort(CharSequence tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastLong(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastShort(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
    }
    //</editor-fold>

}
