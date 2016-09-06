package com.andframe.adapter;

import android.view.View;

import com.andframe.feature.AfView;
import com.andframe.widget.AfContactsListView;
import com.andframe.widget.AfContactsRefreshView;

import java.util.List;

/**
 * 联系人分组列表专用适配器
 * @param <G>
 * @param <C>
 */
@SuppressWarnings("unused")
public abstract class AfContactsAdapter<G, C> extends AfExpandableAdapter<G, C>{

	private IAfGroupItem<G> mHeader = null;

	public AfContactsAdapter(AfContactsListView listView, List<AfGroup<G, C>> groups) {
		super(listView.getContext(), groups);
		mHeader = getItemLayout(0);
		View view = mHeader.onInflateItem(mInflater, listView);
		mHeader.onHandle(new AfView(view));
		listView.setHeaderView(view);
	}

	public AfContactsAdapter(AfContactsRefreshView listView, List<AfGroup<G, C>> groups) {
		super(listView.getContext(), groups);
		mHeader = getItemLayout(0);
		View view = mHeader.onInflateItem(mInflater, listView);
		mHeader.onHandle(new AfView(view));
		listView.setHeaderView(view);
	}

	public void bindHeader(View header, int group, int child, int alpha) {
		mHeader.onBinding(mGroups.get(group).mValue,true);
	}

}
