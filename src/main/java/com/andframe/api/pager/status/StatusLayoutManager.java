package com.andframe.api.pager.status;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.ViewGroup;

/**
 * 可切换状态页面的布局
 * Created by SCWANG on 2016/10/20.
 */

@SuppressWarnings("unused")
public interface StatusLayoutManager<T extends ViewGroup> extends LayoutManager<T> {

    @LayoutRes
    int defaultLayoutEmptyId();
    @IdRes
    int defaultLayoutEmptyTextId();
    @IdRes
    int defaultLayoutEmptyButtonId();
    @StringRes
    int defaultLayoutEmptyMessageId();

    @LayoutRes
    int defaultLayoutErrorId();
    @IdRes
    int defaultLayoutErrorButtonId();
    @IdRes
    int defaultLayoutErrorTextId();

    @LayoutRes
    int defaultLayoutInvalidNetId();
    @IdRes
    int defaultLayoutInvalidNetButtonId();
    @IdRes
    int defaultLayoutInvalidNetTextId();

    @LayoutRes
    int defaultLayoutProgressId();
    @IdRes
    int defaultLayoutProgressTextId();

    enum Status{
        other,progress,content,empty,error,invalidNet
    }

    void setOnRefreshListener(OnRefreshListener listener);
    void setEmptyLayout(int layoutId);
    void setEmptyLayout(int layoutId, int msgId);
    void setErrorLayout(int layoutId, int msgId);
    void setEmptyLayout(int layoutId, int msgId, int btnId);
    void setErrorLayout(int layoutId, int msgId, int btnId);
    void setEmptyLayout(int layoutId, int msgId, int btnId, String message);
    void setProgressLayout(int layoutId);
    void setProgressLayout(int layoutId, int msgId);
    void setInvalidNetLayout(int layoutId);
    void setInvalidNetLayout(int layoutId, int msgId);
    void setInvalidNetLayout(int layoutId, int msgId, int btnId);
    void autoCompletedLayout();
    void showEmpty();
    void showEmpty(String empty);
    void showProgress();
    void showProgress(String message);
    void showInvalidNet();
    void showInvalidNet(String message);
    void showError(String error);
    void showContent();

    boolean isProgress();
    boolean isContent();
    boolean isEmpty();
}
