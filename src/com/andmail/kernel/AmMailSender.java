package com.andmail.kernel;

import android.os.AsyncTask;

import com.andmail.api.MailKerneler;
import com.andmail.api.MailSender;
import com.andmail.api.model.MailSenderModel;

public class AmMailSender extends AsyncTask<Void, Void, Void> implements MailSender {

	private static MailSenderModel defaultModel;

	private MailKerneler mMailKerneler;

	String mSubject;
	protected String mContent;

	MailSenderModel mModel;

	AmMailSender(String subject, String content) {
		this(defaultModel, subject, content);
	}

	AmMailSender(MailSenderModel model, String subject, String content) {
		mModel = model;
		mSubject = subject;
		mContent = content;

		mMailKerneler = newMailKerneler(model);
	}

	@Override
	public MailKerneler newMailKerneler(MailSenderModel model) {
		return new AmSmtpKerneler(model.getHost(), model.getUsername(), model.getPassword());
	}

	@Override
	public void send() throws Exception {
		mMailKerneler.create(mModel.getUsername(), mModel.getSendto());
		mMailKerneler.setContent(mSubject, mContent);
		mMailKerneler.send();
	}

	@Override
	public void sendTask() {
		execute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			this.send();
		} catch (Exception e) {
			e.printStackTrace();
			onTaskException(e);
		}
		return null;
	}

	@Override
	public void onTaskException(Exception e) {

	}

	public static void setDefaultMailModel(MailSenderModel model) {
		defaultModel = model;
	}
}
