package com.andadvert.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络配置
 * @author 树朾
 */
public class AfNetwork
{
    public static final int TYPE_NONE = -1;
    public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;

    public static int getNetworkState(Context context)
    {
        //获得手机所有连接管理对象（包括对wi-fi等连接的管理）
        try
        {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                //获得网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected())
                {
                    //判断当前网络是否已连接
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                        ;
                    return info.getType();
                }
            }
        }
        catch (Throwable e)
        {
            return TYPE_NONE;
        }
        return TYPE_NONE;
    }
}
