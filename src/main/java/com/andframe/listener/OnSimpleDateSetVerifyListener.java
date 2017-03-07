package com.andframe.listener;

import android.widget.DatePicker;

import com.andframe.api.DialogBuilder;

import java.util.Calendar;
import java.util.Date;

/**
 * 简单时间监听器
 */
@SuppressWarnings("unused")
public abstract class OnSimpleDateSetVerifyListener implements DialogBuilder.OnDateSetVerifyListener {
    @Override
    public final void onDateSet(DatePicker view, int year, int month, int day) {
    }

    @Override
    public boolean onPreDateSet(DatePicker view, int year, int month, int day) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date(0));
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, month);
        calender.set(Calendar.DAY_OF_MONTH, day);
        return onPreDateSet(calender.getTime(), year, month, day);
    }

    protected abstract boolean onPreDateSet(Date date, int year, int month, int day);
}
