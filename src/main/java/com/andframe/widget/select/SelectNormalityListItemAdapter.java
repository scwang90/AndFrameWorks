package com.andframe.widget.select;

import java.util.List;

@SuppressWarnings("unused")
public abstract class SelectNormalityListItemAdapter<T> extends SelectListItemAdapter<T> {

	public SelectNormalityListItemAdapter() {
		beginSelectMode();
	}

	public SelectNormalityListItemAdapter(List<T> list) {
		super(list);
		beginSelectMode();
	}

	public SelectNormalityListItemAdapter(List<T> list, boolean dataSync) {
		super(list, dataSync);
		beginSelectMode();
	}

	@Override
	public boolean closeSelectMode() {
		return false;
	}
}
