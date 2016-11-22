package com.andframe.task;

import android.support.annotation.NonNull;

import com.andframe.R;
import com.andframe.api.LoadSuccessHandler;
import com.andframe.api.LoadTasker;
import com.andframe.api.page.Pager;

import java.lang.ref.WeakReference;

/**
 * 自带反馈的任务
 * Created by SCWANG on 2016/11/11.
 */

public class AfFeedbackLoadingTask<T> extends AfHandlerTask {

    protected T data;
    protected CharSequence intent;
    protected LoadTasker<T> tasker;
    protected LoadSuccessHandler<T> success;
    protected WeakReference<Pager> mPager;

    public AfFeedbackLoadingTask(@NonNull CharSequence intent, @NonNull Pager pager) {
        this(intent, pager, null);
    }

    public AfFeedbackLoadingTask(@NonNull CharSequence intent, @NonNull Pager pager, LoadSuccessHandler<T> success) {
        this.intent = intent;
        this.mPager = new WeakReference<>(pager);
        this.success = success;
    }

    public AfFeedbackLoadingTask<T> loading(LoadTasker<T> working) {
        this.tasker = working;
        return this;
    }

    public AfFeedbackLoadingTask<T> success(LoadSuccessHandler<T> success) {
        this.success = success;
        return this;
    }

    @Override
    protected boolean onPrepare() {
        showProgressDialog();
        return super.onPrepare();
    }

    @Override
    protected void onHandle() {
        hideProgressDialog();
        if (isFinish()) {
            makeToastSucccess();
            if (success != null) {
                if (data != null) {
                    success.onSuccess(data);
                } else {
                    makeToastNull();
                }
            }
        } else {
            makeToastFail();
        }
    }

    @Override
    protected void onWorking() throws Exception {
        for (int i = 0; i < 20 && tasker == null; i++) {
            Thread.sleep(100);
        }
        if (tasker != null) {
            data = tasker.onLoading();
        }
    }

    private void showProgressDialog() {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.showProgressDialog(String.format(pager.getContext().getString(R.string.task_format_loading),intent));
        }
    }

    private void makeToastSucccess() {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.makeToastShort(String.format(pager.getContext().getString(R.string.task_format_success),intent));
        }
    }

    private void makeToastNull() {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.makeToastShort(String.format(pager.getContext().getString(R.string.task_format_nulldata),intent));
        }

    }

    private void makeToastFail() {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.makeToastShort(String.format(pager.getContext().getString(R.string.task_format_fail),intent));
        }
    }


    private void hideProgressDialog() {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.hideProgressDialog();
        }
    }

}
