package com.andframe.util.java;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class AfZipUtil {

	/**
	 * 压缩
	 * 
	 * @param zipFileName
	 *            压缩产生的zip包文件名--带路径,如果为null或空则默认按文件名生产压缩文件名
	 * @param relativePath
	 *            相对路径，默认为空
	 * @param directory
	 *            文件或目录的绝对路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author yayagepei
	 * @date 2008-8-26
	 */
	public static void zip(String zipFileName, String relativePath,
			String directory) throws FileNotFoundException, IOException {
		String fileName = zipFileName;
		if (fileName == null || fileName.trim().equals("")) {
			File temp = new File(directory);
			if (temp.isDirectory()) {
				fileName = directory + ".zip";
			} else {
				if (directory.indexOf(".") > 0) {
					fileName = directory.substring(0,
							directory.lastIndexOf("."))
							+ "zip";
				} else {
					fileName = directory + ".zip";
				}
			}
		}
		ZipOutputStream zos = new ZipOutputStream(
				new FileOutputStream(fileName));
		try {
			zip(zos, relativePath, directory);
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != zos) {
				zos.close();
			}
		}
	}

	/** */
	/**
	 * 压缩
	 * 
	 * @param zos
	 *            压缩输出流
	 * @param relativePath
	 *            相对路径
	 * @param absolutPath
	 *            文件或文件夹绝对路径
	 * @throws IOException
	 * @author yayagepei
	 * @date 2008-8-26
	 */
	private static void zip(ZipOutputStream zos, String relativePath,
			String absolutPath) throws IOException {
		File file = new File(absolutPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File tempFile = files[i];
				if (tempFile.isDirectory()) {
					String newRelativePath = relativePath + tempFile.getName()
							+ File.separator;
					createZipNode(zos, newRelativePath);
					zip(zos, newRelativePath, tempFile.getPath());
				} else {
					zipFile(zos, tempFile, relativePath);
				}
			}
		} else {
			zipFile(zos, file, relativePath);
		}
	}

	/** */
	/**
	 * 压缩文件
	 * 
	 * @param zos
	 *            压缩输出流
	 * @param file
	 *            文件对象
	 * @param relativePath
	 *            相对路径
	 * @throws IOException
	 * @author yayagepei
	 * @date 2008-8-26
	 */
	private static void zipFile(ZipOutputStream zos, File file,
			String relativePath) throws IOException {
		ZipEntry entry = new ZipEntry(relativePath + file.getName());
		zos.putNextEntry(entry);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			int BUFFERSIZE = 2 << 10;
			int length = 0;
			byte[] buffer = new byte[BUFFERSIZE];
			while ((length = is.read(buffer, 0, BUFFERSIZE)) >= 0) {
				zos.write(buffer, 0, length);
			}
			zos.flush();
			zos.closeEntry();
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != is) {
				is.close();
			}
		}
	}

	/** */
	/**
	 * 创建目录
	 * 
	 * @param zos
	 *            zip输出流
	 * @param relativePath
	 *            相对路径
	 * @throws IOException
	 * @author yayagepei
	 * @date 2008-8-26
	 */
	private static void createZipNode(ZipOutputStream zos, String relativePath)
			throws IOException {
		ZipEntry zipEntry = new ZipEntry(relativePath);
		zos.putNextEntry(zipEntry);
		zos.closeEntry();
	}

	/** */
	/**
	 * 解压缩zip包
	 * 
	 * @param zipFilePath
	 *            zip文件路径
	 * @param targetPath
	 *            解压缩到的位置，如果为null或空字符串则默认解压缩到跟zip包同目录跟zip包同名的文件夹下
	 * @throws IOException
	 * @author yayagepei
	 * @date 2008-9-28
	 */
	@SuppressWarnings("resource")
	public static void unzip(String zipFilePath, String targetPath)
			throws IOException {
		OutputStream os = null;
		InputStream is = null;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFilePath);
			String directoryPath = "";
			if (null == targetPath || "".equals(targetPath)) {
				directoryPath = zipFilePath.substring(0,
						zipFilePath.lastIndexOf("."));
			} else {
				directoryPath = targetPath;
			}
			Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();
//			Enumeration entryEnum = zipFile.getEntries();
			if (null != entryEnum) {
				ZipEntry zipEntry = null;
				while (entryEnum.hasMoreElements()) {
					zipEntry = (ZipEntry) entryEnum.nextElement();
					if (zipEntry.isDirectory()) {
						directoryPath = directoryPath + File.separator
								+ zipEntry.getName();
						System.out.println(directoryPath);
						continue;
					}
					if (zipEntry.getSize() > 0) {
						// 文件
						File targetFile = buildFile(directoryPath
								+ File.separator + zipEntry.getName(), false);
						os = new BufferedOutputStream(new FileOutputStream(
								targetFile));
						is = zipFile.getInputStream(zipEntry);
						byte[] buffer = new byte[4096];
						int readLen = 0;
						while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
							os.write(buffer, 0, readLen);
						}

						os.flush();
						os.close();
					} else {
						// 空目录
						buildFile(directoryPath + File.separator
								+ zipEntry.getName(), true);
					}
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != zipFile) {
				zipFile = null;
			}
			if (null != is) {
				is.close();
			}
			if (null != os) {
				os.close();
			}
		}
	}

	public static File buildFile(String fileName, boolean isDirectory) {
		File target = new File(fileName);
		if (isDirectory) {
			target.mkdirs();
		} else {
			if (!target.getParentFile().exists()) {
				target.getParentFile().mkdirs();
				target = new File(target.getAbsolutePath());
			}
		}
		return target;
	}
}
