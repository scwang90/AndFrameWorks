package com.andframe.listener;

import android.content.DialogInterface;

import com.andframe.exception.AfExceptionHandler;

/**
 * 安全取消监听器
 */
@SuppressWarnings("unused")
public class SafeOnCancelListener implements DialogInterface.OnCancelListener {

    private DialogInterface.OnCancelListener listener;

    public SafeOnCancelListener() {
    }

    public SafeOnCancelListener(DialogInterface.OnCancelListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        try {
            if (listener != null) {
                listener.onCancel(dialog);
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnCancelListener.onCancel");
        }
    }
}
