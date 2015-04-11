package com.andframe.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfViewable;

public class AfModuleProgressImpl extends AfModuleProgress{

	public AfModuleProgressImpl(AfViewable view) {
		super(view);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		mTvDescription = view.findTextViewById(R.id.module_progress_loadinfo);
		if(mTvDescription != null){
			return (View) mTvDescription.getParent();
		}
		return mTvDescription;
	}

	@Override
	protected TextView findDescription(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findTextViewById(R.id.module_progress_loadinfo);
	}

}
