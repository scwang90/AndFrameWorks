package com.ontheway.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Stack;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ontheway.exception.AfToastException;

public class AfFileUtil {

	/**
	 * 递归删除文件 file
	 * @param file
	 * @return
	 */
	@SuppressWarnings("serial")
	public static boolean deleteFile(final File file){
		if (file != null && file.exists()) {
			Stack<File> stack = new Stack<File>(){{push(file);}};
			File top = null;
			while (!stack.empty() && (top = stack.peek()) != null) {
				File[] files = top.listFiles();
				if (files != null && files.length > 0) {
					for (File item : files) {
						stack.push(item);
					}
				}else {
					File to = new File(top.getAbsolutePath() + System.currentTimeMillis());
					top.renameTo(to);
					if (to.delete()) {
						stack.pop();
					}else {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}


	/**
	 * @Description: 文件复制
	 * @param fileFrom
	 * @param fileTo
	 * @return false 复制失败
	 */
	public static boolean copyFile(String fileFrom, String fileTo) {  
        try {  
            FileInputStream in = new java.io.FileInputStream(fileFrom);  
            FileOutputStream out = new FileOutputStream(fileTo);  
            byte[] bt = new byte[1024];  
            int count;  
            while ((count = in.read(bt)) > 0) {  
                out.write(bt, 0, count);  
            }  
            in.close();  
            out.close();  
            return true;  
        } catch (IOException ex) {  
            return false;  
        }  
    } 
	/**
	 * 递归移动文件file到 目录path
	 * @param file 要移动的文件（可以是目录，会递归移动子目录和文件）
	 * @param path 指定到的目录 （不存在自动创建）
	 * @return false 移动失败
	 */
	public static boolean moveFile(File file,File path){
		if (!file.exists() || (path.exists() && path.isFile())) {
			return false;
		}
		if (!path.exists()) {
			path.mkdir();
		}
		File to = new File(path, file.getName());
		if (file.isDirectory()) {
			for (File element : file.listFiles()) {
				if (!moveFile(element,to)) {
					return false;
				}
			}
			to = new File(file.getAbsolutePath()+ System.currentTimeMillis());
			file.renameTo(to);
			to.delete();
			return true;
		}else {
			if (to.exists()) {
				File tto = new File(path, ""+ System.currentTimeMillis());
				to.renameTo(tto);
				tto.delete();
			}
			return file.renameTo(to);
		}
	}
	
	public static String  getFileSize(long size){
		int index = 0;
		String[] units = new String[]{"字节","KB","MB","GB","TB"};
		while (index < units.length - 1 && size > 1024) {
			index++;
			size = size/1024;
		}
		return size + units[index];
	}
	/**
	 * 打开文件
	 * 
	 * @param file
	 * @throws AfToastException 
	 */
	public static void openFile(Context context, File file) throws Exception {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(/* uri */Uri.fromFile(file), type);
		// 跳转
		try {
			context.startActivity(intent);
		} catch (Throwable e) {
			// TODO: handle exception
			throw new AfToastException("找不到相应的软件打开文件~");
		}
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * @param file
	 */
	public static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length());
		end = end.toLowerCase(Locale.ENGLISH);
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	protected static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };
}
