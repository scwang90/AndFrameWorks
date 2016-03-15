package com.andframe.layoutbind.framework;

import android.view.View;


public interface IAfLayoutModule
{
	void hide();
	void show();
	View getLayout();
	boolean isValid();
	void setEnabled (boolean enabled);
	boolean isVisibility();
}
