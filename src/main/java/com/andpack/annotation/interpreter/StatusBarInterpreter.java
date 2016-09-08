package com.andpack.annotation.interpreter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import com.andframe.$;
import com.andframe.api.page.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.statusbar.StatusBarPadding;
import com.andpack.annotation.statusbar.StatusBarPaddingH;
import com.andpack.annotation.statusbar.StatusBarPaddingHS;
import com.andpack.annotation.statusbar.StatusBarPaddingHType;
import com.andpack.annotation.statusbar.StatusBarPaddingS;
import com.andpack.annotation.statusbar.StatusBarPaddingType;
import com.andpack.annotation.statusbar.StatusBarTranslucent;
import com.andpack.annotation.statusbar.StatusBarTranslucentDark;
import com.flyco.systembar.SystemBarHelper;

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
            SystemBarHelper.immersiveStatusBar(activity, translucent.value());
            SystemBarHelper.tintStatusBar(activity, resources.getColor(translucent.color()), translucent.value());
        }
        StatusBarTranslucentDark translucentDark = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarTranslucentDark.class);
        if (translucentDark != null) {
            ret = true;
            SystemBarHelper.setStatusBarDarkMode(activity);
            SystemBarHelper.tintStatusBar(activity, resources.getColor(translucentDark.color()), translucentDark.value());
        }
        StatusBarPadding padding = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPadding.class);
        if (padding != null) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.with(pager).$(padding.value()).foreach((ViewQuery.ViewEacher<View>)view -> SystemBarHelper.setPadding(activity,view));
        }
        StatusBarPaddingH paddingH = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingH.class);
        if (paddingH != null) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.with(pager).$(paddingH.value()).foreach((ViewQuery.ViewEacher<View>)view -> SystemBarHelper.setHeightAndPadding(activity,view));
        }
        StatusBarPaddingType paddingType = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingType.class);
        if (paddingType != null && paddingType.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.with(pager).$(null,paddingType.value()).foreach((ViewQuery.ViewEacher<View>)view -> SystemBarHelper.setPadding(activity,view));
        }
        StatusBarPaddingHType paddingHType = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingHType.class);
        if (paddingHType != null && paddingHType.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.with(pager).$(null,paddingHType.value()).foreach((ViewQuery.ViewEacher<View>)view -> SystemBarHelper.setHeightAndPadding(activity,view));
        }

        StatusBarPaddingS paddingS = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingS.class);
        if (paddingS != null && paddingS.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.with(pager).$(null,paddingS.value()).foreach((ViewQuery.ViewEacher<View>)view ->SystemBarHelper.setPadding(activity,view));
        }
        StatusBarPaddingHS paddingHS = AfReflecter.getAnnotation(pager.getClass(), Activity.class, StatusBarPaddingHS.class);
        if (paddingHS != null && paddingHS.value().length > 0) {
            ret = true;
            defaultTranslucent(pager, translucent, translucentDark);
            $.with(pager).$(null,paddingHS.value()).foreach((ViewQuery.ViewEacher<View>)view -> SystemBarHelper.setHeightAndPadding(activity,view));
        }
        return ret;
    }

    private static void defaultTranslucent(Pager pager, StatusBarTranslucent translucent, StatusBarTranslucentDark translucentDark) {
        if (translucent == null && translucentDark == null && pager instanceof Activity) {
            SystemBarHelper.immersiveStatusBar(pager.getActivity());
            SystemBarHelper.tintStatusBar(pager.getActivity(), 0, 0);
        }
    }

}
