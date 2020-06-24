package com.andframe.api.viewer;

import android.view.View;

/**
 * 视图功能模块
 */
@SuppressWarnings("unused")
public interface ViewModule
{
	void hide();
	void show();
	View getView();
	boolean isValid();
	boolean isVisibility();
}