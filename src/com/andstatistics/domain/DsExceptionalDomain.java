package com.andstatistics.domain;

import com.andrestrequest.http.DefaultRequestHandler.HttpMethod;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsExceptional;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class DsExceptionalDomain extends BaseDomain<DsExceptional> {

    public DsExceptionalDomain(){
        super("DsExceptional");
    }

    public void exceptional(DsExceptional exceptional) throws Exception {
        handler.doRequest(HttpMethod.POST,"/Exceptional",exceptional);
    }
}
