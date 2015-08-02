package com.andstatistics.model;

import com.andframe.model.Exceptional;

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
	 * 渠道
	 */
	public String channel;
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
	
	public static DsExceptional from(Exceptional exceptional) {
		DsExceptional dsExceptional = new DsExceptional();
		dsExceptional.device = exceptional.Device;
		dsExceptional.message = exceptional.Message;
		dsExceptional.name = exceptional.Name;
		dsExceptional.platform = exceptional.Platform;
		dsExceptional.stack = exceptional.Stack;
		dsExceptional.thread = exceptional.Thread;
		dsExceptional.version = exceptional.Version;
		dsExceptional.status = exceptional.Status;
		return dsExceptional;
	}
}