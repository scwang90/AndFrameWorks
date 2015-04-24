package com.andadver;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andframe.application.AfApplication;

/**
 * 广告适配器
 * @author SCWANG
 *	适配不同的广告平台
 */
public class AdvertAdapter {

	public static class AdCustom{
		public String Id = "";
		public int Points = 0;
		public transient Bitmap Icon = null;
		public String Name = "";
		public String Text = "";
		public String Action = "";
		public String Package = "";
		public String Filesize = "";
		public String Provider = "";
		public String Version = "";
		public String Description = "";
		public String[] ImageUrls = null;
	}

	/**
	 * 单例Key
	 */
	public static final String KEY_PLUGIN = "PLUGIN_ADVERT";

	/**在线配置**/
	public static final String KEY_DEPLOY = "KEY_DEPLOY";


	protected static String mValue = "advert";
	protected static boolean IS_HIDE= true;
//	private static AdvertAdapter mAdvertAdapter;

	public static String DEFAULT_CHANNEL = "advert";
	
	public String getValue() {
		return mValue;
	}

	/**
	 * 获取全局 广告适配器
	 * @return
	 */
	public static AdvertAdapter getInstance(){
		String key = KEY_PLUGIN;
		AdvertAdapter adapter = AfApplication.getApp().getSingleton(key);
		if (adapter == null) {
			adapter = new AdvertAdapter();
			AfApplication.getApp().setSingleton(key,adapter);
		}
		return adapter;
//		return mAdvertAdapter = AfApplication.getApp().getPlugin(key);
	}

	/**
	 * 初始化广告
	 * @param context
	 */
	public void initInstance(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 关闭广告（软件退出）
	 * @param context
	 */
	public void uninstallAd(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 是否隐藏整个广告系统
	 * @return
	 */
	public boolean isHide() {
		// TODO Auto-generated method stub
		return IS_HIDE;
	}
	/**
	 * 是否支持点数
	 * @return
	 */
	public boolean isSupportPoint() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 是否支持自定义广告
	 * @return
	 */
	public boolean isSupportCustom() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 获取渠道
	 * @return
	 */
	public String getChannel() {
		return DEFAULT_CHANNEL ;
	}
	
	/**
	 * 获取 点数点名称
	 * @return
	 */
	public String getCurrency(){
		return "";
	}
	/**
	 * 显示插屏广告
	 * @param context
	 */
	public void showPopAd(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 检查更新
	 * @param context
	 */
	public void checkUpdate(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示意见反馈
	 * @param context
	 */
	public void showFeedback(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示综合推荐列表
	 * @param context
	 */
	public void showOffers(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示应用推荐列表广告
	 * @param context
	 */
	public void showAppOffers(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示游戏列表推荐页面
	 * @param context
	 */
	public void showGameOffers(Context context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示互动广告
	 * @param context
	 * @param layout
	 */
	public void showBannerAd(Context context, LinearLayout layout) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 获取点数点
	 * @param context
	 * @param pointsNotifier
	 */
	public void getPoints(Context context, PointsNotifier pointsNotifier) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 奖励点数点
	 * @param context
	 * @param pointsNotifier
	 */
	public void awardPoints(Context context,int  award, final PointsNotifier notifier) {
		// TODO Auto-generated method stub

	}
	/**
	 * 消费点数点
	 * @param context
	 * @param pointsNotifier
	 */
	public void spendPoints(Context context,int  spend, final PointsNotifier notifier) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示更多
	 * @param context
	 */
	public boolean showMore(Context context) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 获取在线配置
	 * @param context
	 * @param key
	 * @param vdefault
	 * @return
	 */
	public  String getConfig(Context context,String key, String vdefault) {
		// TODO Auto-generated method stub
		return vdefault;
	}
	/**
	 * 获取弹出广告视图
	 * @param context
	 * @return
	 */
	public View getPopAdView(Context context) {
		// TODO Auto-generated method stub
		return new TextView(context){{
				setText("\r\n    亲，欢迎您的再次光临\r\n");
				setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			}
		};
	}
	
	/**
	 * 随机获取一个自定义广告
	 * @return
	 */
	public AdCustom getAdCustom(Context context){
		return null;
	}

	/**
	 * 获取广自定义广告列表
	 * @return
	 */
	public List<AdCustom> getAdCustomList(Context context){
		return new ArrayList<AdCustom>();
	}

	/**
	 * 下载指定的自定义广告
	 * @return
	 */
	public void downloadAd(Context context,AdCustom adinfo){
	}

	/**
	 * 显示详细自定义广告
	 * @param con
	 * @param mAdCustom
	 */
	public void showDetailAd(Context context, AdCustom adinfo) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 检查在线配置 是否躲避广告
	 * @param context
	 */
	public boolean mIsOnlineHideChecking;
	protected void doCheckOnlineHide(final Context context) {
		// TODO Auto-generated method stub
	}
	public void onCheckOnlineHideFail(Throwable mException) {
		// TODO Auto-generated method stub
		
	}

	void setHide(boolean value) {
		// TODO Auto-generated method stub
		IS_HIDE = value;
	}

	void setValue(String value) {
		// TODO Auto-generated method stub
		mValue = value;
	}
	
}
