package com.andframe.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andframe.$;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.LifeCycleInjecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.DialogBuilder;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.ViewQueryHelper;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.impl.helper.AfViewQueryHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 框架 AfFragment
 * @author 树朾
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AfFragment extends Fragment implements Pager, ViewQueryHelper {

    //<editor-fold desc="属性字段">
    // 根视图
    protected View mRootView = null;
    protected boolean mIsRecycled = false;
    //</editor-fold>

    //<editor-fold desc="ViewQuery 集成">
    protected ViewQuery<? extends ViewQuery> $$ = AfViewQueryHelper.newHelper(this);

    @Override
    public void setViewQuery(@NonNull ViewQuery<? extends ViewQuery> viewQuery) {
        this.$$ = viewQuery;
    }

    @Override
    public ViewQuery<? extends ViewQuery> getViewQuery() {
        return $$;
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(View... views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Collection<View> views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
        return $$.query(id, ids);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(String idValue, String... idValues) {
        return $$.query(idValue);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
        return $$.query(type);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
        return $$.query(types);
    }

    @Override
    public ViewQuery<? extends ViewQuery> with(View... views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> with(Collection<View> views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(Integer id, int... ids) {
        return $$.query(id, ids);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(String idValue, String... idValues) {
        return $$.query(idValue);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(Class<? extends View> type) {
        return $$.query(type);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(Class<? extends View>[] types) {
        return $$.query(types);
    }
    //</editor-fold>

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
    public <T extends Task> T postTask(T task) {
        return $.task().postTask(task);
    }
    //</editor-fold>

    //<editor-fold desc="页面切换">

    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            startFragment(clazz,args);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            startActivity(clazz, args);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void startActivity(Class<? extends Activity> clazz,Object... args) {
        startActivity(new AfIntent(getActivity(), clazz, args));
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
        startActivityForResult(new AfIntent(getActivity(), clazz, args), request);
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        AfFragmentActivity.start(this, clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        AfFragmentActivity.startResult(this, clazz, request, args);
    }

    @Override
    public void setResultOk(Object... args) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setResult(RESULT_OK, new AfIntent().putKeyVaules(args));
        }
    }
    //</editor-fold>

    //<editor-fold desc="生命周期">

    /**
     * 锁住 上级的 View onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        try {
            mIsRecycled = false;
            View view = LifeCycleInjecter.injectCreateView(this, inflater, container, bundle);
            if (view != null) {
                mRootView = view;
            } else {
                mRootView = onCreateView(inflater, container);
                if (mRootView == null) {
                    mRootView = super.onCreateView(inflater, container, bundle);
                }
            }
        } catch (Throwable e) {
            makeToastShort("页面初始化异常！", e);
            AfExceptionHandler.handle(e, TAG("onCreateView"));
            return new View(inflater.getContext());
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ViewBinder.doBind(this);
        } catch (Throwable e) {
            makeToastShort("AfFragment#ViewBinder.doBind异常！", e);
            AfExceptionHandler.handle(e, TAG("onViewCreated"));
        }
    }

    @CallSuper@BindViewCreated
    protected void onViewCreated() {

    }

    /**
     * 自定义 View onCreateView(LayoutInflater, ViewGroup)
     */
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        int layoutId = LayoutBinder.getBindLayoutId(this, inflater.getContext());
        if (layoutId != 0) {
            return inflater.inflate(layoutId, container, false);
        }
        return null;
    }

    @Override
    public final void onCreate(Bundle bundle) {
        try {
            mIsRecycled = false;
            $.pager().onFragmentCreate(this);
            super.onCreate(bundle);
            Injecter.doInject(this, getContext());
            LifeCycleInjecter.injectOnCreate(this, bundle);
            onCreated();
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, TAG("onCreate.doInject"));
            makeToastShort("AfFragment#onCreate异常！", e);
        }
    }

    @CallSuper
    protected void onCreated() {

    }

    @Override
    public void onResume() {
        try {
            $.pager().onFragmentResume(this);
            super.onResume();
            LifeCycleInjecter.injectOnResume(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onResume");
        }
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
            $.pager().onFragmentPause(this);
            LifeCycleInjecter.injectOnPause(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onPause");
        }
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
            $.pager().onFragmentStart(this);
            LifeCycleInjecter.injectOnStart(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onStart");
        }
    }

    @Override
    public void onStop() {
        try {
            super.onStop();
            $.pager().onFragmentStop(this);
            LifeCycleInjecter.injectOnStop(this);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onStop");
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            $.pager().onFragmentAttach(this, context);
            super.onAttach(context);
            LifeCycleInjecter.injectOnAttach(this, context);
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfFragment.onAttach");
        }
    }
    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     * final 重写 onActivityResult 使用 try-catch 调用
     * onActivityResult(AfIntent intent, int requestCode,int resultCode)
     * @see AfFragment#onActivityResult(AfIntent intent, int requestCode, int resultCode)
     * {@link AfFragment#onActivityResult(AfIntent intent, int requestCode, int resultCode)}
     */
    @Override
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            onActivityResult(new AfIntent(data), requestCode, resultCode);
        } catch (Throwable e) {
            if (!(e instanceof AfToastException)) {
                AfExceptionHandler.handle(e, TAG("onActivityResult"));
            }
            makeToastShort("反馈信息读取错误！", e);
        }
    }

    /**
     * 安全 onActivityResult(AfIntent intent, int requestCode,int resultCode)
     * 在onActivityResult(int requestCode, int resultCode, Intent data) 中调用
     * 并使用 try-catch 提高安全性，子类请重写这个方法
     *
     * @see AfFragment#onActivityResult(int, int, android.content.Intent)
     * {@link AfFragment#onActivityResult(int, int, android.content.Intent)}
     */
    @SuppressWarnings("RestrictedApi")
    protected void onActivityResult(AfIntent intent, int requestCode, int resultCode) {
        super.onActivityResult(requestCode, resultCode, intent);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                afment.onActivityResult(intent, requestCode, resultCode);
            }
        }
    }

    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
            $$.clearIdCache();
		    mRootView = null;
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

    @Override
    public void onDetach() {
        try {
            super.onDetach();
            mIsRecycled = true;
            LifeCycleInjecter.injectonDetach(this);
            $.pager().onFragmentDetach(this);
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

    /**
     * 结束页面(只有Activity为AfFragmentActivity时生效)
     */
    public void finish() {
        FragmentActivity activity = getActivity();
        if (activity instanceof AfFragmentActivity) {
            activity.finish();
        }
    }

    //</editor-fold>

    //<editor-fold desc="气泡封装">
    @Override
    public void makeToastLong(CharSequence tip) {
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(CharSequence tip) {
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastLong(int resId) {
        Toast.makeText(AfApp.get(), resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastLong(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeToastShort(int resId) {
        Toast.makeText(AfApp.get(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToastShort(CharSequence tip, Throwable e) {
        tip = AfExceptionHandler.tip(e, tip.toString());
        Toast.makeText(AfApp.get(), tip, Toast.LENGTH_SHORT).show();
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
    public boolean isFinishing() {
        FragmentActivity activity = getActivity();
        return activity != null && activity.isFinishing();
    }

    @Override
    public boolean isShowing() {
        return !isRecycled() && !isFinishing();
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
    //</editor-fold>

    //<editor-fold desc="新建事件">

    /**
     * Activity onNewIntent 转发
     */
    @SuppressWarnings("UnusedParameters")
    public void onNewIntent(Intent intent) {
        Injecter.doInjectExtra(this);
        LifeCycleInjecter.injectonNewIntent(this, intent);
    }
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

    /**
     * 是否正在显示进度对话框
     */
    public boolean isProgressDialogShowing() {
        return dialogBuilder != null && dialogBuilder.isProgressDialogShowing();
    }

    //</editor-fold>

}
