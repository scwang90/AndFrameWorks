package com.andframe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.recycler.RecyclerBaseAdapter;
import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.ListItem;
import com.andframe.api.ListItemAdapter;
import com.andframe.exception.AfExceptionHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 通用列表适配器
 *
 * @param <T>
 */
public abstract class AfListAdapter<T> extends RecyclerBaseAdapter<ViewHolderItem<T>> implements ListItemAdapter<T> {

    //<editor-fold desc="属性字段">
    protected boolean mDataSync;
    protected Context mContext;
    protected List<T> mltArray = new ArrayList<>();
    //</editor-fold>

    //<editor-fold desc="构造方法">
    public AfListAdapter(Context context, List<T> list) {
        this(context, list, true);
    }

    /**
     * @param dataSync 数据同步（true） 适配器的数据和外部的list同步 （false）适配器的数据独立管理
     */
    public AfListAdapter(Context context, List<T> list, boolean dataSync) {
        if (dataSync && list != null) {
            mltArray = list;
        } else if (list != null) {
            mltArray.addAll(list);
        }
        mDataSync = dataSync;
        mContext = context;
    }
    //</editor-fold>

    //<editor-fold desc="子类实现">
    protected abstract ListItem<T> newListItem(int viewType);
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
    public boolean addAll(@NonNull Collection<? extends T> list) {
        boolean ret = mltArray.addAll(list);
        notifyDataSetChanged();
        return ret;
    }

    /**
     * 适配器新增 数据刷新 接口
     */
    public void set(List<T> list) {
        if (mDataSync) {
            mltArray = list;
        } else {
            mltArray = new ArrayList<>(list);
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
    public boolean addAll(int index, @NonNull Collection<? extends T> collection) {
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
        //noinspection SuspiciousToArrayCall
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

    //<editor-fold desc="适配实现">

    public boolean isDataSync() {
        return mDataSync;
    }

    @Override
    public List<T> getList() {
        if (mDataSync) {
            return mltArray;
        } else {
            return new ArrayList<>(mltArray);
        }
    }

    @Override
    public ViewHolderItem<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            ListItem<T> item = newListItem(viewType);
            View view = onInflateItem(item, parent);
            return new ViewHolderItem<>(item, view);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfListAdapter.onCreateViewHolder");
            return new ViewHolderItem<>(null, new View(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position) {
        try {
            bindingItem(holder.itemView, holder.getItem(), position);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfListAdapter.onBindViewHolder.bindingItem");
        }
    }

    @Override
    public Object getItem(int i) {
        return mltArray.get(i);
    }

    @Override
    public int getItemCount() {
        return mltArray.size();
    }

//    protected ListItem<T> newListItem(List<T> ltarray, int position) {
//        return newListItem();
//    }

    protected View onInflateItem(ListItem<T> item, ViewGroup parent) {
        return item.onCreateView(mContext, parent);
    }

    protected boolean bindingItem(View view, ListItem<T> item, int index) {
        item.onBinding(view, get(index), index);
        return true;
    }

    //</editor-fold>

}
