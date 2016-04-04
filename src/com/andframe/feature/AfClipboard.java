package com.andframe.feature;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

/**
 * 剪贴板封装
 * Created by SCWANG on 2016/4/3.
 */
public class AfClipboard {

    public static Exception copy(Context context, String value) {
        try {
            String service = Activity.CLIPBOARD_SERVICE;
            if (Build.VERSION.SDK_INT < 11) {
                Class<android.text.ClipboardManager> clazz = android.text.ClipboardManager.class;
                android.text.ClipboardManager clipboard = clazz
                        .cast(context.getSystemService(service));
                clipboard.setText(value);
            } else {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(service);
                clipboard.setPrimaryClip(ClipData.newPlainText(null, value));
            }
            return null;
        } catch (Exception e) {
            return e;
        }
    }
}
