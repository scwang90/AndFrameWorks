package com.andstatistics;

import com.andstatistics.model.DsDeploy;

/**
 * Created by SCWANG on 2015-07-26.
 */
public interface LoadDeployListener {
    void onLoadDeployFailed();
    void onLoadDeployFinish(DsDeploy deploy);
}
