package com.andframe.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfViewable;

public class AfModuleNodataImpl extends AfModuleNodata {
	
	public static final int ID_BUTTON = R.id.module_nodata_button;

	public AfModuleNodataImpl(AfViewable view) {
		super(view,R.id.module_nodata_layout);
	}
	
	@Override
	protected View findRefreshButton(AfViewable view) {
		return view.findViewById(R.id.module_nodata_button);
	}

	@Override
	protected TextView findDescription(AfViewable view) {
		return view.findViewByID(R.id.module_nodata_description);
	}
}
