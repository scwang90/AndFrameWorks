package com.andframe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.andframe.application.AfAppSettings;
import com.andframe.application.AfApplication;
import com.andframe.application.AfUpdateService;
import com.andframe.feature.AfIntent;
import com.andframe.util.android.AfNetwork;

/**
 * 框架App主页面
 *
 * @author 树朾
 *         实现 更新通知，网络改变通知接口
 *         主要实现了更新提示，网络无效提示
 *         onCreate 向 App 注册主页面
 *         onDestroy 向 App 解除主页面
 *         <p/>
 *         返回按键 提示 "再按一次退出程序"
 */
public abstract class AfMainActivity extends com.andframe.activity.framework.AfActivity {

    protected long mExitTime;
    protected long mExitInterval = 2000;
    protected boolean mNotifyNetInvaild = true;
    protected boolean mDoubleBackKeyPressed = true;

    public AfMainActivity() {
        AfApplication.getApp().setMainActivity(this);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle, intent);
        if (AfNetwork.getNetworkState(this) == AfNetwork.TYPE_NONE) {
            // 显示网络不可用对话框
            if (mNotifyNetInvaild) showNetworkInAvailable();
        } else if (AfAppSettings.getInstance().isAutoUpdate()) {
            //自动更新
            AfUpdateService.getInstance().checkUpdate();
        }
    }

    @Override
    protected boolean onBackKeyPressed() {
        boolean isHandled = super.onBackKeyPressed();
        if (!isHandled && mDoubleBackKeyPressed) {
            isHandled = true;
            if ((System.currentTimeMillis() - mExitTime) > mExitInterval) {
                makeToastShort("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
        }
        return isHandled;
    }

    /**
     * 显示网络不可用对话框
     */
    protected void showNetworkInAvailable() {
        doShowDialog("当前网络不可用", "你可以浏览离线信息或者设置网络连接"
                , "设置网络", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 如果没有网络连接，则进入网络设置界面
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }, "浏览离线信息", null);
    }

}
