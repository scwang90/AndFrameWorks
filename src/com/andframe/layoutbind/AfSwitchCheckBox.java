package com.andframe.layoutbind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.framework.IAfLayoutModule;


public class AfSwitchCheckBox implements IAfLayoutModule, OnCheckedChangeListener {

	private CheckBox mCheckBox = null;
	private Boolean mIsValid = false;
	private OnCheckedChangeListener mListener = null;
	private List<String> mltVaules = new ArrayList<String>();

	public AfSwitchCheckBox(CheckBox checkbox) {
		mCheckBox = checkbox;
		mltVaules.add("");
		mltVaules.add("");
		mCheckBox.setOnCheckedChangeListener(this);
		mIsValid = mCheckBox != null;
	}

	public AfSwitchCheckBox(AfViewable page, int id) {
		View view = page.findViewById(id);
		if (view instanceof CheckBox) {
			mltVaules.add("");
			mltVaules.add("");
			mCheckBox = (CheckBox) view;
			mCheckBox.setOnCheckedChangeListener(this);
		}
		mIsValid = mCheckBox != null;
	}

	public final void setValue(boolean checked, String value) {
		if (mIsValid) {
			mltVaules.set(checked ? 1 : 0, value);
		}
	}

	public final void setValueId(boolean checked, Integer id) {
		if (mIsValid) {
			Context context = mCheckBox.getContext();
			Resources resources = context.getResources();
			mltVaules.set(checked ? 1 : 0, resources.getString(id));
			if(mCheckBox.isChecked() == checked){
				mCheckBox.setText(mltVaules.get(checked ? 1 : 0));
			}
		}
	}

	public final void setValues(Collection<String> values) {
		mltVaules.clear();
		for (String value : values) {
			mltVaules.add(value);
		}
		if(mltVaules.size() >= 2){
			if(mCheckBox.isChecked()){
				mCheckBox.setText(mltVaules.get(1));
			}else{
				mCheckBox.setText(mltVaules.get(0));
			}
		}
	}

	public final void setValueIds(Collection<Integer> ids) {
		if (mIsValid) {
			Context context = mCheckBox.getContext();
			Resources resources = context.getResources();
			mltVaules.clear();
			for (Integer id : ids) {
				mltVaules.add(resources.getString(id));
			}
		}
	}

	public final void setOnCheckedChangeListener(
			OnCheckedChangeListener listener) {
		mListener = listener;
	}

	@Override
	public final void onCheckedChanged(CompoundButton button,
			boolean isChecked) {
		mCheckBox.setText(mltVaules.get(isChecked?1:0));
		if (mListener != null) {
			mListener.onCheckedChanged(button, isChecked);
		}
	}

	@Override
	public final void hide() {
		mCheckBox.setVisibility(View.GONE);
	}

	@Override
	public final void show() {
		mCheckBox.setVisibility(View.VISIBLE);
	}

	@Override
	public final View getLayout() {
		return mCheckBox;
	}

	@Override
	public final boolean isValid() {
		return mIsValid;
	}

	@Override
	public void setEnabled(boolean enabled) {
		mCheckBox.setEnabled(enabled);
	}

	@Override
	public boolean isVisibility() {
		return mCheckBox.getVisibility() == View.VISIBLE;
	}
}
