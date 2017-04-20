package com.andcloud.model;

import java.util.Set;

/**
 * 抽象接口
 * Created by SCWANG on 2017/4/20.
 */

public interface AvModel {
    String getClassName();
    String getObjectId();
    Object get(String key);
    Set<String> getPendingKeys();
}
