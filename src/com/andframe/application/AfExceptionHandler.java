package com.andframe.application;

import java.io.FileWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.activity.framework.AfActivity;
import com.andframe.caches.AfDurableCache;
import com.andframe.helper.android.AfDeviceInfo;
import com.andframe.model.Exceptional;
import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfDateGuid;

public class AfExceptionHandler implements UncaughtExceptionHandler{

	protected static final String DURABLE_HANDLER = "63214261915190904102";
	protected static final String DURABLE_UNCAUGHT = "02589350915190904102";
	
	protected static boolean mIsShowDialog = false;
	protected static AfExceptionHandler INSTANCE = null;
	protected Thread.UncaughtExceptionHandler mDefaultHandler;

	public AfExceptionHandler() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	public static void register() {
		if(INSTANCE == null){
			INSTANCE = AfApplication.getApp().getExceptionHandler();
		}
	}

	public static AfExceptionHandler getInstance() {
		return INSTANCE;
	}

	public static Exceptional getHandler(Throwable ex,String remark) {
		return getHandler(Thread.currentThread(),ex,remark);
	}
	
	public static Exceptional getHandler(Thread thread,Throwable ex,String remark) {
		Exceptional ehandler = new Exceptional();
		ehandler.Thread = "异常线程：" + thread;
		
		ehandler.Remark = remark;
		ehandler.Name = getExceptionName(ex);
		ehandler.Message = getExceptionMessage(ex);
		ehandler.Stack = getAllStackTraceInfo(ex);
		ehandler.Version = AfApplication.getVersion();
		ehandler.Device = AfDeviceInfo.detDeviceMessage();

		return ehandler;
	}
	
	public static void handleAttach(Throwable ex,String remark) {
		if(INSTANCE != null){
			String handlerid;
			StackTraceElement[] stacks = ex.getStackTrace();
			if(stacks != null && stacks.length > 0){
				handlerid = stacks[0].toString();
			}else{
				handlerid = AfDateGuid.NewID();
			}
			INSTANCE.onHandlerAttachException(ex,remark,handlerid);
		}
	}

	public static void handler(Throwable ex,String remark) {
		if(INSTANCE != null){
			String handlerid;
			StackTraceElement[] stacks = ex.getStackTrace();
			if(stacks != null && stacks.length > 0){
				handlerid = stacks[0].toString();
			}else{
				handlerid = AfDateGuid.NewID();
			}
			INSTANCE.onHandlerException(ex,remark,handlerid);
		}
	}
	protected void onHandlerAttachException(Throwable ex, String remark,String handlerid) {
		// TODO Auto-generated method stub
		try {
			String msg = "时间:" + AfDateFormat.FULL.format(new Date()) +
					"\r\n\r\n备注:\r\n" + "Attach-"+remark +
					"\r\n\r\n异常:\r\n" + getExceptionName(ex) + 
					"\r\n\r\n信息:\r\n" + getExceptionMessage(ex) + 
					"\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) + 
					"\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
			AfDurableCache dc = AfDurableCache.getInstance("attach");
			dc.put(AfDateGuid.NewID(), msg);
			
			
			AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && mIsShowDialog) {
				doShowDialog(activity,"异常捕捉",msg,handlerid);
			}
			
			String path = AfDurableCache.getPath();
			FileWriter writer = new FileWriter(path+"/attach-"+AfDateFormat.FULL.format(new Date()));
			writer.write(msg);
			writer.close();
		} catch (Throwable e) {
			// TODO: handle exception
		}
	}

	protected void onHandlerException(Throwable ex, String remark, String handlerid) {
		// TODO Auto-generated method stub
		try {
			String msg = "时间:" + AfDateFormat.FULL.format(new Date()) +
					"\r\n\r\n备注:\r\n" + remark +
					"\r\n\r\n异常:\r\n" + getExceptionName(ex) + 
					"\r\n\r\n信息:\r\n" + getExceptionMessage(ex) + 
					"\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) + 
					"\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
			AfDurableCache dc = AfDurableCache.getInstance("handler");
			dc.put(AfDateGuid.NewID(), msg);
			
			AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && mIsShowDialog) {
				doShowDialog(activity,"异常捕捉",msg,handlerid);
			}
			
			String path = AfDurableCache.getPath();
			FileWriter writer = new FileWriter(path+"/handler-"+AfDateFormat.FULL.format(new Date()));
			writer.write(msg);
			writer.close();
		} catch (Throwable e) {
			// TODO: handle exception
		}
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable ex) {
		try {
			ex.printStackTrace();
			String msg = "日期:" +AfDateFormat.FULL.format(new Date()) + 
					"\r\n\r\n备注:\r\n" + "程序崩溃" +
					"\r\n\r\n异常:\r\n" + getExceptionName(ex) + 
					"\r\n\r\n信息:\r\n" + getExceptionMessage(ex) + 
					"\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) + 
					"\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
			AfDurableCache dc = AfDurableCache.getInstance("error");
			dc.put(AfDateGuid.NewID(), msg);
			
			AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && mIsShowDialog) {
				doShowDialog(activity, "程序崩溃了", msg,new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						// TODO Auto-generated method stub
						mDefaultHandler.uncaughtException(thread, ex);
						return false;
					}
				});
			}else{
				mDefaultHandler.uncaughtException(thread, ex);
			}
			String path = AfDurableCache.getPath();
			FileWriter writer = new FileWriter(path+"/error-"+AfDateFormat.FULL.format(new Date()));
			writer.write(msg);
			writer.close();
			return;
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(mDefaultHandler != null){
			mDefaultHandler.uncaughtException(thread, ex);
		}else{
			
		}
	}

	public static String getAllStackTraceInfo(Throwable ex) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("本地推栈:\r\n");
		sb.append(getPackageStackTraceInfo(ex));
		sb.append("\r\n全部堆栈:\r\n");
		sb.append(getStackTraceInfo(ex));
		return sb.toString();
	}

	public static String getExceptionMessage(Throwable ex) {
		// TODO Auto-generated method stub
		String message = ex.getMessage();
		ex = ex.getCause();
		if (ex != null) {
			String format = "%s\r\n-------------------------->\r\n%s";
			message = String.format(format, getExceptionMessage(ex),message);
		}
		return message;
	}

	public static String getExceptionName(Throwable ex) {
		// TODO Auto-generated method stub
		String name = ex.getClass().toString();
		ex = ex.getCause();
		if (ex != null) {
			String format = "%s -> %s";
			name = String.format(format, getExceptionName(ex),name);
		}
		return name;
	}

	public static String getPackageStackTraceInfo(Throwable ex) {
		// TODO Auto-generated method stub
		String TraceInfo = "";
		String Package = AfApplication.getApp().getPackageName() + ".";
		for (StackTraceElement stack : ex.getStackTrace()) {
			if (stack.getClassName().startsWith(Package)) {
				TraceInfo += stack.toString() + "\r\n";
			}
		}
		if(TraceInfo.length() > 0){
			return TraceInfo;
		}
		if (ex.getCause() != null) {
			return getPackageStackTraceInfo(ex.getCause());
		}
		return TraceInfo;
	}
	
	public static String getStackTraceInfo(Throwable ex) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getClass().toString());
		builder.append("\r\n");
		for (StackTraceElement stack : ex.getStackTrace()) {
			builder.append(stack);
			builder.append("\r\n");
		}
		String TraceInfo = builder.toString();
		ex = ex.getCause();
		if (ex != null) {
			String format = "%s\r\n<--------------------------\r\n%s";
			TraceInfo = String.format(format,TraceInfo,getStackTraceInfo(ex));
		}
		return TraceInfo;
	}
	
	public static HashMap<String, String> mDialogMap = new HashMap<String, String>();

	public static synchronized void doShowDialog(Context activity, String title, String msg,Callback callback, Looper looper,String id) {
		// TODO Auto-generated method stub
		if(mDialogMap.containsKey(id)){
			return;
		}
		final String tid = id; 
		final String ttitle = title; 
		final String tmsg = msg;
		final Callback tcallback = callback;
		final Context tactivity = activity; 
		final Looper tLooper = looper; 
		new Thread(){
			public void run() {
				try {
					Looper.prepare();
					mDialogMap.put(tid, ttitle);
					Builder dialog = new Builder(tactivity);
					dialog.setTitle(ttitle);
					dialog.setCancelable(false);
					dialog.setMessage(tmsg);
					dialog.setNeutralButton("我知道了", 
						new OnClickListener() {
							@Override
							@SuppressLint("HandlerLeak")
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								mDialogMap.remove(tid);
								if(tLooper != null && tcallback != null){
									new Handler(tLooper, tcallback).sendMessage(Message.obtain());
								}else if(tcallback != null){
									tcallback.handleMessage(Message.obtain());
								}
								Handler handler = new Handler(){
									@Override
									public void handleMessage(Message msg) {
										// TODO Auto-generated method stub
										Looper.myLooper().quit();
									}
								};
								handler.sendMessageDelayed(Message.obtain(), 300);
							}
						});
					dialog.show();
					Looper.loop();
				} catch (Throwable e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	public static void doShowDialog(Context activity, String title, String msg,String id) {
		// TODO Auto-generated method stub
		doShowDialog(activity, title, msg, null,null,id);
	}
	
	public static void doShowDialog(Context activity, String title, String msg,Callback callback) {
		// TODO Auto-generated method stub
		doShowDialog(activity, title, msg, callback,null,AfDateGuid.NewID());
	}
	
	public static void doShowDialog(Context activity, String title, String msg,Callback callback,String id) {
		// TODO Auto-generated method stub
		doShowDialog(activity, title, msg, callback,null,id);
	}

}
