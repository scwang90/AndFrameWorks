package com.andframe.layoutbind.framework;

import android.view.View;


public interface IAfLayoutModule
{
	public void hide();
	public void show();
	public View getLayout();
	public boolean isValid();
	public void setEnabled (boolean enabled);
	boolean isVisibility();
}
