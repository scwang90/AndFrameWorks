package com.andcloud;

public class CloudExceptionEvent extends CloudEvent {

	/** 加载deploy失败 */
	public static final String CLOUD_EXCEPTION = CLOUD_PREFIX + "exception";

	public Throwable exception;

	public CloudExceptionEvent(Throwable exception, String param) {
		super(CLOUD_EXCEPTION, param);
		this.exception = exception;
	}

}
