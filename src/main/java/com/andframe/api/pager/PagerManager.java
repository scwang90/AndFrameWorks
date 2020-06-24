package com.andframe.api.pager;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.andframe.fragment.AfFragment;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

@SuppressWarnings("unused")
public interface PagerManager {

    void onActivityCreated(Activity activity);
    void onActivityDestroy(Activity activity);
    void onActivityResume(Activity activity);
    void onActivityPause(Activity activity);
    void onActivityStop(Activity activity);
    void onActivityRestart(Activity activity);
    void onActivityStart(Activity activity);

    void onFragmentCreate(AfFragment fragment);
    void onFragmentAttach(AfFragment fragment, Context context);
    void onFragmentDetach(AfFragment fragment);
    void onFragmentResume(AfFragment fragment);
    void onFragmentPause(AfFragment fragment);
    void onFragmentStart(AfFragment fragment);
    void onFragmentStop(AfFragment fragment);

    boolean hasActivityRunning();
    boolean hasActivity(Class<? extends Activity> clazz);
    boolean hasPager(Class<? extends Pager> clazz);

    @Nullable
    Pager currentPager();
    @Nullable
    Activity currentActivity();
    @Nullable
    <T extends Pager> T getPager(Class<T> clazz);
    @Nullable
    <T extends Activity> T getActivity(Class<T> clazz);
    @Nullable
    <T extends Fragment> T getFragment(Class<T> clazz);

    void finishCurrentActivity();
    void finishActivity(Activity activity);
    void finishAllActivity();

    /**
     * 隐式打开主页（需要子类自己实现）
     */
    void startForeground();

    /**
     * 显示打开主页
     */
    void startForeground(Class<? extends Activity> clazz);

    boolean startPager(Class clazz, Object... args);
    void startService(Class<? extends Service> clazz, Object... args);
    void startActivity(Class<? extends Activity> clazz, Object... args);
    void startFragment(Class<? extends Fragment> clazz, Object... args);
    void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args);
    void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args);


    void finishBatchUntil(Pager pager);
    void finishBatchUntil(Class<? extends Pager> pager);

}
