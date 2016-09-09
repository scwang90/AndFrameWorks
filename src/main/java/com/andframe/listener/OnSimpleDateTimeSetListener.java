package com.andframe.listener;

import com.andframe.api.DialogBuilder;

import java.util.Calendar;
import java.util.Date;

/**
 * 简单日期时间监听器
 */
@SuppressWarnings("unused")
public abstract class OnSimpleDateTimeSetListener implements DialogBuilder.OnDateTimeSetListener {
    @Override
    public void onDateTimeSet(int year, int month, int day, int hour, int minute) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date(0));
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, month);
        calender.set(Calendar.DAY_OF_MONTH, day);
        calender.set(Calendar.HOUR_OF_DAY, hour);
        calender.set(Calendar.MINUTE, minute);
        onDateTimeSet(calender.getTime(), year, month, day, hour, minute);
    }

    protected abstract void onDateTimeSet(Date time, int year, int month, int day, int hour, int minute);
}