package com.ontheway.constant;

public class UPFileType {
	
	private static final String folders[] = new String[]{
		"File/Apk"
	};
	
	public static String getPathfrom(int filetype)
	{
		try
		{
			return folders[filetype];
		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 上传或下载文件类型为apk类型
	 */
	public static final int TYPE_APK = 0;
}
