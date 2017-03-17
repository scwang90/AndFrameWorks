package com.andcloud;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.andcloud.domain.AvDeployDomain;
import com.andcloud.model.Deploy;
import com.andcloud.util.ACache;
import com.andcloud.util.AfNetwork;
import com.andcloud.util.AfVersion;

import java.util.List;

import static com.andcloud.AndCloud.postEvent;

public class DeployCheckTask extends AsyncTask<Void,Void,Void> {

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
	private Throwable mThrowable;

	public DeployCheckTask(Context context, LoadDeployListener listener,
			String defchannel, String channel) {
		this.mContext = context;
		this.channel = channel;
		this.defchannel = defchannel;
		if (listener == null) {
			mListener = new AndCloud();
		}
	}

	private ACache getCaches() {
		return ACache.get(mContext,"deploy");
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (!mIsOnlineHideChecking) {
			try {
				if (AfNetwork.getNetworkState(mContext) != AfNetwork.TYPE_NONE) {
					AvDeployDomain domain = new AvDeployDomain();
					final List<Deploy> deploys = domain.list();
					mDeploy = this.deploy(deploys, defchannel);
					mDeploy = this.deploy(deploys, channel);
				}else {
					mDeploy = doReadCache();
				}
			} catch (Throwable e) {
				mThrowable = e;
			} finally {
				mIsOnlineHideChecking  = false;
				publishProgress();
			}
		}
		return null;
	}

	private Deploy deploy(List<Deploy> deploys, String channel) {
		for (Deploy deploy : deploys) {
			if (deploy.getName().equals(channel)) {
				if (deploy.getVerson() == 0 || deploy.getVerson() == AfVersion.getPackageVersionCode(mContext)) {
					return deploy;
				}
			}
		}
		return mDeploy;
	}

	public Deploy doReadCache() {
		ACache cache = getCaches();
		Deploy deploy = new Deploy();
		deploy.setBusinessModel(String.valueOf(true).equals(cache.getAsString(KEY_BUSINESSMODEL)));
		deploy.setRemark("default_cache");
		return deploy;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		mIsOnlineHideChecking = false;
	}


	@Override
	protected void onProgressUpdate(Void... values) {
		mIsOnlineHideChecking = false;
		if (mThrowable != null) {
			//AfToastException异常 会发生 但是概率很低 1% 关闭通知
			postEvent(new CloudExceptionEvent(mThrowable, "CheckDeploy error"));
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					deploy(mContext, mListener, defchannel, channel);
				}
			}, 30 * 1000);
		} else {
			if (mListener != null) {
				if (mDeploy == null){
					mListener.onLoadDeployFailed();
				} else {
					AndCloud.Deploy = mDeploy;
					getCaches().put(KEY_BUSINESSMODEL, mDeploy.isBusinessModel());
					mListener.onLoadDeployFinish(mDeploy);
				}
			}
		}
	}

	public static void deploy(Context context, LoadDeployListener listener, String defchannel, String channel) {
		new DeployCheckTask(context, listener, defchannel, channel).execute();
	}

}
