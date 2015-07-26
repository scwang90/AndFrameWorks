package com.andcloud;

import com.andcloud.model.Deploy;

/**
 * Created by SCWANG on 2015-07-26.
 */
public interface LoadDeployListener {
    void onLoadDeployFailed();
    void onLoadDeployFinish(Deploy deploy);
}
