package com.andframe.activity;


import android.support.annotation.CallSuper;

import com.andframe.$;
import com.andframe.application.AfAppSettings;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
public class AfMainActivity extends AfActivity {

    protected long mExitTime;
    protected long mExitInterval = 2000;
    protected boolean mDoubleBackKeyPressed = true;

    @CallSuper
    public void onViewCreated()  {
        super.onViewCreated();
        if (AfAppSettings.getInstance().isAutoUpdate()) {
            $.update().checkUpdate();
        }
    }

    @Override
    protected boolean onBackKeyPressed() {
        boolean isHandled = super.onBackKeyPressed();
        if (!isHandled && mDoubleBackKeyPressed) {
            isHandled = true;
            if ((System.currentTimeMillis() - mExitTime) > mExitInterval) {
                makeToastShort("再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
        }
        return isHandled;
    }

}
