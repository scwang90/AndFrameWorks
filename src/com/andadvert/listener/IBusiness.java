package com.andadvert.listener;

import com.andadvert.model.OnlineDeploy;

public interface IBusiness {
	void notifyBusinessModelClose();
	void notifyBusinessModelStart(OnlineDeploy deploy);
	
}
