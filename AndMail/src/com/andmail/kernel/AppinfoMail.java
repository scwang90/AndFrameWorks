package com.andmail.kernel;

import com.andframe.application.AfApplication;
import com.andmail.model.MailModel;

public abstract class AppinfoMail extends MailResender{
	
	protected String mailtype = "";
	protected static String appinfo = "";

	public AppinfoMail(MailModel model, String sendto, String subject,
			String content) {
		super(model, sendto, subject, content);
		// TODO Auto-generated constructor stub
	}

	public AppinfoMail(MailModel model, String subject, String content) {
		super(model, subject, content);
		// TODO Auto-generated constructor stub
	}

	public AppinfoMail(String subject, String content) {
		super(subject, content);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send() throws Exception {
		// TODO Auto-generated method stub
		this.packAppinfo();
		super.send();
	}
	
	protected void packAppinfo() {
		// TODO Auto-generated method stub
		AfApplication app = AfApplication.getApp();
		mContent = mContent  + "\r\n\r\n" + appinfo;
		mSubject = mSubject+" "+mailtype+" "+ app.getAppName();
	}
	
	public static void updateAppinfo(String info) {
		appinfo = info;
	}
	
}
