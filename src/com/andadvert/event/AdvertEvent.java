package com.andadvert.event;

public class AdvertEvent {
	/** ID前缀 */
	public static final String ADVERT_PREFIX = "advert_";
	/** 发现测试 */
	public static final String ADVERT_FIND_TEST = ADVERT_PREFIX + "find_test";
	/** 发现重读测试 */
	public static final String ADVERT_FIND_REPEAT_TEST = ADVERT_PREFIX + "find_repeat_test";
	/** 墨点点数作弊增加 */
	public static final String ADVERT_POINT_INCREASE_CHEAT = ADVERT_PREFIX + "point_increase_cheat";
	/** 墨点点数引导增加 */
	public static final String ADVERT_POINT_INCREASE_IMPORTUNE = ADVERT_PREFIX + "point_increase_importune";
	/** 墨点点数增加 */
	public static final String ADVERT_POINT_INCREASE = ADVERT_PREFIX + "point_increase";

	public String event;
	public String param;

	public AdvertEvent(String event) {
		this.event = event;
	}

	public AdvertEvent(String event, String param) {
		this.event = event;
		this.param = param;
	}
}
