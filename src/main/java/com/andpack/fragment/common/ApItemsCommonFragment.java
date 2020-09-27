package com.andpack.fragment.common;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andpack.fragment.ApItemsFragment;
import com.andpack.impl.ApItemsCommonHelper;

/**
 * 通用列表页面
 * Created by SCWANG on 2017/5/18.
 */
public abstract class ApItemsCommonFragment<T> extends ApItemsFragment<T> {

    protected ApItemsCommonHelper<T> mApItemsCommonHelper;// = new ApItemsCommonHelper<>(this);

    public ApItemsCommonFragment() {
        super(null);
        this.mApItemsCommonHelper = new ApItemsCommonHelper<>(this);
        this.mApHelper = mApItemsCommonHelper;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return mApItemsCommonHelper.onCreateView(inflater, container);
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
