package com.ontheway.util;

import java.io.File;
/**
 * @Description: 通用版本转换类
 * @Author: scwang
 * @Version: V1.0, 2015-3-13 下午5:04:21
 * @Modified: 初次创建AfVersion类
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
		// TODO: handle exception
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
			// TODO: handle exception
		}
		return 0;
	}
	
	public static String getLastVersion(String filepath) {
		String rt = null;
		int one = 0;
		int two = 0;
		int three = 0;
		int fore = 0;

		try {
			File file = new File(filepath);
			if (file.exists()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					String[] vs = files[i].getName().split("\\.");
					if (vs.length > 3) {
						int one1 = Integer.parseInt(vs[0]);
						int two1 = Integer.parseInt(vs[1]);
						int three1 = Integer.parseInt(vs[2]);
						int fore1 = Integer.parseInt(vs[3]);

						if (one1 > one) // 完全覆盖
						{
							one = one1;
							two = two1;
							three = three1;
							fore = fore1;
						} else if (one1 == one)// 需要比较2、3、4
						{
							if (two1 > two) // 覆盖2、3、4
							{
								two = two1;
								three = three1;
								fore = fore1;
							} else if (two1 == two) // 需要比较3、4
							{
								if (three1 > three) // 覆盖3、4
								{
									three = three1;
									fore = fore1;
								} else if (three1 == three) // 需要比较4
								{
									if (fore1 > fore)// 覆盖4
									{
										fore = fore1;
									}
								}
							}

						}
					}
				}

				rt = "" + one + "." + two + "." + three + "." + fore;
			}
		} catch (Throwable ex) {
		}

		return rt;
	}
	
	
}
