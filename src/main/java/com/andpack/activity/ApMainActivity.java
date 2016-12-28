package com.andpack.activity;

import com.andpack.application.ApAppSettings;
import com.andpack.application.ApUpdateService;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
public class ApMainActivity extends ApActivity {

    protected long mExitTime;
    protected long mExitInterval = 2000;
    protected boolean mDoubleBackKeyPressed = true;

    @Override
    protected void onViewCreated() throws Exception {
        super.onViewCreated();
        if (ApAppSettings.settings().isAutoUpdate()) {
            ApUpdateService.getInstance().checkUpdate();
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

}
