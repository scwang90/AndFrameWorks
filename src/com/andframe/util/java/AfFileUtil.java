package com.andframe.util.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

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
	 *  单文件复制
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

	/**
	 *  转换文件大小到String显示描述 如 648KB,5MB 整数
	 * @author 树朾
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size){
		int index = 0;
		String[] units = new String[]{"bt","KB","MB","GB","TB"};
		while (index < units.length - 1 && size > 1024) {
			index++;
			size = size/1024;
		}
		return size + units[index];
	}
}
