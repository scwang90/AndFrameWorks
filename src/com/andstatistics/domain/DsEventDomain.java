package com.andstatistics.domain;

import com.andrestrequest.http.api.HttpMethod;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsEvent;

/**
 * 事件
 * Created by SCWANG on 2015-07-29.
 */
public class DsEventDomain extends BaseDomain<DsEvent> {

    public DsEventDomain(){
        super("DsEvent");
    }

    public void triggerEvent(DsEvent event) throws Exception {
        super.doRequest(HttpMethod.POST,"/TriggerEvent",event);
    }
}
