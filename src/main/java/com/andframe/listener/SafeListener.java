package com.andframe.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.andframe.api.DialogBuilder;
import com.andframe.exception.AfExceptionHandler;

/**
 * 安全取听器
 * Created by SCWANG on 2016/11/26.
 */

//@SuppressWarnings("unused")
public class SafeListener implements
        View.OnClickListener,
        View.OnLongClickListener,
        View.OnTouchListener,
        DialogInterface.OnClickListener,
        DialogInterface.OnCancelListener,
        DialogInterface.OnMultiChoiceClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        DialogBuilder.OnDateTimeSetListener{

    View.OnClickListener clickListener;
    View.OnLongClickListener longClockListener;
    View.OnTouchListener touthListener;
    DialogInterface.OnClickListener dialogClickListener;
    DialogInterface.OnCancelListener cancelListener;
    DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    DialogBuilder.OnDateTimeSetListener dateTimeSetListener;

    public SafeListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SafeListener(View.OnLongClickListener longClockListener) {
        this.longClockListener = longClockListener;
    }

    public SafeListener(View.OnTouchListener touthListener) {
        this.touthListener = touthListener;
    }

    public SafeListener(DialogInterface.OnClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
    }

    public SafeListener(DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener) {
        this.multiChoiceClickListener = multiChoiceClickListener;
    }

    public SafeListener(DialogBuilder.OnDateTimeSetListener dateTimeSetListener) {
        this.dateTimeSetListener = dateTimeSetListener;
    }

    public SafeListener(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }

    public SafeListener(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.dateSetListener = dateSetListener;
    }

    public SafeListener(DialogInterface.OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public SafeListener() {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dateSetListener != null) {
            try {
                dateSetListener.onDateSet(view, year, month, dayOfMonth);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.dateSetListener.onDateSet");
            }
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (timeSetListener != null) {
            try {
                timeSetListener.onTimeSet(view, hourOfDay, minute);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.timeSetListener.onTimeSet");
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (cancelListener != null) {
            try {
                cancelListener.onCancel(dialog);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.cancelListener.onCancel");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialogClickListener != null) {
            try {
                dialogClickListener.onClick(dialog, which);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.dialogClickListener.onClick");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (multiChoiceClickListener != null) {
            try {
                multiChoiceClickListener.onClick(dialog, which, isChecked);
            } catch (Exception e) {
                AfExceptionHandler.handle(e, "SafeListener.multiChoiceClickListener.onClick");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            try {
                clickListener.onClick(v);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.clickListener.onClick");
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClockListener != null) {
            try {
                return longClockListener.onLongClick(v);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.longClockListener.onLongClick");
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (touthListener != null) {
            try {
                return touthListener.onTouch(v, event);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.touthListener.onTouch");
            }
        }
        return false;
    }

    @Override
    public void onDateTimeSet(int year, int month, int day, int hour, int minute) {
        if (dateTimeSetListener != null) {
            try {
                dateTimeSetListener.onDateTimeSet(year, month, day, hour, minute);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.dateTimeSetListener.onDateTimeSet");
            }
        }
    }
}
