package com.andpack.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.annotation.interpreter.ReflecterCacher;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfFragment;
import com.andframe.listener.SafeListener;
import com.andframe.util.java.AfReflecter;
import com.andpack.R;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.annotation.BackgroundTranslucent;
import com.andpack.annotation.RegisterEventBus;
import com.andpack.annotation.interpreter.StatusBarInterpreter;
import com.andpack.api.ApPager;
import com.andpack.application.ApApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import static android.support.v4.graphics.ColorUtils.setAlphaComponent;
import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApPagerHelper {

    protected ApPager pager;

    protected RegisterEventBus mEventBus;

    //<editor-fold desc="滑动关闭">
    private SwipeBackActivityHelper mSwipeBackHelper;
    protected boolean mIsUsingSwipeBack = false;
    //</editor-fold>

    public ApPagerHelper(ApPager pager) {
        this.pager = pager;
        initRegisterEventBus();
    }

    protected static Map<Class, Method[]> mEventBusMap = new HashMap<>();

    private void initRegisterEventBus() {
        Method[] methods = mEventBusMap.get(pager.getClass());
        if (methods == null) {
            methods = ReflecterCacher.getMethodAnnotation(pager, Subscribe.class);
            //methods = AfReflecter.getMethodAnnotation(pager.getClass(), getStopClass(), Subscribe.class);
            mEventBusMap.put(pager.getClass(), methods);
        }
        if (methods.length > 0) {
            for (Method method : methods) {
                int modifiers = method.getModifiers();
                if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
                    pager.makeToastShort("被标记[Subscribe]的方法必须是public并且不能是static");
                    return;
                }
            }
            mEventBus = new RegisterEventBus() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RegisterEventBus.class;
                }
            };
        }
    }

    @NonNull
    protected Class<?> getStopClass() {
        return pager instanceof Activity ? AfActivity.class : AfFragment.class;
    }

    public void setTheme(@StyleRes int resid) {
        mIsUsingSwipeBack = resid == R.style.AppTheme_SwipeBack;
    }

    public void onCreate() {
        if (mEventBus != null) {
            EventBus.getDefault().register(pager);
        }
//        if (mIsUsingSwipeBack) {
//            mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
//            mSwipeBackHelper.onActivityCreate();
//        }
    }

    public void onDestroy() {
        if (mEventBus != null) {
            EventBus.getDefault().unregister(pager);
        }
        if (ApApp.getApp().isDebug()) {
            ApApp.getApp().getRefWatcher().watch(this);
        }
    }

    public void onDestroyView() {
        if (mEventBus != null) {
            EventBus.getDefault().unregister(pager);
        }
    }

    public void onViewCreated() {
        try {
            StatusBarInterpreter.interpreter(pager);
            if (pager.getView() != null) {
                $.query(pager.getView())
                        .$(Toolbar.class)
                        .foreach(Toolbar.class, (ViewQuery.ViewEacher<Toolbar>)
                                view -> view.setNavigationOnClickListener(new SafeListener(
                                        (View.OnClickListener) v -> pager.getActivity().finish())));
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("ApPagerHelper.onViewCreated 失败"));
        }
    }

    public View findViewById(int id) {
        if (mSwipeBackHelper != null) {
            try {
                return mSwipeBackHelper.findViewById(id);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.findViewById 失败"));
            }
        }
        return null;
    }

    public void onPostCreate(Bundle bundle) {
        try {
            if (mIsUsingSwipeBack) {
                mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
                mSwipeBackHelper.onActivityCreate();
            }
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.onPostCreate();
                SwipeBackLayout layout = mSwipeBackHelper.getSwipeBackLayout();
                performBackgroundTranslucent(layout);
                ViewGroup root = (ViewGroup)pager.getActivity().getWindow().getDecorView();
                FrameLayout frame = new FrameLayout(pager.getContext());
                for (int i = 0; i < root.getChildCount(); i++) {
                    View child = root.getChildAt(i);
                    if (child != layout) {
                        root.removeViewAt(i--);
                        frame.addView(child);
                    }
                }
                if (frame.getChildCount() > 0) {
                    View view = layout.getChildAt(0);
                    layout.removeViewAt(0);
                    layout.addView(frame);
                    frame.addView(view, 0);
                    AfReflecter.setPreciseMemberByType(layout, frame, View.class, SwipeBackLayout.class);
                }
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.onPostCreate 失败"));
        }
    }

    private void performBackgroundTranslucent(SwipeBackLayout layout) {
        BackgroundTranslucent translucent;
        if (pager instanceof ApFragmentActivity) {
            Class<?> fragment = ((ApFragmentActivity) pager).getFragmentClazz();
            translucent = getAnnotation(fragment, Fragment.class, BackgroundTranslucent.class);
        } else {
            translucent = getAnnotation(pager.getClass(), getStopClass(), BackgroundTranslucent.class);
        }
        if (translucent != null && layout.getChildCount() > 0) {
            View view = layout.getChildAt(0);
            if (view != null) {
                view.setBackgroundColor(0);
            }
            int color = ContextCompat.getColor(layout.getContext(), translucent.color());
            layout.setBackgroundColor(setAlphaComponent(color, (int) (255 * translucent.value())));
        }
    }

    public boolean finish() {
        try {
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.getSwipeBackLayout().scrollToFinishActivity();
                mSwipeBackHelper = null;
                return true;
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.scrollToFinishActivity 失败"));
        }
        return false;
    }

    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }
}
