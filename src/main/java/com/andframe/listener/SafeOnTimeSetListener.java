package com.andframe.listener;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import com.andframe.exception.AfExceptionHandler;

/**
 * 安全时间监听器
 */
@SuppressWarnings("unused")
public class SafeOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {

    private boolean fdealwith = false;
    private TimePickerDialog.OnTimeSetListener listener;

    public SafeOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        if (!fdealwith && listener != null) {
            fdealwith = true;
            try {
                listener.onTimeSet(view, hour, minute);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnTimeSetListener.onTimeSet");
            }
        }
    }
}