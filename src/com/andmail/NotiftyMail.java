package com.andmail;

import android.content.Context;

import com.andmail.kernel.AppinfoMail;
import com.andmail.util.ACache;
import com.andmail.util.AfMD5;

public class NotiftyMail extends AppinfoMail {

	public enum SginType{
		TITLE,CONTENT,ALL;
	}

	private ACache mCache;
	private String md5 = AfMD5.getMD5("");
	private boolean mSendOnce = false;

	public NotiftyMail(Context context, String title, String content) {
		super(context, title, content);
		mCache = getaCache(context);
		mailtype = "事件通知";
		md5 = AfMD5.getMD5(title+content);
	}

	public NotiftyMail(Context context, SginType type,String title,String content) {
		super(context, title, content);
		mCache = getaCache(context);
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

	public NotiftyMail(Context context, boolean sendonce,String title,String content) {
		this(context, title, content);
		mCache = getaCache(context);
		mailtype = "事件通知";
		mSendOnce = sendonce;
	}

	private ACache getaCache(Context context) {
		return ACache.get(context, "NotiftyMail");
	}

	public static void sendNotifty(Context context, String title,String content){
		new NotiftyMail(context, title, content).sendTask();
	}
	
	public static void sendNotifty(Context context, SginType type,String title,String content){
		new NotiftyMail(context, type, title, content).sendTask();
	}
	
	public static void sendNotifty(Context context, boolean sendonce,String title,String content){
		new NotiftyMail(context, sendonce, title, content).sendTask();
	}

	@Override
	public void send() throws Exception {
		if (!mSendOnce || mCache.getAsString(md5) == null) {//标记相同错误只发送一次
			super.send();
			mCache.put(md5, md5);
		}
	}

	@Override
	public void onTaskException(Exception e) {
		super.onTaskException(e);
		if (mSendOnce) {
			mCache.put(md5, md5);
		}
	}
}
