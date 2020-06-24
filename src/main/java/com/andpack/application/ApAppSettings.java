package com.andpack.application;

import android.content.Context;

import com.andframe.application.AfApp;
import com.andframe.application.AppSettings;

/**
 *
 * Created by SCWANG on 2016/12/28.
 */

public class ApAppSettings extends AppSettings {

    //是否开启自动更新
    public static final String KEY_BL_ISAUTOUPDATE = "KEY_BL_ISAUTOUPDATE";

    protected ApAppSettings(Context context) {
        super(context);
        setDefault(KEY_BL_ISAUTOUPDATE, true);
    }

    public static ApAppSettings settings(){
        if(mInstance == null){
            mInstance = AfApp.get().newAppSetting();
        }
        return (ApAppSettings)mInstance;
    }

    public boolean isAutoUpdate() {
        return mShared.get(KEY_BL_ISAUTOUPDATE,false, boolean.class);
    }

    public void setAutoUpdate(boolean value) {
        mShared.put(KEY_BL_ISAUTOUPDATE, value);
    }

}
