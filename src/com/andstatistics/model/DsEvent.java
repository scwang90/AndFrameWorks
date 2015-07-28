package com.andstatistics.model;

import java.io.Serializable;

/**
 * 数据库表ds_event
 * @author 树朾
 * @date 2015-07-28 23:44:10 中国标准时间
 */
//@Table("ds_event")
public class DsEvent implements Serializable {

//	@Id
	/**
	 * ID主键
	 */
	public String keyId;
	/**
	 * 事件ID
	 */
	public String eventId;
	/**
	 * 设备唯一ID
	 */
	public String uniqueId;
	/**
	 * 应用ID
	 */
	public String appId;
	/**
	 * 事件参数
	 */
	public String parameter;
	/**
	 * 备注
	 */
	public String remark;
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

	public DsEvent() {
		// TODO Auto-generated constructor stub
	}
	
}