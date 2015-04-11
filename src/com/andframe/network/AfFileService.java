package com.andframe.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.andframe.application.AfApplication;

public class AfFileService {

    public class OPFileType
    {
        public final static int DELETE = 0;//删除操作
        public final static int COPY = 1;//复制操作
        public final static int MOVETO = 2;//移动操作
        public final static int REPLACE = 3;//替换操作
        public final static int EXIST = 4;//检查存在操作
        public final static int UPLOAD = 5;//上传操作
    }
    
	protected static boolean debug = true;

	protected static int port = 8089;
	protected static String server = "127.0.0.1";

	protected static String SerivcePath = "/FileService";
	protected static String ServletPath = "/FileServlet";

	protected static String webRoot = "http://127.0.0.1:8089/FileService";
	protected static String servletUrl = "http://127.0.0.1:8089/FileService/FileServlet";


	protected static AfFileService mInstance;
	
	public AfFileService(){
		AfFileService.initialize();
	}

	public static void initialize(Context context){
		if(mInstance == null){
			mInstance = AfApplication.getApp().getFileService();
		}
	}

	public static AfFileService getInstance(){
		return mInstance;
	}
	
	/***
	 * 
	 * @param serivce 形如 "/FileService"
	 * @param servlet 形如 "/FileServlet"
	 */
	public static void setServer(String serivce,String servlet){
		ServletPath = servlet;
		SerivcePath = serivce;
		AfFileService.initialize();
	}

	/***
	 * 
	 * @param ip
	 * @param port
	 */
	public static void setServer(String ip,int port){
		AfFileService.port = port;
		AfFileService.server = ip;
		AfFileService.initialize();
	}
	
	protected static void initialize() {
		// TODO Auto-generated method stub
		webRoot = "http://"+server+":"+port+SerivcePath;
		servletUrl = "http://"+server+":"+port+SerivcePath+ServletPath;
	}

	protected static String GetUrlUpload(String filename,int folder){
		return GetUrl(OPFileType.UPLOAD,"filename="+filename+"&&tofolder="+folder);
	}
	
	
	protected static String GetUrl(int optype,String parms){
		StringBuilder url = new StringBuilder(servletUrl);
		url.append("?op=");
		url.append(optype);
		if(parms != null && !parms.trim().equals("")){
			String trim = parms.trim();
			if(!trim.equals("")){
				if(!trim.startsWith("&&")){
					url.append("&&");
				}
				url.append(parms);
			}
		}
		return url.toString();
	}

	/* 上传文件至Server的方法 */
	protected static boolean uploadFile(String Url, String srcPath)
			throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";

		URL url = new URL(Url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Charset", "UTF-8");
		httpURLConnection.setRequestProperty("Content-Type",
				"multipart/form-data;boundary=" + boundary);

		DataOutputStream dos = new DataOutputStream(
				httpURLConnection.getOutputStream());
		dos.writeBytes(twoHyphens + boundary + end);
		dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
				+ srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
		dos.writeBytes(end);

		FileInputStream fis = new FileInputStream(srcPath);
		byte[] buffer = new byte[8192]; // 8k
		int count = 0;
		while ((count = fis.read(buffer)) != -1) {
			dos.write(buffer, 0, count);

		}
		fis.close();

		dos.writeBytes(end);
		dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
		dos.flush();

		InputStream is = httpURLConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String result = br.readLine();

		dos.close();
		is.close();
		if ("true".equals(result)) {
			return true;
		} else {
			return false;
		}
	}

	/* 上传文件至Server的方法 */
	protected static boolean uploadFile(String Url, InputStream fis) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";

		URL url = new URL(Url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Charset", "UTF-8");
		httpURLConnection.setRequestProperty("Content-Type",
				"multipart/form-data;boundary=" + boundary);

		DataOutputStream dos = new DataOutputStream(
				httpURLConnection.getOutputStream());
		dos.writeBytes(twoHyphens + boundary + end);
		dos.writeBytes(end);

		byte[] buffer = new byte[8192]; // 8k
		int count = 0;
		while ((count = fis.read(buffer)) != -1) {
			dos.write(buffer, 0, count);
		}
		fis.close();

		dos.writeBytes(end);
		dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
		dos.flush();

		InputStream is = httpURLConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String result = br.readLine();

		dos.close();
		is.close();
		if ("true".equals(result)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 根据URL下图片 注意，此方法仅供用于Android程序
	 * @param strUrl
	 *            图片网络地址
	 * @return 如果成功，返回Bitmap对象
	 * @throws Exception
	 */
	public static BitmapDrawable downloadDrawable(Context context, String turl)
			throws Exception {
		BitmapDrawable bd = null;
		//turl = URLEncoder.encode(turl, "UTF-8").replace("%2F","/").replace("%3A",":");
		URL url = new URL(turl);
		// 创建连接
		HttpURLConnection hc = (HttpURLConnection) url.openConnection();
		// 获取数据
		bd = new BitmapDrawable(context.getResources(), hc.getInputStream());
		// 关闭连接
		hc.disconnect();
		if(bd.getBitmap() == null){
			throw new Exception("下载图片失败");
		}
		return bd;
	}

	/**
	 * 根据URL下图片 注意，此方法仅供用于Android程序
	 * 
	 * @param strUrl
	 *            图片网络地址
	 * @return 如果成功，返回Bitmap对象
	 * @throws Exception
	 */
	public static Bitmap downloadBitmap(String strUrl) throws Exception {
		Bitmap bm = null;
		// 实例化url
		URL url = new URL(strUrl);
		// 1.使用URL获得输入流
		InputStream in = url.openStream();
		// 通过BitmapFactory获得实例
		bm = BitmapFactory.decodeStream(in);
		return bm;
	}

	/**
	 * 从服务器下载文件
	 * 
	 * @param Url
	 * @param outpath
	 * @return
	 * @throws Exception
	 */
	public static boolean downloadFile(String Url, String outpath)
			throws Exception {
		int byteread = 0;

		URL url = new URL(Url);

		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(outpath);

			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			return true;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}


}
