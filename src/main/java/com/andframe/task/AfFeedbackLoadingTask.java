package com.andframe.task;

import android.support.annotation.NonNull;

import com.andframe.R;
import com.andframe.api.EmptyVerdicter;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.LoadEmptyHandler;
import com.andframe.api.task.LoadSuccessHandler;
import com.andframe.api.task.LoadTasker;
import com.andframe.api.task.Tasker;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * 自带反馈的任务
 * Created by SCWANG on 2016/11/11.
 */

public class AfFeedbackLoadingTask<T> extends AfFeedbackTask {

    protected T data;
    protected LoadTasker<T> tasker;
    protected LoadEmptyHandler empty = this::makeToastNull;
    protected LoadSuccessHandler<T> success;
    protected EmptyVerdicter<T> isempty = model -> model instanceof Collection ? ((Collection) model).isEmpty() : model == null;
    protected boolean mFeedbackOnEmpty = false;

    public AfFeedbackLoadingTask(@NonNull CharSequence intent, @NonNull Pager pager) {
        this(intent, pager, null);
    }

    public AfFeedbackLoadingTask(@NonNull CharSequence intent, @NonNull Pager pager, LoadSuccessHandler<T> success) {
        super(intent, pager);
        this.intent = intent;
        this.mPager = new WeakReference<>(pager);
        this.success = success;
    }

    //<editor-fold desc="Description">
    @Deprecated@Override
    public final AfFeedbackTask working(Tasker working) {
        return super.working(working);
    }

    @Deprecated@Override
    public final AfFeedbackTask success(boolean feedback, Runnable success) {
        return super.success(feedback, success);
    }

    @Deprecated@Override
    public final AfFeedbackTask success(Runnable success) {
        return super.success(success);
    }
    //</editor-fold>

    public AfFeedbackLoadingTask<T> empty(LoadEmptyHandler empty) {
        this.empty = empty;
        return this;
    }

    public AfFeedbackLoadingTask<T> empty(boolean feedback, LoadEmptyHandler empty) {
        this.mFeedbackOnEmpty = feedback;
        this.empty = empty;
        return this;
    }

    public AfFeedbackLoadingTask<T> isempty(EmptyVerdicter<T> isempty) {
        this.isempty = isempty;
        return this;
    }

    public AfFeedbackLoadingTask<T> loading(LoadTasker<T> working) {
        this.tasker = working;
        return this;
    }

    public AfFeedbackLoadingTask<T> success(LoadSuccessHandler<T> success) {
        return success(true, success);
    }

    public AfFeedbackLoadingTask<T> success(boolean feedback, LoadSuccessHandler<T> success) {
        this.success = success;
        this.mFeedbackOnSuccess = feedback;
        return this;
    }

    @Override
    protected void onHandle() {
        hideProgressDialog();
        if (isFinish()) {
            makeToastSucccess();
            if (success != null) {
                if (!isempty.isEmpty(data)) {
                    success.onSuccess(data);
                } else {
                    empty.onEmpty();
                }
            }
        } else {
            makeToastFail(mException);
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

    private void makeToastNull() {
        Pager pager = mPager.get();
        if (pager != null && mFeedbackOnEmpty) {
            pager.makeToastShort(String.format(pager.getContext().getString(R.string.task_format_nulldata),intent));
        }

    }

}
