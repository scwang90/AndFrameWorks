package com.andframe.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.IViewQuery;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.application.AfApp;
import com.andframe.application.AfApplication;
import com.andframe.application.AfDaemonThread;
import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfDialogBuilder;
import com.andframe.feature.AfIntent;
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
public abstract class AfDialog extends Dialog implements AfPageable {

    //<editor-fold desc="属性字段">
    // 根视图
    protected View mRootView = null;

    protected AfThreadWorker mWorker = null;
    protected AfDialogBuilder mDialogBuilder;
    protected boolean mIsRecycled = false;
    //</editor-fold>

    //<editor-fold desc="构造方法">
    public AfDialog(Context context) {
        super(context);
        mDialogBuilder = newDialogBuilder();
    }

    public AfDialog(Context context, int themeResId) {
        super(context, themeResId);
        mDialogBuilder = newDialogBuilder();
    }

    public AfDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mDialogBuilder = newDialogBuilder();
    }
    //</editor-fold>

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

    /**
     * 开始 IViewQuery 查询
     * @param id 控件Id
     */
    @SuppressWarnings("unused")
    protected IViewQuery $(int... id) {
        IViewQuery query = AfApp.get().getViewQuery(mRootView);
        if (id == null || id.length == 0) {
            return query;
        }
        return query.id(id[0]);
    }
    @SuppressWarnings("unused")
    protected IViewQuery $(View view) {
        return AfApp.get().getViewQuery(view);
    }

    //<editor-fold desc="生命周期">
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
     * 查询系统数据变动
     */
    @Override
    public void onQueryChanged() {
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
    //</editor-fold>

    //<editor-fold desc="重写布局">
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
    //</editor-fold>

    //<editor-fold desc="页面跳转">
    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
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
        getActivity().startActivityForResult(new Intent(getActivity(), clazz), request);
    }

    public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
        AfIntent intent = new AfIntent(getActivity(), clazz);
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
        getActivity().startActivityForResult(intent, request);
    }
    //</editor-fold>

    //<editor-fold desc="气泡提示">
    @Override
    public void makeToastLong(CharSequence tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(CharSequence tip) {
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
    public void makeToastLong(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }
    //</editor-fold>

    //<editor-fold desc="接口实现">

    @Override
    public Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return ((Activity) context);
        }
        return AfApplication.getApp().getCurActivity();
    }

    @Override
    public View getView() {
        return mRootView;
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
    //</editor-fold>

    //<editor-fold desc="对话框封装">
    /**
     * 创建对话框构建器
     */
    @NonNull
    protected AfDialogBuilder newDialogBuilder() {
        return new AfDialogBuilder(getContext());
    }

    //<editor-fold desc="进度对话框">
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    public Dialog showProgressDialog(CharSequence message) {
        return showProgressDialog(message, false, 20);
    }

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     * @param cancel  是否可取消
     */
    public Dialog showProgressDialog(CharSequence message, boolean cancel) {
        return showProgressDialog(message, cancel, 20);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param cancel   是否可取消
     * @param textsize 字体大小
     */
    public Dialog showProgressDialog(CharSequence message, boolean cancel, int textsize) {
        return mDialogBuilder.showProgressDialog(message, cancel, textsize);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 取消监听器
     */
    public Dialog showProgressDialog(CharSequence message, OnCancelListener listener) {
        return mDialogBuilder.showProgressDialog(message, listener);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 取消监听器
     * @param textsize 字体大小
     */
    public Dialog showProgressDialog(CharSequence message, OnCancelListener listener, int textsize) {
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
    protected void setProgressDialogText(CharSequence text) {
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
    public Dialog doShowDialog(CharSequence title, CharSequence message) {
        return mDialogBuilder.showDialog(title, message);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    public Dialog doShowDialog(CharSequence title, CharSequence message, OnClickListener lpositive) {
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
    public Dialog doShowDialog(CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive) {
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
    public Dialog doShowDialog(CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) {
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
    public Dialog doShowDialog(CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
    public Dialog doShowDialog(int iconres, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) {
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
    public Dialog doShowDialog(int iconres, CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
                               CharSequence title, CharSequence message,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
    public Dialog doShowViewDialog(CharSequence title, View view, CharSequence positive, OnClickListener lpositive) {
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
    public Dialog doShowViewDialog(CharSequence title, View view, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) {
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
    public Dialog doShowViewDialog(CharSequence title, View view,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
    public Dialog doShowViewDialog(int iconres, CharSequence title, View view, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) {
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
    public Dialog doShowViewDialog(int iconres, CharSequence title, View view,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
                                   int iconres, CharSequence title, View view,CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
    public Dialog doSelectItem(CharSequence title, CharSequence[] items, OnClickListener listener, boolean cancel) {
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
    public Dialog doSelectItem(CharSequence title, CharSequence[] items, OnClickListener listener, OnCancelListener oncancel) {
        return mDialogBuilder.selectItem(title, items, listener, oncancel);
    }

    /**
     * 显示一个单选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    public Dialog doSelectItem(CharSequence title, CharSequence[] items, OnClickListener listener) {
        return mDialogBuilder.selectItem(title, items, listener);
    }
    //</editor-fold>

    //<editor-fold desc="输入对话框">
    /**
     * 弹出一个文本输入框
     *  @param title    标题
     * @param listener 监听器
     */
    public Dialog doInputText(CharSequence title, InputTextListener listener) {
        return mDialogBuilder.inputText(title, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public Dialog doInputText(CharSequence title, int type, InputTextListener listener) {
        return mDialogBuilder.inputText(title, type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    public Dialog doInputText(CharSequence title, CharSequence defaul, InputTextListener listener) {
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
    public Dialog doInputText(CharSequence title, CharSequence defaul, int type, InputTextListener listener) {
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
    public Dialog doShowDialog(String key, CharSequence title, CharSequence message) {
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
    public Dialog doShowDialog(String key, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive) {
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
                               CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) {
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
                               CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
                               int iconres, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative) {
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
                               int iconres, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
                               CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
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
    public Dialog doSelectDateTime(CharSequence title, AfDialogBuilder.OnDateTimeSetListener listener) {
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
    public Dialog doSelectDateTime(final CharSequence title, final Date value, final AfDialogBuilder.OnDateTimeSetListener listener) {
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
    public Dialog doSelectTime(CharSequence title, TimePickerDialog.OnTimeSetListener listener) {
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
    public Dialog doSelectTime(CharSequence title, Date value, final TimePickerDialog.OnTimeSetListener listener) {
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
    public Dialog doSelectDate(CharSequence title, DatePickerDialog.OnDateSetListener listener) {
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
    public Dialog doSelectDate(CharSequence title, Date value, DatePickerDialog.OnDateSetListener listener) {
        return mDialogBuilder.selectDate(title,value, listener);
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="新建事件">

    /**
     * 按下返回按键
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
    //</editor-fold>

}
