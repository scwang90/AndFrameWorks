package com.andframe.util.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * 状态栏兼容
 * Created by zhy on 15/9/21.
 */
public class AfStatusBarCompat {

    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = contentView.getChildAt(0);
            //改变颜色时避免重复添加statusBarView
            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }

    public static int getStatusBarHeight(Context context) {
        return getStatusBarHeight(context, false);
    }

    public static int getStatusBarHeight(Context context, boolean real) {
        int result = 0;
        if (real && Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 25) {
            return result;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static View compatPadding(View root) {
        return compatPadding(root,false);
    }

    public static View compatPadding(View root, boolean only19) {
        if (Build.VERSION.SDK_INT >= 19 && root != null && (!only19 || Build.VERSION.SDK_INT < 25)) {
            int bot = root.getPaddingBottom();
            int top = root.getPaddingTop();
            int lef = root.getPaddingLeft();
            int rig = root.getPaddingRight();
            int she = getStatusBarHeight(root.getContext());
            root.setPadding(lef, top + she, rig, bot);
            ViewGroup.LayoutParams params = root.getLayoutParams();
            if (params != null && params.height > 0) {
                params.height = params.height + she;
                root.setLayoutParams(params);
            }
        }
        return root;
    }

    public static View compatScroll(View root) {
        return compatScroll(root, false);
    }

    public static View compatScroll(View root, boolean only19) {
        if (Build.VERSION.SDK_INT >= 19 && root != null && (!only19 || Build.VERSION.SDK_INT < 25)) {
            int she = getStatusBarHeight(root.getContext());
            root.scrollTo(root.getScrollX(), root.getScrollY() - she);
        }
        return root;
    }
}
