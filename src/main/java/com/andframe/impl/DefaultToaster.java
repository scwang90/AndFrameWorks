package com.andframe.impl;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
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

public class DefaultToaster implements Toaster, ToastBuilder {

    private Viewer viewer;
    private CharSequence msg;
    private int duration = Toast.LENGTH_SHORT;
    private int msgId = View.NO_ID;
    private boolean shown = false;

    public DefaultToaster() {
        $.dispatch(()->{
            if (!shown && (!TextUtils.isEmpty(msg)||msgId!=View.NO_ID)) {
                show();
            } else {
                shown = false;
            }
        });
    }

    public DefaultToaster(Viewer viewer) {
        this.viewer = viewer;
        $.dispatch(()->{
            if (!shown && (!TextUtils.isEmpty(msg)||msgId!=View.NO_ID)) {
                show();
            } else {
                shown = false;
            }
        });
    }

    protected Context getContext() {
        return this.viewer == null ? AfApp.get() : (viewer.getContext() == null ? AfApp.get() : viewer.getContext());
    }

    @Override
    public ToastBuilder builder() {
        if (shown) {
            return new DefaultToaster(this.viewer);
        }
        return this;
    }

    //<editor-fold desc="气泡封装">

    @Override
    public void toast(int resId) {
        builder().shorter().msg(resId).show();
    }

    @Override
    public void toast(CharSequence msg) {
        builder().shorter().msg(msg).show();
    }

    @Override
    public void error(CharSequence remark, Throwable e) {
        builder().shorter().msg(AfExceptionHandler.tip(e, remark.toString())).show();
    }

    @Override
    public void show() {
        try {
            this.shown = true;
            if (msg != null) {
                Toast.makeText(getContext(), msg, duration).show();
            } else if (msgId != View.NO_ID){
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
