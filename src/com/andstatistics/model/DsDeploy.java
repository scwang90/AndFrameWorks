package com.andstatistics.model;

import com.andframe.util.java.AfVersion;

import java.io.Serializable;

/**
 * 数据库表ds_deploy
 * @author 树朾
 * @date 2015-08-24 12:58:02 中国标准时间
 */
//@Table("ds_deploy")
@SuppressWarnings("serial")
public class DsDeploy implements Serializable{

	/**
	 * ID主键
	 */
	//@Id
	public String keyId;
	/**
	 * 应用ID
	 */
	public String appId;
	/**
	 * 配置名称
	 */
	public String name;
	/**
	 * url参数（多个用 | 隔开）
	 */
	public String urls;
	/**
	 * 应用版本
	 */
	public String version;
	/**
	 * 配置备注
	 */
	public String remark;
	/**
	 * 是否开启商业模式
	 */
	public Boolean business;
	/**
	 * 创建时间
	 */
	public java.util.Date createTime;
	/**
	 * 更新时间
	 */
	public java.util.Date updateTime;

	public DsDeploy() {
	}

	public int getVersion() {
		return AfVersion.transformVersion(version);
	}
}