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
		// TODO Auto-generated constructor stub
	}
	
	public MailResender(MailModel model, String sendto, String subject,
			String content) {
		super(model, sendto, subject, content);
		// TODO Auto-generated constructor stub
	}

	public MailResender(MailModel model, String subject, String content) {
		super(model, subject, content);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onException(Throwable e) {
		// TODO Auto-generated method stub
		super.onException(e);
		mCache.addExceptionHandlerSet(new ResendEntity(this,e));
	}
	
	@Override
	protected void onWorking(Message msg) throws Exception {
		// TODO Auto-generated method stub
		super.onWorking(msg);
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
			// TODO: handle exception
		}
	}
	
	public static class ResendEntity{
		
		public String mSubject = "";
		public String mContent = "";
		public String mException = "";
		public Date mSendTime = new Date();
		
		public ResendEntity() {
			// TODO Auto-generated constructor stub
		}
		
		public ResendEntity(MailResender mail,Throwable e) {
			// TODO Auto-generated constructor stub
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
			// TODO Auto-generated constructor stub
		}

		public Set<ResendEntity> getExceptionHandlerSet(Set<ResendEntity> defvalue) {
			// TODO Auto-generated method stub
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
				// TODO: handle exception
				e.printStackTrace();
			}
			return setHandler;
		}

		public void putExceptionHandlerSet(Set<ResendEntity> sthandler) {
			// TODO Auto-generated method stub
			Gson json = new Gson();
			Set<String> strset = new HashSet<String>();
			for (ResendEntity handler : sthandler) {
				strset.add(json.toJson(handler));
			}
			putStringSet(CACHE_KEY, strset);
		}

		public void addExceptionHandlerSet(ResendEntity handler) {
			// TODO Auto-generated method stub
			Gson json = new Gson();
			Set<String> strset = getStringSet(CACHE_KEY, new HashSet<String>());
			strset.add(json.toJson(handler));
			putStringSet(CACHE_KEY, strset);
		}
	}

}
