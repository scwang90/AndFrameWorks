package com.andadvert.listener;
/**
 * 点数监听接口
 * @author 树朾
 */
public interface PointsNotifier {
	void getPointsFailed(String error) ;
	void getPoints(String currency, int point);
}
