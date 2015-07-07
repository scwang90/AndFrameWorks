package com.andcloud;

import java.util.List;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import com.andcloud.activity.ThreadActivity;
import com.andframe.application.AfExceptionHandler;
import com.andframe.application.AfNotifyCenter;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.avos.avoscloud.feedback.FeedbackThread.SyncCallback;

public class FeedbackAgent {

	FeedbackThread defaultThread;
	Context mContext;
	boolean contactSwitch = true;

	public FeedbackAgent(Context context) {
		this.mContext = context;
		this.defaultThread = FeedbackThread.getInstance();
	}

	public FeedbackThread getDefaultThread() {
		return this.defaultThread;
	}

	public void startDefaultThreadActivity() {
		Intent intent = new Intent(this.mContext, ThreadActivity.class);
		intent.addFlags(268435456);
		this.mContext.startActivity(intent);
	}

	public boolean isContactEnabled() {
		return this.contactSwitch;
	}

	public void isContactEnabled(boolean flag) {
		this.contactSwitch = flag;
	}

	public void sync() {
		
		try {
			final List<Comment> comments = FeedbackThread.getInstance().getCommentsList();
			FeedbackThread.getInstance().sync(new SyncCallback() {
				int oldcount = (comments!=null)?comments.size():0;
				@Override
				public void onCommentsSend(List<Comment> comments, AVException e) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onCommentsFetch(List<Comment> comments, AVException e) {
					// TODO Auto-generated method stub
					if (comments != null && comments.size() > oldcount) {
						oldcount = comments.size();
						//startActivity(FeedbacksActivity.class);
						Intent intent = new Intent(mContext, ThreadActivity.class);
						Notification notify = AfNotifyCenter.getNotification("用户反馈", "您有新的消息", intent);
						AfNotifyCenter.notify(notify, 1256);
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, ("FeedbackAgent.FeedbackThread.sync"));
		}
	}
}
