package com.andframe.task;

import android.support.annotation.NonNull;

import com.andframe.api.Tasker;
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
        this.success = success;
        return this;
    }

    @Override
    protected boolean onPrepare() {
        showProgressDialog("正在" + intent + "...");
        return super.onPrepare();
    }

    @Override
    protected void onHandle() {
        hideProgressDialog();
        if (isFinish()) {
            makeToastShort(intent + "成功");
            if (success != null) {
                success.run();
            }
        } else {
            makeToastShort(makeErrorToast(intent+"失败"));
        }
    }

    @Override
    protected void onWorking() throws Exception {
        if (working != null) {
            working.onWorking();
        }
    }

    private void showProgressDialog(String message) {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.showProgressDialog(message);
        }
    }

    private void makeToastShort(String message) {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.makeToastShort(message);
        }
    }

    private void hideProgressDialog() {
        Pager pager = mPager.get();
        if (pager != null) {
            pager.hideProgressDialog();
        }
    }

}
