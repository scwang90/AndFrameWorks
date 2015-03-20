package com.ontheway.layoutbind;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.ontheway.activity.framework.AfViewable;


public class AfLayoutCheckBox extends AfLayoutModule implements OnClickListener{

	private CheckBox mCheckBox = null;

	public AfLayoutCheckBox(AfViewable view,CheckBox checkbox) {
		super(view);
		// TODO Auto-generated constructor stub
		mCheckBox = checkbox;
		mLayout = findLayout(view);
		mIsValid = mLayout != null;
		if(isValid()){
			mLayout.setOnClickListener(this);
		}
	}

	public AfLayoutCheckBox(AfViewable view, int id) {
		super(view);
		// TODO Auto-generated constructor stub
		mCheckBox = view.findCheckBoxById(id);
		mLayout = findLayout(view);
		mIsValid = mLayout != null;
		if(isValid()){
			mLayout.setOnClickListener(this);
		}
	}

	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		if(mCheckBox != null && mCheckBox.getParent() instanceof View){
			return(View)mCheckBox.getParent();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mCheckBox.setChecked(!mCheckBox.isChecked());
	}
}
