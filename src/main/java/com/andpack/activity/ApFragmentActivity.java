package com.andpack.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.application.AfApp;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.MustLogined;
import com.andpack.api.ApPager;
import com.andpack.application.ApApp;
import com.andpack.impl.ApPagerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
@SuppressWarnings("unused")
public class ApFragmentActivity extends AfFragmentActivity implements ApPager {

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    static {
        activityClazz = ApFragmentActivity.class;
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        mHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        mHelper.onCreate();
        checkMustLoginedOnCreate();
        super.onCreate(bundle, intent);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mHelper.finish()) {
            return;
        }
        super.finish();
    }

    //<editor-fold desc="下拉刷新">
    @Override
    public boolean onMore() {
        return false;
    }

    @Override
    public boolean onRefresh() {
        return false;
    }
    //</editor-fold>


    //<editor-fold desc="FragmentActivity启动">
    public static void start(Class<? extends Fragment> clazz, Object... params){
        startFragment(clazz, params);
    }
    public static void startResult(Class<? extends Fragment> clazz,int request, Object... params){
        startFragmentForResult(clazz, request, params);
    }
    public static void startFragment(Class<? extends Fragment> clazz, Object... params){
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivity(ApFragmentActivity.class, list.toArray());
        }
    }
    public static void startFragmentForResult(Class<? extends Fragment> clazz,int request, Object... params){
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivityForResult(activityClazz, request, list.toArray());
        }
    }
    //</editor-fold>


    //<editor-fold desc="登录检测">

    protected static final int REQUSET_LOGIN = 10;

    /**
     * 在创建页面的时候检测是否要求登录
     */

    protected void checkMustLoginedOnCreate() throws Exception {
        Class<?> fragment = Class.forName(mFragmentClazz);
        MustLogined mustLogined = AfReflecter.getAnnotation(fragment, Fragment.class, MustLogined.class);
        if (mustLogined != null && !ApApp.getApApp().isUserLogined()) {
            interruptReplaceFragment = true;
            startLoginPager(mustLogined);
        }
    }

    /**
     * 启动指定的登录页面
     */
    protected void startLoginPager(MustLogined logined) {
        if (Activity.class.isAssignableFrom(logined.value())) {
            startActivityForResult(new Intent(this,logined.value()), REQUSET_LOGIN);
        } else if (Fragment.class.isAssignableFrom(logined.value())) {
            //noinspection unchecked
            startFragmentForResult((Class<? extends Fragment>)logined.value(), REQUSET_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(AfIntent intent, int requestcode, int resultcode) throws Exception {
        super.onActivityResult(intent, requestcode, resultcode);
        if (requestcode == REQUSET_LOGIN) {
            if (ApApp.getApApp().isUserLogined()) {
                interruptReplaceFragment = false;
                replaceFragment();
            } else {
                finish();
            }
        }
    }

    protected boolean interruptReplaceFragment = false;
    @Override
    protected void replaceFragment() throws Exception {
        if (!interruptReplaceFragment) {
            super.replaceFragment();
        }
    }
    //</editor-fold>

}
