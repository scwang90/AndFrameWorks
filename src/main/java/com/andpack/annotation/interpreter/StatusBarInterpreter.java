package com.andpack.annotation.interpreter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import com.andframe.$;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.api.pager.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.statusbar.StatusBarMargin;
import com.andpack.annotation.statusbar.StatusBarMarginS;
import com.andpack.annotation.statusbar.StatusBarMarginType;
import com.andpack.annotation.statusbar.StatusBarPadding;
import com.andpack.annotation.statusbar.StatusBarPaddingS;
import com.andpack.annotation.statusbar.StatusBarPaddingType;
import com.andpack.annotation.statusbar.StatusBarTranslucent;
import com.andpack.annotation.statusbar.StatusBarTranslucentDark;
import com.andpack.util.ApStatusBarUtil;

/**
 * 系统任务栏透明 注解 实现类
 * Created by SCWANG on 2016/9/8.
 */
public class StatusBarInterpreter {

    public static boolean interpreter(Pager pager) {
        boolean ret = false;
        Activity activity = pager.getActivity();
        Resources resources = activity.getResources();
        StatusBarTranslucent translucent = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarTranslucent.class);
        if (translucent != null) {
            ret = true;
            ApStatusBarUtil.immersive(activity, resources.getColor(translucent.color()), translucent.value());
        }
        StatusBarTranslucentDark translucentDark = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarTranslucentDark.class);
        if (translucentDark != null) {
            ret = true;
            ApStatusBarUtil.darkMode(activity, resources.getColor(translucentDark.color()), translucentDark.value());
        }
        StatusBarPadding padding = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPadding.class);
        if (padding != null) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.query(pager).$(null, padding.value()).foreach((ViewQuery.ViewEacher<View>) view -> ApStatusBarUtil.setPaddingSmart(activity,view));
        }
        StatusBarPaddingType paddingType = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingType.class);
        if (paddingType != null && paddingType.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.query(pager).$(paddingType.value()).foreach((ViewQuery.ViewEacher<View>) view -> ApStatusBarUtil.setPaddingSmart(activity,view));
        }
        StatusBarPaddingS paddingS = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingS.class);
        if (paddingS != null && paddingS.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.query(pager).$(null,paddingS.value()).foreach((ViewQuery.ViewEacher<View>)view ->ApStatusBarUtil.setPaddingSmart(activity,view));
        }
        StatusBarMargin margin = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarMargin.class);
        if (margin != null && margin.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.query(pager).$(null, margin.value()).foreach((ViewQuery.ViewEacher<View>) view -> ApStatusBarUtil.setMargin(activity,view));
        }
        StatusBarMarginType marginType = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarMarginType.class);
        if (marginType != null && marginType.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.query(pager).$(marginType.value()).foreach((ViewQuery.ViewEacher<View>) view -> ApStatusBarUtil.setMargin(activity,view));
        }
        StatusBarMarginS marginS = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarMarginS.class);
        if (marginS != null && marginS.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.query(pager).$(null, marginS.value()).foreach((ViewQuery.ViewEacher<View>)view -> ApStatusBarUtil.setMargin(activity,view));
        }
        return ret;
    }

    private static void defaultTranslucent(Pager pager, StatusBarTranslucent translucent, StatusBarTranslucentDark translucentDark) {
        if (translucent == null && translucentDark == null && (pager instanceof Activity || pager.getActivity() instanceof AfFragmentActivity)) {
            ApStatusBarUtil.immersive(pager.getActivity());
        }
    }

}
