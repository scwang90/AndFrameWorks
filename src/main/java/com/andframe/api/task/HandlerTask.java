package com.andframe.api.task;

import com.andframe.task.AfHandlerTask;

/**
 * 任务
 * Created by SCWANG on 2016/10/13.
 */

public interface HandlerTask extends Task {

    interface OnTaskFinishListener {
        void onTaskFinish(AfHandlerTask task);
    }

    HandlerTask setListener(OnTaskFinishListener listener);

}
