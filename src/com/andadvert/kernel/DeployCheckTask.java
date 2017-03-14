package com.andadvert.kernel;

import android.content.Context;
import android.os.AsyncTask;

import com.andadvert.AdvertAdapter;
import com.andadvert.model.OnlineDeploy;
import com.andadvert.util.ACache;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.exception.AfToastException;
import com.andframe.helper.java.AfTimeSpan;
import com.andframe.util.java.AfStringUtil;
import com.andframe.util.java.AfVersion;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeployCheckTask extends AsyncTask<Void,Void,Void>  {

	/** 是否隐藏广告缓存标记 */
	public static final String KEY_SHOWAD = "70460105144142804102";
	/** 是否隐藏广告缓存标记 */
	public static final String KEY_CONFIG = "25791220347152804102";

	protected Date bedin = new Date();
	protected Date end = new Date();
	protected Context mContext;
	private AdvertAdapter mAdapter;
	private static boolean mIsOnlineHideChecking = false;
	
	public DeployCheckTask(Context context,AdvertAdapter adapter) {
		mContext = context;
		mAdapter = adapter;
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (!mIsOnlineHideChecking) {
			try {
				mIsOnlineHideChecking  = true;
				bedin = new Date();
				int span = 5*60*1000;

				String deploy = "deploy";
				deploy = mAdapter.getConfig(mContext, AdvertAdapter.KEY_DEPLOY, deploy);
				while (deploy.equals("deploy")) {
                    if (System.currentTimeMillis() - bedin.getTime() > span) {
						ACache cache = ACache.get(mContext);
						if (String.valueOf(true).equals(cache.getAsString(KEY_SHOWAD))) {
							mAdapter.helper.setHide(false);
							return null;
						}
                        //以下异常 会发生 但是概率很低 1%
                        throw new RuntimeException("获取在线数据超时");
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
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				mIsOnlineHideChecking  = false;
			}
		}
		return null;
	}

	public void doAnalysisDeploy(String onlinekey) throws Exception {
		Gson gson = new Gson();
		String[] keys = onlinekey.split("\\|");
		List<OnlineDeploy> configs = new ArrayList<OnlineDeploy>();
		for (String json : keys) {
			if (AfStringUtil.isNotEmpty(json)) {
				OnlineDeploy config = gson.fromJson(json, OnlineDeploy.class);
				config.Verson = AfVersion.transformVersion(config.Version);
				configs.add(config);
			}
		}
		this.doConfig(configs,mAdapter.getDefChannel());
		this.doConfig(configs,mAdapter.getChannel());
	}

	private void doConfig(List<OnlineDeploy> configs, String channel) {
		for (OnlineDeploy config : configs) {
			if (config.Name.equals(channel)) {
				if (config.Verson == 0 || config.Verson == AfApplication.getVersionCode()) {
					mAdapter.helper.setValue(config);
					break;
				}
			}
		}
	}
	
	public void doReadCache() {
		AfPrivateCaches caches = AfPrivateCaches.getInstance();
		if (!caches.getBoolean(KEY_SHOWAD, true)) {
			mAdapter.helper.setHide(false);
			return;
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		mIsOnlineHideChecking = false;
	}

	@Override
	protected boolean onHandle(/*Message msg*/) {
		mIsOnlineHideChecking = false;
		if (isFail()) {
			//AfToastException异常 会发生 但是概率很低 1% 关闭通知
			if (!(mException instanceof AfToastException)) {
				AfExceptionHandler.handle(mException, "WapsCheckDeploy error");
			}
			mAdapter.helper.onCheckOnlineHideFail(mException);
		}else if(AfApplication.getApp().isDebug()){
			String tip = "Waps耗时" + (1.0f*(end.getTime()-bedin.getTime())/1000)+"秒";
			AfApplication.getApp().getCurActivity().makeToastLong(tip);

		}
		if(isFinish()){
			AfPrivateCaches caches = AfPrivateCaches.getInstance();
			caches.put(KEY_SHOWAD, mAdapter.helper.isHide());
			if (!mAdapter.helper.isHide()) {
				mAdapter.notifyBusinessModelStart(mAdapter.helper.getDeploy());
			}else {
				mAdapter.notifyBusinessModelClose();
			}
		}
		return false;
	}
	
}
