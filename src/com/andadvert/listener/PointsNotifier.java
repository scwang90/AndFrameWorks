package com.andadvert.listener;
/**
 * 点数监听接口
 * @author Administrator
 */
public interface PointsNotifier {
	public void getPointsFailed(String error) ;
	public void getPoints(String currency, int point);
}
