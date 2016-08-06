package com.andframe.adapter;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfExceptionHandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AfListAdapterWrapper<T> extends AfListAdapter<T> implements WrapperListAdapter {

	protected AfListAdapter<T> wrapped;

	public AfListAdapterWrapper(AfListAdapter<T> wrapped) {
		super(wrapped.mInflater.getContext(), null);
		this.wrapped = wrapped;
	}

	@Override
	public ListAdapter getWrappedAdapter() {
		return wrapped;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		wrapped.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		wrapped.unregisterDataSetObserver(observer);
	}

	@Override
	public void notifyDataSetChanged() {
		wrapped.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		wrapped.notifyDataSetInvalidated();
	}

	@Override
	public boolean add(T data) {
		return wrapped.add(data);
	}

	@Override
	public boolean addAll(@NonNull Collection<? extends T> ltdata) {
		return wrapped.addAll(ltdata);
	}

	@Override
	public void set(List<T> ltdata) {
		wrapped.set(ltdata);
	}

	@Override
	public T set(int index, T obj) {
		return wrapped.set(index, obj);
	}

	@Override
	public T remove(int index) {
		return wrapped.remove(index);
	}

	@Override
	public boolean remove(Object object) {
		return wrapped.remove(object);
	}

	@Override
	public boolean removeAll(@NonNull Collection<?> collection) {
		return wrapped.removeAll(collection);
	}

	@Override
	public boolean retainAll(@NonNull Collection<?> collection) {
		return wrapped.retainAll(collection);
	}

	@Override
	public int size() {
		return wrapped.size();
	}

	@NonNull
	@Override
	public List<T> subList(int start, int end) {
		return wrapped.subList(start, end);
	}

	@NonNull
	@Override
	public Object[] toArray() {
		return wrapped.toArray();
	}

	@Override
	public void add(int index, T object) {
		wrapped.add(index, object);
	}

	@Override
	public boolean addAll(int index, @NonNull Collection<? extends T> collection) {
		return wrapped.addAll(index, collection);
	}

	@NonNull
	@Override
	public <T1> T1[] toArray(@NonNull T1[] array) {
		return wrapped.toArray(array);
	}

	@Override
	public void clear() {
		wrapped.clear();
	}

	@Override
	public boolean contains(Object object) {
		return wrapped.contains(object);
	}

	@Override
	public boolean containsAll(@NonNull Collection<?> collection) {
		return wrapped.containsAll(collection);
	}

	@Override
	public T get(int location) {
		return wrapped.get(location);
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}

	@Override
	public int indexOf(Object object) {
		return wrapped.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	@NonNull
	@Override
	public Iterator<T> iterator() {
		return wrapped.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return wrapped.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return wrapped.listIterator();
	}

	@NonNull
	@Override
	public ListIterator<T> listIterator(int location) {
		return wrapped.listIterator(location);
	}

	@Override
	public int getCount() {
		return wrapped.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		return wrapped.getItem(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return wrapped.getItemId(arg0);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
//		return wrapped.getView(position, view, parent);
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate 否则手机内存负载不起
		IListItem<T> item;
		try {
			if (view == null || !(view.getTag() instanceof IListItem)) {
				item = getListItem(wrapped.mltArray, position);
				item.onHandle(new AfView(view = onInflateItem(item, parent)));
				view.setTag(item);
			} else {
				item = (IListItem<T>) view.getTag();
			}
			bindingItem(item, position);
		} catch (Throwable e) {
			String remark = "AfListAdapter("+wrapped.getClass().getName()+").getView\r\n";
			View cview = view;
			if (parent != null){
				cview = parent;
			}
			if (cview != null && cview.getContext() != null){
				remark += "class = " + cview.getContext().getClass().toString();
			}
			if (view == null) {
				view = new View(wrapped.mInflater.getContext());
			}
			AfExceptionHandler.handle(e, remark);
		}
		return view;
	}

	@Override
	public List<T> getList() {
		return wrapped.getList();
	}

	@Override
	public T getItemAt(int index) {
		return wrapped.getItemAt(index);
	}

	@Override
	protected IListItem<T> getListItem(List<T> ltarray, int position) {
		return wrapped.getListItem(ltarray, position);
	}

	@Override
	protected View onInflateItem(IListItem<T> item, ViewGroup parent) {
		return wrapped.onInflateItem(item, parent);
	}

	@Override
	protected boolean bindingItem(IListItem<T> item, int index) {
		return wrapped.bindingItem(item, index);
	}

	@Override
	public IListItem<T> getListItem(T data) {
		return wrapped.getListItem(data);
	}

	@Override
	public boolean hasStableIds() {
		return wrapped.hasStableIds();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return wrapped.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		return wrapped.isEnabled(position);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return wrapped.getDropDownView(position, convertView, parent);
	}

	@Override
	public int getItemViewType(int position) {
		return wrapped.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return wrapped.getViewTypeCount();
	}
}
