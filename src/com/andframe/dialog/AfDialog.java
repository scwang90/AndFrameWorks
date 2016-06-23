package com.andframe.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfSoftInputListener;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.application.AfApplication;
import com.andframe.application.AfDaemonThread;
import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfException;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfDailog;
import com.andframe.feature.AfIntent;
import com.andframe.feature.AfSoftInputer;
import com.andframe.thread.AfData2Task;
import com.andframe.thread.AfData3Task;
import com.andframe.thread.AfDataTask;
import com.andframe.thread.AfTask;
import com.andframe.thread.AfThreadWorker;
import com.andframe.util.java.AfStackTrace;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AfDialog
 * Created by Administrator on 2016/3/5 0005.
 */
public abstract class AfDialog extends Dialog implements AfPageable, AfSoftInputListener {

    // 根视图
    protected View mRootView = null;

    protected AfThreadWorker mWorker = null;
    protected ProgressDialog mProgress = null;
    protected boolean mIsRecycled = false;

    public AfDialog(Context context) {
        super(context);
    }

    public AfDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AfDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    /**
     * 获取LOG日志 TAG 是 AfActivity 的方法
     * 用户也可以重写自定义TAG,这个值AfActivity在日志记录时候会使用
     * 子类实现也可以使用
     */
    protected String TAG() {
        return "AfDialog(" + getClass().getName() + ")";
    }

    protected String TAG(String tag) {
        return "AfDialog(" + getClass().getName() + ")." + tag;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        try {
            if (AfStackTrace.isLoopCall()) {
                super.onCreate(bundle);
                return;
            }
            Injecter.doInject(this, getContext());
            LayoutBinder.doBind(this);
            this.onCreate(bundle!= null?new AfBundle(bundle):null);
        } catch (final Throwable e) {
            if (!(e instanceof AfToastException)) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        AfExceptionHandler.handle(e, TAG() + ".onCreate");
                    }
                }, 500);
            }
            makeToastLong("页面启动失败", e);
            this.dismiss();
        }
    }

    /**
     * 新的 onCreate 实现
     * 重写的 时候 一般情况下请 调用
     * super.onCreate(bundle,intent);
     *
     * @throws Exception 安全异常
     */
    protected void onCreate(AfBundle bundle) throws Exception {
        super.onCreate(bundle != null ? bundle.getBundle() : null);
        if (bundle != null) {
            AfApplication.getApp().onRestoreInstanceState();
        }
    }


    /**
     * 为了实现对软键盘输入法显示和隐藏 的监听重写了 setContentView
     * 子类在对 setContentView 重写的时候请调用
     * super.setContentView(res);
     * 否则不能对软键盘进行监听
     */
    @Override
    public void setContentView(int res) {
        setContentView(LayoutInflater.from(getContext()).inflate(res, null));
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
        ViewBinder.doBind(this, this);
        AfSoftInputer inputer = new AfSoftInputer(getContext());
        inputer.setBindListener(view, this);
    }

    @Override
    public void onSoftInputShown() {

    }

    @Override
    public void onSoftInputHiden() {

    }

    /**
     * 查询系统数据变动
     */
    @Override
    public void onQueryChanged() {
    }

    @Override
    public Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return ((Activity) context);
        }
        return AfApplication.getApp().getCurActivity();
    }

    @Override
    public String getString(int resid) {
        return getActivity().getString(resid);
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

    /**
     * 为本页面开启一个独立后台线程 供 postTask 的 任务(AfTask)运行 注意：开启线程之后 postTask
     * 任何任务都会在该线程中运行。 如果 postTask 前一个任务未完成，后一个任务将等待
     */
    protected void buildThreadWorker() {
        if (mWorker == null) {
            mWorker = new AfThreadWorker(this.getClass().getSimpleName());
        }
    }

    /**
     * 抛送任务到Worker执行
     * @param task 任务标识
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

    @Override
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    public void startActivity(Class<? extends Activity> clazz,Object... args) {
        AfIntent intent = new AfIntent(getActivity(), clazz);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<? extends Object>)arg);
                    } else {
                        intent.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz,
                                       int request) {
        getActivity().startActivityForResult(new Intent(getActivity(), clazz), request);
    }

    public void startActivityForResult(Class<? extends Activity> clazz,
                                       int request, Object... args) {
        AfIntent intent = new AfIntent(getActivity(), clazz);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        intent.putList((String) args[2 * i], (List<? extends Object>)arg);
                    } else {
                        intent.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        getActivity().startActivityForResult(intent, request);
    }


    @Override
    public boolean getSoftInputStatus() {
        return new AfSoftInputer(getActivity()).getSoftInputStatus();
    }

    @Override
    public boolean getSoftInputStatus(View view) {
        return new AfSoftInputer(getActivity()).getSoftInputStatus(view);
    }

    @Override
    public void setSoftInputEnable(EditText editview, boolean enable) {
        new AfSoftInputer(getActivity()).setSoftInputEnable(editview, enable);
    }

    @Override
    public void makeToastLong(String tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(String tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(int resid) {
        Toast.makeText(getContext(), resid, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(int resid) {
        Toast.makeText(getContext(), resid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(String tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip);
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }


    @Override
    public Resources getResources() {
        return getActivity().getResources();
    }

    @Override
    public final View findViewById(int id) {
        if (mRootView != null) {
            return mRootView.findViewById(id);
        }
        return null;
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

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    public final void showProgressDialog(String message) {
        showProgressDialog(message, false, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     * @param cancel  是否可取消
     */
    public final void showProgressDialog(String message, boolean cancel) {
        showProgressDialog(message, cancel, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param cancel   是否可取消
     * @param textsize 字体大小
     */
    public final void showProgressDialog(String message, boolean cancel,
                                         int textsize) {
        try {
            mProgress = new ProgressDialog(getActivity());
            mProgress.setMessage(message);
            mProgress.setCancelable(cancel);
            mProgress.setOnCancelListener(null);
            mProgress.show();

            setDialogFontSize(mProgress, textsize);
        } catch (Throwable e) {
            //进过日志验证，这个异常会发送，但是概率非常小，注释掉异常通知
//			AfExceptionHandler.handle(e, "AfActivity.showProgressDialog");
        }
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 字体大小
     */
    public final void showProgressDialog(String message,
                                         OnCancelListener listener) {
        try {
            mProgress = new ProgressDialog(getActivity());
            mProgress.setMessage(message);
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(listener);
            mProgress.show();

            setDialogFontSize(mProgress, 25);
        } catch (Throwable e) {
            //进过日志验证，这个异常会发送，但是概率非常小，注释掉异常通知
//			AfExceptionHandler.handle(e, "AfActivity.showProgressDialog");
        }
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 是否可取消
     * @param textsize 字体大小
     */
    public final void showProgressDialog(String message, OnCancelListener listener, int textsize) {
        try {
            mProgress = new ProgressDialog(getActivity());
            mProgress.setMessage(message);
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(listener);
            mProgress.show();

            setDialogFontSize(mProgress, textsize);
        } catch (Throwable e) {
            //进过日志验证，这个异常会发送，但是概率非常小，注释掉异常通知
//			AfExceptionHandler.handle(e, "AfActivity.showProgressDialog");
        }
    }

    /**
     * 隐藏 进度对话框
     */
    public final void hideProgressDialog() {
        try {
            if (mProgress != null && !isRecycled()) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfActivity.hideProgressDialog");
        }
    }

    @Override
    public void startActivity(Intent intent) {

    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title   显示标题
     * @param message 显示内容
     */
    public AlertDialog doShowDialog(String title, String message) {
        return doShowDialog(0, title, message, "我知道了", null, "", null);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    public AlertDialog doShowDialog(String title, String message, OnClickListener lpositive) {
        return doShowDialog(0, title, message, "我知道了", lpositive, "", null);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public AlertDialog doShowDialog(String title, String message, String positive, OnClickListener lpositive) {
        return doShowDialog(0, title, message, positive, lpositive, "", null);
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
    public AlertDialog doShowDialog(String title, String message,
                                    String positive, OnClickListener lpositive, String negative,
                                    OnClickListener lnegative) {
        return doShowDialog(0, title, message, positive, lpositive, negative, lnegative);
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
    public AlertDialog doShowDialog(String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String neutral, OnClickListener lneutral,
                                    String negative, OnClickListener lnegative) {
        return doShowDialog(0, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    public AlertDialog doShowDialog(int iconres, String title, String message,
                                    String positive, OnClickListener lpositive, String negative,
                                    OnClickListener lnegative) {
        return doShowDialog(iconres, title, message, positive, lpositive, "", null, negative, lnegative);
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
    public AlertDialog doShowDialog(int iconres, String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String neutral, OnClickListener lneutral,
                                    String negative, OnClickListener lnegative) {
        return doShowDialog(-1, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    @SuppressLint("NewApi")
    public AlertDialog doShowDialog(int theme, int iconres,
                                    String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String neutral, OnClickListener lneutral,
                                    String negative, OnClickListener lnegative) {
        return new AfDailog(getActivity()).doShowDialog(theme, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    @Override
    public AlertDialog doShowViewDialog(String title, View view, String positive,
                                        OnClickListener lpositive) {
        return doShowViewDialog(title, view, positive, lpositive, "", null);
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
    public AlertDialog doShowViewDialog(String title, View view, String positive,
                                        OnClickListener lpositive, String negative,
                                        OnClickListener lnegative) {
        return doShowViewDialog(0, title, view, positive, lpositive, negative, lnegative);
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
    public AlertDialog doShowViewDialog(String title, View view,
                                        String positive, OnClickListener lpositive,
                                        String neutral, OnClickListener lneutral,
                                        String negative, OnClickListener lnegative) {
        return doShowViewDialog(0, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    public AlertDialog doShowViewDialog(int iconres, String title, View view,
                                        String positive, OnClickListener lpositive,
                                        String negative, OnClickListener lnegative) {
        return doShowViewDialog(0, title, view, positive, lpositive, "", null, negative, lnegative);
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
    public AlertDialog doShowViewDialog(int iconres, String title, View view,
                                        String positive, OnClickListener lpositive,
                                        String neutral, OnClickListener lneutral,
                                        String negative, OnClickListener lnegative) {
        return doShowViewDialog(-1, iconres, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    @SuppressLint("NewApi")
    @Override
    public AlertDialog doShowViewDialog(int theme,
                                        int iconres, String title, View view,
                                        String positive, OnClickListener lpositive,
                                        String neutral, OnClickListener lneutral,
                                        String negative, OnClickListener lnegative) {
        return new AfDailog(getActivity()).doShowViewDialog(theme, iconres, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
    }

    /**
     * 显示一个单选对话框 （设置可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param cancel   取消选择监听器
     */
    public AlertDialog doSelectItem(String title, String[] items, OnClickListener listener,
                                    boolean cancel) {
        return new AfDailog(getActivity()).doSelectItem(title, items, listener, cancel);
    }

    /**
     * 显示一个单选对话框
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param oncancel 取消选择监听器
     */
    public AlertDialog doSelectItem(String title, String[] items, OnClickListener listener,
                                    final OnClickListener oncancel) {
        return new AfDailog(getActivity()).doSelectItem(title, items, listener, oncancel);
    }

    /**
     * 显示一个单选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    public AlertDialog doSelectItem(String title, String[] items, OnClickListener listener) {
        return doSelectItem(title, items, listener, null);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    public void doInputText(String title, InputTextListener listener) {
        doInputText(title, "", InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public AlertDialog doInputText(String title, int type, InputTextListener listener) {
        return doInputText(title, "", type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public AlertDialog doInputText(String title, String defaul, int type, InputTextListener listener) {
        return new AfDailog(getActivity()).doInputText(title, defaul, type, listener);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param key     不再显示KEY
     * @param title   显示标题
     * @param message 显示内容
     */
    public AlertDialog doShowDialog(String key, String title, String message) {
        return doShowDialog(key, 0, 0, title, message, "我知道了", null, "", null);
    }

    /**
     * 显示对话框
     *
     * @param key       不再显示KEY
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public AlertDialog doShowDialog(String key, String title, String message, String positive, OnClickListener lpositive) {
        return doShowDialog(key, 0, 0, title, message, positive, lpositive, "", null);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                                    String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String negative, OnClickListener lnegative) {
        return doShowDialog(key, defclick, 0, title, message, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                                    String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String neutral, OnClickListener lneutral,
                                    String negative, OnClickListener lnegative) {
        return doShowDialog(key, defclick, 0, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                                    int iconres, String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String negative, OnClickListener lnegative) {
        if (defclick == 1) {
            defclick = 2;
        }
        return doShowDialog(key, defclick, iconres, title, message, positive, lpositive, "", null, negative, lnegative);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                                    int iconres, String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String neutral, OnClickListener lneutral,
                                    String negative, OnClickListener lnegative) {
        return doShowDialog(key, defclick, -1, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }


    /**
     * 显示视图对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                                    int theme, int iconres,
                                    String title, String message,
                                    String positive, OnClickListener lpositive,
                                    String neutral, OnClickListener lneutral,
                                    String negative, OnClickListener lnegative) {
        return new AfDailog(getActivity()).doShowDialog(key, defclick, theme, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }

    /**
     * 选择日期时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(AfDailog.OnDateTimeSetListener listener) {
        return doSelectDateTime("", new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(String title, AfDailog.OnDateTimeSetListener listener) {
        return doSelectDateTime(title, new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(Date value, AfDailog.OnDateTimeSetListener listener) {
        return doSelectDateTime("", value, listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(final String title, final Date value, final AfDailog.OnDateTimeSetListener listener) {
        return new AfDailog(getActivity()).doSelectDateTime(title, value, listener);
    }

    /**
     * 选择时间
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(AfDailog.OnTimeSetListener listener) {
        return doSelectTime("", new Date(), listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(String title, AfDailog.OnTimeSetListener listener) {
        return doSelectTime(title, new Date(), listener);
    }

    /**
     * 选择时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(Date value, AfDailog.OnTimeSetListener listener) {
        return doSelectTime("", value, listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(String title, Date value, final AfDailog.OnTimeSetListener listener) {
        return new AfDailog(getActivity()).doSelectTime(title, value, listener);
    }

    /**
     * 选择日期
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(AfDailog.OnDateSetListener listener) {
        return doSelectDate("", new Date(), listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(String title, AfDailog.OnDateSetListener listener) {
        return doSelectDate(title, new Date(), listener);
    }

    /**
     * 选择日期
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(Date value, AfDailog.OnDateSetListener listener) {
        return doSelectDate("", value, listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(String title, Date value, AfDailog.OnDateSetListener listener) {
        return new AfDailog(getActivity()).doSelectDate(title,value, listener);
    }

    protected void setProgressDialogText(ProgressDialog dialog, String text) {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        setViewFontText(view, text);
    }

    private void setViewFontText(View view, String text) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setViewFontText(parent.getChildAt(i), text);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setText(text);
        }
    }

    private void setDialogFontSize(Dialog dialog, int size) {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        setViewFontSize(view, size);
    }

    private void setViewFontSize(View view, int size) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setViewFontSize(parent.getChildAt(i), size);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    /**
     * 按下返回按键
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public void onBackPressed() {

    }

    /**
     * 按键按下事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 按键弹起事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 按键重复事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    /**
     * 按键onKeyShortcut事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 按键onKeyLongPress事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

}
