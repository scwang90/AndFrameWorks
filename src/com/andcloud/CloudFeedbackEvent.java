package com.andcloud;

import com.avos.avoscloud.feedback.Comment;

import java.util.List;

public class CloudFeedbackEvent extends CloudEvent {

	/** 加载deploy失败 */
	public static final String CLOUD_FEEDBACK = CLOUD_PREFIX + "feedback";
	public List<Comment> comments;

	public CloudFeedbackEvent(List<Comment> comments) {
		super(CLOUD_FEEDBACK, "您有新的反馈消息");
		this.comments = comments;
	}
}
