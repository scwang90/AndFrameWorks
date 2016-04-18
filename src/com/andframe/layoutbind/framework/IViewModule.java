package com.andframe.layoutbind.framework;


import android.view.View;

public interface IViewModule
{
	void hide();
	void show();
	View getTarget();
	boolean isValid();
	boolean isVisibility();
}
