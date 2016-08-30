package com.andoffice.application;

import com.andframe.application.AfApp;
import com.andoffice.R;

public abstract class AbApplication extends AfApp {

	public static final int DEBUG_TESTDATA = 0;
	public static final int DEBUG_TEST = 1;
	private static int debugMode;

	public static int getDebugMode() {
		return debugMode;
	}

	@Override
	public String getAppName() {
		return getString(R.string.app_name);
	}

	public static void setDebugMode(int debugMode) {
		AbApplication.debugMode = debugMode;
	}
}
