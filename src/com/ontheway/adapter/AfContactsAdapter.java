package com.ontheway.adapter;

import java.util.List;

import android.view.View;

import com.ontheway.activity.framework.AfView;
import com.ontheway.view.AfContactsListView;
import com.ontheway.view.AfContactsRefreshView;

public abstract class AfContactsAdapter<G, C> extends AfExpandableAdapter<G, C>{

	private IAfGroupItem<G> mHeader = null;

	public AfContactsAdapter(AfContactsListView listView, List<AfGroup<G, C>> groups) {
		super(listView.getContext(), groups);
		mHeader = getItemLayout(0);
		View view = mInflater.inflate(mHeader.getLayoutId(), listView, false);
		mHeader.onHandle(new AfView(view));
		listView.setHeaderView(view);
	}

	public AfContactsAdapter(AfContactsRefreshView listView, List<AfGroup<G, C>> groups) {
		super(listView.getContext(), groups);
		mHeader = getItemLayout(0);
		View view = mInflater.inflate(mHeader.getLayoutId(), listView, false);
		mHeader.onHandle(new AfView(view));
		listView.setHeaderView(view);
	}

	public void bindHeader(View header, int group,int child, int alpha) {
		// TODO Auto-generated method stub
		mHeader.onBinding(mGroups.get(group).mValue,true);
	}

}
