package com.andframe.activity.framework;


/**
 * 框架页面监听接口 AfSoftInputPageListener
 * @author 树朾
 * 	主要监听键盘显示和隐藏
 * 	页面需要查询数据变动
 */
public interface AfSoftInputPageListener {
	/**
	 * 当软键盘显示
	 */
	void onSoftInputShown();
	/**
	 * 当面软键盘收起
	 */
	void onSoftInputHiden();
}
