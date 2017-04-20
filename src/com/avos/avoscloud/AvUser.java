package com.avos.avoscloud;

import com.avos.avoscloud.ops.AVOp;

import java.util.Map;

/**
 * 开放权限
 * Created by SCWG on 2017/4/20.
 */

public class AvUser extends AVUser {

    @Override
    public Map<String, AVOp> getOperationQueue() {
        return super.getOperationQueue();
    }
}
