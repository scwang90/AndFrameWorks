package com.andframe.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfExceptionHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 通用列表适配器
 * @param <T>
 */
public abstract class AfListAdapter<T> extends BaseAdapter implements List<T> {

	protected boolean mDataSync;
	protected LayoutInflater mInflater;
	protected List<T> mltArray = new ArrayList<>();

	public AfListAdapter(Context context, List<T> ltdata) {
		this(context, ltdata, true);
	}

	/**
	 * @param dataSync 数据同步（true） 适配器的数据和外部的list同步 （false）适配器的数据独立管理
	 */
	public AfListAdapter(Context context, List<T> ltdata, boolean dataSync) {
		if (dataSync) {
			mltArray = ltdata;
		} else {
			mltArray = new ArrayList<>(ltdata);
		}
		mDataSync = dataSync;
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * @return 是否数据同步
	 */
	public boolean isDataSync() {
		return mDataSync;
	}

	//<editor-fold desc="重写通知">
	private final DataSetObservable mDataSetObservable = new DataSetObservable(){
		@Override
		public void registerObserver(DataSetObserver observer) {
			if (observer != null) {
				synchronized(mObservers) {
					if (!mObservers.contains(observer)) {
						mObservers.add(observer);
					}
				}
			}
		}
		@Override
		public void unregisterObserver(DataSetObserver observer) {
			if (observer != null) {
				synchronized(mObservers) {
					int index = mObservers.indexOf(observer);
					if (index > -1) {
						mObservers.remove(index);
					}
				}
			}
		}
	};
	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}
	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}
	public void notifyDataSetInvalidated() {
		mDataSetObservable.notifyInvalidated();
	}
	//</editor-fold>

	//<editor-fold desc="集合操作">
	/**
	 * 适配器新增 点击更多 数据追加接口
	 */
	public boolean add(T data) {
		boolean ret = mltArray.add(data);
		notifyDataSetChanged();
		return ret;
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 */
	@Override
	public boolean addAll(@NonNull Collection<? extends T> ltdata) {
		boolean ret = mltArray.addAll(ltdata);
		notifyDataSetChanged();
		return ret;
	}

	/**
	 * 适配器新增 数据刷新 接口
	 */
	public void set(List<T> ltdata) {
		if (mDataSync) {
			mltArray = ltdata;
		} else {
			mltArray = new ArrayList<>(ltdata);
		}
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 单个数据刷新 接口
	 */
	public T set(int index, T obj) {
		if (mltArray.size() > index) {
			T model = mltArray.set(index, obj);
			notifyDataSetChanged();
			return model;
		}
		return null;
	}

	/**
	 * 适配器新增 数据删除 接口
	 */
	public T remove(int index) {
		if (mltArray.size() > index) {
			T remove = mltArray.remove(index);
			notifyDataSetChanged();
			return remove;
		}
		return null;
	}

	@Override
	public boolean remove(Object object) {
		if (mltArray.remove(object)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(@NonNull Collection<?> collection) {
		if (mltArray.removeAll(collection)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	@Override
	public boolean retainAll(@NonNull Collection<?> collection) {
		if (mltArray.retainAll(collection)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return mltArray.size();
	}

	@NonNull
	@Override
	public List<T> subList(int start, int end) {
		return mltArray.subList(start, end);
	}

	@NonNull
	@Override
	public Object[] toArray() {
		return mltArray.toArray();
	}

	/**
	 * 适配器新增 数据插入 接口
	 */
	public void add(int index, T object) {
		if (mltArray.size() >= index) {
			mltArray.add(index, object);
			notifyDataSetChanged();
		}
	}

	/**
	 * 适配器新增 数据插入 接口
	 */
	public boolean addAll(int index,  @NonNull Collection<? extends T> collection) {
		if (mltArray.size() >= index) {
			boolean ret = mltArray.addAll(index, collection);
			notifyDataSetChanged();
			return ret;
		}
		return false;
	}

	@NonNull
	@Override
	public <T1> T1[] toArray(@NonNull T1[] array) {
		return mltArray.toArray(array);
	}

	@Override
	public void clear() {
		mltArray.clear();
		notifyDataSetChanged();
	}

	@Override
	public boolean contains(Object object) {
		return mltArray.contains(object);
	}

	@Override
	public boolean containsAll(@NonNull Collection<?> collection) {
		return mltArray.containsAll(collection);
	}

	@Override
	public T get(int location) {
		return mltArray.get(location);
	}

	@Override
	public int hashCode() {
		return mltArray.hashCode();
	}

	@Override
	public int indexOf(Object object) {
		return mltArray.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return mltArray.isEmpty();
	}

	@NonNull
	@Override
	public Iterator<T> iterator() {
		return mltArray.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return mltArray.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return mltArray.listIterator();
	}

	@NonNull
	@Override
	public ListIterator<T> listIterator(int location) {
		return mltArray.listIterator(location);
	}
	//</editor-fold>

	//<editor-fold desc="适配器实现">
	@Override
	public int getCount() {
		return mltArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return getItemAt(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(int position, View view, ViewGroup parent) {
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate 否则手机内存负载不起
		IListItem<T> item;
		try {
			if (view == null || !(view.getTag() instanceof IListItem)) {
				item = getListItem(mltArray,position);
				item.onHandle(new AfView(view = onInflateItem(item, parent)));
				view.setTag(item);
			} else {
				item = (IListItem<T>) view.getTag();
			}
			bindingItem(item, position);
		} catch (Throwable e) {
			String remark = "AfListAdapter("+getClass().getName()+").getView\r\n";
			View cview = view;
			if (parent != null){
				cview = parent;
			}
			if (cview != null && cview.getContext() != null){
				remark += "class = " + cview.getContext().getClass().toString();
			}
			if (view == null) {
				view = new View(mInflater.getContext());
			}
			AfExceptionHandler.handle(e, remark);
		}
		return view;
	}
	//</editor-fold>

	//<editor-fold desc="扩展方法">
	/**
	 * 获取数据列表
	 */
	public List<T> getList() {
		if (mDataSync) {
			return mltArray;
		} else {
			return new ArrayList<>(mltArray);
		}
	}

	public T getItemAt(int index) {
		return mltArray.get(index);
	}

	protected IListItem<T> getListItem(List<T> ltarray, int position) {
		return getListItem(ltarray.get(position));
	}

	protected View onInflateItem(IListItem<T> item, ViewGroup parent) {
		return item.onCreateView(mInflater, parent);
	}

	protected boolean bindingItem(IListItem<T> item, int index) {
		item.onBinding(getItemAt(index),index);
		return true;
	}
	//</editor-fold>

	//<editor-fold desc="子类实现">
	protected abstract IListItem<T> getListItem(T data);
	//</editor-fold>

	public interface IListItem<T> {
		/**
		 * 从视图中取出控件
		 */
		void onHandle(AfView view);
		/**
		 * 将数据绑定到控件显示
		 */
		void onBinding(T model,int index);
		/**
		 * 创建视图
		 */
		View onCreateView(LayoutInflater inflater, ViewGroup parent);
	}

}
