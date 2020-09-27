package com.andframe.impl.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.andframe.api.dialog.DialogBuilder;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class DialogBluePrint {

    public int iconId;
    public CharSequence title;
    public CharSequence message;
    public Drawable iconDrawable;
    public List<ButtonEntity> buttons = new ArrayList<>(3);

    public boolean cancelable = true;
    public DialogInterface.OnCancelListener cancelListener;
    public DialogInterface.OnDismissListener dismissListener;
    public CharSequence[] items;
    public boolean[] itemsChecked;
    public DialogInterface.OnClickListener itemsListener;
    public DialogInterface.OnMultiChoiceClickListener itemsMultiListener;
    public View view;

    /**
     * input nonth
     */
    public Date month;
    public DatePickerDialog.OnDateSetListener monthListener;

    /**
     * input date
     */
    public Date date;
    public DatePickerDialog.OnDateSetListener dateListener;

    /**
     * input time
     */
    public Date time;
    public TimePickerDialog.OnTimeSetListener timeListener;

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

    /**
     * No Ask
     */
    public CharSequence key;
    public int keyButtonIndex;
}
