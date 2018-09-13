package com.andframe.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TimePicker;

import com.andframe.api.DialogBuilder;
import com.andframe.exception.AfExceptionHandler;

/**
 * 安全取听器
 * Created by SCWANG on 2016/11/26.
 */

@SuppressWarnings("unused")
public class SafeListener implements
        View.OnClickListener,
        View.OnLongClickListener,
        View.OnTouchListener,
        SeekBar.OnSeekBarChangeListener,
        DialogInterface.OnClickListener,
        DialogInterface.OnCancelListener,
        DialogInterface.OnMultiChoiceClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        DialogBuilder.OnDateTimeSetListener{

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClockListener;
    private View.OnTouchListener touthListener;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener;
    private DialogInterface.OnClickListener dialogClickListener;
    private DialogInterface.OnCancelListener cancelListener;
    private DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private DialogBuilder.OnDateTimeSetListener dateTimeSetListener;
    private long lastTime = 0;
    private int intervalTime = 0;

    public SafeListener(View.OnClickListener clickListener) {
        this(clickListener, 1000);
    }

    public SafeListener(View.OnClickListener listener, int intervalTime) {
        this.clickListener = listener;
        this.intervalTime = intervalTime;
    }

    public SafeListener(View.OnLongClickListener longClockListener) {
        this.longClockListener = longClockListener;
    }

    public SafeListener(View.OnTouchListener touthListener) {
        this.touthListener = touthListener;
    }

    public SafeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        this.seekBarChangeListener = seekBarChangeListener;
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
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.multiChoiceClickListener.onClick");
            }
        }
    }

    @Override
    public void onClick(View v) {
        long thisTime = System.currentTimeMillis();
        if (clickListener != null && thisTime - lastTime > intervalTime) {
            try {
                lastTime = thisTime;
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBarChangeListener != null) {
            try {
                seekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onProgressChanged");
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBarChangeListener != null) {
            try {
                seekBarChangeListener.onStartTrackingTouch(seekBar);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onStartTrackingTouch");
            }
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBarChangeListener != null) {
            try {
                seekBarChangeListener.onStopTrackingTouch(seekBar);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onStopTrackingTouch");
            }
        }
    }
}
