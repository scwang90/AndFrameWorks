package com.andframe.adapter.recycler;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * RecyclerView.Adapter 与 BaseAdapter 合二为一
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements ListAdapter {

	//<editor-fold desc="重写通知">
	protected DataSetObservable mDataSetObservable = new DataSetObservable(this);
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}
	//	@Override
//	public void notifyDataSetChanged() {
//		mDataSetObservable.notifyChanged();
//	}
//	@Override
	public void notifyDataSetInvalidated() {
		mDataSetObservable.notifyInvalidated();
	}
	//</editor-fold>

	//<editor-fold desc="RecyclerView.Adapter 实现(取消，智能放到子类去实现)">

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		holder.setView(getView(position, holder.getView(), holder.getParent()));
	}

	@Override
	public int getItemCount() {
		return getCount();
	}
	//</editor-fold>

	//<editor-fold desc="BaseAdapter 实现">

	//<editor-fold desc="重写通知已经实现">

//	private final android.database.DataSetObservable mDataSetObservable = new android.database.DataSetObservable();
//	public void registerDataSetObserver(DataSetObserver observer) {
//		mDataSetObservable.registerObserver(observer);
//	}
//
//	public void unregisterDataSetObserver(DataSetObserver observer) {
//		mDataSetObservable.unregisterObserver(observer);
//	}
//
//	/**
//	 * Notifies the attached observers that the underlying data has been changed
//	 * and any View reflecting the data set should refresh itself.
//	 */
//	public void notifyDataSetChanged() {
//		mDataSetObservable.notifyChanged();
//	}
//
//	/**
//	 * Notifies the attached observers that the underlying data is no longer valid
//	 * or available. Once invoked this adapter is no longer valid and should
//	 * not report further data set changes.
//	 */
//	public void notifyDataSetInvalidated() {
//		mDataSetObservable.notifyInvalidated();
//	}
	//</editor-fold>

	//<editor-fold desc="父类中已经实现 （RecyclerView.Adapter） ">
	//	/**
//	 * @param observer
//     */
//	public boolean hasStableIds() {
//		return false;
//	}
	//</editor-fold>

	public boolean areAllItemsEnabled() {
		return true;
	}

	public boolean isEnabled(int position) {
		return true;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean isEmpty() {
		return getCount() == 0;
	}
	//</editor-fold>
}
