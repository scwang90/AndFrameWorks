package com.andframe.module;

import android.view.View;

import com.andframe.R;
import com.andframe.api.view.Viewer;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.feature.AfView;

@SuppressWarnings("unused")
public class AfModuleTitlebarImpl extends AfModuleTitlebar {

    public static final int FUNCTION_ADD = 10;
    public static final int FUNCTION_OK = 11;

    public static final int ID_GOBACK = R.id.af_titlebar_left_goback;
    public static final int ID_MEUN = R.id.af_titlebar_right_img;

    private OnClickListener mBtOkClickListener;
    private OnClickListener mBtAddClickListener;

    protected AfModuleTitlebarImpl() {

    }

    protected AfModuleTitlebarImpl(Viewer viewable, boolean override) {
        super(override ? viewable: new AfView(viewable.getView()));
    }

    public AfModuleTitlebarImpl(Viewer viewable) {
        super(viewable, R.id.af_titlebar_layout);
    }

    public AfModuleTitlebarImpl(Viewer viewable, int function) {
        super(viewable, function,R.id.af_titlebar_layout);
    }

    public AfModuleTitlebarImpl(Viewer viewable, String title) {
        super(viewable, title,R.id.af_titlebar_layout);
    }

    @Override
    public int getTitleTextId() {
        return R.id.af_titlebar_title;
    }

    @Override
    public int getRightImgId() {
        return R.id.af_titlebar_right_img;
    }

    @Override
    public int getRightTxtId() {
        return R.id.af_titlebar_right_txt;
    }

    @Override
    public int getBtGoBackId() {
        return R.id.af_titlebar_left_goback;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == getRightImgId()) {
            if (mFunction == FUNCTION_OK && mBtOkClickListener != null) {
                try {
                    mBtOkClickListener.onClick(v);
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, "AfModuleTitlebarImpl.mBtOK.onClick");
                }
                return;
            } else if (mFunction == FUNCTION_ADD && mBtAddClickListener != null) {
                try {
                    mBtAddClickListener.onClick(v);
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, "AfModuleTitlebarImpl.mBtAdd.onClick");
                }
                return;
            }
        }
        super.onClick(v);
    }

    public void setFunction(int function) {
        super.setFunction(function);
        switch (function) {
            case FUNCTION_ADD:
                mBtRightImg.setImageResource(R.drawable.af_icon_add);
                break;
            case FUNCTION_OK:
                mBtRightImg.setImageResource(R.drawable.af_icon_ok);
                break;
            case FUNCTION_MENU:
                mBtRightImg.setImageResource(R.drawable.af_icon_menu);
                break;
        }
    }

    public void setOnAddListener(OnClickListener listener) {
        mBtAddClickListener = listener;
    }

    public void setOnOkListener(OnClickListener listener) {
        mBtOkClickListener = listener;
    }

    public void setFunctionOnClickListener(int function, OnClickListener listener) {
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
