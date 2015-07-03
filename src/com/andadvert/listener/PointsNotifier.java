package com.andadvert.listener;
/**
 * 点数监听接口
 * @author 树朾
 */
public interface PointsNotifier {
	public void getPointsFailed(String error) ;
	public void getPoints(String currency, int point);
}
