package com.andframe.listener;

import android.content.DialogInterface;

import com.andframe.exception.AfExceptionHandler;

/**
 * 安全多选监听器
 * Created by SCWANG on 2016/9/19.
 */
public class SafeOnMultiChoiceClickListener implements DialogInterface.OnMultiChoiceClickListener {

    private DialogInterface.OnMultiChoiceClickListener listener;

    public SafeOnMultiChoiceClickListener(DialogInterface.OnMultiChoiceClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        try {
            if (listener != null) {
                listener.onClick(dialog, which, isChecked);
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "SafeOnClickListener.onClick");
        }
    }
}
