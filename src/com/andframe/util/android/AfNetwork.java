package com.andframe.util.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Description: 网络配置
 * @Author: scwang
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

    public static void showNetInAvailable(final Activity context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("当前网络不可用");
        builder.setMessage("你可以浏览离线信息或者设置网络连接。");
        builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent("/");
                ComponentName comp = new ComponentName("com.android.settings",
                        "com.android.settings.WirelessSettings");
                mIntent.setComponent(comp);
                mIntent.setAction("android.intent.action.VIEW");
                context.startActivity(mIntent);
            }
        });
        builder.setNegativeButton("浏览离线信息",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }
}
