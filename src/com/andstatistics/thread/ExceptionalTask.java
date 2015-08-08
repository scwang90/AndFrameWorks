package com.andstatistics.thread;

import android.content.Context;
import android.os.Message;

import com.andframe.thread.AfTask;
import com.andframe.thread.AfThreadWorker;
import com.andframe.util.android.AfNetwork;
import com.andframe.util.java.AfStringUtil;
import com.andstatistics.domain.DsExceptionalDomain;
import com.andstatistics.model.DsExceptional;

import java.util.UUID;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class ExceptionalTask extends BaseTask{

    private final DsExceptional mExceptional;
    private final Context mContext;

    public ExceptionalTask(Context context, DsExceptional event) {
        this.mExceptional = event;
        this.mContext = context;
        if (AfStringUtil.isEmpty(this.mExceptional.keyId)){
            this.mExceptional.keyId = UUID.randomUUID().toString();
        }
    }

    public static void triggerExceptional(Context context,DsExceptional event) {
        worker.post(new ExceptionalTask(context,event));
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        while (AfNetwork.getNetworkState(mContext) == AfNetwork.TYPE_NONE){
            Thread.sleep(30*1000);
        }
        DsExceptionalDomain domain = new DsExceptionalDomain();
        domain.exceptional(mExceptional);
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        triggerExceptional(mContext, mExceptional);
    }
}
