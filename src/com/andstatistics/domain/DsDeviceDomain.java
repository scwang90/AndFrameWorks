package com.andstatistics.domain;

import com.andrestful.api.HttpMethod;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsDevice;

/**
 * 设备
 * Created by SCWANG on 2015-07-29.
 */
public class DsDeviceDomain extends BaseDomain {

    public DsDeviceDomain(){
        super("DsDevice");
    }

    public void initDevice(DsDevice device,String channel) throws Exception {
        super.doRequest(HttpMethod.POST, "/InitDevice/" + channel, device);
    }

    public void uninstall(DsDevice device,String channel) throws Exception {
        super.doRequest(HttpMethod.POST,"/Uninstall/"+channel,device);
    }
}
