package com.andstatistics.thread;

import java.util.UUID;

import android.content.Context;
import android.os.Message;

import com.andframe.util.android.AfNetwork;
import com.andframe.util.java.AfStringUtil;
import com.andstatistics.domain.DsDeviceDomain;
import com.andstatistics.model.DsDevice;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class DeviceInitializeTask extends BaseTask{

    private final Context mContext;
    private final DsDevice mDevice;
    private final String mChannel;
    private final boolean mStart;

    public DeviceInitializeTask(Context context,DsDevice device,String channel, boolean start) {
        this.mDevice = device;
        this.mContext = context;
        this.mStart = start;
        this.mChannel = channel;
        if (AfStringUtil.isEmpty(this.mDevice.keyId)){
            this.mDevice.keyId = UUID.randomUUID().toString();
        }
    }

    public static void initDevice(Context context,DsDevice device,String channel) {
        worker.post(new DeviceInitializeTask(context,device,channel,true));
    }

    public static void uninstall(Context context, DsDevice device,String channel) {
        worker.post(new DeviceInitializeTask(context,device,channel,false));
    }

    @Override
    protected void onWorking(/*Message msg*/) throws Exception {
        while (AfNetwork.getNetworkState(mContext) == AfNetwork.TYPE_NONE){
            Thread.sleep(30*1000);
        }
        DsDeviceDomain domain = new DsDeviceDomain();
        if (mStart){
            domain.initDevice(mDevice,mChannel);
        } else {
            domain.uninstall(mDevice,mChannel);
        }
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        try {
            Thread.sleep(30*1000);
            initDevice(mContext,mDevice,mChannel);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
