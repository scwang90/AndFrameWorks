package com.andmail;

import android.content.Context;

import com.andmail.kernel.AppinfoMail;

public class FeedBackMail extends AppinfoMail {

	public FeedBackMail(Context context, String subject, String content) {
		super(context, subject, content);
		mailtype =  "意见反馈";
	}

}
