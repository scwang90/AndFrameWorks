package com.andframe.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


/**
 * 页面化的对话框
 * Created by Administrator on 2016/3/5 0005.
 */
public class AfPageDialog extends AfDialog{

    public AfPageDialog(Context context) {
        super(context);
    }

    public AfPageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AfPageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.TOP);
        getWindow().setBackgroundDrawable(null);
    }
}
