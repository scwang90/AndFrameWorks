package com.andframe.layoutbind;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.framework.AfViewModule;


public class AfLayoutCheckBox extends AfViewModule implements OnClickListener{

	private OnClickListener mListener;
	private CompoundButton mButton = null;

	public AfLayoutCheckBox() {
	}

	public AfLayoutCheckBox(AfViewable view, CompoundButton checkbox) {
		super((View) checkbox.getParent());
		mButton = checkbox;
		if (isValid()) {
			mButton.setClickable(false);
			target.setOnClickListener(this);
		}
	}

	public AfLayoutCheckBox(AfViewable view, int id) {
		super((View) view.findViewByID(id).getParent());
		mButton = view.findViewByID(id);
		if (isValid()) {
			mButton.setClickable(false);
			target.setOnClickListener(this);
		}
	}

	@Override
	protected void onCreated(AfViewable viewable, View view) {
		super.onCreated(viewable, view);
		if (view instanceof CompoundButton) {
			mButton = ((CompoundButton) view);
			mButton.setClickable(false);
			target = (View) view.getParent();
			target.setOnClickListener(this);
		} else if (view instanceof ViewGroup) {
			mButton = findCompoundButton(((ViewGroup) view));
			if (mButton != null) {
				target = view;
				mButton.setClickable(false);
				target.setOnClickListener(this);
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

	@Override
	public boolean isValid() {
		return super.isValid() && mButton != null;
	}

	@ViewDebug.ExportedProperty
	public boolean isChecked() {
		if (mButton == null) {
			return false;
		}
		return mButton.isChecked();
	}

	public void setChecked(boolean checked) {
		if (isValid()) {
			mButton.setChecked(checked);
		}
	}

	@Override
	public void setOnClickListener(OnClickListener listener) {
		mListener = listener;
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
