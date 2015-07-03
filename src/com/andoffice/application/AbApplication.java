package com.andoffice.application;

import com.andoffice.R;
import com.andframe.application.AfApplication;

public abstract class AbApplication extends AfApplication {


	@Override
	public String getAppName() {
		// TODO Auto-generated method stub
		return getString(R.string.app_name);
	}

}
