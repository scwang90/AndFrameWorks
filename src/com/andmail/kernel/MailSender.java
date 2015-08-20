package com.andmail.kernel;

import android.os.Message;

import com.andframe.application.AfApplication;
import com.andframe.thread.AfTask;
import com.andmail.model.MailModel;

public class MailSender extends AfTask{
	
	protected static MailModel defaultModel;

	protected MailBySmtp mMail;

	protected String mSubject;
	protected String mContent;
	protected String mSendTo;

	protected MailModel mModel;

	public MailSender(String subject, String content) {
		this(defaultModel,defaultModel.username,subject,content);
	}

	public MailSender(MailModel model,String subject, String content) {
		this(model,model.username,subject,content);
	}

	public MailSender(MailModel model,String sendto,String subject, String content) {
		mModel = model;
		mSubject = subject;
		mContent = content;

		mSendTo = sendto;
		mMail = new MailBySmtp(model.host, model.username, model.password);
	}

	public void send() throws Exception {
		mMail.create(mModel.username,mSendTo , mSubject);
		mMail.addContent(mContent);
		mMail.send();
	}

	public void sendTask() {
		AfApplication.postTask(this);
	}

	@Override
	protected void onWorking(Message msg) throws Exception {
		this.send();
	}

	public static void setDefaultMailModel(MailModel model) {
		defaultModel = model;
	}
}
