package com.andcloud.model;

import com.avos.avoscloud.ops.AVOp;

import java.util.Map;

/**
 * 抽象接口
 * Created by SCWANG on 2017/4/20.
 */

public interface AvModel {
    String getClassName();
    String getObjectId();
    Object get(String key);
    Map<String, AVOp> getOperationQueue();
}
