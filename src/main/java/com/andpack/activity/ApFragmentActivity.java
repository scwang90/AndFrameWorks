package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.annotation.pager.BindLaunchMode;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.pager.AfPagerManager;
import com.andframe.util.java.AfReflecter;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
@SuppressWarnings("unused")
public class ApFragmentActivity extends AfFragmentActivity implements ApPager {

    protected ApPagerHelper mApHelper = new ApPagerHelper(this);

    @Override
    public void setTheme(@StyleRes int resid) {
        mApHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) {
        mApHelper.onCreate();
        super.onCreate(bundle, intent);
    }

    @Override
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @BindViewCreated@CallSuper
    protected void onViewCreated() {
        mApHelper.onViewCreated();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mApHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mApHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mApHelper.finish()) {
            return;
        }
        super.finish();
    }

    //<editor-fold desc="跳转封装">
    public static void start(Class<? extends Fragment> clazz, Object... params){
        AfActivity activity = AfPagerManager.getInstance().currentActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            (activity).startActivity(getActivityClazz(clazz), list.toArray());
        }
    }
    public static void startResult(Class<? extends Fragment> clazz,int request, Object... params){
        AfActivity activity = AfPagerManager.getInstance().currentActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            (activity).startActivityForResult(getActivityClazz(clazz), request, list.toArray());
        }
    }
    public static void startResult(Fragment fragment, Class<? extends Fragment> clazz,int request, Object... params){
        Context context = fragment.getContext();
        if (context != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            fragment.startActivityForResult(new AfIntent(context, getActivityClazz(clazz), list.toArray()), request);
        }
    }
    //</editor-fold>

    //<editor-fold desc="反射缓存">
    private static Map<Class, Class<? extends AfFragmentActivity>> modelCache = new HashMap<>();
    private static Class<? extends AfFragmentActivity> getActivityClazz(Class<? extends Fragment> clazz) {
        Class<? extends AfFragmentActivity> activityClazz = modelCache.get(clazz);
        if (activityClazz != null) {
            return activityClazz;
        }
        BindLaunchMode annotation = AfReflecter.getAnnotation(clazz, AfFragment.class, BindLaunchMode.class);
        if (annotation != null) {
            switch (annotation.value()) {
                case standard:
                    activityClazz = ApFragmentActivity.class;
                    break;
                case singleTop:
                    activityClazz = ApFragmentSingleTopActivity.class;
                    break;
                case singleInstance:
                    activityClazz = ApFragmentSingleInstanceActivity.class;
                    break;
                case singleTask:
                    activityClazz = ApFragmentSingleTaskActivity.class;
                    break;
            }
        } else {
            activityClazz = ApFragmentActivity.class;
        }
        modelCache.put(clazz, activityClazz);
        return activityClazz;
    }
    //</editor-fold>

}
