package com.andmail;

import com.andmail.kernel.AppinfoMail;

public class FeedBackMail extends AppinfoMail{

	public FeedBackMail(String subject, String content) {
		super(subject, content);
		mailtype =  "意见反馈";
	}

}
