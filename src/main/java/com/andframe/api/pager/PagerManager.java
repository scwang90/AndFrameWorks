package com.andframe.api.pager;

import android.content.Context;

import com.andframe.activity.AfActivity;
import com.andframe.fragment.AfFragment;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

public interface PagerManager {

    void onActivityCreated(AfActivity activity);
    void onActivityDestroy(AfActivity activity);
    void onActivityResume(AfActivity activity);
    void onActivityPause(AfActivity activity);

    void onFragmentAttach(AfFragment fragment, Context context);
    void onFragmentDetach(AfFragment fragment);
    void onFragmentResume(AfFragment fragment);
    void onFragmentPause(AfFragment fragment);

    boolean hasActivityRuning();
    boolean hasActivity(Class<? extends AfActivity> clazz);

    AfActivity currentActivity();
    AfActivity getActivity(Class<? extends AfActivity> clazz);

    void finishCurrentActivity();
    void finishActivity(AfActivity activity);
    void finishAllActivity();
}
