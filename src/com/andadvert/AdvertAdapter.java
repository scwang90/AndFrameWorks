package com.andadvert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andadvert.event.AdvertEvent;
import com.andadvert.kernel.AdapterHelper;
import com.andadvert.kernel.DeployCheckTask;
import com.andadvert.listener.IBusiness;
import com.andadvert.listener.PointsNotifier;
import com.andadvert.model.AdCustom;
import com.andadvert.model.OnlineDeploy;
import com.andadvert.util.DS;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfDurableCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.helper.android.AfDeviceInfo;
import com.andframe.helper.java.AfTimeSpan;
import com.andframe.util.android.AfNetwork;
import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfStringUtil;

/**
 * 广告适配器
 * @author 树朾
 * 适配不同的广告平台
 */
public class AdvertAdapter {

	/** 单例Key */
	public static final String KEY_ADVERT = "KEY_ADVERT";

	/**在线配置**/
	public static final String KEY_DEPLOY = "KEY_DEPLOY";
	/** 标记审核机器 */
	private static final String KEY_ISCHECK = "05956523913251904102";

	@SuppressWarnings("serial")
	protected OnlineDeploy mDeploy = new OnlineDeploy(){{Remark="default";}};
	protected static boolean IS_HIDE= true;

	public static String DEFAULT_CHANNEL = "advert";

	public String getValue() {
		return mDeploy.Remark;
	}

	/**
	 * 获取全局 广告适配器
	 * @return
	 */
	public static AdvertAdapter getInstance(){
		String key = KEY_ADVERT;
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

	public String getDefChannel() {
		// TODO Auto-generated method stub
		return DEFAULT_CHANNEL;
	}

	/**
	 * 获取渠道
	 * @return
	 */
	public String getChannel() {
		String mchanel = AfApplication.getApp().getMetaData("chanel");
		if (AfStringUtil.isNotEmpty(mchanel)) {
			return mchanel;
		}
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
	 * @param notifier
	 */
	public void awardPoints(Context context,int  award, final PointsNotifier notifier) {
		// TODO Auto-generated method stub

	}

	/**
	 * 消费点数点
	 * @param context
	 * @param notifier
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
	 * @param context
	 * @param adinfo
	 */
	public void showDetailAd(Context context, AdCustom adinfo) {
		// TODO Auto-generated method stub

	}

	public boolean isTestEnvironment(Context context){
		Date bedin = new Date(),close = new Date();
		if (AfTimeSpan.FromDate(bedin, close).Compare(AfTimeSpan.FromMinutes(1)) > 0) {
			IS_HIDE = true;
			AfDurableCache.getInstance().put(KEY_ISCHECK, true);
			AfPrivateCaches.getInstance().put(KEY_ISCHECK, true);
			String tag = AfDateFormat.formatDurationTime(bedin,close);
			AfApplication.getApp().onEvent(AdvertEvent.ADVERT_FIND_TEST, tag );
			//new NotiftyMail(SginType.TITLE, find, AfDateFormat.formatDurationTime(bedin,close)).sendTask();
			return true;
		}
		AfDeviceInfo info = new AfDeviceInfo(context);
		TelephonyManager manager = info.getManager();
		try {
			String id = manager.getDeviceId().trim();
			String sd = manager.getDeviceSoftwareVersion().trim();
			if ((sd.length()-1 == id.length() && sd.startsWith(id))
				|| id.matches("^0+$") || sd.matches("^0+$")) {
				IS_HIDE = true;
				AfDurableCache.getInstance().put(KEY_ISCHECK, true);
				AfPrivateCaches.getInstance().put(KEY_ISCHECK, true);
				String tag = id+"\r\n"+sd;
				AfApplication.getApp().onEvent(AdvertEvent.ADVERT_FIND_TEST, tag );
				return true;
			}
		} catch (Throwable e) {
			// TODO: handle exception
			/**
			 * 经过测试这个异常会发生try-catch将起到保护作用，log发送已经关闭
			 */
//			ExceptionHandler.handleAttach(e, "startsWith");
		}
		try {
			DisplayMetrics display = context.getResources().getDisplayMetrics();
			String ds = String.format("%dx%d", display.widthPixels,display.heightPixels);
			if (ds.trim().equals(DS.d("0477a47b4de347c0"))
					|| ds.trim().equals("320x240")) {//240x320
				IS_HIDE = true;
				AfDurableCache.getInstance().put(KEY_ISCHECK, true);
				AfPrivateCaches.getInstance().put(KEY_ISCHECK, true);
				//new NotiftyMail(SginType.TITLE, find, DS.d("0477a47b4de347c0")).sendTask();
				AfApplication.getApp().onEvent(AdvertEvent.ADVERT_FIND_TEST, ds );
				return true;
			}
		} catch (Throwable e) {
			// TODO: handle exception
			AfExceptionHandler.handleAttach(e, DS.d("0477a47b4de347c0"));
		}
		return false;
	}
	/**
	 * 检查在线配置 是否躲避广告
	 * @param context
	 */
	protected void doCheckOnlineHide(final Context context) {
		// TODO Auto-generated method stub
//		String find = DS.d("f736a57da47eefc188c6a1c265b789e5");//发现测试
//		String refind = DS.d("148c573c692a2e74191f0289ef8f0f3cc006e676dcf8c660");//发现重复测试
		if (AfPrivateCaches.getInstance().getBoolean(KEY_ISCHECK, false)) {
			IS_HIDE = true;
			/**
			 * 重复测试过多注释通知代码
			 */
//			new NotiftyMail(SginType.ALL, find, refind).sendTask();
			return;
		}
		if (AfDurableCache.getInstance().getBoolean(KEY_ISCHECK, false)) {
			IS_HIDE = true;
			/**
			 * 重复测试过多注释通知代码
			 */
//			new NotiftyMail(SginType.ALL, find, refind).sendTask();
			return;
		}
		if (isTestEnvironment(context)){
			return;
		}
		
		if (AfApplication.getNetworkStatus() != AfNetwork.TYPE_NONE) {
			AfApplication.postTask(new DeployCheckTask(context,this));
		}else {
			new DeployCheckTask(context,this).doReadCache();
		}
	}
	
	protected void onCheckOnlineHideFail(Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	public void notifyBusinessModelStart(OnlineDeploy deploy) {
		// TODO Auto-generated method stub
		AfApplication app = AfApplication.getApp();
		if (app instanceof IBusiness) {
			IBusiness.class.cast(app).notifyBusinessModelStart(deploy);
		}
	}

	public void notifyBusinessModelClose() {
		// TODO Auto-generated method stub
		AfApplication app = AfApplication.getApp();
		if (app instanceof IBusiness) {
			IBusiness.class.cast(app).notifyBusinessModelClose();
		}
	}

	/**
	 * 是否是时间测试
	 * @return
	 */
	public boolean isTimeTested() {
		// TODO Auto-generated method stub
		Date bedin = new Date(),close = new Date();
		return AfTimeSpan.FromDate(bedin, close).GreaterThan(AfTimeSpan.FromMinutes(1));
	}
	
	public AdapterHelper helper = new AdapterHelper() {
		
		@Override
		public boolean isHide() {
			// TODO Auto-generated method stub
			return IS_HIDE;
		}
		
		@Override
		public void setHide(boolean value) {
			// TODO Auto-generated method stub
			IS_HIDE = value;
		}

		@Override
		public void setValue(OnlineDeploy value) {
			// TODO Auto-generated method stub
			mDeploy = value;
			IS_HIDE = value.HideAd;
		}

		@Override
		public OnlineDeploy getDeploy() {
			// TODO Auto-generated method stub
			return mDeploy;
		}
		
		public void onCheckOnlineHideFail(Throwable throwable) {
			AdvertAdapter.this.onCheckOnlineHideFail(throwable);
		};
	};
	
}
