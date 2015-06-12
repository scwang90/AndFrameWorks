package com.andrestrequest.http;

/**
 * 错误信息类
 * 
 * @description ErrorMessage
 * @author wanda
 * @version V1.0, 2015年3月13日 上午10:14:00
 * @modified 初次创建ErrorMessage类
 */
public class ErrorMessage
{
	private String code;

	private String errorMessage;

	public ErrorMessage()
	{
	}

	public ErrorMessage(String code, String errorMessage)
	{
		this.code = code;
		this.errorMessage = errorMessage;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * 系统错误
	 */
	public static final String SYSTEM_ERROR = "10001";

	/**
	 * 数据库连接错误
	 */
	public static final String MYSQL_CONNECT_ERROR = "10011";

	/**
	 * sql错误
	 */
	public static final String BADSQL_ERROR = "10012";

	/**
	 * json解析错误
	 */
	public static final String JSON_ERROR = "10013";

	/**
	 * 空指针错误
	 */
	public static final String NULL_POINTER_ERROR = "10014";

	/**
	 * 参数错误
	 */
	public static final String PARAMETER_ERROR = "10100";

	/**
	 * 激活网关key错误
	 */
	public static final String GATEWAY_KEY_ERROR = "20001";

	/**
	 * 保存网关出错
	 */
	public static final String GATEWAY_SAVE_ERROR = "20002";

	/**
	 * 删除网关错
	 */
	public static final String GATEWAY_DELETE_ERROR = "20003";

	/**
	 * 获取网关出错
	 */
	public static final String GATEWAY_GET_ERROR = "20004";

	/**
	 * 根据传入ID罗列网关出错
	 */
	public static final String GATEWAY_LIST_BY_IDS_ERROR = "20007";

	/**
	 * 罗列网关出错
	 */
	public static final String GATEWAY_LIST_ERROR = "20005";

	/**
	 * 没有操作指定网关权限
	 */
	public static final String GATEWAY_AUTHORITY_ERROR = "20006";

	/**
	 * 
	 */
	public static final String GATEWAY_TYPE_GET_ERROR = "20011";

	/**
	 * 
	 */
	public static final String GATEWAY_TYPE_LIST_ERROR = "20012";

	/**
	 * 传感器错误
	 */
	public static final String SENSOR_ERROR = "30001";

	/**
	 * 创建传感器错误
	 */
	public static final String SENSOR_SAVE_ERROR = "30002";

	/**
	 * 更新传感器错误
	 */
	public static final String SENSOR_UPDATE_ERROR = "30003";

	/**
	 * 删除传感器错误
	 */
	public static final String SENSOR_DELETE_ERROR = "30004";

	/**
	 * 获取传感器错误
	 */
	public static final String SENSOR_GET_ERROR = "30005";

	/**
	 * 罗列传感器错误
	 */
	public static final String SENSOR_LIST_ERROR = "30006";
	
	/**
	 * 根据传入ID获取传感器错误
	 */
	public static final String SENSOR_LIST_BY_ID_ERROR = "30007";

	/**
	 * 
	 */
	public static final String SENSOR_TYPE_GET_ERROR = "30011";

	/**
	 * 
	 */
	public static final String SENSOR_TYPE_LIST_ERROR = "30012";

	/**
	 * 传感器属性错误
	 */
	public static final String SENSOR_PROPERTY_ERROR = "40001";

	/**
	 * 添加传感器属性错误
	 */
	public static final String SENSOR_PROPERTY_SAVE_ERROR = "40002";
	
	/**
	 * 更新传感器属性错误
	 */
	public static final String SENSOR_PROPERTY_UPDATE_ERROR = "40003";
	/**
	 * 删除传感器属性错误
	 */
	public static final String SENSOR_PROPERTY_DELETE_ERROR = "40004";
	/**
	 * 
	 */
	public static final String SENSOR_PROPERTY_TYPE_GET_ERROR = "40011";

	/**
	 * 
	 */
	public static final String SENSOR_PROPERTY_TYPE_LIST_ERROR = "40012";
	
	/**
	 * 获取单个传感器属性数据类型错误
	 */
	public static final String PROPERTY_DATA_TYPE_GET_ERROR = "40021";
	
	/**
	 * 获取全部传感器属性数据类型错误
	 */
	public static final String PROPERTY_DATA_TYPE_LIST_ERROR = "40022";
	
	
	/**
	 * 获取单个传感器属性码信息错误
	 */
	public static final String PROPERTY_CODE_GET_ERROR = "40031";
	
	
	/**
	 * 获取全部传感器属性码信息错误
	 */
	public static final String PROPERTY_CODE_LIST_ERROR = "40032";
	
	/**
	 * 按传感器属性编码获取传感器属性失败
	 */
	public static final String GET_SENSOR_PROPERTY_BY_CODE = "40041";
	
	/**
	 * 单一传感器下的传感器属性对应的属性不唯一错误
	 */
	public static final String GET_SENSOR_PROPERTY_BY_CODE_IS_NOT_ONLY = "40042";
	
	/* ligy start */

	/**
	 * 添加触发器错误
	 */
	public static final String TRIGGER_SAVE_ERROR = "50001";

	/**
	 * 删除触发器错误
	 */
	public static final String TRIGGER_DELETE_ERROR = "50002";

	/**
	 * 更新触发器错误
	 */
	public static final String TRIGGER_UPDATE_ERROR = "50003";

	/**
	 * 获取触发器错误
	 */
	public static final String TRIGGER_GET_ERROR = "50004";

	/**
	 * 获取全部触发器错误
	 */
	public static final String TRIGGER_LIST_ERROR = "50005";

	/**
	 * 按id数组查询触发器错误
	 */
	public static final String TRIGGER_LIST_BY_IDS_ERROR = "50006";

	/**
	 * 触发器个数到达上限，不可以继续添加
	 */
	public static final String TRIGGER_ENOUGH_ERROR = "50007";

	/**
	 * 查询网关阀值类型列表失败
	 */
	public static final String TRIGGER_THRESHOLD_TYPE_LIST_ERROR = "50011";

	/**
	 * 没有此用户
	 */
	public static final String NO_USER_ERROR = "60001";

	/**
	 * 用户没有传感器操作权限
	 */
	public static final String SENSOR_USER_AUTHORITY_ERROR = "30007";
	
	/**
	 * 没有这个用户userKey
	 */
	public static final String NO_USER_KEY_ERROR = "60002";

	/* ligy end */
}
