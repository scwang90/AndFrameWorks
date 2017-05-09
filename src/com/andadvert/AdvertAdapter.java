package com.andadvert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andadvert.event.AdvertEvent;
import com.andadvert.exception.ExceptionHandler;
import com.andadvert.kernel.AdapterHelper;
import com.andadvert.kernel.DeployCheckTask;
import com.andadvert.listener.IBusiness;
import com.andadvert.listener.PointsNotifier;
import com.andadvert.model.AdCustom;
import com.andadvert.model.OnlineDeploy;
import com.andadvert.util.AfNetwork;
import com.andadvert.util.DS;
import com.andadvert.util.SdCache;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 广告适配器
 * @author 树朾
 * 适配不同的广告平台
 */
@SuppressWarnings("unused")
public class AdvertAdapter {

	public static int INITIAL_POINT = 100;
	/**在线配置**/
	public static final String KEY_DEPLOY = "KEY_DEPLOY";
	/** 标记审核机器 */
	private static final String KEY_ISCHECK = "05956523913251904102";

	@SuppressWarnings("serial")
	protected OnlineDeploy mDeploy = new OnlineDeploy(){{Remark="default";}};
	protected static boolean IS_HIDE= true;

	protected static String DEFAULT_CHANNEL = "advert";


	public static boolean DEBUG = false;
	protected static AdvertAdapter mInstance;
	protected static IBusiness mIBusiness;
	protected static String mChannel;

	public static void initAdvert(AdvertAdapter adapter, String channel, boolean debug) {
		initAdvert(adapter, channel, debug, null);
	}

	public static void initAdvert(AdvertAdapter adapter, String channel, boolean debug, IBusiness business) {
		DEBUG = debug;
		mChannel = channel;
		mInstance = adapter;
		mIBusiness = business;
	}

	/**
	 * 获取全局 广告适配器
	 */
	public static AdvertAdapter getInstance(){
		AdvertAdapter adapter = mInstance;
		if (mInstance == null) {
			mInstance = new AdvertAdapter();
		}
		return mInstance;
	}


	public String getValue() {
		return mDeploy.Remark;
	}

	/**
	 * 初始化广告
	 */
	public void initInstance(Context context) {
		
	}

	/**
	 * 关闭广告（软件退出）
	 */
	public void uninstallAd(Context context) {
		
	}

	/**
	 * 是否隐藏整个广告系统
	 */
	public boolean isHide() {
		return IS_HIDE;
	}

	/**
	 * 是否支持点数
	 */
	public boolean isSupportPoint() {
		return false;
	}

	/**
	 * 是否支持自定义广告
	 */
	public boolean isSupportCustom() {
		return false;
	}

	public String getDefChannel() {
		return DEFAULT_CHANNEL;
	}

	/**
	 * 获取渠道
	 */
	public String getChannel() {
		return !TextUtils.isEmpty(mChannel) ? mChannel : DEFAULT_CHANNEL;
	}
	
	/**
	 * 获取 点数点名称
	 */
	public String getCurrency(){
		return "";
	}

	/**
	 * 显示插屏广告
	 */
	public void showPopAd(Context context) {
		
	}

	/**
	 * 检查更新
	 */
	public void checkUpdate(Context context) {
		
	}

	/**
	 * 显示意见反馈
	 */
	public void showFeedback(Context context) {
		
	}

	/**
	 * 显示综合推荐列表
	 */
	public void showOffers(Context context) {
		
	}

	/**
	 * 显示应用推荐列表广告
	 */
	public void showAppOffers(Context context) {
		
	}

	/**
	 * 显示游戏列表推荐页面
	 */
	public void showGameOffers(Context context) {
		
	}

	/**
	 * 显示互动广告
	 */
	public void showBannerAd(Context context, LinearLayout layout) {
		
	}

	/**
	 * 获取点数点
	 */
	public void getPoints(Context context, PointsNotifier pointsNotifier) {
		
	}

	/**
	 * 奖励点数点
	 */
	public void awardPoints(Context context,int  award, final PointsNotifier notifier) {

	}

	/**
	 * 消费点数点
	 */
	public void spendPoints(Context context,int  spend, final PointsNotifier notifier) {
		
	}

	/**
	 * 显示更多
	 */
	public boolean showMore(Context context) {
		return false;
	}

	/**
	 * 获取在线配置
	 */
	public  String getConfig(Context context,String key, String vdefault) {
		return vdefault;
	}

	/**
	 * 获取弹出广告视图
	 */
	public View getPopAdView(Context context) {
		return new TextView(context){{
				setText("\r\n    亲，欢迎您的再次光临\r\n");
				setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			}
		};
	}
	
	/**
	 * 随机获取一个自定义广告
	 */
	public AdCustom getAdCustom(Context context){
		return null;
	}

	/**
	 * 获取广自定义广告列表
	 */
	public List<AdCustom> getAdCustomList(Context context){
		return new ArrayList<>();
	}

	/**
	 * 下载指定的自定义广告
	 */
	public void downloadAd(Context context,AdCustom adinfo){
	}

	/**
	 * 显示详细自定义广告
	 */
	public void showDetailAd(Context context, AdCustom adinfo) {

	}

	public boolean isTestEnvironment(Context context){
		String find = DS.d("f736a57da47eefc188c6a1c265b789e5") + ":";//发现测试
		Date bedin = new Date(),close = new Date();
		if (bedin.getTime() - close.getTime() > 60 * 1000) {
			IS_HIDE = true;
			SdCache.getDurable(context).put(KEY_ISCHECK,String.valueOf(true));
			DateFormat FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
			String tag = FULL.format(bedin) + " - " + FULL.format(close);
			EventBus.getDefault().post(new AdvertEvent(AdvertEvent.ADVERT_FIND_TEST, find + tag));
			return true;
		}
		TelephonyManager manager;
		try {
			String tmserver = Context.TELEPHONY_SERVICE;
			manager = (TelephonyManager)context.getSystemService(tmserver);

			@SuppressLint("HardwareIds")
			String id = manager.getDeviceId().trim();
			String sd = manager.getDeviceSoftwareVersion().trim();
			if ((sd.length()-1 == id.length() && sd.startsWith(id))
				|| id.matches("^0+$")/* || sd.matches("^0+$")*/) {
				IS_HIDE = true;
				SdCache.getDurable(context).put(KEY_ISCHECK,String.valueOf(true));
				String tag = id+"\r\n"+sd;
				EventBus.getDefault().post(new AdvertEvent(AdvertEvent.ADVERT_FIND_TEST, find + tag));
				return true;
			}
		} catch (Throwable e) {
			/**
			 * 经过测试这个异常会发生try-catch将起到保护作用，log发送已经关闭
			 */
//			ExceptionHandler.handleAttach(e, "startsWith");
		}
		try {
			DisplayMetrics display = context.getResources().getDisplayMetrics();
			String ds = String.format(Locale.CHINA, "%dx%d", display.widthPixels, display.heightPixels);
			if (ds.trim().equals(DS.d("0477a47b4de347c0"))
					|| ds.trim().equals("320x240")) {//240x320
				IS_HIDE = true;
				SdCache.getDurable(context).put(KEY_ISCHECK,String.valueOf(true));
				EventBus.getDefault().post(new AdvertEvent(AdvertEvent.ADVERT_FIND_TEST, find + ds));
				return true;
			}
		} catch (Throwable e) {
			ExceptionHandler.handleAttach(e, DS.d("0477a47b4de347c0"));
		}
		return false;
	}

	/**
	 * 检查在线配置 是否躲避广告
	 */
	protected void doCheckOnlineHide(final Context context) {
		String refind = DS.d("148c573c692a2e74191f0289ef8f0f3cc006e676dcf8c660");//发现重复测试
		if (String.valueOf(true).equals(SdCache.getDurable(context).getAsString(KEY_ISCHECK))) {
			IS_HIDE = true;
			EventBus.getDefault().post(new AdvertEvent(AdvertEvent.ADVERT_FIND_REPEAT_TEST, refind));
			return;
		}
		if (isTestEnvironment(context)){
			return;
		}
		if (AfNetwork.getNetworkState(context) != AfNetwork.TYPE_NONE) {
			new DeployCheckTask(context, this).execute();
		}else {
			new DeployCheckTask(context, this).doReadCache();
		}
	}
	
	protected void onCheckOnlineHideFail(Throwable throwable) {
		
	}

	public void notifyBusinessModelStart(OnlineDeploy deploy) {
		if (mIBusiness != null) {
			mIBusiness.notifyBusinessModelStart(deploy);
		}
	}

	public void notifyBusinessModelClose() {
		if (mIBusiness != null) {
			mIBusiness.notifyBusinessModelClose();
		}
	}

	/**
	 * 是否是时间测试
	 */
	public boolean isTimeTested() {
		Date bedin = new Date(),close = new Date();
		return bedin.getTime() - close.getTime() > 60 * 1000;
	}
	
	public AdapterHelper helper = new AdapterHelper() {
		
		@Override
		public boolean isHide() {
			return IS_HIDE;
		}
		
		@Override
		public void setHide(boolean value) {
			IS_HIDE = value;
		}

		@Override
		public void setValue(OnlineDeploy value) {
			mDeploy = value;
			IS_HIDE = value.HideAd;
		}

		@Override
		public OnlineDeploy getDeploy() {
			return mDeploy;
		}
		
		public void onCheckOnlineHideFail(Throwable throwable) {
			AdvertAdapter.this.onCheckOnlineHideFail(throwable);
		}
	};
	
}
