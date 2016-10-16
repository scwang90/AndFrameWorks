package com.andframe.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andframe.activity.AfActivity;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LifeCycleInjecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.pager.BindLayout;
import com.andframe.api.DialogBuilder;
import com.andframe.api.page.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfIntent;
import com.andframe.feature.AfView;
import com.andframe.impl.helper.AfViewQueryHelper;
import com.andframe.task.AfData2Task;
import com.andframe.task.AfData3Task;
import com.andframe.task.AfDataTask;
import com.andframe.task.AfTask;
import com.andframe.task.AfTaskExecutor;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
import java.util.List;

/**
 * 框架 AfFragment
 * @author 树朾
 */
public abstract class AfFragment extends Fragment implements Pager, ViewQueryHelper {

    //<editor-fold desc="属性字段">
    // 根视图
    protected View mRootView = null;
    protected boolean mIsRecycled = false;
    //</editor-fold>

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

    public AfActivity getAfActivity() {
        if (super.getActivity() instanceof AfActivity) {
            return ((AfActivity) super.getActivity());
        }
        return AfApp.get().getCurActivity();
    }

    /**
     * 获取LOG日志 TAG 是 AfFragment 的方法
     * 用户也可以重写自定义TAG,这个值AfActivity在日志记录时候会使用
     * 子类实现也可以使用
     */
    protected String TAG() {
        return "AfFragment(" + getClass().getName() + ")";
    }

    protected String TAG(String tag) {
        return "AfFragment(" + getClass().getName() + ")." + tag;
    }

    //<editor-fold desc="任务封装">
    /**
     * 抛送任务到Worker执行
     * @param task 任务标识
     */
    public <T extends AfTask> T postTask(T task) {
        return AfTaskExecutor.getInstance().postTask(task);
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

    //<editor-fold desc="页面切换">
    @Override
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getAfActivity(), clazz));
    }

    public void startActivity(Class<? extends Activity> clazz,Object... args) {
        AfIntent intent = new AfIntent(getAfActivity(), clazz);
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
    public void startActivityForResult(Class<? extends Activity> clazz,
                                       int request) {
        startActivityForResult(new Intent(getAfActivity(), clazz), request);
    }

    public void startActivityForResult(Class<? extends Activity> clazz,
                                       int request, Object... args) {
        AfIntent intent = new AfIntent(getAfActivity(), clazz);
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
    //</editor-fold>

    //<editor-fold desc="生命周期">

    /**
     * 锁住 上级的 View onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mRootView = onCreateView(inflater, container);
        if (mRootView == null) {
            mRootView = super.onCreateView(inflater, container, bundle);
        }
        try {
            ViewBinder.doBind(this);
            onCreated(new AfView(mRootView), new AfBundle(getArguments()));
        } catch (Throwable e) {
            if (!(e instanceof AfToastException)) {
                AfExceptionHandler.handle(e, TAG("onCreateView"));
            }
            makeToastLong("页面初始化异常！", e);
        }
        return mRootView;
    }

    /**
     * 自定义 View onCreateView(LayoutInflater, ViewGroup)
     */
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        BindLayout layout = AfReflecter.getAnnotation(this.getClass(), AfFragment.class, BindLayout.class);
        if (layout != null) {
            return inflater.inflate(layout.value(), container, false);
        }
        return null;
    }

    /**
     * 自定义 View onCreate(Bundle)
     */
    protected void onCreated(AfView rootView, AfBundle bundle) throws Exception {

    }

    @Override
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Injecter.doInject(this, getContext());
            LifeCycleInjecter.injectOnCreate(this, bundle);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, TAG("onCreate.doInject"));
            makeToastLong("页面初始化异常！", e);
        }
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            LifeCycleInjecter.injectOnResume(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onResume");
        }
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
            LifeCycleInjecter.injectOnStart(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onStart");
        }
    }

    @Override
    public void onStop() {
        try {
            super.onStop();
            LifeCycleInjecter.injectOnStop(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onStop");
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            LifeCycleInjecter.injectOnAttach(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onAttach");
        }
    }
    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     * final 重写 onActivityResult 使用 try-catch 调用
     * onActivityResult(AfIntent intent, int requestcode,int resultcode)
     * @see AfFragment#onActivityResult(AfIntent intent, int requestcode, int resultcode)
     * {@link AfFragment#onActivityResult(AfIntent intent, int requestcode, int resultcode)}
     */
    @Override
    public final void onActivityResult(int requestcode, int resultcode, Intent data) {
        try {
            onActivityResult(new AfIntent(data), requestcode, resultcode);
        } catch (Throwable e) {
            if (!(e instanceof AfToastException)) {
                AfExceptionHandler.handle(e, TAG("onActivityResult"));
            }
            makeToastLong("反馈信息读取错误！", e);
        }
    }

    /**
     * 安全 onActivityResult(AfIntent intent, int requestcode,int resultcode)
     * 在onActivityResult(int requestcode, int resultCode, Intent data) 中调用
     * 并使用 try-catch 提高安全性，子类请重写这个方法
     *
     * @see AfFragment#onActivityResult(int, int, android.content.Intent)
     * {@link AfFragment#onActivityResult(int, int, android.content.Intent)}
     */
    protected void onActivityResult(AfIntent intent, int requestcode, int resultcode) {
        super.onActivityResult(requestcode, resultcode, intent);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                afment.onActivityResult(intent, requestcode, resultcode);
            }
        }
    }

    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
//		    mRootView = null;
            LifeCycleInjecter.injectOnDestroyView(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            mIsRecycled = true;
            LifeCycleInjecter.injectOnDestroy(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onDestroy");
        }
    }

    /**
     * 第一次切换到本页面
     */
    protected void onFirstSwitchOver() {
    }

    /**
     * 每次切换到本页面
     * @param count 切换序号
     */
    protected void onSwitchOver(int count) {
    }

    /**
     * 离开本页面
     */
    protected void onSwitchLeave() {
    }

    //</editor-fold>

    //<editor-fold desc="气泡封装">
    @Override
    public void makeToastLong(CharSequence tip) {
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(CharSequence tip) {
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_SHORT).show();
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
    @Nullable
    @Override
    public View getView() {
        return mRootView;
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

    //<editor-fold desc="新建事件">
    /**
     * 按下返回按键
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 按键按下事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    @SuppressWarnings("UnusedParameters")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 按键弹起事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    @SuppressWarnings("UnusedParameters")
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 按键重复事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    @SuppressWarnings("UnusedParameters")
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    /**
     * 按键onKeyShortcut事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    @SuppressWarnings("UnusedParameters")
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 按键onKeyLongPress事件
     *
     * @return 返回 true 表示已经处理 否则 Activity 会处理
     */
    @SuppressWarnings("UnusedParameters")
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }
    //</editor-fold>

    //<editor-fold desc="进度显示对话框">
    DialogBuilder dialogBuilder;
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    public Dialog showProgressDialog(CharSequence message) {
        if (dialogBuilder == null) {
            dialogBuilder = AfApp.get().newDialogBuilder(getContext());
        }
        return dialogBuilder.showProgressDialog(message);
    }
    /**
     * 动态改变等待对话框的文字
     *
     * @param text   更新的文字
     */
    public void setProgressDialogText(CharSequence text) {
        if (dialogBuilder == null) {
            return;
        }
        dialogBuilder.setProgressDialogText(text);
    }
    /**
     * 隐藏 进度对话框
     */
    public void hideProgressDialog() {
        if (dialogBuilder == null) {
            return;
        }
        dialogBuilder.hideProgressDialog();
    }
    //</editor-fold>

}
