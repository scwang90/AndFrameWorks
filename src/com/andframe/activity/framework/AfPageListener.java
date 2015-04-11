package com.andframe.activity.framework;


/**
 * 框架页面监听接口 AfPageListener
 * @author SCWANG
 * 	主要监听键盘显示和隐藏
 * 	页面需要查询数据变动
 */
public interface AfPageListener{
	/**
	 * 当软键盘显示
	 */
	public void onSoftInputShown();
	/**
	 * 当面软键盘收起
	 */
	public void onSoftInputHiden();
	/**
	 * 查询系统数据变动
	 */
	public void onQueryChanged();
}
