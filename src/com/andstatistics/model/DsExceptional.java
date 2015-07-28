package com.andstatistics.model;

import java.io.Serializable;

/**
 * 数据库表ds_exceptional
 * @author 树朾
 * @date 2015-07-28 23:44:10 中国标准时间
 */
//@Table("ds_exceptional")
public class DsExceptional implements Serializable {

//	@Id
	/**
	 * ID主键
	 */
	public String keyId;
	/**
	 * 应用ID
	 */
	public String appId;
	/**
	 * 应用名称
	 */
	public String name;
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
	/**
	 * 用户相关信息（建议用JSON，max-256）
	 */
	public String user;
	/**
	 * 设备信息
	 */
	public String device;
	/**
	 * 应用版本信息
	 */
	public String version;
	/**
	 * 错误信息
	 */
	public String message;
	/**
	 * 错误堆栈信息
	 */
	public String stack;
	/**
	 * 错误线程信息
	 */
	public String thread;
	/**
	 * 运行平台（Android,IOS,Windows）
	 */
	public String platform;
	/**
	 * 状态（0-无效 1-新错误 2-正在处理 3-处理完成提交 4-确认问题已经解决）
	 */
	public Integer status;

	public DsExceptional() {
		// TODO Auto-generated constructor stub
	}
	
	public String getKeyId(){
		return this.keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
		
	public String getAppId(){
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
		
	public String getName(){
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
		
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
		
	public String getUser(){
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}
		
	public String getDevice(){
		return this.device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
		
	public String getVersion(){
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
		
	public String getMessage(){
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
	public String getStack(){
		return this.stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}
		
	public String getThread(){
		return this.thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}
		
	public String getPlatform(){
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
		
	public Integer getStatus(){
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
		

}