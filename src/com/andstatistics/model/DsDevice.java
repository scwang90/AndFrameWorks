package com.andstatistics.model;

import java.io.Serializable;

/**
 * 数据库表ds_device
 * @author 树朾
 * @date 2015-07-28 23:44:10 中国标准时间
 */
//@Table("ds_device")
@SuppressWarnings("serial")
public class DsDevice implements Serializable {

//	@Id
	/**
	 * ID主键
	 */
	public String keyId;
	/**
	 * 设备唯一ID
	 */
	public String uniqueId;
	/**
	 * 应用ID
	 */
	public String appId;
	/**
	 * 设备imei
	 */
	public String imei;
	/**
	 * 设备mac地址
	 */
	public String mac;
	/**
	 * 用户相关ID
	 */
	public String userId;
	/**
	 * 创建时间
	 */
	public java.util.Date createTime;
	/**
	 * 更新时间
	 */
	public java.util.Date updateTime;

	public DsDevice() {
		// TODO Auto-generated constructor stub
	}

	public DsDevice(DsApplication application) {
		this.appId = application.keyId;
	}
}