package com.andmail.model;

import com.andframe.application.AfApplication;
import com.andframe.helper.android.AfDesHelper;
import com.google.gson.Gson;

public class MailModel {

	public String host = "";//"imap.163.com";
	public String username = "";//"zaituren@163.com";
	public String password = "";

	public static MailModel fromJson(String json) {
		// TODO Auto-generated method stub
		try {
			return new Gson().fromJson(json, MailModel.class);
		} catch (Throwable e) {
			// TODO: handle exception
			return null;
		}
	}

	public static MailModel fromJsonCode(String code) {
		// TODO Auto-generated method stub
		try {
			String key = AfApplication.getApp().getDesKey();
			String json = new AfDesHelper(key).decrypt(code);
			return new Gson().fromJson(json, MailModel.class);
		} catch (Throwable e) {
			// TODO: handle exception
			return null;
		}
	}
}
