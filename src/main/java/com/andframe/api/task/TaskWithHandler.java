package com.andframe.api.task;

/**
 * 带有Handler的任务
 * Created by SCWANG on 2016/10/13.
 */

public interface TaskWithHandler extends Task {

    interface OnTaskFinishListener {
        void onTaskFinish(TaskWithHandler task);
    }

    TaskWithHandler setListener(OnTaskFinishListener listener);

}
