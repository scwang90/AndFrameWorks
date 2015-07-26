package com.andcloud;

import android.content.Context;
import android.os.Message;

import com.andcloud.domain.AvDeployDomain;
import com.andcloud.model.Deploy;
import com.andframe.application.AfApplication;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.thread.AfHandlerTask;
import com.andframe.util.android.AfNetwork;

import java.util.List;

public class DeployCheckTask extends AfHandlerTask{

	/** 是否隐藏广告缓存标记 */
	public static final String KEY_BUSINESSMODEL = "70460105144142804102";
	/** 是否隐藏广告缓存标记 */
	public static final String KEY_CONFIG = "25791220347152804102";

	protected Context mContext;
	private Deploy mDeploy;
	private LoadDeployListener mListener;
	private static boolean mIsOnlineHideChecking = false;

	public DeployCheckTask(Context context, LoadDeployListener listener) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mListener = listener;
	}

	protected AfPrivateCaches getCaches(){
		return AfPrivateCaches.getInstance("deploy");
	}
	
	@Override
	public boolean onPrepare() {
		// TODO Auto-generated method stub
		if (mIsOnlineHideChecking) {
			return false;
		}
		mIsOnlineHideChecking  = true;
		return super.onPrepare();
	}
	
	@Override
	protected void onWorking(Message msg) throws Exception {
		// TODO Auto-generated method stub
		if (AfApplication.getNetworkStatus() != AfNetwork.TYPE_NONE) {
			AvDeployDomain domain = new AvDeployDomain();
			final List<Deploy> deploys = domain.list();
			mDeploy = this.deploy(deploys, AndCloud.DefChannel);
			mDeploy = this.deploy(deploys, AndCloud.Channel);
		}else {
			mDeploy = doReadCache();
		}
	}

	private Deploy deploy(List<Deploy> deploys, String channel) {
		// TODO Auto-generated method stub
		for (Deploy deploy : deploys) {
			if (deploy.getName().equals(channel)) {
				if (deploy.getVerson() == 0 ||
						deploy.getVerson() == AfApplication.getVersionCode()) {
					return deploy;
				}
			}
		}
		return null;
	}
	
	public Deploy doReadCache() {
		// TODO Auto-generated method stub
		Deploy deploy = new Deploy();
		deploy.setBusinessModel(getCaches().getBoolean(KEY_BUSINESSMODEL, false));
		deploy.setRemark("default_cache");
		return deploy;
	}
	
	@Override
	protected void onCancel() {
		// TODO Auto-generated method stub
		super.onCancel();
		mIsOnlineHideChecking = false;
	}

	@Override
	protected boolean onHandle(Message msg) {
		// TODO Auto-generated method stub
		mIsOnlineHideChecking = false;
		if (mDeploy == null){
			mListener.onLoadDeployFailed();
		} else {
			getCaches().put(KEY_BUSINESSMODEL, mDeploy.isBusinessModel());
			mListener.onLoadDeployFinish(mDeploy);
		}
		return false;
	}
	
}
