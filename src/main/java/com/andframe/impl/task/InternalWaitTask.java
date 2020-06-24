package com.andframe.impl.task;

import com.andframe.R;
import com.andframe.api.pager.Pager;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;

/**
 * 带等待对话框的任务
 * Created by SCWANG on 2017/5/3.
 */
@SuppressWarnings("WeakerAccess")
public class InternalWaitTask extends InternalTask {

    protected WaitTaskBuilder builder;

    public InternalWaitTask(WaitTaskBuilder builder) {
        super(builder);
        this.builder = builder;
    }

    @Override
    protected boolean onPrepare() {
        showProgressDialog();
        return super.onPrepare();
    }

    @Override
    protected void onHandle() {
        hideProgressDialog();
        if (success()) {
            makeToastSuccess();
        } else if (mException != null) {
            makeToastFail(mException);
        }
        super.onHandle();
    }

    //<editor-fold desc="对话框实现">
    protected void showProgressDialog() {
        Pager pager = builder.pager.get();
        if (pager != null) {
            pager.showProgressDialog(String.format(AfApp.get().getString(R.string.task_format_loading),builder.intent));
        }
    }

    protected void makeToastSuccess() {
        Pager pager = builder.pager.get();
        if (pager != null && builder.feedbackOnSuccess) {
            pager.toast(String.format(AfApp.get().getString(R.string.task_format_success),builder.intent));
        }
    }

    protected void makeToastFail(Throwable e) {
        Pager pager = builder.pager.get();
        if (pager != null && builder.feedbackOnException) {
            pager.toast(AfExceptionHandler.tip(e, String.format(AfApp.get().getString(R.string.task_format_fail), builder.intent)));
        }
    }

    protected void hideProgressDialog() {
        Pager pager = builder.pager.get();
        if (pager != null) {
            pager.hideProgressDialog();
        }
    }
    //</editor-fold>

}
