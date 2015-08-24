package com.andstatistics.domain;

import com.andrestrequest.http.DefaultRequestHandler;
import com.andrestrequest.http.DefaultRequestHandler.HttpMethod;
import com.andrestrequest.http.Response;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsDeploy;
import com.andstatistics.model.DsDevice;

import java.util.List;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class DsDeployDomain extends BaseDomain<DsDeploy> {

    public DsDeployDomain(){
        super("DsDeploy");
    }

    public List<DsDeploy> findByAppId(String appkey) throws Exception{
        Response response = super.doRequest(HttpMethod.POST, "/findByAppId/" + appkey);
        return response.getBodyAsObjects(DsDeploy.class);
    }
}
