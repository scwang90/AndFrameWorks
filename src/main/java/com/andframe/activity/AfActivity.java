package com.andframe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.LifeCycleInjecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.api.DialogBuilder;
import com.andframe.api.page.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.helper.AfViewQueryHelper;
import com.andframe.task.AfData2Task;
import com.andframe.task.AfData3Task;
import com.andframe.task.AfDataTask;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfTask;
import com.andframe.task.AfTaskExecutor;
import com.andframe.util.java.AfStackTrace;

import java.util.ArrayList;
import java.util.List;

/**
 * 框架 Activity
 * Created by SCWANG on 2016/9/1.
 */
public class AfActivity extends AppCompatActivity implements Pager, ViewQueryHelper {

    protected View mRootView = null;

    protected boolean mIsRecycled = false;
    protected boolean mIsResume = true;

    /**
     * 获取LOG日志 TAG 是 AfActivity 的方法
     * 用户也可以重写自定义TAG,这个值AfActivity在日志记录时候会使用
     * 子类实现也可以使用
     */
    protected String TAG() {
        return "AfActivity(" + getClass().getName() + ")";
    }

    protected String TAG(String tag) {
        return "AfActivity(" + getClass().getName() + ")." + tag;
    }

    //<editor-fold desc="ViewQuery 集成">
    ViewQueryHelper mViewQueryHelper = new AfViewQueryHelper(this);

    /**
     * 开始 ViewQuery 查询
     *
     * @param id 控件Id
     */
    @Override
    public ViewQuery $(int id, int... ids) {
        return mViewQueryHelper.$(id, ids);
    }

    /**
     * 开始 ViewQuery 查询
     *
     * @param views 可选的多个 View
     */
    @Override
    public ViewQuery $(View... views) {
        return mViewQueryHelper.$(views);
    }
    //</editor-fold>

    /**
     * 判断是否被回收
     *
     * @return true 已经被回收
     */
    @Override
    public boolean isRecycled() {
        return mIsRecycled;
    }

    //<editor-fold desc="重写布局">

    /**
     * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
     * 子类在对 setContentView 重写的时候请调用
     * super.setContentView(res);
     * 否则不能对软键盘进行监听
     */
    @Override
    public void setContentView(int res) {
        setContentView(LayoutInflater.from(this).inflate(res, null));
    }

    /**
     * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
     * 子类在对 setContentView 重写的时候请调用
     * super.setContentView(view);
     * 否则不能对软键盘进行监听
     */
    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(LP_MP, LP_MP));
    }

    /**
     * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
     * 子类在对 setContentView 重写的时候请调用
     * super.setContentView(view,params);
     * 否则不能对软键盘进行监听
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mRootView = view;
        ViewBinder.doBind(this, view);
    }
    //</editor-fold>

    //<editor-fold desc="生命周期">

    /**
     * final 原始 onCreate(Bundle bundle)
     * 子类只能重写 onCreate(Bundle bundle,AfIntent intent)
     */
    @Override
    protected void onCreate(Bundle bundle) {
        try {
            AfApp.get().setCurActivity(this, this);
//            this.onQueryChanged();
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfActivity.setCurActivity");
        }
        try {
            if (AfStackTrace.isLoopCall()) {
                //System.out.println("递归检测");
                super.onCreate(bundle);
                return;
            }
            Injecter.doInject(this);
            LayoutBinder.doBind(this);
        } catch (final Throwable e) {
            //handle 可能会根据 Activity 弹窗提示错误信息
            //当前 Activity 即将关闭，提示窗口也会关闭
            //用定时器 等到原始 Activity 再提示弹窗
            if (!(e instanceof AfToastException)) {
                AfDispatcher.dispatch(() -> AfExceptionHandler.handle(e, TAG() + ".onCreate"), 500);
            }
            super.onCreate(bundle);
            makeToastLong("页面启动失败", e);
            this.finish();
            return;
        }
        try {
            LifeCycleInjecter.injectOnCreate(this, bundle);
            this.onCreate(bundle, new AfIntent(getIntent()));
        } catch (final Exception e) {
            if (!(e instanceof AfToastException)) {
                AfDispatcher.dispatch(() -> AfExceptionHandler.handle(e, TAG() + ".onCreate"), 500);
            }
            makeToastLong("页面启动失败", e);
            this.finish();
        }
    }

    /**
     * 新的 onCreate 实现
     * 重写的 时候 一般情况下请 调用
     * super.onCreate(bundle,intent);
     *
     * @throws Exception 安全异常
     */
    @SuppressWarnings("UnusedParameters")
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle);
        if (bundle != null) {
            AfApp.get().onRestoreInstanceState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mIsResume = true;
        LifeCycleInjecter.injectOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LifeCycleInjecter.injectOnPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LifeCycleInjecter.injectOnStart(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LifeCycleInjecter.injectOnRestart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mIsResume = false;
        LifeCycleInjecter.injectOnStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AfApp.get().setCurActivity(this, null);
            mIsResume = false;
            mIsRecycled = true;
            LifeCycleInjecter.injectOnDestroy(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfActivity.onDestroy");
        }
    }

//    /**
//     * 查询系统数据变动
//     */
//    public void onQueryChanged() {
//
//    }

    /**
     * 保证在还原数据时不会崩溃
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Throwable e) {
            if (AfApp.get().isDebug()) {
                AfExceptionHandler.handle(e, "AfActivity.onRestoreInstanceState");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            this.mIsResume = false;
            AfApp.get().onSaveInstanceState();
            super.onSaveInstanceState(outState);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfActivity.onSaveInstanceState");
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     * final 重写 onActivityResult 使用 try-catch 调用
     * onActivityResult(AfIntent intent, int requestcode,int resultcode)
     * @see AfActivity#onActivityResult(AfIntent intent, int requestcode, int resultcode)
     * {@link AfActivity#onActivityResult(AfIntent intent, int requestcode, int resultcode)}
     */
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        try {
            if (AfStackTrace.isLoopCall()) {
                //System.out.println("递归检测");
                return;
            }
            onActivityResult(new AfIntent(data), requestcode, resultcode);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, TAG() + ".onActivityResult");
            makeToastLong("反馈信息读取错误！", e);
        }
    }

    /**
     * 安全 onActivityResult(AfIntent intent, int requestcode,int resultcode)
     * 在onActivityResult(int requestcode, int resultCode, Intent data) 中调用
     * 并使用 try-catch 提高安全性，子类请重写这个方法
     *
     * @param intent      Intent 的子类 支持对象持久化
     * @param requestcode 请求码
     * @param resultcode  返回码
     * @see AfActivity#onActivityResult(int, int, android.content.Intent)
     */
    protected void onActivityResult(AfIntent intent, int requestcode, int resultcode) throws Exception {
        super.onActivityResult(requestcode, resultcode, intent);
    }

    //</editor-fold>

    //<editor-fold desc="接口实现">

    @Override
    public View getView() {
        return mRootView;
    }

    @SuppressWarnings("unused")
    public boolean isResume() {
        return mIsResume;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public <T extends View> T findViewById(int id, Class<T> clazz) {
        View view = findViewById(id);
        if (clazz.isInstance(view)) {
            return clazz.cast(view);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewByID(int id) {
        try {
            return (T) findViewById(id);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, TAG("findViewByID"));
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="页面切换">
    @Override
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void startActivity(Class<? extends Activity> clazz, Object... args) {
        AfIntent intent = new AfIntent(this, clazz);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<?>) arg);
                    } else {
                        intent.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz, int request) {
        startActivityForResult(new Intent(this, clazz), request);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz,
                                       int request, Object... args) {
        AfIntent intent = new AfIntent(this, clazz);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<?>) arg);
                    } else {
                        intent.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        startActivityForResult(intent, request);
    }

    @SuppressWarnings("unused")
    public void setResultOk(Object... args) {
        AfIntent intent = new AfIntent();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<?>) arg);
                    } else {
                        intent.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        setResult(RESULT_OK, intent);
    }
    //</editor-fold>

    //<editor-fold desc="气泡提示">
    @Override
    public void makeToastLong(int resid) {
        Toast.makeText(this, resid, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(int resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(CharSequence tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(CharSequence tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }
    //</editor-fold>

    //<editor-fold desc="任务封装">

    /**
     * 抛送任务到Worker执行
     */
    @Override
    public <T extends AfTask> T postTask(T task) {
        return AfTaskExecutor.getInstance().postTask(task);
    }

    /**
     * 抛送带数据任务到Worker执行
     */
    @Override
    public <T> AfDataTask postDataTask(T t, AfDataTask.OnTaskHandlerListener<T> task) {
        return postTask(new AfDataTask<>(t, task));
    }

    /**
     * 抛送带数据任务到Worker执行
     */
    @Override
    public <T, TT> AfData2Task postDataTask(T t, TT tt, AfData2Task.OnData2TaskHandlerListener<T, TT> task) {
        return postTask(new AfData2Task<>(t, tt, task));
    }

    /**
     * 抛送带数据任务到Worker执行
     */
    @Override
    public <T, TT, TTT> AfData3Task postDataTask(T t, TT tt, TTT ttt, AfData3Task.OnData3TaskHandlerListener<T, TT, TTT> task) {
        return postTask(new AfData3Task<>(t, tt, ttt, task));
    }
    //</editor-fold>

    //<editor-fold desc="事件转发">

    /**
     * 转发 onBackPressed 事件给 AfFragment
     */
    @Override
    public void onBackPressed() {
        if (AfStackTrace.isLoopCall()) {
            super.onBackPressed();
            return;
        }

        if (!this.onBackKeyPressed()) {
            super.onBackPressed();
        }
    }

    /**
     * 转发 onBackPressed 事件给 AfFragment
     */
    protected boolean onBackKeyPressed() {
        boolean isHandled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onBackPressed() || isHandled;
            }
        }
        return isHandled;
    }

    /**
     * 转发 onKeyLongPress 事件给 AfFragment
     */
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean isHandled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onKeyLongPress(keyCode, event) || isHandled;
            }
        }
        return isHandled || super.onKeyLongPress(keyCode, event);
    }

    /**
     * 转发 onKeyShortcut 事件给 AfFragment
     */
    @Override
    @SuppressLint("NewApi")
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        boolean isHandled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onKeyShortcut(keyCode, event) || isHandled;
            }
        }
        return isHandled || super.onKeyShortcut(keyCode, event);
    }

    /**
     * 转发 onKeyMultiple 事件给 AfFragment
     */
    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        boolean isHandled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onKeyMultiple(keyCode, repeatCount, event) || isHandled;
            }
        }
        return isHandled || super.onKeyMultiple(keyCode, repeatCount, event);
    }

    /**
     * 转发 onKeyUp 事件给 AfFragment
     */
    @Override
    @SuppressLint("NewApi")
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean isHandled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onKeyUp(keyCode, event) || isHandled;
            }
        }
        return isHandled || super.onKeyUp(keyCode, event);
    }

    /**
     * 转发 onKeyDown 事件给 AfFragment
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isHandled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onKeyDown(keyCode, event) || isHandled;
            }
        }
        return isHandled || super.onKeyDown(keyCode, event);
    }
    //</editor-fold>

    //<editor-fold desc="进度显示对话框">
    DialogBuilder dialogBuilder = AfApp.get().newDialogBuilder(this);
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    public Dialog showProgressDialog(CharSequence message) {
        return dialogBuilder.showProgressDialog(message);
    }
    /**
     * 动态改变等待对话框的文字
     *
     * @param text   更新的文字
     */
    public void setProgressDialogText(CharSequence text) {
        dialogBuilder.setProgressDialogText(text);
    }
    /**
     * 隐藏 进度对话框
     */
    public void hideProgressDialog() {
        dialogBuilder.hideProgressDialog();
    }
    //</editor-fold>

}
