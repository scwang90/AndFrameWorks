package com.andaddress;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.andframe.application.AfApplication;
import com.andframe.helper.android.AfDesHelper;
import com.andframe.util.java.AfMD5;
import com.andrestrequest.AndRestConfig;
import com.andrestrequest.http.DefaultRequestHandler;
import com.andrestrequest.http.DefaultRequestHandler.HttpMethod;
import com.andrestrequest.http.Response;

public class AddressKernel {
	
	private static final String NOT_FIND = "未获取";
	private static boolean found = false;
	private static boolean loading = false;
	private static String address = NOT_FIND;
	private static String city = NOT_FIND;
	private static String operator = NOT_FIND;
	
//	private static String url = "http://www.ip138.com";
	private static String url = "1591b7dba62df3fc2a5fe7ccfe6a22ae4771b69463d76844";

	private static void loading(){
		if (!loading) {
			loading = true;
			new Thread() {
				public void run() {
					try {
						work();
						AfApplication.getApp().onUpdateAppinfo();
						//AppinfoMail.updateAppinfo();
					} catch (Exception e) {
						// TODO: handle exception
					}
					loading = false;
				};
			}.start();
		}
	}

	static {
		try {
			String key = AfMD5.getMD5("");
			AfDesHelper helper = new AfDesHelper(key);
			url = helper.decrypt(url);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void initialize() {
		// TODO Auto-generated method stub
		loading();
	}

	/**
	 * 获取（访问网络，延迟，异常）
	 * @throws IOException
	 */
	public static void work() throws Exception {
		// TODO Auto-generated method stub
		String charset = AndRestConfig.getCharset();
		try {
			AndRestConfig.setCharset("GBK");
			DefaultRequestHandler handler = DefaultRequestHandler.getInstance();
			HttpMethod method = HttpMethod.GET;
			Response response = handler.doRequest(method , url);
			Pattern compile = Pattern.compile("<iframe\\s*src=[\"|'](\\S*)[\"|']");
			Matcher matcher = compile.matcher(response.getBody());
			if(matcher.find()){
				String url = matcher.group(1);
				response = handler.doRequest(method , url);
				compile = Pattern.compile(">.*\\[(.+)\\].*：([\\u4e00-\\u9fa5]+)\\s*([\\u4e00-\\u9fa5]+)\\s*<");
				matcher = compile.matcher(response.getBody());
				if(matcher.find()){
					address = matcher.group(1);
					city = matcher.group(2);
					operator = matcher.group(3);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally{
			AndRestConfig.setCharset(charset);
		}
	}
	
	public static String getAddress() {
		// TODO Auto-generated method stub
		if (!found) {
			loading();
		}
		return address;
	}
	
	public static String getCity() {
		// TODO Auto-generated method stub
		if (!found) {
			loading();
		}
		return city;
	}
	
	public static String getOperator() {
		// TODO Auto-generated method stub
		if (!found) {
			loading();
		}
		return operator;
	}
}
