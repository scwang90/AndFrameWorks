package com.andstatistics;

import android.content.Context;
import android.os.Message;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.thread.AfHandlerTask;
import com.andframe.util.android.AfNetwork;
import com.andstatistics.domain.DsDeployDomain;
import com.andstatistics.model.DsDeploy;

import java.util.List;

public class DeployCheckTask extends AfHandlerTask{

	/** 是否隐藏广告缓存标记 */
	public static final String KEY_BUSINESSMODEL = "70460105144142804112";
	/** 是否隐藏广告缓存标记 */
	public static final String KEY_CONFIG = "25791220347152804112";

	protected Context mContext;
	private DsDeploy mDeploy;
	private LoadDeployListener mListener;
	private String channel;
	private String defchannel;
	private static boolean mIsOnlineHideChecking = false;

	public DeployCheckTask(Context context, LoadDeployListener listener,
						   String defchannel, String channel) {
		this.channel = channel;
		this.defchannel = defchannel;
		if (listener == null) {
			mListener = new AndStatistics();
		}
	}

	protected AfPrivateCaches getCaches(){
		return AfPrivateCaches.getInstance("deploy");
	}
	
	@Override
	public boolean onPrepare() {
		if (mIsOnlineHideChecking) {
			return false;
		}
		mIsOnlineHideChecking  = true;
		return super.onPrepare();
	}
	
	@Override
	protected void onWorking(/*Message msg*/) throws Exception {
		if (AfApplication.getNetworkStatus() != AfNetwork.TYPE_NONE) {
			DsDeployDomain domain = new DsDeployDomain();
			final List<DsDeploy> deploys = domain.findByAppId(AndStatistics.helper.getAppkey());
			mDeploy = this.deploy(deploys, defchannel);
			mDeploy = this.deploy(deploys, channel);
		}else {
			mDeploy = doReadCache();
		}
	}

	private DsDeploy deploy(List<DsDeploy> deploys, String channel) {
		for (DsDeploy deploy : deploys) {
			if (deploy.name.equals(channel)) {
				if (deploy.getVersion() == 0 ||
						deploy.getVersion() == AfApplication.getVersionCode()) {
					return deploy;
				}
			}
		}
		return mDeploy;
	}
	
	public DsDeploy doReadCache() {
		DsDeploy deploy = new DsDeploy();
		deploy.business = (getCaches().getBoolean(KEY_BUSINESSMODEL, false));
		deploy.remark = ("default_cache");
		return deploy;
	}
	
	@Override
	protected void onCancel() {
		super.onCancel();
		mIsOnlineHideChecking = false;
	}

	@Override
	protected void onException(Throwable e) {
		super.onException(e);
		AfExceptionHandler.handleAttach(e,"DeployCheckTask.onException");
		try {
			Thread.sleep(30*1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		deploy(mContext,mListener, defchannel,channel);
	}

	@Override
	protected boolean onHandle(/*Message msg*/) {
		mIsOnlineHideChecking = false;
		if (mListener != null) {
			if (mDeploy == null){
				mListener.onLoadDeployFailed();
			} else {
				AndStatistics.Deploy = mDeploy;
				getCaches().put(KEY_BUSINESSMODEL, mDeploy.business);
				mListener.onLoadDeployFinish(mDeploy);
			}
		}
		return false;
	}

	public static void deploy(Context context, LoadDeployListener listener, String defchannel, String channel) {
		AfApplication.postTask(new DeployCheckTask(context, listener,defchannel,channel));
	}
}
