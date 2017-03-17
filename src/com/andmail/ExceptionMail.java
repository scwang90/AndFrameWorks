package com.andmail;

public class ExceptionMail /*extends AppinfoMail*/ {

//	private String md5 = AfApplication.getApp().getDesKey();
//	private Exceptional mExceptional;
//
//	public ExceptionMail(Exceptional ex) {
//		super(title(ex), "");
//		mailtype = "异常捕捉";
//		mExceptional = ex;
//		md5 = AfMD5.getMD5(ex.Name+ex.Message+ex.Stack);
//	}
//
//	private static String title(Exceptional ex) {
//		if (ex.Message == null || ex.Message.length() == 0) {
//			return ex.Name;
//		}else if(ex.Message.length() > 32){
//			return ex.Name;
//		}
//		return ex.Message;
//	}
//
//	protected void packAppinfo() {
//		mContent = "发送时间:" +AfDateFormat.FULL.format(mExceptional.RegDate) +
//				"\r\n\r\n异常名称:\r\n" + mExceptional.Name +
//				"\r\n\r\n异常信息:\r\n" + mExceptional.Message +
//				"\r\n\r\n备注信息:\r\n" + mExceptional.Remark;
//		super.packAppinfo();
//		mContent = mContent +
//				"\r\n\r\n异常线程:\r\n" + mExceptional.Thread +
//				"\r\n\r\n" + mExceptional.Stack;
//	}
//
//	@Override
//	public void send() throws Exception {
//		AfPrivateCaches cache = AfPrivateCaches.getInstance();
//		if (cache.get(md5, String.class) == null) {//标记相同错误只发送一次
//			super.send();
//			cache.put(md5, md5);
//		}
//	}
//
//	@Override
//	protected void onException(Throwable e) {
//		super.onException(e);
//		AfPrivateCaches.getInstance().put(md5, md5);
//	}
}
