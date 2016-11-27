package com.andframe.task;

import android.support.annotation.NonNull;

import com.andframe.R;
import com.andframe.api.task.Tasker;
import com.andframe.api.page.Pager;

import java.lang.ref.WeakReference;

/**
 * 自带反馈的任务
 * Created by SCWANG on 2016/11/11.
 */

public class AfFeedbackTask extends AfHandlerTask {

    protected Tasker working;
    protected Runnable success;
    protected CharSequence intent;
    protected WeakReference<Pager> mPager;
    protected boolean mFeedbackOnSuccess = true;

    public AfFeedbackTask(@NonNull CharSequence intent, @NonNull Pager pager) {
        this(intent, pager, null);
    }

    public AfFeedbackTask(@NonNull CharSequence intent, @NonNull Pager pager, Runnable success) {
        this.intent = intent;
        this.mPager = new WeakReference<>(pager);
        this.success = success;
    }

    public AfFeedbackTask working(Tasker working) {
        this.working = working;
        return this;
    }

    public AfFeedbackTask success(Runnable success) {
        return success(true, success);
    }

    public AfFeedbackTask success(boolean feedback, Runnable success) {
        this.success = success;
        this.mFeedbackOnSuccess = feedback;
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
                success.run();
            }
        } else {
            makeToastFail();
        }
    }

    @Override
    protected void onWorking() throws Exception {
        for (int i = 0; i < 20 && working == null; i++) {
            Thread.sleep(100);
        }
        if (working != null) {
            working.onWorking();
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
        if (pager != null && mFeedbackOnSuccess) {
            pager.makeToastShort(String.format(pager.getContext().getString(R.string.task_format_success),intent));
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
