package com.andframe.impl.dialog;

import android.content.DialogInterface;

public class ButtonEntity {
    public int color;
    public CharSequence text;
    public DialogInterface.OnClickListener listener;

    public ButtonEntity() {
    }

    public ButtonEntity(CharSequence text, int color, DialogInterface.OnClickListener listener) {
        this.text = text;
        this.color = color;
        this.listener = listener;
    }
}