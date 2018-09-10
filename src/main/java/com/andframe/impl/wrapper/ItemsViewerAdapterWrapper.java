package com.andframe.impl.wrapper;

import android.annotation.TargetApi;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.andframe.adapter.recycler.DataSetObservable;
import com.andframe.adapter.recycler.RecyclerBaseAdapter;
import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.query.hindler.Where;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * ItemsViewerAdapterWrapper
 * Created by SCWANG on 2016/10/29.
 */
@SuppressWarnings("WeakerAccess")
public class ItemsViewerAdapterWrapper<T> extends RecyclerBaseAdapter<ViewHolderItem<T>> implements ItemsViewerAdapter<T>, WrapperListAdapter {

    protected ItemsViewerAdapter<T> wrapped;

    public ItemsViewerAdapterWrapper(ItemsViewerAdapter<T> wrapped) {
        this.wrapped = wrapped;
        //为特殊 Warpper 做准备
        mDataSetObservable = new DataSetObservableWrapper(this);
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return wrapped;
    }

    //<editor-fold desc="由于Final导致的特殊Wrapper">

    protected class DataSetObservableWrapper extends DataSetObservable {
        public DataSetObservableWrapper(RecyclerView.Adapter adapter) {
            super(adapter);
        }
        public void notifyChanged() {
            wrapped.notifyDataSetChanged();
        }

    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        wrapped.setHasStableIds(hasStableIds);
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        wrapped.unregisterAdapterDataObserver(observer);
    }

//	@Override
//	public void notifyDataSetChanged() {
//		wrapped.notifyDataSetChanged();
//	}
//	@Override
//	public boolean hasStableIds() {
//		return wrapped.hasStableIds();
//	}
    //</editor-fold>

    //<editor-fold desc="Wrapper API-24 && JAVA_8">
    @Override @TargetApi(24)
    public Spliterator<T> spliterator() {
        return wrapped.spliterator();
    }

    @Override @TargetApi(24)
    public void replaceAll(UnaryOperator<T> operator) {
        wrapped.replaceAll(operator);
    }

    @Override @TargetApi(24)
    public void sort(Comparator<? super T> c) {
        wrapped.sort(c);
    }

    @Override @TargetApi(24)
    public boolean removeIf(Predicate<? super T> filter) {
        return wrapped.removeIf(filter);
    }

    @Override @TargetApi(24)
    public Stream<T> stream() {
        return wrapped.stream();
    }

    @Override @TargetApi(24)
    public Stream<T> parallelStream() {
        return wrapped.parallelStream();
    }

    @Override @TargetApi(24)
    public void forEach(Consumer<? super T> action) {
        wrapped.forEach(action);
    }
    //</editor-fold>

    //<editor-fold desc="Wrapper AfListAdapter">

//    @Override
//    public boolean isDataSync() {
//        return wrapped.isDataSync();
//    }

    @NonNull
    @Override
    public ItemViewer<T> newItemViewer(int viewType) {
        return wrapped.newItemViewer(viewType);
    }

    @NonNull
    @Override
    public View inflateItem(ItemViewer<T> item, ViewGroup parent) {
        return wrapped.inflateItem(item, parent);
    }

    @Override
    public void bindingItem(View view, ItemViewer<T> item, int index) {
        wrapped.bindingItem(view, item, index);
    }

    @Override
    public void notifyDataSetInvalidated() {
        wrapped.notifyDataSetInvalidated();
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
    public int getItemCount() {
        return wrapped.getItemCount();
    }

    @NonNull
    @Override
    public List<T> getList() {
        return wrapped.getList();
    }

    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position) {
        wrapped.onBindViewHolder(holder, position);
    }

    @Override
    public ViewHolderItem<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return wrapped.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void set(@NonNull List<T> list) {
        wrapped.set(list);
    }

    @Override
    public void put(T model) {
        wrapped.put(model);
    }

    //</editor-fold>

    //<editor-fold desc="Wrapper BaseAdapter">
    @Override
    public boolean areAllItemsEnabled() {
        return wrapped.areAllItemsEnabled();
    }

//    @Override
//    public int getCount() {
//        return wrapped.getCount();
//    }

    @Override
    public Object getItem(int i) {
        return wrapped.getItem(i);
    }

    @Override
    public int getItemViewType(int position) {
        return wrapped.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return wrapped.getView(i, view, viewGroup);
    }

    @Override
    public int getViewTypeCount() {
        return wrapped.getViewTypeCount();
    }

    @Override
    public boolean isEnabled(int position) {
        return wrapped.isEnabled(position);
    }
    //</editor-fold>

    //<editor-fold desc="Wrapper Recycler.Adapter">
    @Override
    public long getItemId(int position) {
        return wrapped.getItemId(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        wrapped.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position, List<Object> payloads) {
        wrapped.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        wrapped.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(ViewHolderItem<T> holder) {
        return wrapped.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolderItem<T> holder) {
        wrapped.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolderItem<T> holder) {
        wrapped.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(ViewHolderItem<T> holder) {
        wrapped.onViewRecycled(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        wrapped.registerAdapterDataObserver(observer);
    }
    //</editor-fold>

    //<editor-fold desc="Wrapper List">
    @Override
    public boolean add(T data) {
        return wrapped.add(data);
    }

    @Override
    public void add(int index, T object) {
        wrapped.add(index, object);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> collection) {
        return wrapped.addAll(index, collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> list) {
        return wrapped.addAll(list);
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
    public T set(int index, T obj) {
        return wrapped.set(index, obj);
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

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] array) {
        return wrapped.toArray(array);
    }
    //</editor-fold>

    //<editor-fold desc="Wrapper ListQuery">
    @Override
    public void remove(Where<T> where) {
        wrapped.remove(where);
    }
    //</editor-fold>
}
