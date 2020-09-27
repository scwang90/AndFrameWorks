package com.andframe.impl.pager;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andframe.activity.AfActivity;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.api.pager.Pager;
import com.andframe.api.pager.PagerManager;
import com.andframe.application.AfApp;
import com.andframe.exception.AfException;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.wrapper.PagerWrapper;

import java.util.Stack;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

public class AfPagerManager implements PagerManager {

    //<editor-fold desc="功能实现">
    // 当前主页面
    private Stack<Activity> mStackActivity = new Stack<>();

    public AfPagerManager() {
    }

    @Override@CallSuper
    public void onActivityCreated(Activity activity) {
        if (!mStackActivity.contains(activity)) {
            mStackActivity.push(activity);
        }
    }

    @Override@CallSuper
    public void onActivityDestroy(Activity activity) {
        if (mStackActivity.contains(activity)) {
            mStackActivity.remove(activity);
        }
    }

    @Override
    public void onActivityResume(Activity activity) {

    }

    @Override@CallSuper
    public void onActivityPause(Activity activity) {
        if (activity.isFinishing()) {
            if (mStackActivity.contains(activity)) {
                mStackActivity.remove(activity);
            }
        }
    }

    @Override
    public void onActivityStart(Activity activity) {

    }

    @Override
    public void onActivityRestart(Activity activity) {

    }

    @Override
    public void onActivityStop(Activity activity) {
    }

    @Override
    public void onFragmentCreate(AfFragment fragment) {

    }

    @Override
    public void onFragmentAttach(AfFragment fragment, Context context) {

    }

    @Override
    public void onFragmentDetach(AfFragment fragment) {

    }

    @Override
    public void onFragmentResume(AfFragment fragment) {

    }

    @Override
    public void onFragmentPause(AfFragment fragment) {

    }

    @Override
    public void onFragmentStart(AfFragment fragment) {

    }

    @Override
    public void onFragmentStop(AfFragment fragment) {

    }

    @Override
    public boolean hasActivityRunning() {
        return !mStackActivity.isEmpty();
    }

    @Override
    public boolean hasActivity(Class<? extends Activity> clazz) {
        for (Activity activity : mStackActivity) {
            if (activity.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPager(Class<? extends Pager> pager) {
        for (Activity activity : mStackActivity) {
            if (pager.isAssignableFrom(activity.getClass())) {
                return true;
            } else if (activity instanceof AfFragmentActivity) {
                if (pager.isAssignableFrom(((AfFragmentActivity) activity).getFragmentClazz())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Activity currentActivity() {
        System.out.println(this + " currentActivity - size = " + mStackActivity.size());
        if (mStackActivity.isEmpty()) {
            return null;
        }
        return mStackActivity.peek();
    }

    @Nullable
    @Override
    public Pager currentPager() {
        System.out.println(this + " currentPager - size = " + mStackActivity.size());
        if (mStackActivity.isEmpty()) {
            return null;
        }
        Activity activity = mStackActivity.peek();
        if (activity instanceof AfFragmentActivity) {
            Fragment fragment = ((AfFragmentActivity) activity).getFragment();
            if (fragment instanceof Pager) {
                return (Pager) fragment;
            }
        }

        if (activity instanceof AfActivity) {
            return (AfActivity)activity;
        } else if (activity != null) {
            return new PagerWrapper(activity);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends Pager> T getPager(Class<T> clazz) {
        for (int i = mStackActivity.size() ; i > 0; i--) {
            Activity activity = mStackActivity.get(i - 1);
            if (clazz.isAssignableFrom(activity.getClass())) {
                return clazz.cast(activity);
            } else if (activity instanceof AfFragmentActivity) {
                Fragment fragment = ((AfFragmentActivity) activity).getFragment();
                if (clazz.isAssignableFrom(fragment.getClass())) {
                    return clazz.cast(fragment);
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends Activity> T getActivity(Class<T> clazz) {
        for (Activity activity : mStackActivity) {
            if (clazz.isAssignableFrom(activity.getClass())) {
                return clazz.cast(activity);
            }
        }
        return null;
    }


    @Nullable
    @Override
    public <T extends Fragment> T getFragment(Class<T> clazz) {
        for (Activity activity : mStackActivity) {
            if (activity instanceof AfFragmentActivity) {
                Fragment fragment = ((AfFragmentActivity) activity).getFragment();
                if (clazz.isAssignableFrom(fragment.getClass())) {
                    return clazz.cast(fragment);
                }
            }
        }
        return null;
    }

    @Override
    public boolean finishBatchUntil(Pager pager) {
        while (!mStackActivity.empty()) {
            Activity activity = mStackActivity.peek();
            if (activity == pager) {
                return true;
            } else if (activity instanceof AfFragmentActivity) {
                Fragment fragment = ((AfFragmentActivity) activity).getFragment();
                if (pager == fragment) {
                    return true;
                }
            }
            mStackActivity.pop().finish();
        }
        return false;
    }

    @Override
    public boolean finishBatchUntil(Class<? extends Pager> pager) {
        while (!mStackActivity.empty()) {
            Activity activity = mStackActivity.peek();
            if (pager.isAssignableFrom(activity.getClass())) {
                return true;
            } else if (activity instanceof AfFragmentActivity) {
                if (pager.isAssignableFrom(((AfFragmentActivity) activity).getFragmentClazz())) {
                    return true;
                }
            }
            mStackActivity.pop().finish();
        }
        startPager(pager);
        return false;
    }

    @Override
    public void finishCurrentActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void finishActivity(Activity activity) {
        if (activity != null && mStackActivity.contains(activity)) {
            activity.finish();
        }
    }

    @Override
    public void finishAllActivity() {
        for (int i = 0, size = mStackActivity.size(); i < size; i++) {
            if (null != mStackActivity.get(i)) {
                mStackActivity.get(i).finish();
            }
        }
    }

    @Override
    public void startForeground() {
        throw new AfException("如要使用startForeground功能，请自行继承" + getClass().getSimpleName() + "并实现startForeground");
    }

    @Override
    public void startForeground(Class<? extends Activity> clazz) {
        Activity lastActivity = null;
        while (mStackActivity.size() > 0) {
            lastActivity = mStackActivity.peek();
            if (clazz.isAssignableFrom(lastActivity.getClass())) {
                return;
            }
            mStackActivity.pop().finish();
        }
        if (lastActivity instanceof AfActivity) {
            ((AfActivity) lastActivity).startActivity(clazz);
        }
        if (lastActivity != null) {
            lastActivity.startActivity(new Intent(lastActivity, clazz));
        } else {
            startActivity(clazz);
        }
    }


    @Override
    public void startActivity(Class<? extends Activity> clazz, Object... args) {
        Activity activity = currentActivity();
        if (activity instanceof AfActivity) {
            ((AfActivity) activity).startActivity(clazz, args);
        } else if (activity != null) {
            activity.startActivity(new AfIntent(activity, clazz, args));
        } else {
            AfApp app = AfApp.get();
            AfIntent intent = new AfIntent(app, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putKeyVaules(args);
            app.startActivity(intent);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            startFragment(clazz,args);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            startActivity(clazz, args);
        } else if (Service.class.isAssignableFrom(clazz)) {
            startService(clazz, args);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void startService(Class<? extends Service> clazz, Object... args) {
        Activity activity = currentActivity();
        if (activity instanceof AfActivity) {
            ((AfActivity) activity).startService(clazz, args);
        } else if (activity != null) {
            activity.startService(new AfIntent(activity, clazz, args));
        } else {
            AfApp app = AfApp.get();
            app.startService(new AfIntent(app, clazz, args));
        }
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        Activity activity = currentActivity();
        if (activity instanceof AfActivity) {
            ((AfActivity) activity).startFragment(clazz, args);
        } else if (activity != null) {
            AfFragmentActivity.start(null, clazz, args);
        } else {
            AfFragmentActivity.start(null, clazz, args);
        }
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
        Activity activity = currentActivity();
        if (activity instanceof AfFragmentActivity) {
            ((AfFragmentActivity) activity).getFragment().startActivityForResult(new AfIntent(activity,clazz).putKeyVaules(args), request);
        } else if (activity instanceof AfActivity) {
            ((AfActivity) activity).startActivityForResult(clazz, request, args);
        } else if (activity != null) {
            activity.startActivityForResult(new AfIntent(activity, clazz, args), request);
        }
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        Activity activity = currentActivity();
        if (activity instanceof AfActivity) {
            ((AfActivity) activity).startFragmentForResult(clazz, request, args);
        }
    }

    //</editor-fold>
}
