package com.andaddress;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.andframe.helper.android.AfDesHelper;
import com.andframe.util.java.AfMD5;

public class AddressKernel {

	//private static String url = "http://iframe.ip138.com/ic.asp";
	private static String url = "1591b7dba62df3fc2a5fe7ccfe6a22ae9bafcfaefbd94b1d";
	//"http://www.ip138.com/";
	private static Document mDocument;

	static {
		try {
			String key = AfMD5.getMD5("");
			AfDesHelper helper = new AfDesHelper(key );
			url = helper.decrypt(url);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void initialize() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				try {
					work();
//					AppinfoMail.updateAppinfo();
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}
	/**
	 * 获取（访问网络，延迟，异常）
	 * @throws IOException
	 */
	public static void work() throws Exception {
		// TODO Auto-generated method stub
		mDocument = Jsoup.connect(url).get();
		Element iframe = mDocument.select("iframe").first();
		mDocument = Jsoup.connect(iframe.attr("src")).get();
	}
	
	public static String getAddress() {
		// TODO Auto-generated method stub
		if (mDocument != null) {
			String text = mDocument.text();
			try {
				text = text.substring(text.indexOf('[')+1);
				text = text.substring(0, text.indexOf(']')-1);
			} catch (Throwable e) {
				// TODO: handle exception
			}
			return text;
		}
		return "未获取";
	}
	
	public static String getCity() {
		// TODO Auto-generated method stub
		if (mDocument != null) {
			String text = mDocument.text();
			try {
				text = text.substring(text.lastIndexOf("：")+1);
				text = text.substring(0, text.indexOf(' ')-1);
			} catch (Throwable e) {
				// TODO: handle exception
			}
			return text;
		}
		return "未获取";
	}
	
	public static String getOperator() {
		// TODO Auto-generated method stub
		if (mDocument != null) {
			String text = mDocument.text();
			try {
				text = text.substring(text.lastIndexOf(' ')+1);
			} catch (Throwable e) {
				// TODO: handle exception
				return text +":"+e.getClass().toString() +":"+e.getMessage();
			}
			return text;
		}
		return "未获取";
	}
}
