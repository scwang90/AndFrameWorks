package com.andpack.annotation.interpreter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.andframe.$;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.api.pager.Pager;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.ViewQuery.ViewIterator;
import com.andpack.annotation.statusbar.StatusBarMargin;
import com.andpack.annotation.statusbar.StatusBarMarginType;
import com.andpack.annotation.statusbar.StatusBarPadding;
import com.andpack.annotation.statusbar.StatusBarPaddingType;
import com.andpack.annotation.statusbar.StatusBarTranslucent;
import com.andpack.annotation.statusbar.StatusBarTranslucentDark;
import com.andpack.annotation.statusbar.idname.StatusBarMargin$;
import com.andpack.annotation.statusbar.idname.StatusBarPadding$;
import com.andpack.application.ApApp;
import com.andpack.util.ApStatusBarUtil;

import java.util.Map;

import static com.andframe.annotation.interpreter.ReflecterCacher.getStopType;
import static com.andframe.util.java.AfReflecter.getAnnotation;
import static com.andpack.util.ApStatusBarUtil.setMargin;
import static com.andpack.util.ApStatusBarUtil.setPaddingSmart;

/**
 * 系统任务栏透明 注解 实现类
 * Created by SCWANG on 2016/9/8.
 */
public class StatusBarInterpreter {

    private static Map<Class, StatusBar> statusBarMap = new ArrayMap<>();

    public static boolean interpreter(Pager pager) {
        boolean ret = false;
        Activity activity = pager.getActivity();
        Resources resources = activity.getResources();
        StatusBar statusBar = getStatusBar(pager);

        if (statusBar.translucent != null) {
            ret = true;
            ApStatusBarUtil.immersive(activity, resources.getColor(statusBar.translucent.color()), statusBar.translucent.value());
        } else if (statusBar.translucentDark != null) {
            ret = true;
            ApStatusBarUtil.darkMode(activity, resources.getColor(statusBar.translucentDark.color()), statusBar.translucentDark.value());
        }
        if (statusBar.padding != null) {
            ret = true;
            defaultTranslucent(pager, statusBar.translucent, statusBar.translucentDark);
            $.query(pager).query(null, statusBar.padding.value()).foreach((ViewQuery.ViewIterator<View>) view -> setPaddingSmart(activity,view));
        }
        if (statusBar.paddingS != null) {
            ret = true;
            defaultTranslucent(pager, statusBar.translucent, statusBar.translucentDark);
            $.query(pager).query(null, statusBar.paddingS.value()).foreach((ViewQuery.ViewIterator<View>) view -> setPaddingSmart(activity,view));
        }
        if (statusBar.paddingType != null && statusBar.paddingType.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, statusBar.translucent, statusBar.translucentDark);
            $.query(pager).query(statusBar.paddingType.value()).foreach((ViewQuery.ViewIterator<View>) view -> setPaddingSmart(activity,view));
        }
        if (statusBar.margin != null && statusBar.margin.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, statusBar.translucent, statusBar.translucentDark);
            $.query(pager).query(null, statusBar.margin.value()).foreach((ViewQuery.ViewIterator<View>) view -> setMargin(activity,view));
        }
        if (statusBar.marginType != null && statusBar.marginType.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, statusBar.translucent, statusBar.translucentDark);
            $.query(pager).query(statusBar.marginType.value()).foreach((ViewIterator<View>) view -> setMargin(activity,view));
        }
        if (statusBar.marginS != null && statusBar.marginS.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, statusBar.translucent, statusBar.translucentDark);
            $.query(pager).query(null, statusBar.marginS.value()).foreach((ViewQuery.ViewIterator<View>) view -> setMargin(activity,view));
        }
        return ret;
    }

    private static StatusBar getStatusBar(Pager pager) {
        StatusBar statusBar = statusBarMap.get(pager.getClass());
        if (statusBar == null) {
            statusBar = new StatusBar(pager);
            statusBarMap.put(pager.getClass(), statusBar);
        }
        return statusBar;
    }

    private static StatusBarTranslucentDark getStatusBarTranslucentDarkAnnotation(Pager pager) {
        Activity activity = pager.getActivity();
        if (pager instanceof Fragment && !(activity instanceof AfFragmentActivity)) {
            StatusBarTranslucentDark translucent = getAnnotation(pager.getClass(), getStopType(pager), StatusBarTranslucentDark.class);
            if (translucent != null) {
                return translucent;
            }
        }
        return getAnnotation(pager.getClass(), getStopType(pager), StatusBarTranslucentDark.class);
    }

    private static StatusBarTranslucent getStatusBarTranslucentAnnotation(Pager pager) {
        Activity activity = pager.getActivity();
        if (pager instanceof Fragment && !(activity instanceof AfFragmentActivity)) {
            StatusBarTranslucent translucent = getAnnotation(pager.getClass(), getStopType(pager), StatusBarTranslucent.class);
            if (translucent != null) {
                return translucent;
            }
        }
        return getAnnotation(pager.getClass(), getStopType(pager), StatusBarTranslucent.class);
    }

    private static void defaultTranslucent(Pager pager, StatusBarTranslucent translucent, StatusBarTranslucentDark translucentDark) {
        if (translucent == null && translucentDark == null && (pager instanceof Activity || pager.getActivity() instanceof AfFragmentActivity)) {
            translucent = ApApp.getApp().defaultStatusBarTranslucent();
            ApStatusBarUtil.immersive(pager.getActivity(), ContextCompat.getColor(pager.getContext(), translucent.color()), translucent.value());
        }
    }

    private static class StatusBar {
        private StatusBarTranslucent translucent;
        private StatusBarTranslucentDark translucentDark;
        private StatusBarPadding padding;
        private StatusBarPadding$ paddingS;
        private StatusBarPaddingType paddingType;
        private StatusBarMargin margin;
        private StatusBarMargin$ marginS;
        private StatusBarMarginType marginType;

        StatusBar(Pager pager) {
            translucent = getStatusBarTranslucentAnnotation(pager);
            translucentDark = getStatusBarTranslucentDarkAnnotation(pager);
            padding = getAnnotation(pager.getClass(), getStopType(pager), StatusBarPadding.class);
            paddingS = getAnnotation(pager.getClass(), getStopType(pager), StatusBarPadding$.class);
            paddingType = getAnnotation(pager.getClass(), getStopType(pager), StatusBarPaddingType.class);
            margin = getAnnotation(pager.getClass(), getStopType(pager), StatusBarMargin.class);
            marginS = getAnnotation(pager.getClass(), getStopType(pager), StatusBarMargin$.class);
            marginType = getAnnotation(pager.getClass(), getStopType(pager), StatusBarMarginType.class);
        }

    }
}
