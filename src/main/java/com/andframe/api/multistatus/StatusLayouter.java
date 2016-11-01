package com.andframe.api.multistatus;

import android.view.View;
import android.view.ViewGroup;

/**
 * 可切换状态页面的布局
 * Created by SCWANG on 2016/10/20.
 */

public interface StatusLayouter {
    ViewGroup getLayout();
    void setContenView(View content);
    void setOnRefreshListener(OnRefreshListener listener);
    void setEmptyLayout(int layoutId);
    void setEmptyLayout(int layoutId, int msgId);
    void setErrorLayout(int layoutId, int msgId);
    void setEmptyLayout(int layoutId, int msgId, int btnId);
    void setErrorLayout(int layoutId, int msgId, int btnId);
    void setEmptyLayout(int layoutId, int msgId, int btnId, String message);
    void setProgressLayout(int layoutId);
    void setProgressLayout(int layoutId, int msgId);
    void setInvalidnetLayout(int layoutId);
    void setInvalidnetLayout(int layoutId, int msgId);
    void setInvalidnetLayout(int layoutId, int msgId, int btnId);
    void autoCompletedLayout();
    void showEmpty();
    void showEmpty(String empty);
    void showProgress();
    void showProgress(String message);
    void showInvalidnet();
    void showInvalidnet(String message);
    void showError(String error);
    void showContent();

    boolean isProgress();
    boolean isContent();
}
