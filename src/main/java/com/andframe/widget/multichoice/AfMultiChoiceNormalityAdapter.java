package com.andframe.widget.multichoice;

import android.content.Context;

import java.util.List;

public abstract class AfMultiChoiceNormalityAdapter<T> extends AfMultiChoiceAdapter<T>{

	public AfMultiChoiceNormalityAdapter(Context context, List<T> ltdata) {
		super(context, ltdata);
		beginMultiChoice();
	}

	public AfMultiChoiceNormalityAdapter(Context context, List<T> ltdata, boolean dataSync) {
		super(context, ltdata, dataSync);
		beginMultiChoice();
	}

	@Override
	public boolean closeMultiChoice() {
		return false;
	}
}
