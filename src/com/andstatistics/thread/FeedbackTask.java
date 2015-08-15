package com.andstatistics.thread;

import java.util.UUID;

import android.content.Context;
import android.os.Message;

import com.andframe.util.android.AfNetwork;
import com.andframe.util.java.AfStringUtil;
import com.andstatistics.domain.DsFeedbackDomain;
import com.andstatistics.model.DsFeedback;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class FeedbackTask extends BaseTask{

    private final DsFeedback mFeedback;
    private final Context mContext;

    public FeedbackTask(Context context, DsFeedback feedback) {
        this.mFeedback = feedback;
        this.mContext = context;
        if (AfStringUtil.isEmpty(this.mFeedback.keyId)){
            this.mFeedback.keyId = UUID.randomUUID().toString();
        }
    }

    public static void triggerFeedback(Context context,DsFeedback event) {
        worker.post(new FeedbackTask(context,event));
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        while (AfNetwork.getNetworkState(mContext) == AfNetwork.TYPE_NONE){
            Thread.sleep(30*1000);
        }
        DsFeedbackDomain domain = new DsFeedbackDomain();
        domain.exceptional(mFeedback);
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        triggerFeedback(mContext, mFeedback);
    }
}
