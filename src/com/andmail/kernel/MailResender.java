package com.andmail.kernel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.os.Message;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfSharedPreference;
import com.andframe.util.java.AfDateFormat;
import com.andmail.model.MailModel;
import com.google.gson.Gson;

public class MailResender extends MailSender{

	public ResendEntityCache mCache = new ResendEntityCache();

	public MailResender(String subject, String content) {
		super(subject, content);
	}
	
	public MailResender(MailModel model, String sendto, String subject,
			String content) {
		super(model, sendto, subject, content);
	}

	public MailResender(MailModel model, String subject, String content) {
		super(model, subject, content);
	}

	@Override
	protected void onException(Throwable e) {
		super.onException(e);
		mCache.addExceptionHandlerSet(new ResendEntity(this,e));
	}
	
	@Override
	protected void onWorking(/*Message msg*/) throws Exception {
		super.onWorking(/*msg*/);
		try {
			Set<ResendEntity> sethanlder = mCache.getExceptionHandlerSet(null);
			for (ResendEntity entity : sethanlder) {
				String content = AfDateFormat.FULL.format(entity.mSendTime);
				content = "原始时间:"+content +"\r\n\r\n"+entity.mContent+entity.mException;
				new MailSender(mModel,mSendTo,entity.mSubject,content).send();
			}
			sethanlder.clear();
			mCache.putExceptionHandlerSet(sethanlder);
		} catch (Throwable e) {
		}
	}
	
	public static class ResendEntity{
		
		public String mSubject = "";
		public String mContent = "";
		public String mException = "";
		public Date mSendTime = new Date();
		
		public ResendEntity() {
		}
		
		public ResendEntity(MailResender mail,Throwable e) {
			mSubject = mail.mSubject;
			mContent = mail.mContent;
			mException = "\r\n\r\n重发异常:\r\n" + AfExceptionHandler.getExceptionName(e) + 
			"\r\n\r\n重发信息:\r\n" + AfExceptionHandler.getExceptionMessage(e) + 
			"\r\n\r\n重发堆栈:\r\n" + AfExceptionHandler.getPackageStackTraceInfo(e);
		}
	}

	public static class ResendEntityCache extends AfSharedPreference {
		
		private static final String CACHE_KEY = "25262223115182804102";
		private static final String CACHE_NAME = "02979904115182804102";

		public ResendEntityCache() {
			super(AfApplication.getApp(), CACHE_NAME);
		}

		public Set<ResendEntity> getExceptionHandlerSet(Set<ResendEntity> defvalue) {
			Set<String> strset = getStringSet(CACHE_KEY, null);
			if (strset == null) {
				return defvalue;
			}
			Gson json = new Gson();
			Set<ResendEntity> setHandler = new HashSet<ResendEntity>();
			try {
				for (String jvalue : strset) {
					setHandler.add(json.fromJson(jvalue, ResendEntity.class));
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return setHandler;
		}

		public void putExceptionHandlerSet(Set<ResendEntity> sthandler) {
			Gson json = new Gson();
			Set<String> strset = new HashSet<String>();
			for (ResendEntity handler : sthandler) {
				strset.add(json.toJson(handler));
			}
			putStringSet(CACHE_KEY, strset);
		}

		public void addExceptionHandlerSet(ResendEntity handler) {
			Gson json = new Gson();
			Set<String> strset = getStringSet(CACHE_KEY, new HashSet<String>());
			strset.add(json.toJson(handler));
			putStringSet(CACHE_KEY, strset);
		}
	}

}
