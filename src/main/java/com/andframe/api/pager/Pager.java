package com.andframe.api.pager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.andframe.api.Constanter;
import com.andframe.api.Toaster;
import com.andframe.api.task.Task;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.Viewer;

/**
 * 框架页面接口 Pager
 * Created by SCWANG on 2016/9/1.
 */
@SuppressWarnings("unused")
public interface Pager extends Viewer, Toaster, Constanter {

    boolean isRecycled();
    boolean isFinishing();
    boolean isShowing();//!isRecycled() && !isFinishing()

    Activity getActivity();

    void finish();

    void setResultOk(Object... args);

    //<editor-fold desc="页面切换">

    void startActivity(Intent intent);

    void startFragment(Class<? extends Fragment> clazz, Object... args);

    void startActivity(Class<? extends Activity> clazz, Object... args);

    void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args);

    void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args);

    boolean startPager(Class clazz, Object... args);

    //</editor-fold>

    //<editor-fold desc="异步任务">
    <T extends Task> T postTask(T task);
    //</editor-fold>

    //<editor-fold desc="进度显示对话框">
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    Dialog showProgressDialog(CharSequence message);

    /**
     * 动态改变等待对话框的文字
     *
     * @param text   更新的文字
     */
    void setProgressDialogText(CharSequence text);

    /**
     * 隐藏 进度对话框
     */
    void hideProgressDialog();

    /**
     * 是否正在显示进度对话框
     */
    boolean isProgressDialogShowing();

    /**
     * 设置 viewQuery
     * @param viewQuery new viewQuery
     */
    void setViewQuery(@NonNull ViewQuery<? extends ViewQuery> viewQuery);

    /**
     * 获取 ViewQuery
     * @return ViewQuery
     */
    ViewQuery<? extends ViewQuery> getViewQuery();
    //</editor-fold>

}