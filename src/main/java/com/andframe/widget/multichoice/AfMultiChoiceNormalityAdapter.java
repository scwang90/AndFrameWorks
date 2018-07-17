package com.andframe.widget.multichoice;

import java.util.List;

@SuppressWarnings("unused")
public abstract class AfMultiChoiceNormalityAdapter<T> extends AfMultiChoiceAdapter<T>{

	public AfMultiChoiceNormalityAdapter() {
		beginMultiChoice();
	}

	public AfMultiChoiceNormalityAdapter(List<T> list) {
		super(list);
		beginMultiChoice();
	}

	public AfMultiChoiceNormalityAdapter(List<T> list, boolean dataSync) {
		super(list, dataSync);
		beginMultiChoice();
	}

	@Override
	public boolean closeMultiChoice() {
		return false;
	}
}
