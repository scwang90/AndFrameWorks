package com.andrestrequest.http;

/**
 * 错误信息类
 * 
 * @description ErrorMessage
 * @author wanda
 * @version V1.0, 2015年3月13日 上午10:14:00
 * @modified 初次创建ErrorMessage类
 */
public abstract class ErrorMessage
{
	public abstract String getCode();
	public abstract String getErrorMessage();
}
