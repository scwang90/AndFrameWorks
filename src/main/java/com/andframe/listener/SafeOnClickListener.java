package com.andframe.listener;

import android.content.DialogInterface;
import android.view.View;

import com.andframe.exception.AfExceptionHandler;

/**
 * 安全点击监听器
 */
@SuppressWarnings("unused")
public class SafeOnClickListener implements DialogInterface.OnClickListener,View.OnClickListener {

    private View.OnClickListener vlistener;
    private DialogInterface.OnClickListener dlistener;

    public SafeOnClickListener() {

    }

    public SafeOnClickListener(DialogInterface.OnClickListener listener) {
        this.dlistener = listener;
    }

    public SafeOnClickListener(View.OnClickListener listener) {
        vlistener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        try {
            if (dlistener != null) {
                dlistener.onClick(dialog, which);
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "SafeOnClickListener.onClick");
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (vlistener != null) {
                vlistener.onClick(view);
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "SafeOnClickListener.onClick");
        }
    }
}