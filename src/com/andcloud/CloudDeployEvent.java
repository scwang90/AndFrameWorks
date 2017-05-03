package com.andcloud;

import com.andcloud.model.Deploy;

public class CloudDeployEvent extends CloudEvent {
	public Deploy deploy;
	public CloudDeployEvent(Deploy deploy) {
		super(CLOUD_DEPLOY_FINISHED, "加载部署配置完成");
		this.deploy = deploy;
	}
}
