package com.andcloud;

import java.util.List;

import android.content.Context;
import android.os.Message;

import com.andcloud.domain.AvDeployDomain;
import com.andcloud.model.Deploy;
import com.andframe.application.AfApplication;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.thread.AfHandlerTask;
import com.andframe.util.android.AfNetwork;

public class DeployCheckTask extends AfHandlerTask{

	/** 是否隐藏广告缓存标记 */
	public static final String KEY_BUSINESSMODEL = "70460105144142804102";
	/** 是否隐藏广告缓存标记 */
	public static final String KEY_CONFIG = "25791220347152804102";

	protected Context mContext;
	private Deploy mDeploy;
	private LoadDeployListener mListener;
	private String channel;
	private String defchannel;
	private static boolean mIsOnlineHideChecking = false;

	public DeployCheckTask(Context context, LoadDeployListener listener,
			String defchannel, String channel) {
		// TODO Auto-generated constructor stub
		this.channel = channel;
		this.defchannel = defchannel;
		if (listener == null) {
			mListener = new AndCloud();
		}
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
			mDeploy = this.deploy(deploys, defchannel);
			mDeploy = this.deploy(deploys, channel);
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
		return mDeploy;
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
		if (mListener != null) {
			if (mDeploy == null){
				mListener.onLoadDeployFailed();
			} else {
				AndCloud.Deploy = mDeploy;
				getCaches().put(KEY_BUSINESSMODEL, mDeploy.isBusinessModel());
				mListener.onLoadDeployFinish(mDeploy);
			}
		}
		return false;
	}
	
}
