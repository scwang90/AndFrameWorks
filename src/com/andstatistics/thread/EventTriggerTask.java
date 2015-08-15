package com.andstatistics.thread;

import java.util.UUID;

import android.content.Context;
import android.os.Message;

import com.andframe.util.android.AfNetwork;
import com.andframe.util.java.AfStringUtil;
import com.andstatistics.domain.DsEventDomain;
import com.andstatistics.model.DsEvent;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class EventTriggerTask extends BaseTask{

    private final DsEvent mEvent;
    private final Context mContext;

    public EventTriggerTask(Context context, DsEvent event) {
        this.mEvent = event;
        this.mContext = context;
        if (AfStringUtil.isEmpty(this.mEvent.keyId)){
            this.mEvent.keyId = UUID.randomUUID().toString();
        }
    }

    public static void triggerEvent(Context context,DsEvent event) {
        worker.post(new EventTriggerTask(context,event));
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        while (AfNetwork.getNetworkState(mContext) == AfNetwork.TYPE_NONE){
            Thread.sleep(30*1000);
        }
        DsEventDomain domain = new DsEventDomain();
        domain.triggerEvent(mEvent);
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        triggerEvent(mContext, mEvent);
    }
}
