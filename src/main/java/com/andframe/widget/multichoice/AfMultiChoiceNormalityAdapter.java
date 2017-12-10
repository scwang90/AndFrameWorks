package com.andframe.widget.multichoice;

import java.util.List;

public abstract class AfMultiChoiceNormalityAdapter<T> extends AfMultiChoiceAdapter<T>{

	public AfMultiChoiceNormalityAdapter(List<T> ltdata) {
		super(ltdata);
		beginMultiChoice();
	}

	public AfMultiChoiceNormalityAdapter(List<T> ltdata, boolean dataSync) {
		super(ltdata, dataSync);
		beginMultiChoice();
	}

	@Override
	public boolean closeMultiChoice() {
		return false;
	}
}
