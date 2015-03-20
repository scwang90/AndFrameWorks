package com.ontheway.layoutbind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ontheway.activity.framework.AfViewable;
import com.ontheway.layoutbind.framework.IAfLayoutModule;


public class AfSwitchCheckBox implements IAfLayoutModule, OnCheckedChangeListener {

	private CheckBox mCheckBox = null;
	private Boolean mIsValid = false;
	private OnCheckedChangeListener mListener = null;
	private List<String> mltVaules = new ArrayList<String>();

	public AfSwitchCheckBox(CheckBox checkbox) {
		// TODO Auto-generated constructor stub
		mCheckBox = checkbox;
		mltVaules.add("");
		mltVaules.add("");
		mCheckBox.setOnCheckedChangeListener(this);
		mIsValid = mCheckBox != null;
	}

	public AfSwitchCheckBox(AfViewable page, int id) {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		if (mIsValid) {
			mltVaules.set(checked ? 1 : 0, value);
		}
	}

	public final void setValueId(boolean checked, Integer id) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		mListener = listener;
	}

	@Override
	public final void onCheckedChanged(CompoundButton button,
			boolean isChecked) {
		// TODO Auto-generated method stub
		mCheckBox.setText(mltVaules.get(isChecked?1:0));
		if (mListener != null) {
			mListener.onCheckedChanged(button, isChecked);
		}
	}

	@Override
	public final void hide() {
		// TODO Auto-generated method stub
		mCheckBox.setVisibility(View.GONE);
	}

	@Override
	public final void show() {
		// TODO Auto-generated method stub
		mCheckBox.setVisibility(View.VISIBLE);
	}

	@Override
	public final View getLayout() {
		// TODO Auto-generated method stub
		return mCheckBox;
	}

	@Override
	public final boolean isValid() {
		// TODO Auto-generated method stub
		return mIsValid;
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		mCheckBox.setEnabled(enabled);
	}

	@Override
	public boolean isVisibility() {
		// TODO Auto-generated method stub
		return mCheckBox.getVisibility() == View.VISIBLE;
	}
}
