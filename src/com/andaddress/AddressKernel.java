package com.andaddress;


import com.andframe.application.AfApplication;
import com.andframe.helper.android.AfDesHelper;
import com.andframe.thread.AfTask;
import com.andframe.util.java.AfMD5;
import com.andrestful.api.HttpMethod;
import com.andrestful.api.RequestHandler;
import com.andrestful.api.Response;
import com.andrestful.config.Config;
import com.andrestful.http.MultiRequestHandler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			AfApplication.postTask(new AfTask() {
				@Override
				protected void onWorking(/*Message msg*/) throws Exception {
					work();
					AfApplication.getApp().onUpdateAppinfo();
					//AppinfoMail.updateAppinfo();
					loading = false;
				}

				@Override
				protected void onException(Throwable e) {
					super.onException(e);
					loading = false;
				}
			});
		}
	}

	static {
		try {
			String key = AfMD5.getMD5("");
			AfDesHelper helper = new AfDesHelper(key);
			url = helper.decrypt(url);
		} catch (Throwable e) {
		}
	}
	
	public static void initialize() {
		loading();
	}

	/**
	 * 获取（访问网络，延迟，异常）
	 * @throws IOException
	 */
	public static String work() throws Exception {
		Config config = new Config();
		config.charset = "GBK";
		RequestHandler handler = MultiRequestHandler.getInstance(config);
		Response response = handler.doRequest(HttpMethod.GET, url);
		Pattern compile = Pattern.compile("<iframe\\s*src=[\"|'](\\S*)[\"|']");
		Matcher matcher = compile.matcher(response.getBody());
		if (matcher.find()) {
			String url = matcher.group(1);
			response = handler.doRequest(HttpMethod.GET, url);
			compile = Pattern.compile(">.*\\[(.+)\\].*：([\\u4e00-\\u9fa5]+)\\s*([\\u4e00-\\u9fa5]+)\\s*<");
			matcher = compile.matcher(response.getBody());
			if (matcher.find()) {
				address = matcher.group(1);
				city = matcher.group(2);
				operator = matcher.group(3);
				found = true;
				return city;
			}
		}
		return "获取失败";
	}
	
	public static String getAddress() {
		if (!found) {
			loading();
		}
		return address;
	}
	
	public static String getCity() {
		if (!found) {
			loading();
		}
		return city;
	}
	
	public static String getOperator() {
		if (!found) {
			loading();
		}
		return operator;
	}
}
