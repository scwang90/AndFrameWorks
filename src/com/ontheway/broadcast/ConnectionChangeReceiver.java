package com.ontheway.broadcast;

import com.ontheway.application.AfApplication;
import com.ontheway.util.AfNetwork;

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
    }
}
