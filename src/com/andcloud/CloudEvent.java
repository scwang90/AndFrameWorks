package com.andcloud;

public class CloudEvent {
	/** ID前缀 */
	public static final String CLOUD_PREFIX = "cloud_";
	/** 加载deploy失败 */
	public static final String CLOUD_DEPLOY_FAILED = CLOUD_PREFIX + "deploy_failed";
	/** 加载deploy成功 */
	public static final String CLOUD_DEPLOY_FINISHED = CLOUD_PREFIX + "deploy_finished";

}
