package com.andadvert.event;

public class AdvertExceptionEvent extends AdvertEvent {

	/** 加载deploy失败 */
	public static final String CLOUD_EXCEPTION = ADVERT_PREFIX + "exception";

	public Throwable exception;

	public AdvertExceptionEvent(Throwable exception, String param) {
		super(CLOUD_EXCEPTION, param);
		this.exception = exception;
	}

}
