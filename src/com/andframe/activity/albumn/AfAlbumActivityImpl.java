package com.andframe.activity.albumn;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfViewable;

public class AfAlbumActivityImpl extends AfAlbumActivity{

	@Override
	protected int getAlbumLayoutId() {
		return (R.layout.af_album_main);
	}

	@Override
	protected TextView getTextViewName(AfViewable view) {
		return view.findViewByID(R.id.album_name);
	}

	@Override
	protected TextView getTextViewSize(AfViewable view) {
		return view.findViewByID(R.id.album_size);
	}

	@Override
	protected ViewPager getViewPager(AfViewable view) {
		return view.findViewByID(R.id.pager);
	}

	@Override
	protected TextView getTextViewDetail(AfViewable view) {
		return view.findViewByID(R.id.album_tip);
	}

}
