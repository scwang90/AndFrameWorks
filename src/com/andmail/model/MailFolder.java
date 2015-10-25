package com.andmail.model;

import com.google.gson.Gson;

public class MailFolder extends MailModel{
	
	public String folder = "";//"BACK";
	
	public static MailFolder fromJson(String json) {
		try {
			return new Gson().fromJson(json, MailFolder.class);
		} catch (Throwable e) {
			return null;
		}
	}
	
}
