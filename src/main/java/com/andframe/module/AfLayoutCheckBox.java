package com.andframe.module;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.andframe.api.viewer.Viewer;

@SuppressWarnings("unused")
public class AfLayoutCheckBox extends AfViewModuler implements OnClickListener{

    private OnClickListener mListener;
    private CompoundButton mButton = null;

    public AfLayoutCheckBox() {
    }

    public AfLayoutCheckBox(@NonNull CompoundButton checkbox) {
        initializeComponent(checkbox);
    }

    public AfLayoutCheckBox(@NonNull Viewer viewer, int id) {
        initializeComponent(viewer, id);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        if (view instanceof CompoundButton) {
            mButton = ((CompoundButton) view);
            mButton.setClickable(false);
            view = (View) view.getParent();
            view.setOnClickListener(this);
        } else if (view instanceof ViewGroup) {
            mButton = findCompoundButton(((ViewGroup) view));
            if (mButton != null) {
                mButton.setClickable(false);
                view.setOnClickListener(this);
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

    public boolean isChecked() {
        return mButton != null && mButton.isChecked();
    }

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
