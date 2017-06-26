package com.andpack.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfFragmentActivity;
import com.andframe.annotation.pager.BindLaunchMode;
import com.andframe.api.pager.Pager;
import com.andframe.application.AfApp;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.util.java.AfReflecter;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

import java.util.HashMap;
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
    protected void onCreated(Bundle bundle) {
        mApHelper.onCreate();
        super.onCreated(bundle);
    }

    @Override
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @CallSuper
    public void onViewCreated() {
        mApHelper.onViewCreated();
        super.onViewCreated();
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
        mApHelper.onPostCreate(savedInstanceState);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mApHelper.finish()) {
            return;
        }
        super.finish();
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(this, clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(this, clazz, request, args);
    }

    @Override
    public void postEvent(Object event) {
        mApHelper.postEvent(event);
    }

    //<editor-fold desc="跳转封装">
    public static void  start(Pager pager, Class<? extends Fragment> clazz, Object... params){
        Context context = (pager instanceof Activity) ? (Activity) pager : (pager == null ? null : pager.getContext());
        if (context != null) {
            context.startActivity(newIntent(clazz, context,params));
        } else {
            AfApp app = AfApp.get();
            app.startActivity(newIntent(clazz, app,params).newTask());
        }
    }

    public static void startResult(Pager pager, Class<? extends Fragment> clazz,int request, Object... params){
        if (pager instanceof Activity) {
            Activity activity = (Activity) pager;
            activity.startActivityForResult(newIntent(clazz, activity,params), request);
        } else if(pager instanceof Fragment) {
            Fragment fragment = (Fragment) pager;
            fragment.startActivityForResult(newIntent(clazz, fragment.getContext(),params), request);
        }
    }

    private static AfIntent newIntent(Class<? extends Fragment> clazz, Context context, Object... params) {
        return new AfIntent(context,getActivityClazz(clazz))
                .putKeyVaules(EXTRA_FRAGMENT,clazz.getName())
                .putKeyVaules(params);
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
