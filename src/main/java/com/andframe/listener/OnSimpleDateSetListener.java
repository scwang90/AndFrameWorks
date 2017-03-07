package com.andframe.listener;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * 简单时间监听器
 */
@SuppressWarnings("unused")
public abstract class OnSimpleDateSetListener implements DatePickerDialog.OnDateSetListener {

    protected boolean isSeted = false;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (!isSeted) {
            isSeted = true;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1 && view != null) {
//            onDateSet(new Date(view.getCalendarView().getDate()), year, month, day);
//        } else {
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date(0));
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, month);
            calender.set(Calendar.DAY_OF_MONTH, day);
            onDateSet(calender.getTime(), year, month, day);
//        }
        }
    }

    protected abstract void onDateSet(Date date, int year, int month, int day);
}
