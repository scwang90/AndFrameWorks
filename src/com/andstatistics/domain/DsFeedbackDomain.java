package com.andstatistics.domain;

import com.andrestrequest.http.DefaultRequestHandler.HttpMethod;
import com.andstatistics.domain.base.BaseDomain;
import com.andstatistics.model.DsFeedback;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class DsFeedbackDomain extends BaseDomain<DsFeedback> {

    public DsFeedbackDomain(){
        super("DsFeedback");
    }

    public void exceptional(DsFeedback feedback) throws Exception {
        super.doRequest(HttpMethod.POST,"/Feedback",feedback);
    }
}
