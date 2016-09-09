package com.andframe.listener;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.andframe.exception.AfExceptionHandler;

/**
 * 安全日期监听器
 */
@SuppressWarnings("unused")
public class SafeOnDateSetListener implements DatePickerDialog.OnDateSetListener {

    private boolean fdealwith = false;
    private DatePickerDialog.OnDateSetListener listener;

    public SafeOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (!fdealwith && listener != null) {
            fdealwith = true;
            try {
                listener.onDateSet(view, year, month, day);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnDateSetListener.onDateSet");
            }
        }
    }
}
