package com.andmail.kernel;

import android.content.Context;
import android.content.SharedPreferences;

import com.andmail.api.model.MailSenderModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 可重发的邮件发送器
 */
public class MailResender extends AmMailSender {

	private static final String CACHE_KEY = "25262223115182804102";
	private static final String CACHE_NAME = "02979904115182804102";

	private ResendEntityCache mCache;

	MailResender(Context context, String subject, String content) {
		super(subject, content);
		mCache = new ResendEntityCache(context);
	}
	
	MailResender(Context context, MailSenderModel model, String subject, String content) {
		super(model, subject, content);
		mCache = new ResendEntityCache(context);
	}

	@Override
	public void onTaskException(Exception e) {
		super.onTaskException(e);
		try {
			mCache.addExceptionHandlerSet(new ResendEntity(this,e));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		super.doInBackground(params);
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
			Set<ResendEntity> sethanlder = mCache.getExceptionHandlerSet(null);
			for (ResendEntity entity : sethanlder) {
				String content = format.format(entity.mSendTime);
				content = "原始时间:"+content +"\r\n\r\n"+entity.mContent+entity.mException;
				new AmMailSender(mModel,entity.mSubject,content).send();
				newMailKerneler(mModel).create(mModel.getFrom(), mModel.getSendto()).send();
			}
			sethanlder.clear();
			mCache.putExceptionHandlerSet(sethanlder);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static class ResendEntity implements Serializable {
		
		String mSubject = "";
		String mContent = "";
		String mException = "";
		Date mSendTime = new Date();

		ResendEntity(MailResender mail, Throwable e) {
			mSubject = mail.mSubject;
			mContent = mail.mContent;
			mException = "\r\n\r\n重发异常:\r\n" + e.getClass() +
			"\r\n\r\n重发信息:\r\n" + e.getMessage() +
			"\r\n\r\n重发堆栈:\r\n" + e.getStackTrace()[0];
		}

		int size() {
			return mSubject.length() * 2 + mContent.length() * 2 + mException.length() * 2 + 8;
		}
	}

	private static class ResendEntityCache {

		private final SharedPreferences mShared;

		ResendEntityCache(Context context) {
			mShared = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
		}

		Set<ResendEntity> getExceptionHandlerSet(Set<ResendEntity> defvalue) {
			Set<ResendEntity> setHandler = new HashSet<>();
			Map<String, ?> all = mShared.getAll();
			try {
				for (Map.Entry<String, ?> entry : all.entrySet()) {
                    byte[] bytes = entry.getValue().toString().getBytes("Unicode");
                    ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    setHandler.add((ResendEntity) stream.readObject());
                }
			} catch (Exception e) {
				e.printStackTrace();
				return defvalue;
			}
			return setHandler;
		}

		void putExceptionHandlerSet(Set<ResendEntity> sthandler) throws Exception {
			SharedPreferences.Editor edit = mShared.edit();
			edit.clear();
			for (ResendEntity entity : sthandler) {
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(entity.size());
				ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
				outputStream.writeObject(entity);
				byte[] bytes = byteStream.toByteArray();
				edit.putString(getMD5(bytes), new String(bytes, "Unicode"));
			}
			edit.commit();
		}

		void addExceptionHandlerSet(ResendEntity entity) throws Exception {
			SharedPreferences.Editor edit = mShared.edit();
			edit.clear();
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(entity.size());
			ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
			outputStream.writeObject(entity);
			byte[] bytes = byteStream.toByteArray();
			edit.putString(getMD5(bytes), new String(bytes, "utf-8"));
			edit.commit();
		}
	}

	public static String getMD5(byte[] bytes) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(bytes);
		byte[] m = md5.digest();//加密
		return getString(m);
	}

	private static String getString(byte[] b){
		StringBuilder sb = new StringBuilder();
		for (byte aB : b) {
			sb.append(aB);
		}
		return sb.toString();
	}
}
