package com.andframe.module;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.api.view.Viewer;

public class AfModuleNodataImpl extends AfModuleNodata {
	
	public static final int ID_BUTTON = R.id.module_nodata_button;

	public AfModuleNodataImpl(Viewer view) {
		super(view,R.id.module_nodata_layout);
	}
	
	@Override
	protected View findRefreshButton(Viewer view) {
		return view.findViewById(R.id.module_nodata_button);
	}

	@Override
	protected TextView findDescription(Viewer view) {
		return view.findViewByID(R.id.module_nodata_description);
	}
}
