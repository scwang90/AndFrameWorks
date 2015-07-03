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
		// TODO Auto-generated constructor stub
		if(isValid()){
			mTvAppName = page.findViewByID(R.id.title_appname);
		}
	}

	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		View layout = view.findViewById(R.id.title_appname);
		if(layout != null){
			layout = (View)layout.getParent();
		}
		return layout;
	}
	
	public void setTitle(String description) {
		// TODO Auto-generated constructor stub
		mTvAppName.setText(description);
	}

	public void setTitle(int id) {
		// TODO Auto-generated constructor stub
		mTvAppName.setText(id);
	}

}
