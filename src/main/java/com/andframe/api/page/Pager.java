package com.andframe.api.page;

import android.app.Activity;
import android.app.Dialog;

import com.andframe.api.Constanter;
import com.andframe.api.view.Viewer;
import com.andframe.task.AfData2Task;
import com.andframe.task.AfData3Task;
import com.andframe.task.AfDataTask;
import com.andframe.task.AfTask;

/**
 * 框架页面接口 Pager
 * Created by SCWANG on 2016/9/1.
 */
@SuppressWarnings("unused")
public interface Pager extends Viewer, Constanter {
    boolean isRecycled();

    Activity getActivity();

    //<editor-fold desc="页面切换">
    void startActivity(Class<? extends Activity> clazz);

    void startActivity(Class<? extends Activity> clazz, Object... args);

    void startActivityForResult(Class<? extends Activity> clazz, int request);

    void startActivityForResult(Class<? extends Activity> clazz,
                                int request, Object... args);
    //</editor-fold>

    //<editor-fold desc="气泡提示">
    void makeToastLong(int resid);

    void makeToastShort(int resid);

    void makeToastLong(CharSequence tip);

    void makeToastShort(CharSequence tip);

    void makeToastLong(CharSequence tip, Throwable e);

    //</editor-fold>

    //<editor-fold desc="异步任务">
    <T extends AfTask> T postTask(T task);

    <T> AfDataTask postDataTask(T t, AfDataTask.OnTaskHandlerListener<T> task);

    <T, TT> AfData2Task postDataTask(T t, TT tt, AfData2Task.OnData2TaskHandlerListener<T, TT> task);

    <T, TT, TTT> AfData3Task postDataTask(T t, TT tt, TTT ttt, AfData3Task.OnData3TaskHandlerListener<T, TT, TTT> task);
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
    //</editor-fold>

}