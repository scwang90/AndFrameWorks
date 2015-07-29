package com.andstatistics.kernel;

import android.content.Context;

import com.andframe.helper.android.AfDeviceInfo;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class DeviceKernel {

    AfDeviceInfo info;

    public DeviceKernel(Context context){
        info = new AfDeviceInfo(context);
    }

    public String getMacAddress() {
        return info.getMacAddress();
    }

    public String getDeviceId() {
        return info.getDeviceId();
    }
}
