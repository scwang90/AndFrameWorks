package com.andframe.module;

import android.widget.TextView;

import com.andframe.R;
import com.andframe.api.Viewer;

public class AfModuleProgressImpl extends AfModuleProgress{

	public AfModuleProgressImpl(Viewer view) {
		super(view,R.id.module_progress_layout);
	}
	
	@Override
	protected TextView findDescription(Viewer view) {
		return view.findViewByID(R.id.module_progress_loadinfo);
	}

}
