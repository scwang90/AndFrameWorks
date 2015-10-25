package com.andoffice.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfLayoutModule;
import com.andframe.layoutbind.framework.IAfLayoutModule;

public class ModuleTitleMain extends AfLayoutModule implements IAfLayoutModule {

	private TextView mTvAppName = null;

	public ModuleTitleMain(AfPageable page) {
		super(page);
		if(isValid()){
			mTvAppName = page.findViewByID(R.id.title_appname);
		}
	}

	@Override
	protected View findLayout(AfViewable view) {
		View layout = view.findViewById(R.id.title_appname);
		if(layout != null){
			layout = (View)layout.getParent();
		}
		return layout;
	}
	
	public void setTitle(String description) {
		mTvAppName.setText(description);
	}

	public void setTitle(int id) {
		mTvAppName.setText(id);
	}

}
