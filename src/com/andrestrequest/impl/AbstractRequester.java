package com.andrestrequest.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.andrestrequest.http.DefaultRequestHandler;

/**
 * @Description: Http请求 抽象类
 * @Author: scwang
 * @Version: V1.0, 2015-3-6 下午7:36:01
 * @Modified: 初次创建AbstractRequester类
 */
public abstract class AbstractRequester {

	protected DefaultRequestHandler requestHandler = DefaultRequestHandler.getInstance(); 

	protected Map<String, String> userkeyHeader(String userkey) {
		// TODO Auto-generated method stub
		Map<String ,String> headers = new LinkedHashMap<String, String>();
//		headers.put("userKey", userkey);
		return headers;
	}
	
	protected Map<String, Object> pageParameter(int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Map<String ,Object> params = new LinkedHashMap<String, Object>();
//		params.put("currentPage", currentPage);
//		params.put("pageSize", pageSize);
		return params;
	}
}
