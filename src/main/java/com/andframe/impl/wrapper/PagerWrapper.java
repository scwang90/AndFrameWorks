package com.andframe.impl.wrapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.api.pager.Pager;
import com.andframe.api.view.Viewer;
import com.andframe.task.AfData2Task;
import com.andframe.task.AfData3Task;
import com.andframe.task.AfDataTask;
import com.andframe.task.AfTask;

/**
 * PagerWrapper 实现 pager 和 viewer 的分离
 * Created by SCWANG on 2016/12/22.
 */
@SuppressWarnings("unused")
public class PagerWrapper implements Pager {

    protected Pager pager;
    protected Viewer viewer;

    public PagerWrapper(Pager pager) {
        this.pager = pager;
        this.viewer = pager;
    }

    public PagerWrapper(Pager pager, Viewer viewer) {
        this.pager = pager;
        this.viewer = viewer;
    }

    @Override
    public void finish() {
        pager.finish();
    }

    @Override
    public void setResultOk(Object... args) {
        pager.setResultOk(args);
    }

    @Override
    public Context getContext() {
        return viewer.getContext();
    }

    @Override
    public View getView() {
        return viewer.getView();
    }

    @Override
    public View findViewById(int id) {
        return viewer.findViewById(id);
    }

    @Override
    public <T extends View> T findViewByID(int id) {
        return viewer.findViewByID(id);
    }

    @Override
    public <T extends View> T findViewById(int id, Class<T> clazz) {
        return viewer.findViewById(id, clazz);
    }

    @Override
    public boolean isRecycled() {
        return pager.isRecycled();
    }

    @Override
    public boolean isFinishing() {
        return pager.isFinishing();
    }

    @Override
    public boolean isShowing() {
        return pager.isShowing();
    }

    @Override
    public Activity getActivity() {
        return pager.getActivity();
    }

    @Override
    public void startActivity(Intent intent) {
        pager.startActivity(intent);
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        pager.startFragment(clazz, args);
    }

    @Override
    public void startActivity(Class<? extends Activity> clazz, Object... args) {
        pager.startActivity(clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        pager.startFragmentForResult(clazz, request, args);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
        pager.startActivityForResult(clazz, request, args);
    }

    @Override
    public void makeToastLong(int resid) {
        pager.makeToastLong(resid);
    }

    @Override
    public void makeToastShort(int resid) {
        pager.makeToastShort(resid);
    }

    @Override
    public void makeToastLong(CharSequence tip) {
        pager.makeToastLong(tip);
    }

    @Override
    public void makeToastShort(CharSequence tip) {
        pager.makeToastShort(tip);
    }

    @Override
    public void makeToastShort(CharSequence tip, Throwable e) {
        pager.makeToastShort(tip, e);
    }

    @Override
    public <T extends AfTask> T postTask(T task) {
        return pager.postTask(task);
    }

    @Override
    public <T> AfDataTask postDataTask(T t, AfDataTask.OnTaskHandlerListener<T> task) {
        return pager.postDataTask(t, task);
    }

    @Override
    public <T, TT> AfData2Task postDataTask(T t, TT tt, AfData2Task.OnData2TaskHandlerListener<T, TT> task) {
        return pager.postDataTask(t, tt, task);
    }

    @Override
    public <T, TT, TTT> AfData3Task postDataTask(T t, TT tt, TTT ttt, AfData3Task.OnData3TaskHandlerListener<T, TT, TTT> task) {
        return pager.postDataTask(t, tt, ttt, task);
    }

    @Override
    public Dialog showProgressDialog(CharSequence message) {
        return pager.showProgressDialog(message);
    }

    @Override
    public void setProgressDialogText(CharSequence text) {
        pager.setProgressDialogText(text);
    }

    @Override
    public void hideProgressDialog() {
        pager.hideProgressDialog();
    }

    @Override
    public boolean isProgressDialogShowing() {
        return pager.isProgressDialogShowing();
    }
}
