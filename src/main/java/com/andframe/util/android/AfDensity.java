package com.andframe.util.android;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 *
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public class AfDensity {

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(float pxVal) {
        final float scale = getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        return (pxVal / getDisplayMetrics().scaledDensity);
    }


    /**
     * 获取屏幕像素宽度的 ratio 百分比
     *
     * @return 百分比
     */
    public static int proportion(float ratio) {
        if (ratio > 1) {
            return (int) (ratio + 0.5f);
        }
        return (int) (getDisplayMetrics().widthPixels * ratio + 0.5f);
    }

    /**
     * 获取屏幕像素宽度的 ratio 百分比（offset 屏幕像素偏移量）
     *
     * @param offset 用于布局没有占满屏幕（带边框），
     *               那么 offset = 0 - 边框
     * @return proportion
     */
    public static int proportion(float ratio, int offset) {
        if (ratio > 1) {
            return (int) (ratio + 0.5f + offset);
        }
        return (int) ((getDisplayMetrics().widthPixels + offset) * ratio + 0.5f);
    }

    /**
     * 获取 DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    /**
     * 获取屏幕像素宽度
     *
     * @return 像素宽度
     */
    public static int getWidthPixels() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕像素高度
     *
     * @return 像素高度
     */
    public static int getHeightPixels() {
        return getDisplayMetrics().heightPixels;
    }
}
