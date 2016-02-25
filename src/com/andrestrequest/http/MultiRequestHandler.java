package com.andrestrequest.http;

import com.andframe.util.java.AfStringUtil;
import com.andrestrequest.AndRestConfig;
import com.andrestrequest.http.apache.URIBuilder;
import com.andrestrequest.http.api.HttpMethod;
import com.andrestrequest.http.api.RequestHandler;
import com.andrestrequest.http.config.Config;
import com.andrestrequest.http.config.Loader;
import com.andrestrequest.util.GsonUtil;
import com.andrestrequest.util.StringUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Handler for building and making requests, uses {@link DefaultResponseHandler}
 * to process the response
 */
@SuppressWarnings("deprecation")
public class MultiRequestHandler extends RequestHandler {

    public static boolean DEBUG = false;

    private DefaultHttpClient httpClient;

    private Config config;

    public MultiRequestHandler() {
        // singleton
        config = new Config();//Loader.load("config.properties");
    }

    public MultiRequestHandler(Config config) {
        this.config = config;
    }

    public MultiRequestHandler(String properties) {
        // singleton
        config = Loader.load(properties);
    }

    public static MultiRequestHandler getInstance() {
        return new MultiRequestHandler();
    }

    public static MultiRequestHandler getInstance(String properties) {
        return new MultiRequestHandler(properties);
    }

    public static MultiRequestHandler getInstance(Config config) {
        return new MultiRequestHandler(config);
    }

    /**
     * * Make the request to Xively API and return the response string
     *
     * @param method  http request methods
     * @param path    restful app path
     * @param body    objects to be parsed as body for api call
     * @param params  key-value of params for api call
     * @return response string
     * @throws HttpException
     */
    public synchronized Response doRequest(HttpMethod method, String path, Map<String, String> headers,
                                           Object body, Map<String, Object> params) throws Exception {
        Response response = null;
        HttpRequestBase request = buildRequest(method, path, headers, body, params);

        if (DEBUG) {
            System.out.println(request);
            for (Header header : request.getAllHeaders()) {
                System.out.println(header);
            }
            if (body != null) {
                System.out.println(EntityUtils.toString(getEntity(body), config.charset));
            }
        }
        response = getClient().execute(request, new ResponseHandler<Response>() {
            @Override
            public Response handleResponse(HttpResponse response) throws IOException {
                int statusCode = response.getStatusLine().getStatusCode();
                if (!isHttpStatusOK(statusCode)) {
                    String errorDetail = null;
                    try {
                        errorDetail = EntityUtils.toString(response.getEntity(), AndRestConfig.getCharset());
                    } catch (IOException swallow) {
                    }
                    throw new com.andrestrequest.http.HttpException(
                            "Http response "+statusCode,
                            statusCode, errorDetail);
                }

                Response retval = new Response(statusCode);

                retval.setBody(parseHttpEntity(response.getEntity()));

                Map<String, String> headers = new HashMap<String, String>();
                for (Header header : response.getAllHeaders()) {
                    headers.put(header.getName(), header.getValue());
                }
                retval.setHeaders(headers);

                return retval;
            }
        });

        return response;
    }

    private DefaultHttpClient getClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.getInstance().getHttpClient();
        }
        return httpClient;
    }

    private HttpRequestBase buildRequest(HttpMethod method, String path, Map<String, String> headers,Object body, Map<String, Object> params) throws UnsupportedEncodingException {
        Config.AcceptedMediaType mediaType = config.responseMediaType;

        HttpRequestBase request = null;
        StringEntity entity = getEntity(body);
        switch (method) {
            case DELETE:
                request = new HttpDelete();
                break;
            case GET:
                request = new HttpGet();
                break;
            case POST:
                request = new HttpPost();
                if (entity != null) {
                    ((HttpPost) request).setEntity(entity);
                }
                break;
            case PUT:
                request = new HttpPut();
                if (entity != null) {
                    ((HttpPut) request).setEntity(entity);
                }
                break;
        }

        try {
            if (!path.toLowerCase(Locale.ENGLISH).startsWith("http")) {
                URIBuilder builder = buildUri(method, path, params, mediaType);
                request.setURI(builder.build());
            } else {
                if (params != null && !params.isEmpty()) {
                    for (Entry<String, Object> param : params.entrySet()) {
                        if (path.indexOf('?') > 0) {
                            path = path + "&" + param.getKey() + "=" + param.getValue();
                        } else {
                            path = path + "?" + param.getKey() + "=" + param.getValue();
                        }
                    }
                }
                request.setURI(new URI(path));
            }
        } catch (URISyntaxException e) {
            throw new RequestInvalidException("Invalid URI requested.", e);
        }
        /**
         * 设置 Header
         */
        if (headers != null && !headers.isEmpty()) {
            int index = 0;
            Header[] header = new Header[headers.size()];
            for (Entry<String, String> element : headers.entrySet()) {
                header[index++] = new BasicHeader(element.getKey(), element.getValue());
            }
            request.setHeaders(header);
        }
        return request;
    }

    private URIBuilder buildUri(HttpMethod method, String path, Map<String, Object> params, Config.AcceptedMediaType mediaType) throws UnsupportedEncodingException {
        URIBuilder uriBuilder = new URIBuilder();
        String host = config.ip;
        if (AfStringUtil.isNotEmpty(config.port)) {
            host += ":" + config.port;
        }
        uriBuilder.setScheme("http").setHost(host).setPath("/" + config.version + path);
        if (params != null && !params.isEmpty()) {
            for (Entry<String, Object> param : params.entrySet()) {
                uriBuilder.addParameter(param.getKey(), StringUtil.toString(param.getValue()));
            }
        }
        return uriBuilder;
    }

    private StringEntity getEntity(Object body) {
        Config.AcceptedMediaType mediaType = config.responseMediaType;
        String json = null;//ParserUtil.toJson(body);

        if (body instanceof JSONObject || body instanceof String) {
            // 封装一个 JSON 对象
            json = body.toString();
        } else if (body != null) {
            json = GsonUtil.toJson(body);
        } else {
            return null;
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(json, config.charset);
            entity.setContentType(mediaType.contentType);
        } catch (UnsupportedEncodingException e) {
            throw new RequestInvalidException(
                    "Unable to encode json string for making request.", e);
        }
        return entity;
    }

    private boolean isHttpStatusOK(int statusCode) {
        if (statusCode != config.successcode) {
            return false;
        }
        return true;
    }

    private String parseHttpEntity(HttpEntity entity) throws IOException{
        String response = EntityUtils.toString(entity, AndRestConfig.getCharset());
        if (DEBUG)
        {
            System.out.println("Handling response "+response);
        }
        if (!config.jsonframework || config.status==null || config.status_ok==null || config.result == null || config.message == null) {
            return response;
        }
        try {
            JSONObject object = new JSONObject(response);
            if (config.status_ok.equals("" + object.get(config.status))) {
                return object.get(config.result).toString();
            } else {
                String errormessage = object.get(config.message).toString();
                try {
                    ErrorMessage message = GsonUtil.toObject(errormessage, config.ErrorMessageClass);
                    throw new ServerCodeException(message);
                } catch (ServerCodeException e) {
                    throw e;
                } catch (Throwable e) {
                    throw new ServerException(errormessage);
                }
            }
        } catch (Throwable e) {
            throw new ServerException(response);
        }
    }
}
