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
    void setProgressLayoutId(int progressLayoutId);
    void setEmptyLayoutId(int emptyLayoutId);
    void setErrorLayoutId(int errorLayoutId);
    void setInvalidnetLayoutId(int invalidnetLayoutId);
    void autoCompletedLayout();
    void showEmpty();
    void showEmpty(String empty);
    void showContent();
    void showProgress();
    void showProgress(String message);
    void showError(String error);

    boolean isProgress();
    boolean isContent();
}
