package com.andframe.api;

/**
 * Toast 气泡提示
 * Created by SCWANG on 2016/9/1.
 */
public interface Toaster {

    //<editor-fold desc="气泡提示">
    void makeToastLong(int resId);

    void makeToastShort(int resId);

    void makeToastLong(CharSequence tip);

    void makeToastLong(CharSequence tip, Throwable e);

    void makeToastShort(CharSequence tip);

    void makeToastShort(CharSequence tip, Throwable e);
    //</editor-fold>

}
