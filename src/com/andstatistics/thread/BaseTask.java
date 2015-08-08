package com.andstatistics.thread;

import com.andframe.thread.AfTask;
import com.andframe.thread.AfThreadWorker;

/**
 * Created by SCWANG on 2015-08-01.
 */
public abstract class BaseTask extends AfTask{

    static AfThreadWorker worker = new AfThreadWorker(BaseTask.class);
}
