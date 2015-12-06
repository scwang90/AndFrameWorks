package com.andstatistics.domain;

import com.andrestrequest.http.Response;
import com.andrestrequest.http.api.HttpMethod;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsDeploy;

import java.util.List;

/**
 * 在线部署
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
