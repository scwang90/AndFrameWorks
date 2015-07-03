package com.andframe.util.java;
/**
 * 区域名称简化工具
 * @author 树朾
 */
public class AfCityName {
	
	private static String[] minorities = new String[] { "蒙古族", "回族", "藏族",
			"维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族",
			"土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族",
			"水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族", "达斡尔族", "仫佬族", "羌族",
			"布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族",
			"乌孜别克族", "俄罗斯族", "鄂温克族", "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族",
			"鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族" };

//	private static String[] simpleminorities = new String[] { "蒙古", "回", "藏",
//			"维吾尔", "苗", "彝", "壮", "布依", "朝鲜", "满", "侗", "瑶", "白", "土家", "哈尼",
//			"哈萨克", "傣", "黎", "傈僳", "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇",
//			"柯尔克孜", "土", "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌",
//			"普米", "塔吉克", "怒", "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京",
//			"塔塔尔", "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺" };

	/**
	 * 去除民族地区
	 * @param tCityName
	 *            地名
	 * @return 简化后的地名
	 */
	public static String removeNationArea(String tCityName) {
		// TODO Auto-generated method stub
		int length = tCityName.length();
		String minority = minorities[0];
		if (tCityName.endsWith("自治州") || tCityName.endsWith("自治县")
				|| tCityName.endsWith("自治区")) {
			tCityName = tCityName.substring(0, (length = length - 3));
			for (int i = 0; length > 3 && i < minorities.length; i++) {
				minority = minorities[i];
				if (tCityName.endsWith(minority)) {
					length = length - minority.length();
					tCityName = tCityName.substring(0,length);
					i = -1;
				}
			}
		}
		return tCityName;
	}

	/**
	 * 简化地名
	 * @param tCityName
	 *            地名
	 * @return 简化后的地名
	 */
	public static String SimplifyCityName(String tCityName) {
		// TODO Auto-generated method stub
		if (tCityName.endsWith("自治州") || tCityName.endsWith("自治县")
				|| tCityName.endsWith("自治区")) {
			tCityName = tCityName.substring(0, tCityName.length() - 3);
			for (int i = 0; tCityName.length() > 3 && i < minorities.length; i++) {
				if (tCityName.endsWith(minorities[i])) {
					tCityName = tCityName.substring(0, tCityName.length()
							- minorities[i].length());
					i = -1;
				}
				// else if(simpleminorities.length > 1 &&
				// tCityName.endsWith(simpleminorities[i]))
				// {
				// tCityName = tCityName.substring(0, tCityName.length()
				// - simpleminorities[i].length());
				// i = -1;
				// continue;
				// }
			}
		} else if (tCityName.endsWith("市") || tCityName.endsWith("县")) {
			tCityName = tCityName.substring(0, tCityName.length() - 1);
		} else if (tCityName.endsWith("地区")) {
			tCityName = tCityName.substring(0, tCityName.length() - 2);
		}

		return tCityName;
	}
}
