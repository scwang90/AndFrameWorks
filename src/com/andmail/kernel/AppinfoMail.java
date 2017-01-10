package com.andmail.kernel;

import android.content.Context;

import com.andmail.api.model.MailSenderModel;

public abstract class AppinfoMail extends MailResender {
	
	protected String mailtype = "";
	protected static String appinfo = "";

	public AppinfoMail(Context context, String subject, String content) {
		super(context, subject, content);
	}

	public AppinfoMail(Context context, MailSenderModel model, String subject, String content) {
		super(context, model, subject, content);
	}

	@Override
	public void send() throws Exception {
		this.packAppinfo();
		super.send();
	}
	
	protected void packAppinfo() {
		mContent = mContent  + "\r\n\r\n" + appinfo;
		mSubject = mSubject+" "+mailtype;
	}
	
	public static void updateAppinfo(String info) {
		appinfo = info;
	}
	
}
