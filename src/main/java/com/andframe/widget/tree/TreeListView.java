package com.andframe.widget.tree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.api.viewer.ItemsViewer;
import com.andframe.widget.select.SelectListItemAdapter;
import com.andframe.widget.select.SelectItemsViewer;

@SuppressWarnings("unused")
public class TreeListView extends SelectItemsViewer<ListView> {

	protected TreeViewItemAdapter<?> mAdapter;

	public TreeListView(ItemsViewer<ListView> itemsViewer) {
		super(itemsViewer);
	}

	/**
	 * Deprecated. Use {@link #setAdapter(TreeViewItemAdapter adapter)} from
	 * now on.
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if(adapter instanceof TreeViewItemAdapter){
			mAdapter = (TreeViewItemAdapter<?>) adapter;
		}
	}

	/**
	 * Deprecated. Use {@link #setAdapter(TreeViewItemAdapter adapter)} from
	 * now on.
	 * @deprecated
	 */
	@Override
	public void setAdapter(SelectListItemAdapter<?> adapter) {
		super.setAdapter(adapter);
		if(adapter instanceof TreeViewItemAdapter){
			mAdapter = (TreeViewItemAdapter<?>) adapter;
		}
	}

	public void setAdapter(TreeViewItemAdapter<?> adapter) {
		super.setAdapter(adapter);
		mAdapter = adapter;
	}

    protected ListView onCreateTargetView(Context context, AttributeSet attrs) {
        return new ListView(context);
    }

	@Override
	public void onItemClick(AdapterView<?> adview, View view, int index, long id) {
		if (index < 0 || mAdapter == null || mAdapter.isSelectMode()
				|| mAdapter.isItemClickable(index)) {
			super.onItemClick(adview, view, index, id);
		} else {
			mAdapter.onItemClick(index);
		}
	}
}
