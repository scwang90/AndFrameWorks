package com.andframe.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfViewable;

public class AfModuleProgressImpl extends AfModuleProgress{

	public AfModuleProgressImpl(AfViewable view) {
		super(view);
	}
	
	@Override
	protected View findLayout(AfViewable view) {
		mTvDescription = view.findViewByID(R.id.module_progress_loadinfo);
		if(mTvDescription != null){
			return (View) mTvDescription.getParent();
		}
		return mTvDescription;
	}

	@Override
	protected TextView findDescription(AfViewable view) {
		return view.findViewByID(R.id.module_progress_loadinfo);
	}

}
