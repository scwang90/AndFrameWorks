package com.andrestrequest.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.andrestrequest.http.DefaultRequestHandler;

/**
 * Http请求 抽象类
 * @author 树朾
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
