package com.andframe.impl.dialog;

import android.content.DialogInterface;

public class ButtonEntity {
    public CharSequence text;
    public DialogInterface.OnClickListener listener;

    public ButtonEntity() {
    }

    public ButtonEntity(CharSequence text, DialogInterface.OnClickListener listener) {
        this.text = text;
        this.listener = listener;
    }
}