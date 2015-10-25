package com.andmail;

import com.andframe.application.AfApplication;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.util.java.AfMD5;
import com.andmail.kernel.AppinfoMail;

public class NotiftyMail extends AppinfoMail{
	
	public enum SginType{
		TITLE,CONTENT,ALL
	}

	private String md5 = AfApplication.getApp().getDesKey();
	private boolean mSendOnce = false;

	public NotiftyMail(String title,String content) {
		super(title, content);
		mailtype = "事件通知";
		md5 = AfMD5.getMD5(title+content);
	}

	public NotiftyMail(SginType type,String title,String content) {
		super(title, content);
		mailtype = "事件通知";
		switch (type) {
		case TITLE:
			mSendOnce = true;
			md5 = AfMD5.getMD5(title);
			break;
		case CONTENT:
			mSendOnce = true;
			md5 = AfMD5.getMD5(content);
			break;
		case ALL:
			mSendOnce = true;
			md5 = AfMD5.getMD5(title+content);
			break;
		}
	}
	
	public NotiftyMail(boolean sendonce,String title,String content) {
		this(title, content);
		mailtype = "事件通知";
		mSendOnce = sendonce;
	}

	public static void sendNotifty(String title,String content){
		new NotiftyMail(title, content).sendTask();
	}
	
	public static void sendNotifty(SginType type,String title,String content){
		new NotiftyMail(type, title, content).sendTask();
	}
	
	public static void sendNotifty(boolean sendonce,String title,String content){
		new NotiftyMail(sendonce, title, content).sendTask();
	}

	@Override
	public void send() throws Exception {
		AfPrivateCaches cache = AfPrivateCaches.getInstance();
		if (!mSendOnce || cache.get(md5, String.class) == null) {//标记相同错误只发送一次
			super.send();
			cache.put(md5, md5);
		}
	}
	
	@Override
	protected void onException(Throwable e) {
		super.onException(e);
		if (mSendOnce) {
			AfPrivateCaches.getInstance().put(md5, md5);
		}
	}
}
