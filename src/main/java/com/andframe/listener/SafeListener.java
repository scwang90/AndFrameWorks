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

import java.lang.ref.WeakReference;

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

    private WeakReference<View.OnClickListener> clickListener;
    private WeakReference<View.OnLongClickListener> longClockListener;
    private WeakReference<View.OnTouchListener> touchListener;
    private WeakReference<SeekBar.OnSeekBarChangeListener> seekBarChangeListener;
    private WeakReference<DialogInterface.OnClickListener> dialogClickListener;
    private WeakReference<DialogInterface.OnCancelListener> cancelListener;
    private WeakReference<DialogInterface.OnMultiChoiceClickListener> multiChoiceClickListener;
    private WeakReference<DatePickerDialog.OnDateSetListener> dateSetListener;
    private WeakReference<TimePickerDialog.OnTimeSetListener> timeSetListener;
    private WeakReference<DialogBuilder.OnDateTimeSetListener> dateTimeSetListener;
    private long lastTime = 0;
    private int intervalTime = 0;

    public SafeListener(View.OnClickListener clickListener) {
        this(clickListener, 1000);
    }

    public SafeListener(View.OnClickListener listener, int intervalTime) {
        this.clickListener = new WeakReference<>(listener);
        this.intervalTime = intervalTime;
    }

    public SafeListener(View.OnLongClickListener longClockListener) {
        this.longClockListener = new WeakReference<>(longClockListener);
    }

    public SafeListener(View.OnTouchListener touchListener) {
        this.touchListener = new WeakReference<>(touchListener);
    }

    public SafeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        this.seekBarChangeListener = new WeakReference<>(seekBarChangeListener);
    }

    public SafeListener(DialogInterface.OnClickListener dialogClickListener) {
        this.dialogClickListener = new WeakReference<>(dialogClickListener);
    }

    public SafeListener(DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener) {
        this.multiChoiceClickListener = new WeakReference<>(multiChoiceClickListener);
    }

    public SafeListener(DialogBuilder.OnDateTimeSetListener dateTimeSetListener) {
        this.dateTimeSetListener = new WeakReference<>(dateTimeSetListener);
    }

    public SafeListener(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = new WeakReference<>(timeSetListener);
    }

    public SafeListener(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.dateSetListener = new WeakReference<>(dateSetListener);
    }

    public SafeListener(DialogInterface.OnCancelListener cancelListener) {
        this.cancelListener = new WeakReference<>(cancelListener);
    }

    public SafeListener() {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DatePickerDialog.OnDateSetListener listener = dateSetListener == null ? null : dateSetListener.get();
        if (listener != null) {
            try {
                listener.onDateSet(view, year, month, dayOfMonth);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.dateSetListener.onDateSet");
            }
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TimePickerDialog.OnTimeSetListener listener = timeSetListener == null ? null : timeSetListener.get();
        if (listener != null) {
            try {
                listener.onTimeSet(view, hourOfDay, minute);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.timeSetListener.onTimeSet");
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        TimePickerDialog.OnCancelListener listener = cancelListener == null ? null : cancelListener.get();
        if (listener != null) {
            try {
                listener.onCancel(dialog);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.cancelListener.onCancel");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        TimePickerDialog.OnClickListener listener = dialogClickListener == null ? null : dialogClickListener.get();
        if (listener != null) {
            try {
                listener.onClick(dialog, which);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.dialogClickListener.onClick");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        DialogInterface.OnMultiChoiceClickListener listener = multiChoiceClickListener == null ? null : multiChoiceClickListener.get();
        if (listener != null) {
            try {
                listener.onClick(dialog, which, isChecked);
            } catch (Exception e) {
                AfExceptionHandler.handle(e, "SafeListener.multiChoiceClickListener.onClick");
            }
        }
    }

    @Override
    public void onClick(View v) {
        long thisTime = System.currentTimeMillis();
        View.OnClickListener listener = clickListener == null ? null : clickListener.get();
        if (listener != null && thisTime - lastTime > intervalTime) {
            try {
                lastTime = thisTime;
                listener.onClick(v);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.clickListener.onClick");
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        View.OnLongClickListener listener = longClockListener == null ? null : longClockListener.get();
        if (listener != null) {
            try {
                return listener.onLongClick(v);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.longClockListener.onLongClick");
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View.OnTouchListener listener = touchListener == null ? null : touchListener.get();
        if (listener != null) {
            try {
                return listener.onTouch(v, event);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.touchListener.onTouch");
            }
        }
        return false;
    }

    @Override
    public void onDateTimeSet(int year, int month, int day, int hour, int minute) {
        DialogBuilder.OnDateTimeSetListener listener = dateTimeSetListener == null ? null : dateTimeSetListener.get();
        if (listener != null) {
            try {
                listener.onDateTimeSet(year, month, day, hour, minute);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.dateTimeSetListener.onDateTimeSet");
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SeekBar.OnSeekBarChangeListener listener = seekBarChangeListener == null ? null : seekBarChangeListener.get();
        if (listener != null) {
            try {
                listener.onProgressChanged(seekBar, progress, fromUser);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onProgressChanged");
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        SeekBar.OnSeekBarChangeListener listener = seekBarChangeListener == null ? null : seekBarChangeListener.get();
        if (listener != null) {
            try {
                listener.onStartTrackingTouch(seekBar);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onStartTrackingTouch");
            }
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SeekBar.OnSeekBarChangeListener listener = seekBarChangeListener == null ? null : seekBarChangeListener.get();
        if (listener != null) {
            try {
                listener.onStopTrackingTouch(seekBar);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onStopTrackingTouch");
            }
        }
    }
}
