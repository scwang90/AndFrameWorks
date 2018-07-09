package com.andpack.activity;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.andpack.impl.ApItemsCommonHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApItemsCommonActivity<T>  extends ApItemsActivity<T> {

    protected ApItemsCommonHelper<T> mApItemsCommonHelper;// = new ApItemsCommonHelper<>(this);

    public ApItemsCommonActivity() {
        super(null);
        this.mApItemsCommonHelper = new ApItemsCommonHelper<>(this);
        this.mApHelper = mApItemsCommonHelper;
    }

    protected void setToolbarActionTxt(@StringRes int txtId) {
        mApItemsCommonHelper.setToolbarActionTxt(txtId);
    }

    protected void setToolbarActionTxt(@StringRes int txtId, @Nullable View.OnClickListener listener) {
        mApItemsCommonHelper.setToolbarActionTxt(txtId, listener);
    }

    protected void setToolbarActionTxt(CharSequence txt) {
        mApItemsCommonHelper.setToolbarActionTxt(txt, null);
    }

    protected void setToolbarActionTxt(CharSequence txt, @Nullable View.OnClickListener listener) {
        mApItemsCommonHelper.setToolbarActionTxt(txt, listener);
    }

    protected void setToolbarActionImg(@IdRes int imgId) {
        mApItemsCommonHelper.setToolbarActionImg(imgId, null);
    }

    protected void setToolbarActionImg(@IdRes int imgId, @Nullable View.OnClickListener listener) {
        mApItemsCommonHelper.setToolbarActionImg(imgId, listener);
    }

}
