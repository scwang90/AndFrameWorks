package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.andpack.annotation.BindStatusBarMode;
import com.andpack.constant.StatusBarMode;
import com.andpack.impl.ApActivityHelper;
import com.andframe.activity.AfActivity;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfIntent;
import com.andframe.module.AfModuleTitlebar;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
@BindStatusBarMode(StatusBarMode.translucent)
public class ApActivity extends AfActivity {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApActivityHelper mHelper = new ApActivityHelper(this);

    @Override
    public void setTheme(@StyleRes int resid) {
        mHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        mHelper.onCreate(bundle,intent);
        super.onCreate(bundle, intent);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mHelper.finish()) {
            return;
        }
        super.finish();
    }

    //</editor-fold>
}
