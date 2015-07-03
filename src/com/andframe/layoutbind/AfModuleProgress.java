package com.andframe.layoutbind;

import android.widget.TextView;

import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfLayoutModule;
/**
 * 框架加载组件
 * @author 树朾
 *
 */
public abstract class AfModuleProgress extends AfLayoutModule{

	public TextView mTvDescription = null;

	public AfModuleProgress(AfViewable view) {
		super(view);
		// TODO Auto-generated constructor stub
		if(isValid()){
			mTvDescription = findDescription(view);
			mTvDescription.setText("正在加载...");
		}
	}
	
	/**
	 * 获取信息提示 TextView
	 * @param view
	 * @return
	 */
	protected abstract TextView findDescription(AfViewable view);
	
	public void setDescription(String description) {
		// TODO Auto-generated constructor stub
		if(isValid()){
			mTvDescription.setText(description);
		}
	}

	public void setDescription(int resid) {
		// TODO Auto-generated constructor stub
		if(isValid()){
			mTvDescription.setText(resid);
		}
	}

}
