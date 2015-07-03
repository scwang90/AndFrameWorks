package com.andframe.util.android;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;
/**
 * 调用系统文件选择或者照片选择
 * AfFileSelector
 * @author 树朾
 */
public class AfFileSelector {

	
	private static File mOutputpath;

	/**
	 * showPhotograph 方法对应接受函数
	 * 如果失败将返回 null
	 */
	public static File onActivityPhotographResult(Activity activity,int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			return mOutputpath;
		}
		return null;
	}
	
	/**
	 * showFileChooser方法对应接受函数
	 * 如果失败将返回 ""(空串)
	 */
	public static String onActivityFileResult(Activity activity,int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && null != data) {
			return getPath(activity, data.getData());
		}
		return "";
	}

	/**
	 * showImageChooser方法对应接受函数
	 * 如果失败将返回 ""(空串)
	 */
	public static String onActivityImageResult(Activity activity,int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { Media.DATA };

			Cursor cursor = activity.getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			return picturePath;
		}
		return "";
	}
	/**
	 * 调用相册（图片浏览器）选择图片
	 * @param outputpath 拍照后在手机的储存路径
	 * @param request startActivityForResult 的请求 request 
	 * 用于 onActivityResult 中检测
	 */
	public static void showPhotograph(Activity activity,File outputpath, int request) {
		// TODO Auto-generated method stub
		try {
			mOutputpath = outputpath;
			Uri imageUri = Uri.fromFile(outputpath);//The Uri to store the big bitmap 
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture 
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); 
			activity.startActivityForResult(intent, request);//or TAKE_SMALL_PICTURE 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(activity, "没有找到系统相机程序喔~", Toast.LENGTH_SHORT)
			.show();
		}
	}
	/**
	 * 调用相册（图片浏览器）选择图片
	 * @param request startActivityForResult 的请求 request 
	 * 用于	onActivityResult 中检测
	 */
	public static void showImageChooser(Activity activity, int request) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		try {
			activity.startActivityForResult(i, request);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(activity, "手机上还没有安装图片浏览器.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 调用文件浏览器选择文件
	 * @param request startActivityForResult 的请求 request 
	 * 用于	onActivityResult 中检测
	 */
	public static void showFileChooser(Activity activity, int request) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			intent = Intent.createChooser(intent, "选择要上传的文件");
			activity.startActivityForResult(intent, request);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(activity, "手机上还没有安装文件管理器.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static String getPath(Context context, Uri uri) {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Throwable e) {
				return "";
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return "";
	}

	public static String onActivityResult(Activity activity,int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			return getPath(activity,data.getData());
		}
		return "";
	}

	/**
	 * 调用文件浏览器选择文件
	 * @param title 自定义选择页面标题
	 * @param request startActivityForResult 的请求 request 
	 * 用于	onActivityResult 中检测
	 */
	public static void showFileChooser(Activity activity,String title,int request) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			intent = Intent.createChooser(intent, title);
			activity.startActivityForResult(intent, request);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(activity, "手机上还没有安装文件管理器.",
					Toast.LENGTH_SHORT).show();
		}
	}

}
