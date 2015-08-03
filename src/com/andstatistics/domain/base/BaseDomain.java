package com.andstatistics.domain.base;

import com.andframe.helper.android.AfDesHelper;
import com.andframe.util.java.AfDateGuid;
import com.andframe.util.java.AfMD5;
import com.andrestrequest.http.DefaultRequestHandler;
import com.andrestrequest.http.DefaultRequestHandler.HttpMethod;
import com.andrestrequest.http.Response;
import com.andrestrequest.impl.AbstractRequester;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class BaseDomain<T> extends AbstractRequester {

    protected String controller;

    public BaseDomain(){
        controller = "";
    }
    public BaseDomain(String controller){
        this.controller = controller;
    }

    public Response doRequest(HttpMethod method, String path, Map<String, String> headers, Map<String, Object> params) throws Exception {
        headers = buildToken(headers);
        return handler.doRequest(method,"/"+controller+ path, headers, params);
    }

    public Response doRequest(HttpMethod method, String path) throws Exception {
        return handler.doRequest(method,"/"+controller+ path,buildToken(),null,null);
    }

    public Response doRequest(HttpMethod method, String path, Object body) throws Exception {
        return handler.doRequest(method,"/"+controller+ path, buildToken(), body, null);
    }

    public Response doRequest(HttpMethod method, String path, Map<String, String> headers) throws Exception {
        headers = buildToken(headers);
        return handler.doRequest(method,"/"+controller+ path, headers);
    }

    public Response doRequest(HttpMethod method, String path, Map<String, String> headers, Object body) throws Exception {
        headers = buildToken(headers);
        return handler.doRequest(method,"/"+controller+ path, headers, body);
    }

    public Response doRequest(HttpMethod method, String path, Map<String, String> headers, Object body, Map<String, Object> params) throws Exception {
        headers = buildToken(headers);
        return handler.doRequest(method,"/"+controller+ path, headers, body, params);
    }

    private Map<String, String> buildToken() throws Exception {
        // TODO Auto-generated method stub
        return buildToken(new HashMap<String, String>());
    }

    private Map<String, String> buildToken(Map<String, String> header) throws Exception {
        String strkey = AfMD5.getMD5("");
        AfDesHelper helper = new AfDesHelper(strkey);
        String token = AfDateGuid.NewID();
        token = token.substring(token.length()-3);
        token = token + new StringBuffer(token).reverse();
        header.put("token", helper.encrypt(token));
        return header;
    }
}
