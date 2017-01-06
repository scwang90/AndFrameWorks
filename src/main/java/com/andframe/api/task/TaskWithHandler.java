package com.andframe.api.task;

import com.andframe.task.AfHandlerTask;

/**
 * 带有Handler的任务
 * Created by SCWANG on 2016/10/13.
 */

public interface TaskWithHandler extends Task {

    interface OnTaskFinishListener {
        void onTaskFinish(AfHandlerTask task);
    }

    TaskWithHandler setListener(OnTaskFinishListener listener);

}
