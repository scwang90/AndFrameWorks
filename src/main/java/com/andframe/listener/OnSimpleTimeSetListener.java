package com.andframe.listener;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * 简单时间监听器
 */
@SuppressWarnings("unused")
public abstract class OnSimpleTimeSetListener implements TimePickerDialog.OnTimeSetListener {

    protected boolean isSeted = false;

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        if (!isSeted) {
            isSeted = true;
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date(0));
            calender.set(Calendar.HOUR_OF_DAY, hour);
            calender.set(Calendar.MINUTE, minute);
            onTimeSet(calender.getTime(), hour, minute);
        }
    }

    protected abstract void onTimeSet(Date time, int hour, int minute);
}
