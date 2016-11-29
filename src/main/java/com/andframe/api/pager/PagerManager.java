package com.andframe.api.pager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

public interface PagerManager {

    void onActivityCreated(Activity activity);
    void onActivityDestroy(Activity activity);
    void onActivityResume(Activity activity);
    void onActivityPause(Activity activity);

    void onFragmentAttach(Fragment fragment, Context context);
    void onFragmentDetach(Fragment fragment);
    void onFragmentResume(Fragment fragment);
    void onFragmentPause(Fragment fragment);

    boolean hasActivityRuning();
    boolean hasActivity(Class<? extends Activity> clazz);

    Activity currentActivity();
    Activity getActivity(Class<? extends Activity> clazz);

    void finishCurrentActivity();
    void finishActivity(Activity activity);
    void finishAllActivity();
}
