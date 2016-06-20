package com.andframe.layoutbind;

import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfViewable;

public class AfModuleProgressImpl extends AfModuleProgress{

	public AfModuleProgressImpl(AfViewable view) {
		super(view,R.id.module_progress_layout);
	}
	
	@Override
	protected TextView findDescription(AfViewable view) {
		return view.findViewByID(R.id.module_progress_loadinfo);
	}

}
