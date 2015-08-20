package com.andframe.constant;

import java.util.Locale;


/**
 * @author 树朾
 *
 */
public class AfFileTypeEnum
{
	public static final int UNKOWN = -1;
	public static final int NONE = 0;
	public static final int FLASH = 1;
	public static final int VIDEO = 2;
	public static final int MUSIC = 3;
	public static final int PDF = 4;
	public static final int PICTURE = 5;
	public static final int PPT = 6;
	public static final int WORD = 7;
	public static final int TEXT = 8;

	public static int getType(String name) {
		int index = 0;
		if(name != null && (index = name.indexOf('.')) > -1){
			name = name.toLowerCase(Locale.CHINESE);
			name = name.substring(index); 
			if(name.equals(".swf")){
				return FLASH;
			}else if(name.equals(".mp3")
					|| name.equals(".wma")){
				return MUSIC;
			}else if(name.equals(".pdf")){
				return PDF;
			}else if(name.equals(".jpg")
					|| name.equals(".png")
					|| name.equals(".bmp")){
				return PICTURE;
			}else if(name.equals(".ppt")){
				return PPT;
			}else if(name.equals(".doc")
					|| name.equals(".docx")){
				return WORD;
			}else if(name.equals(".txt")){
				return TEXT;
			}else if(name.equals(".avi")
					|| name.equals(".mpg")
					|| name.equals(".mpeg")
					|| name.equals(".mp4") 
					|| name.equals(".dat") 
					|| name.equals(".rmvb")
					|| name.equals(".flv")
					|| name.equals(".rm") 
					|| name.equals(".mov")
					|| name.equals(".wmv")
					|| name.equals(".mkv")){
				return VIDEO;
			}
		}
		return NONE;
	}
}
