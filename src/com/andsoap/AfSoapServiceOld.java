package com.andsoap;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.andframe.exception.AfToastException;

public class AfSoapServiceOld<T> {

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

	/**
	 * 日期显示格式 "yyyy-MM-dd HH:mm:ss"
	 */
	public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final DateFormat format = new SimpleDateFormat(FORMAT_DATE, Locale.US);
	/**
	 * 枚举DEBUG模式
	 */
	public static final int DEBUG_USER = 0; // 用户使用 调试方式：邮件发送错误信息
	public static final int DEBUG_JAVA = 1; // 电脑调试 调试方式：使用java连接 localhost
	public static final int DEBUG_PHONE = 2; // 手机调试 调试方式：使用simulator连接 10.0.0.2
	public static final int DEBUG_TEST = 3; // 手机调试 测试数据：数据接口返回测试数据 不连接网络


	/**
	 * 设置服务 
	 * @param namespace like "http://tempuri.org/"
	 * @param svc like "Service.svc"
	 * @param sinterface like  "IService/"
	 */
	public static void setServer(String namespace,String svc,String sinterface){
		AfSoapServiceOld.SERVICE_SVC = svc;
		AfSoapServiceOld.SERVICE_INTERFACE = sinterface;
		AfSoapServiceOld.SERVICE_NAMESPACE = namespace;
		WSDLURL = "http://"+SERVER_ADDRESS+":"+SERVER_PORT+"/" + svc;
		SOAP_ACTION = namespace + sinterface;
		LOCAL_WSDLURL = "http://localhost:59176/" + svc;
		PHONE_WSDLURL = "http://10.0.2.2:21754/" + svc;
		MODE_WSDLURL = new String[] { 
				WSDLURL,LOCAL_WSDLURL, PHONE_WSDLURL,WSDLURL 
		};
		Transport = new HttpTransportSE(MODE_WSDLURL[debugmode]);
	}
	
	public static void setServer(String ip,int port){
		//ip = "222.85.149.6";
		AfSoapServiceOld.SERVER_PORT = port;
		AfSoapServiceOld.SERVER_ADDRESS = ip;

		WSDLURL = "http://"+ip+":"+port+"/"+SERVICE_SVC;
		MODE_WSDLURL = new String[] { 
				WSDLURL,LOCAL_WSDLURL, PHONE_WSDLURL,WSDLURL 
		};
		
		Transport = new HttpTransportSE(MODE_WSDLURL[debugmode]);
	}
	
	protected static int SERVER_PORT = 8088;
	protected static String SERVER_ADDRESS = "127.0.0.1";

	protected static String SERVICE_SVC = "Service.svc";
	protected static String SERVICE_INTERFACE = "IService/";
	protected static String SERVICE_NAMESPACE = "http://tempuri.org/";// WebService

	protected static String WSDLURL = "http://"+SERVER_ADDRESS+":"+SERVER_PORT+"/" + SERVICE_SVC;
	protected static String SOAP_ACTION = SERVICE_NAMESPACE + SERVICE_INTERFACE;

	protected static String LOCAL_WSDLURL = "http://localhost:59176/" + SERVICE_SVC;

	protected static String PHONE_WSDLURL = "http://10.0.2.2:21754/" + SERVICE_SVC;

	protected static int debugmode = DEBUG_USER;

	protected static String[] MODE_WSDLURL = new String[] { 
		WSDLURL,LOCAL_WSDLURL, PHONE_WSDLURL,WSDLURL 
	};

	protected static HttpTransportSE Transport = null;//new HttpTransportSE(MODE_WSDLURL[debugmode]);
	protected static SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER11);

	protected Class<?> mClass = null;
	protected static Gson mGson = getGson();
	
	public static Gson getGson() {
		if (mGson == null) {
			mGson = new GsonBuilder()
					.registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter())
					.setDateFormat(FORMAT_DATE).create();
		}
		return mGson;
	}

	public AfSoapServiceOld(Class<?> clazz) {
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
	
	public Object getResponse(String mothedname, String[][] properties) throws Exception{
		SoapObject request = this.getRequest(mothedname);
		if (properties != null) {
			for (int i = 0; i < properties.length; i++) {
				request.addProperty(properties[i][0], properties[i][1]);
			}
		}
		return this.getResponse(mothedname, request);
	}
	public Object getResponse(String shortmothedname, SoapObject request)
			throws Exception {
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			String soapaction = SOAP_ACTION;
			soapaction += getMothedName(shortmothedname);
			Transport.call(soapaction, envelope);
			return envelope.getResponse();
		} catch (Error er) {
			throw new Exception(er.getMessage(), er);
		} catch (XmlPullParserException ex) {
			throw new AfToastException("服务器返回异常", ex);
		} catch (ConnectException ex) {
			throw new AfToastException("连接服务器失败:"+ex.getMessage(), ex);
		} catch (FileNotFoundException ex) {
			throw new AfToastException("服务器地址错误:"+ex.getMessage(), ex);
		} catch (SoapFault ex) {
			throw new AfToastException("服务器反馈:"+ex.faultstring, ex);
		}
	}

	public List<T> getList(String mothedname, String[][] properties)
			throws Exception {
		SoapObject response = (SoapObject) this
				.getResponse(mothedname, properties);
		int length = response.getPropertyCount();
		List<T> models = new ArrayList<T>();
		for (int i = 0; i < length; i++) {
			@SuppressWarnings("unchecked")
			T entity = (T) mGson.fromJson(response.getProperty(i).toString(),
					mClass);
			models.add(entity);
		}
		return models;
	}

	public T getModel(String mothedname, String[][] properties)
			throws Exception {
		Object response = this.getResponse(mothedname, properties);
		@SuppressWarnings("unchecked")
		T model = (T) mGson.fromJson(response.toString(), mClass);
		return model;
	}

	public Object doFunction(String mothedname, String[][] properties)
			throws Exception {
		return this.getResponse(mothedname, properties);
	}
}
