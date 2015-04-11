package com.andframe.broadcast;

import com.andframe.activity.AfActivity;
import com.andframe.application.AfApplication;
import com.andframe.util.android.AfNetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ConnectionChangeReceiver extends BroadcastReceiver 
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int iNetworkState = AfNetwork.getNetworkState(context);
        AfApplication.getApp().setNetworkStatus(this,iNetworkState);
        if (AfApplication.getApp().isDebug()) {
        	AfActivity cur = AfApplication.getApp().getCurActivity();
        	if (cur != null) {
        		cur.makeToastShort("NetworkState "+iNetworkState);
			}
		}
    }
}
