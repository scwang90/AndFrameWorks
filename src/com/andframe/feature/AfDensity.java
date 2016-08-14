package com.andframe.feature;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class AfDensity {

	/**
	 * dip 转换成 像素（px）
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 像素（px）转换成dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 像素（px）转换成dip
	 */
	@SuppressWarnings("unused")
	public static float px2dip(int pxValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (pxValue / scale + 0.5f);
	}
	
	protected DisplayMetrics mDisplayMetrics = null;

	public AfDensity() {
		mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
	}

	public AfDensity(Context context) {
		mDisplayMetrics = context.getResources().getDisplayMetrics();
	}
	
	public AfDensity(DisplayMetrics metrics) {
		mDisplayMetrics = metrics;
	}

	/**
	 * dip 转换成 像素（px）
	 * @return 像素（px）
	 */
	public int dip2px(float dipValue) {
		final float scale = mDisplayMetrics.density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 像素（px）转换成dip
	 * @return dip
	 */
	public int px2dip(float pxValue) {
		final float scale = mDisplayMetrics.density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕像素宽度的 ratio 百分比
	 * @return 百分比
	 */
	public int proportion(float ratio) {
		if(ratio > 1){
			return (int)(ratio+0.5f);
		}
		return (int)(mDisplayMetrics.widthPixels*ratio+0.5f);
	}

	/**
	 * 获取屏幕像素宽度的 ratio 百分比（offset 屏幕像素偏移量）
	 * @param offset 用于布局没有占满屏幕（带边框），
	 * 		那么 offset = 0 - 边框
	 * @return proportion
	 */
	public int proportion(float ratio,int offset) {
		if(ratio > 1){
			return (int)(ratio+0.5f+offset);
		}
		return (int)((mDisplayMetrics.widthPixels+offset)*ratio+0.5f);
	}

	/**
	 * 获取屏幕像素宽度
	 * @return 像素宽度
	 */
	public int getWidthPixels(){
		return mDisplayMetrics.widthPixels;
	}

	/**
	 * 获取屏幕像素高度
	 * @return 像素高度
	 */
	public int getHeightPixels(){
		return mDisplayMetrics.heightPixels;
	}
}
