package com.andframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andframe.$;
import com.andframe.annotation.BindMustLogin;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.pager.BindLaunchMode;
import com.andframe.annotation.pager.BindMainPager;
import com.andframe.api.pager.Pager;
import com.andframe.application.AfApp;
import com.andframe.application.AppSettings;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.util.java.AfReflecter;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.widget_frame;

/**
 * 抽象通用页面基类
 * Created by Administrator on 2016/3/1.
 */
@SuppressWarnings("unused")
public class AfFragmentActivity extends AfActivity {

    public static final String EXTRA_FRAGMENT = "EXTRA_FRAGMENT";

    protected Fragment mFragment;
    protected BindMainPager mBindMainPager;

    //<editor-fold desc="跳转封装">
    public static void start(Pager pager, Class<? extends Fragment> clazz, Object... params){
        Context context = (pager instanceof Activity) ? (Activity) pager : (pager == null ? null : pager.getContext());
        if (context != null) {
            context.startActivity(newIntent(clazz, context, params));
        } else {
            AfApp app = AfApp.get();
            app.startActivity(newIntent(clazz, app, params).newTask());
        }
    }
    public static void startResult(Pager pager, Class<? extends Fragment> clazz,int request, Object... params){
        if (pager instanceof Activity) {
            Activity activity = (Activity) pager;
            activity.startActivityForResult(newIntent(clazz, activity, params), request);
        } else if(pager instanceof Fragment) {
            Fragment fragment = (Fragment) pager;
            fragment.startActivityForResult(newIntent(clazz, fragment.getContext(), params), request);
        }
    }
    private static AfIntent newIntent(Class<? extends Fragment> clazz, Context context, Object... params) {
        return new AfIntent(context,getActivityClazz(clazz))
                .put(EXTRA_FRAGMENT,clazz.getName())
                .putKeyVaules(params);
    }
    //</editor-fold>

    //<editor-fold desc=方法">
    @InjectExtra(value = EXTRA_FRAGMENT,remark = "Fragment类名")
    protected String mFragmentClazz = null;

    @Override
    protected void onCreated(Bundle bundle) {
        super.onCreated(bundle);
        checkMainPager();
        checkMustLoginOnCreate();
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(widget_frame);
        setContentView(frameLayout);
        if (bundle == null) {
            replaceFragment();
        }
    }

    //</editor-fold>


    //<editor-fold desc="事件转发">
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode < 0xFFFF && mFragment != null) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    //</editor-fold>


    //<editor-fold desc="主页相关信息">
    protected long mExitTime;
    protected long mExitInterval = 2000;
    protected boolean mDoubleBackKeyPressed = true;

    protected void checkMainPager() {
        Class<?> fragment = getFragmentClazz();
        mBindMainPager = AfReflecter.getAnnotation(fragment, Fragment.class, BindMainPager.class);
        if (mBindMainPager != null) {
            if (AppSettings.getInstance().isAutoUpdate()) {
                $.update().checkUpdate();
            }
        }
    }

    @Override
    protected boolean onBackKeyPressed() {
        if (mBindMainPager != null) {
            boolean isHandled = super.onBackKeyPressed();
            if (!isHandled && mDoubleBackKeyPressed) {
                isHandled = true;
                if ((System.currentTimeMillis() - mExitTime) > mExitInterval) {
                    toast("再按一次退出");
                    mExitTime = System.currentTimeMillis();
                } else {
                    this.finish();
                }
            }
            return isHandled;
        }
        return super.onBackKeyPressed();
    }


    //</editor-fold>

    //<editor-fold desc="登录检测">

    protected static final int REQUEST_LOGIN = 0xFFFF;

    /**
     * 在创建页面的时候检测是否要求登录
     */

    protected void checkMustLoginOnCreate() {
        Class<?> fragment = getFragmentClazz();
        BindMustLogin must = AfReflecter.getAnnotation(fragment, Fragment.class, BindMustLogin.class);
        if (must != null && !AfApp.get().isUserLoggedIn()) {
            interruptReplaceFragment = true;
            startLoginPager(must);
        }
    }

    //<editor-fold desc="反射缓存">
    private static Map<String, Class> typeCache = new HashMap<>();
    private static Map<String, String> nameCache = new HashMap<>();
    private Class<?> getFragmentClass() throws ClassNotFoundException {
        Class type = typeCache.get(mFragmentClazz);
        if (type == null) {
            typeCache.put(mFragmentClazz, type = Class.forName(mFragmentClazz));
        }
        return type;
    }

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
                    activityClazz = AfFragmentActivity.class;
                    break;
                case singleTop:
                    activityClazz = AfFragmentSingleTopActivity.class;
                    break;
                case singleInstance:
                    activityClazz = AfFragmentSingleInstanceActivity.class;
                    break;
                case singleTask:
                    activityClazz = AfFragmentSingleTaskActivity.class;
                    break;
            }
        } else {
            activityClazz = AfFragmentActivity.class;
        }
        modelCache.put(clazz, activityClazz);
        return activityClazz;
    }
    //</editor-fold>

    /**
     * 启动指定的登录页面
     */
    protected void startLoginPager(BindMustLogin must) {
        if (Activity.class.isAssignableFrom(must.value())) {
            startActivityForResult(new Intent(this,must.value()), REQUEST_LOGIN);
        } else if (Fragment.class.isAssignableFrom(must.value())) {
            //noinspection unchecked
            startFragmentForResult((Class<? extends Fragment>)must.value(), REQUEST_LOGIN);
        }
        toast(must.remark());
    }

    @Override
    protected void onActivityResult(AfIntent intent, int requestCode, int resultCode) throws Exception {
        super.onActivityResult(intent, requestCode, resultCode);
        if (requestCode == REQUEST_LOGIN) {
            if (AfApp.get().isUserLoggedIn()) {
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
                mFragment = (Fragment) getFragmentClazz().newInstance();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(widget_frame, mFragment);
                transaction.commit();
            } catch (Throwable e) {
                $.error().handle(e, "AfFragmentActivity Fragment 类型错误：" + mFragmentClazz);
            }
        }
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public Class<?> getFragmentClazz() {
        try {
            return getFragmentClass();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String getFragmentName() {
        if (nameCache.containsKey(mFragmentClazz)) {
            return nameCache.get(mFragmentClazz);
        }
        if (mFragmentClazz.endsWith("Fragment")) {
            String name = mFragmentClazz.substring(mFragmentClazz.lastIndexOf('.') + 1, mFragmentClazz.length());
            nameCache.put(mFragmentClazz, name);
            return name;
        }
        if (mFragment != null) {
            View view = mFragment.getView();
            if (view != null) {
                Toolbar toolbar = $.query(view).query(Toolbar.class).view();
                if (toolbar != null) {
                    CharSequence title = toolbar.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        nameCache.put(mFragmentClazz, title.toString());
                        return title.toString();
                    }
                    title = $.query(toolbar).query(TextView.class).text();
                    if (!TextUtils.isEmpty(title)) {
                        nameCache.put(mFragmentClazz, title.toString());
                        return title.toString();
                    }
                }
            }
        }
        Class type = typeCache.get(mFragmentClazz);
        if (type == null) {
            try {
                typeCache.put(mFragmentClazz, type = Class.forName(mFragmentClazz));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (type != null) {
            Context context = getContext();
            context = context == null ? AfApp.get() : context;
            int id = LayoutBinder.getBindLayoutId(type, context);
            String name = context.getResources().getResourceName(id);
            if (!TextUtils.isEmpty(name)) {
                nameCache.put(mFragmentClazz, name);
                return name;
            }
        }
        nameCache.put(mFragmentClazz, mFragmentClazz);
        return mFragmentClazz;
    }
    //</editor-fold>
}
