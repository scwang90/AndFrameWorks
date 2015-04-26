package com.andadvert.kernel;

import com.andadvert.model.OnlineDeploy;

public abstract class AdapterHelper {
	public abstract boolean isHide();
	public abstract void setHide(boolean value);
	public abstract void setValue(OnlineDeploy value);
	public abstract OnlineDeploy getDeploy();
	public abstract void onCheckOnlineHideFail(Throwable throwable);
}
