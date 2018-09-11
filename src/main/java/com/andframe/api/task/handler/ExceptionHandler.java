package com.andframe.api.task.handler;

import android.support.annotation.NonNull;

/**
 * 任务
 * Created by SCWANG on 2016/10/13.
 */

public interface ExceptionHandler {

    void onTaskException(@NonNull Throwable e);

}
