package com.andframe.layoutbind;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.andframe.activity.framework.AfViewable;


public class AfLayoutCheckBox extends AfLayoutModule implements OnClickListener{

	private CheckBox mCheckBox = null;

	public AfLayoutCheckBox(AfViewable view,CheckBox checkbox) {
		super(view);
		mCheckBox = checkbox;
		mLayout = findLayout(view);
		mIsValid = mLayout != null;
		if(isValid()){
			mLayout.setOnClickListener(this);
		}
	}

	public AfLayoutCheckBox(AfViewable view, int id) {
		super(view);
		mCheckBox = view.findViewByID(id);
		mLayout = findLayout(view);
		mIsValid = mLayout != null;
		if(isValid()){
			mLayout.setOnClickListener(this);
		}
	}

	@Override
	protected View findLayout(AfViewable view) {
		if(mCheckBox != null && mCheckBox.getParent() instanceof View){
			return(View)mCheckBox.getParent();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		mCheckBox.setChecked(!mCheckBox.isChecked());
	}
}
