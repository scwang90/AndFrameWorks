package com.andframe.module;

import android.widget.TextView;

import com.andframe.api.view.Viewer;

/**
 * 框架加载组件
 * @author 树朾
 *
 */
@SuppressWarnings("unused")
public abstract class AfModuleProgress extends AfViewModuler {

	public TextView mTvDescription = null;

	public AfModuleProgress(Viewer view) {
		super(view);
		if(isValid()){
			mTvDescription = findDescription(view);
			mTvDescription.setText("正在加载...");
		}
	}

	public AfModuleProgress(Viewer view, int id) {
		super(view,id);
		if(isValid()){
			mTvDescription = findDescription(view);
			mTvDescription.setText("正在加载...");
		}
	}
	
	/**
	 * 获取信息提示 TextView
	 */
	protected abstract TextView findDescription(Viewer view);
	
	public void setDescription(String description) {
		if(isValid()){
			mTvDescription.setText(description);
		}
	}

	public void setDescription(int resid) {
		if(isValid()){
			mTvDescription.setText(resid);
		}
	}

}
