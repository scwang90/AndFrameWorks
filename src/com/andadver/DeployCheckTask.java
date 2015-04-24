package com.andadver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.os.Message;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.exception.AfToastException;
import com.andframe.helper.java.AfTimeSpan;
import com.andframe.network.AfSoapService;
import com.andframe.thread.AfHandlerTask;
import com.andframe.util.java.AfStringUtil;
import com.andframe.util.java.AfVersion;

public class DeployCheckTask extends AfHandlerTask{

	/**
	 * 是否隐藏广告缓存标记
	 */
	public static final String KEY_ISHIDEAD = "70460105144142804102";
	/**
	 * 是否隐藏广告缓存标记
	 */
	public static final String KEY_CONFIG = "25791220347152804102";
	/**
	 * 标记审核机器
	 */
	private static final String KEY_ISCHECK = "05956523913251904102";

	protected Date bedin = new Date();
	protected Date end = new Date();
	protected Context mContext;
	private AdvertAdapter mAdapter;
	
	public DeployCheckTask(Context context,AdvertAdapter adapter) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mAdapter = adapter;
	}
	@Override
	protected void onWorking(Message msg) throws Exception {
		// TODO Auto-generated method stub
		mAdapter.mIsOnlineHideChecking = true;
		
		bedin = new Date();
		AfTimeSpan span = AfTimeSpan.FromMinutes(5);
		
		String deploy = "deploy";
		deploy = mAdapter.getConfig(mContext, AdvertAdapter.KEY_DEPLOY, deploy);
		while (deploy.equals("deploy")) {
			if (AfTimeSpan.FromDate(bedin, new Date()).Compare(span) > 0) {
				AfPrivateCaches caches = AfPrivateCaches.getInstance();
				if (!caches.getBoolean(KEY_ISHIDEAD, true)) {
					mAdapter.setHide(false);
					return;
				}
				//一下异常 会发生 但是概率很低 1%
				throw new AfToastException("获取在线数据超时");
			}
			Thread.sleep(300);
			deploy = mAdapter.getConfig(mContext, AdvertAdapter.KEY_DEPLOY, deploy);
//			deploy = "{\"Name\":\"poetry\",\"Version\":\"0\",\"HideAd\":false,\"Remark\":\"poetry\",\"Urls\":\"\"}" +
//			"|{\"Name\":\"xiaomi\",\"Version\":\"1.7.0.7\",\"HideAd\":true,\"Remark\":\"xiaomi\",\"Urls\":\"\"}" +
//			"|{\"Name\":\"baidu\",\"Version\":\"1.7.0.7\",\"HideAd\":true,\"Remark\":\"baidu\",\"Urls\":\"\"}";
//			deploy = "{"Name":"poetry","Version":"0","HideAd":false,"Remark":"poetry","Urls":""}" +
//			"|{"Name":"xiaomi","Version":"1.7.0.7","HideAd":true,"Remark":"xiaomi","Urls":""}" +
//			"|{"Name":"baidu","Version":"1.7.0.7","HideAd":true,"Remark":"baidu","Urls":""}";
		}
		this.doAnalysisDeploy(deploy);
		end = new Date();
	}

	public void doAnalysisDeploy(String onlinekey) throws Exception {
		// TODO Auto-generated method stub
		String[] keys = onlinekey.split("\\|");
		List<OnlineDeploy> configs = new ArrayList<OnlineDeploy>();
		for (String json : keys) {
			if (AfStringUtil.isNotEmpty(json)) {
				OnlineDeploy config = AfSoapService.modelFromJsonSafed(json, OnlineDeploy.class);
				config.Verson = AfVersion.transformVersion(config.Version);
				configs.add(config);
			}
		}
		this.doConfig(configs,"poetry");
		this.doConfig(configs,mAdapter.getChannel());
	}

	private void doConfig(List<OnlineDeploy> configs, String channel) {
		// TODO Auto-generated method stub
		for (OnlineDeploy config : configs) {
			if (config.Name.equals(channel)) {
				if (config.Verson == 0 || config.Verson == AfApplication.getVersionCode()) {
					mAdapter.setHide(config.HideAd);
					mAdapter.setValue(config.Remark);
					break;
				}
			}
		}
	}
	
	public void doReadCache() {
		// TODO Auto-generated method stub
		AfPrivateCaches caches = AfPrivateCaches.getInstance();
		if (!caches.getBoolean(KEY_ISHIDEAD, true)) {
			mAdapter.setHide(false);
			return;
		}
	}
	
	@Override
	protected void onCancel() {
		// TODO Auto-generated method stub
		super.onCancel();
		mAdapter.mIsOnlineHideChecking = false;
	}

	@Override
	protected boolean onHandle(Message msg) {
		// TODO Auto-generated method stub
		mAdapter.mIsOnlineHideChecking = false;
		if (isFail()) {
			//AfToastException异常 会发生 但是概率很低 1% 关闭通知
			if (!(mException instanceof AfToastException)) {
				AfExceptionHandler.handler(mException, "WapsCheckDeploy error");
			}
			mAdapter.onCheckOnlineHideFail(mException);
		}else if(AfApplication.getApp().isDebug()){
			String tip = "Waps耗时" + (1.0f*(end.getTime()-bedin.getTime())/1000)+"秒";
			AfApplication.getApp().getCurActivity().makeToastLong(tip);
		}
		if(isFinish()){
			AfPrivateCaches caches = AfPrivateCaches.getInstance();
			caches.put(KEY_ISHIDEAD, mAdapter.isHide());
			if (!mAdapter.isHide()) {
				Application.getApp().notifyBusinessModelStart(mValue);
			}
		}
		return false;
	}
	
}
