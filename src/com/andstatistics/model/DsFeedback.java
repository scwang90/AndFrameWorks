package com.andstatistics.model;

import java.io.Serializable;

/**
 * 数据库表ds_feedback
 * @author 树朾
 * @date 2015-07-28 23:44:10 中国标准时间
 */
//@Table("ds_feedback")
public class DsFeedback implements Serializable {

//	@Id
	/**
	 * ID主键
	 */
	public String keyId;
	/**
	 * 回复ID
	 */
	public String replyId;
	/**
	 * 是否官方回复
	 */
	public Boolean isReply;
	/**
	 * 应用ID
	 */
	public String appId;
	/**
	 * 反馈标题
	 */
	public String title;
	/**
	 * 反馈内容
	 */
	public String content;
	/**
	 * 联系方式
	 */
	public String contact;
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
	 * 应用版本信息
	 */
	public String version;
	/**
	 * 错误信息
	 */
	public String bug;

	public DsFeedback() {
		// TODO Auto-generated constructor stub
	}
	
}