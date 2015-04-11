package com.andmail.model;

import com.google.gson.Gson;

public class MailFolder extends MailModel{
	
	public String folder = "";//"BACK";
	
	public static MailFolder fromJson(String json) {
		// TODO Auto-generated method stub
		try {
			return new Gson().fromJson(json, MailFolder.class);
		} catch (Throwable e) {
			// TODO: handle exception
			return null;
		}
	}
	
}
