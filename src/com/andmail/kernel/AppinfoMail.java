package com.andmail.kernel;

import com.andframe.application.AfApplication;
import com.andmail.model.MailModel;

public abstract class AppinfoMail extends MailResender{
	
	protected String mailtype = "";
	protected static String appinfo = "";

	public AppinfoMail(MailModel model, String sendto, String subject,
			String content) {
		super(model, sendto, subject, content);
	}

	public AppinfoMail(MailModel model, String subject, String content) {
		super(model, subject, content);
	}

	public AppinfoMail(String subject, String content) {
		super(subject, content);
	}

	@Override
	public void send() throws Exception {
		this.packAppinfo();
		super.send();
	}
	
	protected void packAppinfo() {
		AfApplication app = AfApplication.getApp();
		mContent = mContent  + "\r\n\r\n" + appinfo;
		mSubject = mSubject+" "+mailtype+" "+ app.getAppName();
	}
	
	public static void updateAppinfo(String info) {
		appinfo = info;
	}
	
}
