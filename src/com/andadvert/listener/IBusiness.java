package com.andadvert.listener;

import com.andadvert.model.OnlineDeploy;

public interface IBusiness {
	public void notifyBusinessModelClose();
	public void notifyBusinessModelStart(OnlineDeploy deploy);
	
}
