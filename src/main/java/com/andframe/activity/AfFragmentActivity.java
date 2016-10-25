package com.andframe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.andframe.annotation.MustLogined;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.widget_frame;

/**
 * 抽象通用页面基类
 * Created by Administrator on 2016/3/1.
 */
@SuppressWarnings("unused")
public class AfFragmentActivity extends AfActivity {

    protected static final String EXTRA_FRAGMENT = "EXTRA_FRAGMENT";
    protected static Class<? extends AfFragmentActivity> activityClazz = AfFragmentActivity.class;

    private Fragment mFragment;

    public static void startFragment(Class<? extends Fragment> clazz, Object... params){
//        mFragmentClazz = clazz;
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivity(activityClazz, list.toArray());
        }
    }

    public static void startFragmentForResult(Class<? extends Fragment> clazz,int request, Object... params){
//        mFragmentClazz = clazz;
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivityForResult(activityClazz, request, list.toArray());
        }
    }

    //<editor-fold desc=方法">
    @InjectExtra(value = EXTRA_FRAGMENT,remark = "Fragment类名")
    protected String mFragmentClazz = null;

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) {
        super.onCreate(bundle, intent);
        checkMustLoginedOnCreate();
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(widget_frame);
        setContentView(frameLayout);
        replaceFragment();
    }

    //</editor-fold>


    //<editor-fold desc="事件转发">
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode>>16 > 0 && mFragment != null) {
            mFragment.onActivityResult(requestcode, resultcode, data);
        }
    }
    //</editor-fold>

    //<editor-fold desc="登录检测">

    protected static final int REQUSET_LOGIN = 10;

    /**
     * 在创建页面的时候检测是否要求登录
     */

    protected void checkMustLoginedOnCreate() {
        try {
            Class<?> fragment = Class.forName(mFragmentClazz);
            MustLogined must = AfReflecter.getAnnotation(fragment, Fragment.class, MustLogined.class);
            if (must != null && !AfApp.get().isUserLogined()) {
                interruptReplaceFragment = true;
                startLoginPager(must);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动指定的登录页面
     */
    protected void startLoginPager(MustLogined must) {
        if (Activity.class.isAssignableFrom(must.value())) {
            startActivityForResult(new Intent(this,must.value()), REQUSET_LOGIN);
        } else if (Fragment.class.isAssignableFrom(must.value())) {
            //noinspection unchecked
            startFragmentForResult((Class<? extends Fragment>)must.value(), REQUSET_LOGIN);
        }
        makeToastShort(must.remark());
    }

    @Override
    protected void onActivityResult(AfIntent intent, int requestcode, int resultcode) throws Exception {
        super.onActivityResult(intent, requestcode, resultcode);
        if (requestcode == REQUSET_LOGIN) {
            if (AfApp.get().isUserLogined()) {
                interruptReplaceFragment = false;
                replaceFragment();
            } else {
                finish();
            }
        }
    }

    protected boolean interruptReplaceFragment = false;

    protected void replaceFragment() {
        if (!interruptReplaceFragment) {
            try {
                mFragment = (Fragment)Class.forName(mFragmentClazz).newInstance();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(widget_frame, mFragment);
                transaction.commit();
            } catch (Exception e) {
                AfExceptionHandler.handle(e, "AfFragmentActivity Fragment 类型错误：" + mFragmentClazz);
            }
        }
    }
    //</editor-fold>
}
