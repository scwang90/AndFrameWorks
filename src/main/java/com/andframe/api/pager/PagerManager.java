package com.andframe.api.pager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.andframe.activity.AfActivity;
import com.andframe.fragment.AfFragment;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

@SuppressWarnings("unused")
public interface PagerManager {

    void onActivityCreated(AfActivity activity);
    void onActivityDestroy(AfActivity activity);
    void onActivityResume(AfActivity activity);
    void onActivityPause(AfActivity activity);
    void onActivityStop(AfActivity activity);
    void onActivityRestart(AfActivity activity);
    void onActivityStart(AfActivity activity);

    void onFragmentCreate(AfFragment fragment);
    void onFragmentAttach(AfFragment fragment, Context context);
    void onFragmentDetach(AfFragment fragment);
    void onFragmentResume(AfFragment fragment);
    void onFragmentPause(AfFragment fragment);
    void onFragmentStart(AfFragment fragment);
    void onFragmentStop(AfFragment fragment);

    boolean hasActivityRunning();
    boolean hasActivity(Class<? extends AfActivity> clazz);

    @Nullable
    AfActivity currentActivity();
    @Nullable
    AfActivity getActivity(Class<? extends AfActivity> clazz);
    @Nullable
    AfFragment getFragment(Class<? extends AfFragment> clazz);

    void finishCurrentActivity();
    void finishActivity(AfActivity activity);
    void finishAllActivity();

    /**
     * 隐式打开主页（需要子类自己实现）
     */
    void startForeground();

    /**
     * 显示打开主页
     */
    void startForeground(Class<? extends AfActivity> clazz);

    void startActivity(Class<? extends Activity> clazz, Object... args);
    void startFragment(Class<? extends Fragment> clazz, Object... args);
    void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args);
    void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args);


}
