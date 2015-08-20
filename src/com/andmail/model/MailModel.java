package com.andmail.model;

import com.andframe.application.AfApplication;
import com.andframe.helper.android.AfDesHelper;
import com.google.gson.Gson;

public class MailModel {

	public String host = "";//"imap.163.com";
	public String username = "";//"zaituren@163.com";
	public String password = "";

	public static MailModel fromJson(String json) {
		try {
			return new Gson().fromJson(json, MailModel.class);
		} catch (Throwable e) {
			return null;
		}
	}

	public static MailModel fromJsonCode(String code) {
		try {
			String key = AfApplication.getApp().getDesKey();
			String json = new AfDesHelper(key).decrypt(code);
			return new Gson().fromJson(json, MailModel.class);
		} catch (Throwable e) {
			return null;
		}
	}
}
