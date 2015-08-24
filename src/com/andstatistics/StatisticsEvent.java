package com.andstatistics;

public class StatisticsEvent {
	/** ID前缀 */
	public static final String STATISTICS_PREFIX = "statistics_";
	/** 加载deploy失败 */
	public static final String STATISTICS_DEPLOY_FAILED = STATISTICS_PREFIX + "deploy_failed";
	/** 加载deploy成功 */
	public static final String STATISTICS_DEPLOY_FINISHED = STATISTICS_PREFIX + "deploy_finished";

}
