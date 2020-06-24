package com.andframe.impl.dialog;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.andframe.api.dialog.DialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class DialogBluePrint {

    public int iconId;
    public CharSequence title;
    public CharSequence message;
    public Drawable iconDrawable;
    public List<ButtonEntity> buttons = new ArrayList<>(3);

    public boolean cancelable = false;
    public DialogInterface.OnCancelListener cancelListener;
    public DialogInterface.OnDismissListener dismissListener;
    public CharSequence[] items;
    public DialogInterface.OnClickListener itemsListener;
    public View view;

    /**
     * input text
     */
    public int inputType;
    public CharSequence inputText;
    public CharSequence inputHint;
    public DialogBuilder.InputTextListener  inputListener;

    public boolean buildNative;
    public long builderTimeout = 1000;
    public long builderDelayed = 10;

    public CharSequence key;
    public int keyButtonIndex;
}
