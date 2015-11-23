package com.andframe.layoutbind;

import android.view.View;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.application.AfExceptionHandler;

public class AfModuleTitlebarImpl extends AfModuleTitlebar {

    public static final int FUNCTION_ADD = 10;
    public static final int FUNCTION_OK = 11;

    public static final int ID_GOBACK = R.id.af_titlebar_other_goback;
    public static final int ID_MEUN = R.id.af_titlebar_other_meun;

    private View.OnClickListener mBtOkClickListener;
    private View.OnClickListener mBtAddClickListener;

    public AfModuleTitlebarImpl(AfPageable page) {
        super(page,R.id.af_titlebar_other_layout);
    }

    public AfModuleTitlebarImpl(AfPageable page, int function) {
        super(page, function,R.id.af_titlebar_other_layout);
    }

    public AfModuleTitlebarImpl(AfPageable page, String title) {
        super(page, title,R.id.af_titlebar_other_layout);
    }

    @Override
    public int getTitleTextId() {
        return R.id.af_titlebar_other_title;
    }

    @Override
    public int getBtMeunId() {
        return R.id.af_titlebar_other_meun;
    }

    @Override
    public int getBtGoBackId() {
        return R.id.af_titlebar_other_goback;
    }

    @Override
    public void onClick(View v) {
        if (mWeakRefActivity != null) {
            if (v.getId() == getBtMeunId()) {
                if (mFunction == FUNCTION_OK && mBtOkClickListener != null) {
                    try {
                        mBtOkClickListener.onClick(v);
                    } catch (Throwable e) {
                        AfExceptionHandler.handler(e, "AfModuleTitlebarImpl.mBtOK.onClick");
                    }
                    return;
                } else if (mFunction == FUNCTION_ADD && mBtAddClickListener != null) {
                    try {
                        mBtAddClickListener.onClick(v);
                    } catch (Throwable e) {
                        AfExceptionHandler.handler(e, "AfModuleTitlebarImpl.mBtAdd.onClick");
                    }
                    return;
                }
            }
        }
        super.onClick(v);
    }

    public void setFunction(int function) {
        super.setFunction(function);
        switch (function) {
            case FUNCTION_ADD:
                mBtMenu.setImageResource(R.drawable.af_icon_add);
                break;
            case FUNCTION_OK:
                mBtMenu.setImageResource(R.drawable.af_icon_ok);
                break;
            case FUNCTION_MENU:
                mBtMenu.setImageResource(R.drawable.af_icon_menu);
                break;
        }
    }

    public void setOnAddListener(View.OnClickListener listener) {
        mBtAddClickListener = listener;
    }

    public void setOnOkListener(View.OnClickListener listener) {
        mBtOkClickListener = listener;
    }

    public void setFunctionOnClickListener(int function, View.OnClickListener listener) {
        switch (function) {
            case FUNCTION_ADD:
                mBtAddClickListener = listener;
                return;
            case FUNCTION_OK:
                mBtOkClickListener = listener;
                return;
        }
        super.setFunctionOnClickListener(function, listener);
    }
}
