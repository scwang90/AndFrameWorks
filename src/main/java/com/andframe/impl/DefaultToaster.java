package com.andframe.impl;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.andframe.$;
import com.andframe.api.ToastBuilder;
import com.andframe.api.Toaster;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;

/**
 * Toast 气泡提示
 * Created by SCWANG on 2017/3/21.
 */

public class DefaultToaster implements Toaster {

    private Viewer viewer;
    private CharSequence msg;
    private int duration = Toast.LENGTH_SHORT;
    private int msgId;

    public DefaultToaster() {
    }

    public DefaultToaster(Viewer viewer) {
        this.viewer = viewer;
    }

    protected Context getContext() {
        return this.viewer == null ? AfApp.get() : (viewer.getContext() == null ? AfApp.get() : viewer.getContext());
    }

    //<editor-fold desc="气泡封装">

    @Override
    public void toast(int resId) {
        shorter().msg(resId).show();
    }

    @Override
    public void toast(CharSequence msg) {
        shorter().msg(msg).show();
    }

    @Override
    public void error(CharSequence remark, Throwable e) {
        shorter().msg(AfExceptionHandler.tip(e, remark.toString())).show();
    }

    @Override
    public void show() {
        try {
            if (msg != null) {
                Toast.makeText(getContext(), msg, duration).show();
            } else if (msgId != 0){
                Toast.makeText(getContext(), msgId, duration).show();
            }
        } catch (Throwable e) {
            $.error().handle(e, "AfToaster.show");
        }
    }

    @Override
    public ToastBuilder longer() {
        this.duration = Toast.LENGTH_LONG;
        return this;
    }

    @Override
    public ToastBuilder shorter() {
        this.duration = Toast.LENGTH_SHORT;
        return this;
    }

    @Override
    public ToastBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public ToastBuilder msg(CharSequence msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public ToastBuilder msg(int resId) {
        this.msgId = resId;
        return this;
    }
    //</editor-fold>

}
