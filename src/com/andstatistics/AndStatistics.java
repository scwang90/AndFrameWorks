package com.andstatistics;

import android.content.Context;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfException;
import com.andframe.model.Exceptional;
import com.andrestrequest.AndRestConfig;
import com.andrestrequest.http.DefaultResponseHandler;
import com.andstatistics.kernel.DeviceKernel;
import com.andstatistics.kernel.UniqueKernel;
import com.andstatistics.model.DsApplication;
import com.andstatistics.model.DsDevice;
import com.andstatistics.model.DsEvent;
import com.andstatistics.model.DsExceptional;
import com.andstatistics.model.DsFeedback;
import com.andstatistics.thread.DeviceInitializeTask;
import com.andstatistics.thread.EventTriggerTask;
import com.andstatistics.thread.ExceptionalTask;
import com.andstatistics.thread.FeedbackTask;

/**
 * Created by SCWANG on 2015-07-29.
 */
public class AndStatistics {

    public interface Helper{
        DsApplication buildApplication(Context context, String appkey);
        DsDevice buildDevice(Context context, DsApplication application);
        DsEvent buildEvent(Context context, DsApplication application, DsDevice device);
    }

    private static DsDevice device;
    private static DsApplication application;
    private static Context appContext;
    private static String channel;

    public static void initInstance(Context context,String appkey,String channel){
        if (appContext == null) {
            AndStatistics.channel = channel;
            appContext = context.getApplicationContext();
            application = helper.buildApplication(context,appkey);
            device = helper.buildDevice(appContext, application);
            DeviceInitializeTask.initDevice(appContext,device,channel);
        }
    }

    public static void uninstall(Context context){
        if (appContext != null){
            DeviceInitializeTask.uninstall(appContext, device,channel);
            appContext = null;
            application = null;
            device = null;
        }
    }

    public static void event(Context context, String eventId) {
        if (appContext != null){
            AndStatistics.event(context, eventId,"","");
        }
    }

    public static void event(Context context, String eventId, String tag) {
        if (appContext != null){
            AndStatistics.event(context, eventId, tag, "");
        }
    }

    public static void event(Context context, String eventId, String tag,String remark) {
        if (appContext != null){
            DsEvent event = helper.buildEvent(context, application,device);
            event.eventId = eventId;
            event.parameter = tag;
            event.remark = remark;
            event.channel = channel;
            EventTriggerTask.triggerEvent(context,event);
        }
    }

    public static void exceptional(Context context, DsExceptional exceptional){
        if (appContext != null){
            exceptional.appId = application.keyId;
            exceptional.channel = channel;
            ExceptionalTask.triggerExceptional(context,exceptional);
        }
    }

    public static void exceptional(Context context, Exceptional exceptional){
        exceptional(context,DsExceptional.from(exceptional));
    }

    public static void exceptional(Context context, Throwable ex){
        exceptional(context,AfExceptionHandler.getHandler(ex, ""));
    }

    public static void feedback(Context context, DsFeedback feedback){
        if (appContext != null){
            feedback.appId = application.keyId;
            feedback.channel = channel;
            feedback.version = AfApplication.getVersion();
            FeedbackTask.triggerFeedback(context,feedback);
        }
    }

    public static void feedback(Context context,String title,String content){
        if (appContext != null){
            DsFeedback feedback = new DsFeedback();
            feedback.appId = application.keyId;
            feedback.title = title;
            feedback.content = content;
            feedback.channel = channel;
            feedback.version = AfApplication.getVersion();
            FeedbackTask.triggerFeedback(context,feedback);
        }
    }

    public static Helper helper = new Helper() {
        @Override
        public DsApplication buildApplication(Context context, String appkey) {
            return new DsApplication(appkey);
        }

        @Override
        public DsDevice buildDevice(Context context, DsApplication application) {
            DsDevice device = new DsDevice(application);
            DeviceKernel info = new DeviceKernel(context);
            UniqueKernel unique = new UniqueKernel(context);
            device.imei = info.getDeviceId();
            device.mac = info.getMacAddress();
            device.uniqueId = unique.getUniqueId();
            return device;
        }

        @Override
        public DsEvent buildEvent(Context context, DsApplication application, DsDevice device) {
            DsEvent event = new DsEvent();
            event.appId = application.keyId;
            event.uniqueId = device.uniqueId;
            return event;
        }
    };

    static{
        AndRestConfig.setPort("9080");
        AndRestConfig.setIP("222.85.149.6");
//        AndRestConfig.setPort("8080");
//        AndRestConfig.setIP("192.168.31.203");
        AndRestConfig.setVersion("DataStatistics");
        DefaultResponseHandler.RESULT = "result";
        DefaultResponseHandler.STATUS = "status";
        DefaultResponseHandler.STATUS_OK = true;
    }
}
