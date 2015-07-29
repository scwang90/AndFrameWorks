package com.andstatistics.model;

import java.io.Serializable;

/**
 * 数据库表ds_application
 * @author 树朾
 * @date 2015-07-28 23:44:10 中国标准时间
 */
//@Table("ds_application")
public class DsApplication implements Serializable{

//	@Id
	/**
	 * ID主键
	 */
	public String keyId;
	/**
	 * 应用名称
	 */
	public String name;
	/**
	 * 应用类型（Android,IOS,Windows）
	 */
	public String type;
	/**
	 * 应用描述
	 */
	public String description;
	/**
	 * 创建时间
	 */
	public java.util.Date createTime;
	/**
	 * 更新时间
	 */
	public java.util.Date updateTime;

	public DsApplication() {
		// TODO Auto-generated constructor stub
	}

	public DsApplication(String appkey) {
		this.keyId = appkey;
	}
}