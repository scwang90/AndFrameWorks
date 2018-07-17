package com.andframe.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.itemviewer.AfItemViewer;
import com.andframe.adapter.recycler.RecyclerBaseAdapter;
import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.query.hindler.Where;
import com.andframe.exception.AfExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 通用列表适配器
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public abstract class AfListAdapter<T> extends RecyclerBaseAdapter<ViewHolderItem<T>> implements ItemsViewerAdapter<T> {

    //<editor-fold desc="属性字段">
    @SuppressWarnings("WeakerAccess")
    protected boolean mDataSync;
    protected List<T> mltArray = new ArrayList<>();
    //</editor-fold>

    //<editor-fold desc="构造方法">

    public AfListAdapter() {
        mDataSync = true;
    }

    public AfListAdapter(T[] arrays) {
        this(Arrays.asList(arrays), false);
    }

    public AfListAdapter(List<T> list) {
        this(list, true);
    }

    /**
     * @param dataSync 数据同步（true） 适配器的数据和外部的list同步 （false）适配器的数据独立管理
     */
    public AfListAdapter(List<T> list, boolean dataSync) {
        if (dataSync && list != null) {
            mltArray = list;
        } else if (list != null) {
            mltArray.addAll(list);
        }
        mDataSync = dataSync;
    }
    //</editor-fold>

    //<editor-fold desc="子类实现">
    @NonNull
    public abstract ItemViewer<T> newItemViewer(int viewType);
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
    public void set(@NonNull List<T> list) {
        if (mDataSync) {
            if (list instanceof AfListAdapter) {
                list = ((AfListAdapter<T>) list).getList();
            }
            mltArray = list;
        } else {
            mltArray = new ArrayList<>(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void put(T model) {
        add(0, model);
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

    @NonNull
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

    //<editor-fold desc="列表查询">
    @Override
    public void remove(Where<T> where) {
        for (int i = 0; i < mltArray.size(); i++) {
            if (where.where(mltArray.get(i))) {
                mltArray.remove(i--);
            }
        }
        notifyDataSetChanged();
    }
    //</editor-fold>

    //<editor-fold desc="适配实现">

    public boolean isDataSync() {
        return mDataSync;
    }

    @NonNull
    @Override
    public List<T> getList() {
        if (mDataSync) {
            return mltArray;
        } else {
            return new ArrayList<>(mltArray);
        }
    }

    @NonNull
    @Override
    public ViewHolderItem<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            ItemViewer<T> item = newItemViewer(viewType);
            View view = inflateItem(item, parent);
            return new ViewHolderItem<>(item, view);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfListAdapter.onCreateViewHolder");
            return new ViewHolderItem<>(new AfItemViewer<T>() {
                @Override
                public void onBinding(T model, int index) {
                }
            }, new View(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem<T> holder, int position) {
        try {
            bindingItem(holder.itemView, holder.getItem(), position);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfListAdapter.onBindViewHolder.bindingItem");
        }
    }

    @Override
    @Deprecated
    public final Object getItem(int i) {
        return get(i);
    }

    @Override
    public int getItemCount() {
        return mltArray.size();
    }

    @NonNull
    @Override
    public View inflateItem(ItemViewer<T> item, ViewGroup parent) {
        return item.onCreateView(parent.getContext(), parent);
    }

    @Override
    public void bindingItem(View view, ItemViewer<T> item, int index) {
        item.onBinding(view, get(index), index);
    }

    //</editor-fold>

}
