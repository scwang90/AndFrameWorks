package com.andsoap;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.UnsafeAllocator;
import com.andframe.exception.AfToastException;

public class AfSoapService<T> {

	public static class TimestampTypeAdapter implements
			JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

		public JsonElement serialize(Timestamp src, Type arg1,
				JsonSerializationContext arg2) {
			String dateFormatAsString = format.format(new Date(src.getTime()));
			return new JsonPrimitive(dateFormatAsString);
		}

		public Timestamp deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			if (!(json instanceof JsonPrimitive)) {
				throw new JsonParseException(
						"The date should be a string value");
			}
			try {
				Date date = format.parse(json.getAsString());
				return new Timestamp(date.getTime());
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
		}

	}

	public static class Properties extends LinkedHashMap<String, String> {

		private static final long serialVersionUID = -5559158778035005096L;

		public Properties putProperty(String key, String value) {
			put(key, value);
			return this;
		}

		public Properties putObject(String key, Object value) {
			put(key, mGson.toJson(value));
			return this;
		}

		public Iterable<Entry<String, String>> getIterable() {
			// TODO Auto-generated method stub
			return entrySet();
		}
	}

	public static class SoapHolder {
		public String Exception = null;
		public String ReturnValue = null;
		public List<String> ListValue = null;

		private SoapHolder() {
		}

		public String getReturnValue() throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			return new String(Base64.decode(ReturnValue), "UTF-8");
		}

		public int getReturnInt() throws Exception {
			// TODO Auto-generated method stub
			return modelFromJsonSafed(getReturnValue(), int.class);
		}

		public long getReturnLong() throws Exception {
			// TODO Auto-generated method stub
			return modelFromJsonSafed(getReturnValue(), long.class);
		}

		public boolean getReturnBoolean() throws Exception {
			// TODO Auto-generated method stub
			return modelFromJsonSafed(getReturnValue(), boolean.class);
		}

		public <T> T getReturn(Class<T> clazz) throws Exception {
			// TODO Auto-generated method stub
			return modelFromJsonSafed(getReturnValue(), clazz);
		}

		public List<String> getListValue() throws Exception {
			// TODO Auto-generated method stub
			List<String> list = new ArrayList<String>();
			for (String string : ListValue) {
				list.add(new String(Base64.decode(string), "UTF-8"));
			}
			return list;
		}

		public String getException() throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			return new String(Base64.decode(Exception), "UTF-8");
		}
	}

	/**
	 * 日期显示格式 "yyyy-MM-dd HH:mm:ss"
	 */
	public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final DateFormat format = new SimpleDateFormat(FORMAT_DATE,
			Locale.US);
	/**
	 * 枚举DEBUG模式
	 */
	public static final int DEBUG_USER = 0; // 用户使用 调试方式：邮件发送错误信息
	public static final int DEBUG_JAVA = 1; // 电脑调试 调试方式：使用java连接 localhost
	public static final int DEBUG_PHONE = 2; // 手机调试 调试方式：使用simulator连接 10.0.0.2
	public static final int DEBUG_TEST = 3; // 手机调试 测试数据：数据接口返回测试数据 不连接网络

	/**
	 * 设置服务
	 * @param namespace
	 *            like "http://tempuri.org/"
	 * @param svc
	 *            like "Service.svc"
	 * @param sinterface
	 *            like "IService/"
	 */
	public static void setServer(String namespace, String svc, String sinterface) {
		AfSoapService.SERVICE_SVC = svc;
		AfSoapService.SERVICE_INTERFACE = sinterface;
		AfSoapService.SERVICE_NAMESPACE = namespace;
		WSDLURL = "http://" + SERVER_ADDRESS + ":" + SERVER_PORT + "/" + svc;
		SOAP_ACTION = namespace + sinterface;
		LOCAL_WSDLURL = "http://localhost:59176/" + svc;
		PHONE_WSDLURL = "http://10.0.2.2:21754/" + svc;
		MODE_WSDLURL = new String[] { WSDLURL, LOCAL_WSDLURL, PHONE_WSDLURL,
				WSDLURL };
		Transport = new HttpTransportSE(MODE_WSDLURL[debugmode]);
	}

	public static void setServer(String ip, int port) {
		AfSoapService.SERVER_PORT = port;
		AfSoapService.SERVER_ADDRESS = ip;

		WSDLURL = "http://" + ip + ":" + port + "/" + SERVICE_SVC;
		MODE_WSDLURL = new String[] { WSDLURL, LOCAL_WSDLURL, PHONE_WSDLURL,
				WSDLURL };

		Transport = new HttpTransportSE(MODE_WSDLURL[debugmode]);
	}

	protected static int SERVER_PORT = 8088;
	protected static String SERVER_ADDRESS = "127.0.0.1";

	protected static String SERVICE_SVC = "Service.svc";
	protected static String SERVICE_INTERFACE = "IService/";
	protected static String SERVICE_NAMESPACE = "http://tempuri.org/";// WebService

	protected static String WSDLURL = "http://" + SERVER_ADDRESS + ":"
			+ SERVER_PORT + "/" + SERVICE_SVC;
	protected static String SOAP_ACTION = SERVICE_NAMESPACE + SERVICE_INTERFACE;

	protected static String LOCAL_WSDLURL = "http://localhost:59176/"
			+ SERVICE_SVC;

	protected static String PHONE_WSDLURL = "http://10.0.2.2:21754/"
			+ SERVICE_SVC;

	protected static int debugmode = DEBUG_USER;

	protected static String[] MODE_WSDLURL = new String[] { WSDLURL,
			LOCAL_WSDLURL, PHONE_WSDLURL, WSDLURL };

	protected static HttpTransportSE Transport = null;// new
														// HttpTransportSE(MODE_WSDLURL[debugmode]);
	protected static SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER11);

	protected Class<T> mClass = null;
	protected static Gson mGson = getGson();

	public static Gson getGson() {
		if (mGson == null) {
			mGson = new GsonBuilder()
					.registerTypeAdapter(Timestamp.class,
							new TimestampTypeAdapter())
					.setDateFormat(FORMAT_DATE).create();
		}
		return mGson;
	}

	public AfSoapService(Class<T> clazz) {
		this.mClass = clazz;
	}

	public String getMothedName(String shortmothedname) {
		String str = mClass.getName();
		str = str.substring(str.lastIndexOf(".") + 1);
		return str + shortmothedname;
	}

	public SoapObject getRequest(String shortmothedname) {
		return new SoapObject(SERVICE_NAMESPACE, getMothedName(shortmothedname));
	}

	public SoapHolder getResponse(String mothedname, Properties properties)
			throws Exception {
		// TODO Auto-generated method stub
		SoapObject request = this.getRequest(mothedname);
		if (properties != null) {
			for (Entry<String, String> propertie : properties.getIterable()) {
				request.addProperty(propertie.getKey(), propertie.getValue());
			}
		}
		return this.getResponse(mothedname, request);
	}

	public SoapHolder getResponse(String mothedname, String[][] properties)
			throws Exception {
		SoapObject request = this.getRequest(mothedname);
		if (properties != null) {
			for (int i = 0; i < properties.length; i++) {
				request.addProperty(properties[i][0], properties[i][1]);
			}
		}
		return this.getResponse(mothedname, request);
	}

	public SoapHolder getResponse(String shortmothedname, SoapObject request)
			throws Exception {
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			String soapaction = SOAP_ACTION;
			soapaction += getMothedName(shortmothedname);
			Transport.call(soapaction, envelope);
			Object response = envelope.getResponse();
			SoapHolder hoder = modelFromJsonSafed(response.toString(),
					SoapHolder.class);
			if (hoder.Exception != null && !hoder.Exception.equals("")) {
				throw new Exception(hoder.getException());
			}
			return hoder;
		} catch (Error er) {
			throw new Exception(er.getMessage(), er);
		} catch (JsonSyntaxException ex) {
			throw new AfToastException("服务器返回格式异常");
		} catch (UnsupportedEncodingException ex) {
			throw new AfToastException("服务器返回编码异常");
		} catch (XmlPullParserException ex) {
			throw new AfToastException("服务器返回异常", ex);
		} catch (ConnectException ex) {
			throw new AfToastException("连接服务器失败:" + ex.getMessage(), ex);
		} catch (FileNotFoundException ex) {
			throw new AfToastException("服务器地址错误:" + ex.getMessage(), ex);
		} catch (SoapFault ex) {
			throw new AfToastException("服务器反馈:" + ex.faultstring, ex);
		}
	}

	public T getModel(String mothedname, String[][] properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, mClass);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public List<T> getList(String mothedname, String[][] properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		List<T> models = new ArrayList<T>();
		try {
			for (String string : holder.getListValue()) {
				models.add(modelFromJsonSafed(string, mClass));
			}
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
		return models;
	}

	public int getInt(String mothedname, String[][] properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, int.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public long getLong(String mothedname, String[][] properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, long.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public boolean getBoolean(String mothedname, String[][] properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, boolean.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public SoapHolder doFunction(String mothedname, String[][] properties)
			throws Exception {
		return this.getResponse(mothedname, properties);
	}

	public T getModel(String mothedname, Properties properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, mClass);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public List<T> getList(String mothedname, Properties properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		List<T> models = new ArrayList<T>();
		try {
			for (String string : holder.getListValue()) {
				models.add(modelFromJsonSafed(string, mClass));
			}
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
		return models;
	}

	public int getInt(String mothedname, Properties properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, int.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public long getLong(String mothedname, Properties properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, long.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public boolean getBoolean(String mothedname, Properties properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, boolean.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public SoapHolder doFunction(String mothedname, Properties properties)
			throws Exception {
		return this.getResponse(mothedname, properties);
	}

	public T getModel(String mothedname, HashMap<String, String> properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, mClass);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public List<T> getList(String mothedname, HashMap<String, String> properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		List<T> models = new ArrayList<T>();
		try {
			for (String string : holder.getListValue()) {
				models.add(modelFromJsonSafed(string, mClass));
			}
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
		return models;
	}

	public SoapHolder getResponse(String mothedname,
			HashMap<String, String> properties) throws Exception {
		// TODO Auto-generated method stub
		SoapObject request = this.getRequest(mothedname);
		if (properties != null) {
			for (Entry<String, String> propertie : properties.entrySet()) {
				request.addProperty(propertie.getKey(), propertie.getValue());
			}
		}
		return this.getResponse(mothedname, request);
	}

	public int getInt(String mothedname, HashMap<String, String> properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, int.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public long getLong(String mothedname, HashMap<String, String> properties)
			throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, long.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public boolean getBoolean(String mothedname,
			HashMap<String, String> properties) throws Exception {
		SoapHolder holder = this.getResponse(mothedname, properties);
		try {
			String value = holder.getReturnValue();
			return modelFromJsonSafed(value, boolean.class);
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("服务器返回编码异常");
		}
	}

	public SoapHolder doFunction(String mothedname,
			HashMap<String, String> properties) throws Exception {
		return this.getResponse(mothedname, properties);
	}

	/**
	 * listFromJsonSafed 是 AfSoapService 的方法
	 * 		反序列化JSON到对象List<T> 发现 T 中的字段为 null 自动设置默认值 防止出现 null 导致空指针异常
	 * @author 树朾
	 * @param string
	 * @param clazz
	 * @return 如果 json 为null 或者 “null” 返回 null
	 * @throws Exception 构建对象异常
	 */
	public static <T> List<T> listFromJsonSafed(String json, Class<T> clazz) throws Exception {
		if (json == null || json.toLowerCase(Locale.ENGLISH).trim().equals("null")) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			JSONObject element = array.getJSONObject(i);
			list.add(modelFromJsonSafed(element.toString(),clazz));
		}
		return list;
	}
	
	/**
	 * 反序列化JSON到对象T 发现 T 中的字段为 null 自动设置默认值 防止出现 null 导致空指针异常
	 * @param json
	 * @param clazz
	 * @return 如果 json 为null 或者 “null” 返回 null
	 * @throws Exception 构建对象异常
	 */
	public static <T> T modelFromJsonSafed(String json, Class<T> clazz) throws Exception {
		// TODO Auto-generated method stub
		if (json == null || json.toLowerCase(Locale.ENGLISH).trim().equals("null")) {
			return null;
		}
		T model = mGson.fromJson(json, clazz);
		UnsafeAllocator caltor = UnsafeAllocator.create();
		for (Field field : clazz.getFields()) {
			if(field.get(model) == null ){
				Class<?> type = field.getType();
				if (!Modifier.isAbstract(type.getModifiers())) {
					Object object = null;
					for (Constructor<?> constructor : type.getConstructors()) {
						if (constructor.getParameterTypes().length == 0) {
							object = constructor.newInstance(new Object[0]);
							break;
						}
					}
					if (object == null) {
						object = caltor.newInstance(type);
					}
					field.set(model, object);
				}
			}
		};
		return model;
	}
}
