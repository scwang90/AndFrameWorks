package com.andframe.api.service;

import java.io.File;

/**
 * 更新服务
 * Created by SCWANG on 2017/5/2.
 */
public interface UpdateService {
    String getVersion();
    String getServiceVersion();
    void checkUpdate();
    void checkUpdate(boolean feedback);
    void install(File file);
    boolean isNeedUpdate();
}
