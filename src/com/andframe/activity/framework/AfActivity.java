package com.andframe.activity.framework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.application.AfApp;
import com.andframe.application.AfApplication;
import com.andframe.application.AfDaemonThread;
import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfDialogBuilder;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.thread.AfData2Task;
import com.andframe.thread.AfData3Task;
import com.andframe.thread.AfDataTask;
import com.andframe.thread.AfDispatcher;
import com.andframe.thread.AfTask;
import com.andframe.thread.AfThreadWorker;
import com.andframe.util.java.AfStackTrace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 框架 Activity
 *
 * @author 树朾
 *         <p/>
 *         以下是 Activity 像子类提供的 功能方法
 *         <p/>
 *         protected void buildThreadWorker()
 *         为本页面开启一个独立后台线程 供 postTask 的 任务(AfTask)运行 注意：开启线程之后 postTask
 *         任何任务都会在该线程中运行。 如果 postTask 前一个任务未完成，后一个任务将等待
 *         <p/>
 *         protected AfTask postTask(AfTask task)
 *         抛送任务到Worker执行
 */
public abstract class AfActivity extends FragmentActivity implements AfPageable {

//    public static final String EXTRA_DATA = "EXTRA_DATA";//通用数据传递标识
//    public static final String EXTRA_INDEX = "EXTRA_INDEX";//通用下标栓地标识
//    public static final String EXTRA_RESULT = "EXTRA_RESULT";//通用返回传递标识
//    public static final String EXTRA_MAIN = "EXTRA_MAIN";//主要数据传递标识
//    public static final String EXTRA_DEPUTY = "EXTRA_DEPUTY";//主要数据传递标识

//    public static final int LP_MP = LayoutParams.MATCH_PARENT;
//    public static final int LP_WC = LayoutParams.WRAP_CONTENT;

    protected View mRootView = null;
    protected AfThreadWorker mWorker = null;
    protected AfDialogBuilder mDialogBuilder = newDialogBuilder();

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

    /**
     * 开始 IViewQuery 查询
     * @param id 控件Id
     */
    protected IViewQuery $(int... id) {
        IViewQuery query = AfApp.get().getViewQuery(mRootView);
        if (id == null || id.length == 0) {
            return query;
        }
        return query.id(id[0]);
    }
    /**
     * 获取 Application 的 AfApplication实例
     *
     * @return 如果 Application 不是 AfApplication 返回 null
     */
    public AfApplication getAfApplication() {
        Application app = getApplication();
        if (app instanceof AfApplication) {
            return AfApplication.class.cast(app);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void dispatchAfterResume(Runnable runnable) {
        if (isResume()) {
            AfDispatcher.dispatch(runnable);
        } else {
            postDataTask(runnable, new AfDataTask.AbDataTaskHandler<Runnable>() {
                @Override
                public void onTaskBackground(Runnable runnable) throws Exception {
                    while (!isResume() && !isRecycled()) {
                        Thread.sleep(1000);
                    }
                }

                @Override
                public boolean onTaskHandle(Runnable runnable, AfDataTask task) {
                    if (isResume() && !isRecycled()) {
                        AfDispatcher.dispatch(runnable);
                    }
                    return false;
                }
            });
        }
    }

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
        setContentView(view, new LayoutParams(LP_MP, LP_MP));
    }

    /**
     * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
     * 子类在对 setContentView 重写的时候请调用
     * super.setContentView(view,params);
     * 否则不能对软键盘进行监听
     */
    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        mRootView = view;
        ViewBinder.doBind(this,view);
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
                AfDispatcher.dispatch(new Runnable() {
                    @Override
                    public void run() {
                        AfExceptionHandler.handle(e, TAG() + ".onCreate");
                    }
                }, 500);
            }
            super.onCreate(bundle);
            makeToastLong("页面启动失败", e);
            this.finish();
            return;
        }
        try {
            this.onCreate(bundle, new AfIntent(getIntent()));
        } catch (final Exception e) {
            if (!(e instanceof AfToastException)) {
                AfDispatcher.dispatch(new Runnable() {
                    @Override
                    public void run() {
                        AfExceptionHandler.handle(e, TAG() + ".onCreate");
                    }
                }, 500);
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
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle);
        if (bundle != null) {
            AfApplication.getApp().onRestoreInstanceState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.mIsResume = true;
            getAfApplication().setCurActivity(this, this);
            this.onQueryChanged();
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfActivity.onResume");
        }
        try {
            Injecter.doInjectQueryChanged(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfActivity.doInjectQueryChanged");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mIsResume = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            getAfApplication().setCurActivity(this, null);
            mIsResume = false;
            mIsRecycled = true;
            if (mWorker != null) {
                mWorker.quit();
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfActivity.onDestroy");
        }
    }

    /**
     * 查询系统数据变动
     */
    public void onQueryChanged() {

    }

    /**
     * 保证在还原数据时不会崩溃
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Throwable e) {
            if (AfApplication.getApp().isDebug()) {
                AfExceptionHandler.handle(e, "AfActivity.onRestoreInstanceState");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            this.mIsResume = false;
            AfApplication.getApp().onSaveInstanceState();
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
    protected void onActivityResult(AfIntent intent, int requestcode, int resultcode) throws Exception{
        super.onActivityResult(requestcode, resultcode, intent);
    }

    //</editor-fold>

    //<editor-fold desc="接口实现">

    @Override
    public View getView() {
        return mRootView;
    }

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

    public void startActivity(Class<? extends Activity> clazz,Object... args) {
        AfIntent intent = new AfIntent(this, clazz);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<?>)arg);
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

    public void startActivityForResult(Class<? extends Activity> clazz,
                                       int request, Object... args) {
        AfIntent intent = new AfIntent(this, clazz);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<?>)arg);
                    } else {
                        intent.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        startActivityForResult(intent, request);
    }

    public void setResultOk(Object... args) {
        AfIntent intent = new AfIntent();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<?>)arg);
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
    public void makeToastLong(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(String tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip);
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }
    //</editor-fold>

    //<editor-fold desc="任务封装">
    /**
     * 为本页面开启一个独立后台线程 供 postTask 的 任务(AfTask)运行 注意：开启线程之后 postTask
     * 任何任务都会在该线程中运行。 如果 postTask 前一个任务未完成，后一个任务将等待
     */
    @SuppressWarnings("unused")
    protected void buildThreadWorker() {
        if (mWorker == null) {
            mWorker = new AfThreadWorker(this.getClass().getSimpleName());
        }
    }
    /**
     * 抛送任务到Worker执行
     */
    public <T extends AfTask> T postTask(T task) {
        if (mWorker != null) {
            return mWorker.postTask(task);
        }
        return AfDaemonThread.postTask(task);
    }

    /**
     * 抛送带数据任务到Worker执行
     */
    public <T> AfDataTask postDataTask(T t, AfDataTask.OnTaskHandlerListener<T> task) {
        return postTask(new AfDataTask<>(t, task));
    }

    /**
     * 抛送带数据任务到Worker执行
     */
    public <T, TT> AfData2Task postDataTask(T t, TT tt, AfData2Task.OnData2TaskHandlerListener<T, TT> task) {
        return postTask(new AfData2Task<>(t, tt, task));
    }

    /**
     * 抛送带数据任务到Worker执行
     */
    public <T, TT, TTT> AfData3Task postDataTask(T t, TT tt, TTT ttt, AfData3Task.OnData3TaskHandlerListener<T, TT, TTT> task) {
        return postTask(new AfData3Task<>(t, tt, ttt, task));
    }
    //</editor-fold>

    //<editor-fold desc="对话框封装">
    /**
     * 创建对话框构建器
     */
    @NonNull
    protected AfDialogBuilder newDialogBuilder() {
        return new AfDialogBuilder(this);
    }
    //<editor-fold desc="进度对话框">
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    public Dialog showProgressDialog(String message) {
        return showProgressDialog(message, false, 20);
    }

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     * @param cancel  是否可取消
     */
    public Dialog showProgressDialog(String message, boolean cancel) {
        return showProgressDialog(message, cancel, 20);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param cancel   是否可取消
     * @param textsize 字体大小
     */
    public Dialog showProgressDialog(String message, boolean cancel, int textsize) {
        return mDialogBuilder.showProgressDialog(message, cancel, textsize);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 取消监听器
     */
    public Dialog showProgressDialog(String message, OnCancelListener listener) {
        return mDialogBuilder.showProgressDialog(message, listener);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 取消监听器
     * @param textsize 字体大小
     */
    public Dialog showProgressDialog(String message, OnCancelListener listener, int textsize) {
        return mDialogBuilder.showProgressDialog(message, listener, textsize);
    }

    /**
     * 隐藏 进度对话框
     */
    public void hideProgressDialog() {
        mDialogBuilder.hideProgressDialog();
    }

    /**
     * 动态改变对话框的文本
     *
     * @param text 文本
     */
    @SuppressWarnings("unused")
    protected void setProgressDialogText(String text) {
        mDialogBuilder.setProgressDialogText(text);
    }
    //</editor-fold>

    //<editor-fold desc="通用对话框">
    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title   显示标题
     * @param message 显示内容
     */
    public Dialog doShowDialog(String title, String message) {
        return mDialogBuilder.showDialog(title, message);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    public Dialog doShowDialog(String title, String message, OnClickListener lpositive) {
        return mDialogBuilder.showDialog(title, message, lpositive);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public Dialog doShowDialog(String title, String message, String positive, OnClickListener lpositive) {
        return mDialogBuilder.showDialog(title, message, positive, lpositive);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return mDialogBuilder.showDialog(title, message, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowDialog(String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showDialog(title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return mDialogBuilder.showDialog(iconres, title, message, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(int iconres, String title, String message,String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showDialog(iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示视图对话框
     *
     * @param theme     主题
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowDialog(int theme, int iconres,
                             String title, String message,String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showDialog(theme, iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }
    //</editor-fold>

    //<editor-fold desc="自定对话框">
    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    @Override
    public Dialog doShowViewDialog(String title, View view, String positive, OnClickListener lpositive) {
        return mDialogBuilder.showViewDialog(title, view, positive, lpositive);
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowViewDialog(String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return mDialogBuilder.showViewDialog(title, view, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowViewDialog(String title, View view,String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showViewDialog(title, view, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示视图对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowViewDialog(int iconres, String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return mDialogBuilder.showViewDialog(iconres, title, view, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示视图对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowViewDialog(int iconres, String title, View view,String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showViewDialog(iconres, title, view, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示视图对话框
     *
     * @param theme     主题
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @Override
    public Dialog doShowViewDialog(int theme,
                                 int iconres, String title, View view,String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showViewDialog(theme, iconres, title, view, positive, lpositive, negative, lnegative, neutral, lneutral);
    }
    //</editor-fold>

    //<editor-fold desc="多选对话框">
    /**
     * 显示一个单选对话框 （设置可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param cancel   取消选择监听器
     */
    public Dialog doSelectItem(String title, String[] items, OnClickListener listener, boolean cancel) {
        return mDialogBuilder.selectItem(title, items, listener, cancel);
    }

    /**
     * 显示一个单选对话框
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param oncancel 取消选择监听器
     */
    public Dialog doSelectItem(String title, String[] items, OnClickListener listener, OnCancelListener oncancel) {
        return mDialogBuilder.selectItem(title, items, listener, oncancel);
    }

    /**
     * 显示一个单选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    public Dialog doSelectItem(String title, String[] items, OnClickListener listener) {
        return mDialogBuilder.selectItem(title, items, listener);
    }
    //</editor-fold>

    //<editor-fold desc="输入对话框">
    /**
     * 弹出一个文本输入框
     *  @param title    标题
     * @param listener 监听器
     */
    public Dialog doInputText(String title, InputTextListener listener) {
        return mDialogBuilder.inputText(title, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public Dialog doInputText(String title, int type, InputTextListener listener) {
        return mDialogBuilder.inputText(title, type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    public Dialog doInputText(String title, String defaul, InputTextListener listener) {
        return mDialogBuilder.inputText(title, defaul, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public Dialog doInputText(String title, String defaul, int type, InputTextListener listener) {
        return mDialogBuilder.inputText(title, defaul, type, listener);
    }
    //</editor-fold>

    //<editor-fold desc="记忆对话框">
    /**
     * 显示对话框(不再提示) 并添加默认按钮 "我知道了"
     *
     * @param key     不再显示KEY
     * @param title   显示标题
     * @param message 显示内容
     */
    public Dialog doShowDialog(String key, String title, String message) {
        return mDialogBuilder.showKeyDialog(key, title, message);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public Dialog doShowDialog(String key, String title, String message, String positive, OnClickListener lpositive) {
        return mDialogBuilder.showKeyDialog(key, title, message, positive, lpositive);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(String key, int defclick,
                               String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return mDialogBuilder.showKeyDialog(key, defclick, title, message, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(String key, int defclick,
                               String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showKeyDialog(key, defclick, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(String key, int defclick,
                               int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return mDialogBuilder.showKeyDialog(key, defclick, iconres, title, message, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(String key, int defclick,
                               int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showKeyDialog(key, defclick, iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }


    /**
     * 显示视图对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param theme     主题
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog doShowDialog(String key, int defclick,
                               int theme, int iconres,
                               String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return mDialogBuilder.showKeyDialog(key, defclick, theme, iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }
    //</editor-fold>

    //<editor-fold desc="时间对话框">
    /**
     * 选择日期时间
     * @param listener 监听器
     */
    public Dialog doSelectDateTime(AfDialogBuilder.OnDateTimeSetListener listener) {
        return doSelectDateTime("", new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param listener 监听器
     */
    public Dialog doSelectDateTime(String title, AfDialogBuilder.OnDateTimeSetListener listener) {
        return doSelectDateTime(title, new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog doSelectDateTime(Date value, AfDialogBuilder.OnDateTimeSetListener listener) {
        return doSelectDateTime("", value, listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog doSelectDateTime(final String title, final Date value, final AfDialogBuilder.OnDateTimeSetListener listener) {
        return mDialogBuilder.selectDateTime(title, value, listener);
    }

    /**
     * 选择时间
     * @param listener 监听器
     */
    public Dialog doSelectTime(TimePickerDialog.OnTimeSetListener listener) {
        return doSelectTime("", new Date(), listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param listener 监听器
     */
    public Dialog doSelectTime(String title, TimePickerDialog.OnTimeSetListener listener) {
        return doSelectTime(title, new Date(), listener);
    }

    /**
     * 选择时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog doSelectTime(Date value, TimePickerDialog.OnTimeSetListener listener) {
        return doSelectTime("", value, listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog doSelectTime(String title, Date value, final TimePickerDialog.OnTimeSetListener listener) {
        return mDialogBuilder.selectTime(title, value, listener);
    }

    /**
     * 选择日期
     * @param listener 监听器
     */
    public Dialog doSelectDate(DatePickerDialog.OnDateSetListener listener) {
        return doSelectDate("", new Date(), listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param listener 监听器
     */
    public Dialog doSelectDate(String title, DatePickerDialog.OnDateSetListener listener) {
        return doSelectDate(title, new Date(), listener);
    }

    /**
     * 选择日期
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog doSelectDate(Date value, DatePickerDialog.OnDateSetListener listener) {
        return doSelectDate("", value, listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog doSelectDate(String title, Date value, DatePickerDialog.OnDateSetListener listener) {
        return mDialogBuilder.selectDate(title,value, listener);
    }
    //</editor-fold>

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
        fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
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
        fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
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
        fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
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
        fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
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
        fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
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
        fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onKeyDown(keyCode, event) || isHandled;
            }
        }
        return isHandled || super.onKeyDown(keyCode, event);
    }
    //</editor-fold>


}
