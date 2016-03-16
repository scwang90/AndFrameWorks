package com.andframe.feature;

import android.location.Location;

/**
 * 用于计算兴趣点的距离
 */
public class AfDistance {
	
	private static final double DEF_PI = 3.14159265359; // PI
	private static final double DEF_2PI = 6.28318530712; // 2*PI
	private static final double DEF_PI180 = 0.01745329252; // PI/180.0
	private static final double DEF_R = 6370693.5; // 地球半径 单位米
	private static final double DEF_LATITUDE_M = 111189.6;// 纬度1度的实际长度 单位 米

	public static class LocationRadius {
		public double Latitude = 0;
		public double Longitude = 0;
	}

	/**
	 * 计算兴趣点的距离
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return
	 */
	public static double GetShortDistance(double lon1, double lat1,
			double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew;
		// 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2);
		// 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	/**
	 * 计算兴趣点的距离
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return
	 */
	public static double GetLongDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	/**
	 * 计算 兴趣点 Address 和 定位 Location的距离
	 * @param location
	 *            定位
	 * @param address
	 *            兴趣点
	 * @return 距离 （单位米）
	 */
	public static long getDistance(Location location, Location address) {
		double lon1 = location.getLongitude();
		double lat1 = location.getLatitude();
		double lon2 = address.getLongitude();
		double lat2 = address.getLatitude();
		return (long) GetShortDistance(lon1, lat1, lon2, lat2);
	}

	/**
	 * 根据定位 Location 转换 周围半径 radius
	 * @param location
	 *            定位
	 * @param radius
	 *            半径（单位米）
	 * @return 经纬度半径
	 */
	public static LocationRadius tranformRadius(Location location, int radius) {
		LocationRadius tRadius = new LocationRadius();
		tRadius.Latitude = radius/DEF_LATITUDE_M;
		tRadius.Longitude = radius/getLongitudeShift(location);
		return tRadius;
	}

	/**
	 * 根据定位 Location 获取当前纬度下，东西方向1KM对应的经度偏移量
	 * @param location
	 *            定位
	 * @return 经度1度对应的实际距离，单位米
	 */
	public static double getLongitudeShift(Location location) {
		// 获得纬度
		double lat = location.getLatitude();
		// 转换为弧度
		double ns = lat * DEF_PI180;
		// 经度1度对应的实际距离，单位米。（纬度1度对应的距离*纬度的余弦）
		return DEF_LATITUDE_M * Math.cos(ns);
	}
}
