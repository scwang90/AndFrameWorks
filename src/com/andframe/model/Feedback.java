package com.andframe.model;

import java.util.Date;
import java.util.UUID;

import com.andframe.model.framework.AfModel;
import com.andframe.util.UUIDUtil;

public class Feedback extends AfModel{
	
	public String Content = "";
	public String Contact = "";
	public UUID UserID = UUIDUtil.Empty;
	public int CurVersion;
	public String BugInfo = "";

	public Feedback() {
	}

	public Feedback(String Name, String Content, Date Date, String Contact,
			UUID UserID, int CurVersion, String BugInfo) {
		this.Name = Name;
		this.Content = Content;
		this.Contact = Contact;
		this.UserID = UserID;
		this.CurVersion = CurVersion;
		this.BugInfo = BugInfo;
	}

}
