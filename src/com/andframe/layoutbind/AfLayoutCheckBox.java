package com.andframe.layoutbind;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.framework.AfViewModule;


public class AfLayoutCheckBox extends AfViewModule implements OnClickListener{

	private CheckBox mCheckBox = null;

	public AfLayoutCheckBox(AfViewable view,CheckBox checkbox) {
		super((View)checkbox.getParent());
		mCheckBox = checkbox;
		if(isValid()){
			target.setOnClickListener(this);
		}
	}

	public AfLayoutCheckBox(AfViewable view, int id) {
		super((View)view.findViewByID(id).getParent());
		mCheckBox = view.findViewByID(id);
		if(isValid()){
			target.setOnClickListener(this);
		}
	}

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
