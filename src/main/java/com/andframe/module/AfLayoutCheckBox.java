package com.andframe.module;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.andframe.api.Viewer;

@SuppressWarnings("unused")
public class AfLayoutCheckBox extends AfViewModule implements OnClickListener{

    private OnClickListener mListener;
    private CompoundButton mButton = null;

    public AfLayoutCheckBox() {
    }

    public AfLayoutCheckBox(Viewer view, CompoundButton checkbox) {
        super((View) checkbox.getParent());
        mButton = checkbox;
        if (isValid()) {
            mButton.setClickable(false);
            wrapped.setOnClickListener(this);
        }
    }

    public AfLayoutCheckBox(Viewer view, int id) {
        super((View) view.findViewByID(id).getParent());
        mButton = view.findViewByID(id);
        if (isValid()) {
            mButton.setClickable(false);
            wrapped.setOnClickListener(this);
        }
    }

    @Override
    protected void onCreated(Viewer viewable, View view) {
        super.onCreated(viewable, view);
        if (view instanceof CompoundButton) {
            mButton = ((CompoundButton) view);
            mButton.setClickable(false);
            wrapped = (View) view.getParent();
            wrapped.setOnClickListener(this);
        } else if (view instanceof ViewGroup) {
            mButton = findCompoundButton(((ViewGroup) view));
            if (mButton != null) {
                wrapped = view;
                mButton.setClickable(false);
                wrapped.setOnClickListener(this);
            }
        }
    }

    private CompoundButton findCompoundButton(ViewGroup view) {
        for (int i = 0, len = view.getChildCount(); i < len; i++) {
            View child = view.getChildAt(i);
            if (child instanceof CompoundButton) {
                return ((CompoundButton) child);
            } else if (child instanceof ViewGroup) {
                CompoundButton button = findCompoundButton(((ViewGroup) child));
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return mButton != null && mButton.isChecked();
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && mButton != null;
    }

    public void setChecked(boolean checked) {
        if (isValid()) {
            mButton.setChecked(checked);
        }
    }

    @Override
    public void onClick(View v) {
        if (mButton != null) {
            mButton.setChecked(!mButton.isChecked());
            if (mListener != null) {
                mListener.onClick(v);
            }
        }
    }
}
