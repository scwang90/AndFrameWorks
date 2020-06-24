package com.andframe.api.dialog;

import android.app.Dialog;
import android.content.Context;

import com.andframe.impl.dialog.DialogBluePrint;

public interface DialogFactory {
    Dialog build(Context context, DialogBluePrint entity);
}
