package com.andframe.listener;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import com.andframe.api.DialogBuilder;

import java.util.Calendar;
import java.util.Date;

/**
 * 简单时间监听器
 */
@SuppressWarnings("unused")
public abstract class OnSimpleTimeSetVerifyListener implements DialogBuilder.OnTimeSetVerifyListener {
    @Override
    public final void onTimeSet(TimePicker view, int hour, int minute) {
    }

    @Override
    public boolean onPreTimeSet(TimePickerDialog dialog, TimePicker view, int hour, int minute) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date(0));
        calender.set(Calendar.HOUR_OF_DAY, hour);
        calender.set(Calendar.MINUTE, minute);
        return onPreTimeSet(calender.getTime(), hour, minute);
    }

    protected abstract boolean onPreTimeSet(Date time, int hour, int minute);
}
