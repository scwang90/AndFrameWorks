package com.andstatistics.domain;

import com.andrestrequest.http.DefaultRequestHandler.HttpMethod;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsDevice;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class DsDeviceDomain extends BaseDomain<DsDevice> {

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
