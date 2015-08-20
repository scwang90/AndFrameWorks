package com.andframe.util.java;

/**
 * 通用版本转换类
 * @author 树朾
 */
public class AfVersion {
	
	public static String transformVersion(int version) {
		int version1 = (version & 0xFF000000) >>> 24;
		int version2 = (version & 0x00FF0000) >>> 16;
		int version3 = (version & 0x0000FF00) >>> 8;
		int version4 = (version & 0x000000FF);
//		version1 = Math.min(128, version1);
		return version1 + "." + version2 + "." + version3 + "." + version4;
	}

	public static int transformVersion(String version) {
		try {
			String[] vers = version.split("\\.");
			int ver1 = Integer.parseInt(vers[0]);
			int ver2 = Integer.parseInt(vers.length > 1 ? vers[1] : "0");
			int ver3 = Integer.parseInt(vers.length > 2 ? vers[2] : "0");
			int ver4 = Integer.parseInt(vers.length > 3 ? vers[3] : "0");
			ver3 += ver4 / 256;
			ver4 %= 256;
			ver2 += ver4 / 256;
			ver3 %= 256;
			ver1 += ver4 / 256;
			ver2 %= 256;
//			ver1 = Math.min(127, ver1);
			return (ver1 << 24) | (ver2 << 16) | (ver3 << 8) | (ver4);
		} catch (Throwable e) {
		}
		return 0;
	}
}
