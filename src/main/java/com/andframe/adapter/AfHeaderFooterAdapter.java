package com.andframe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.itemviewer.AfItemViewer;
import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.adapter.HeaderFooterAdapter;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemViewerAdapter;
import com.andframe.api.view.Viewer;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.impl.wrapper.ListItemAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 可直接更新状态(不刷新列表)的适配器
 * Created by SCWANG on 2016/8/5.
 */
@SuppressWarnings("unused")
public class AfHeaderFooterAdapter<T> extends ListItemAdapterWrapper<T> implements HeaderFooterAdapter<T> {

    protected List<ItemViewer<T>> mHeaders = new ArrayList<>();
    protected List<ItemViewer<T>> mFooters = new ArrayList<>();

    public AfHeaderFooterAdapter(@NonNull ItemViewerAdapter<T> wrapped) {
        super(wrapped);
    }

    //<editor-fold desc="逻辑连接">
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            ViewHolderItem<T> holder = null;
            if (view != null) {
                //noinspection unchecked
                holder = (ViewHolderItem<T>) view.getTag(KEY_VIEW_TAG);
            }
            if (holder == null) {
                holder = createViewHolder(viewGroup, getItemViewType(i));
                holder.itemView.setTag(KEY_VIEW_TAG, holder);
            }
            bindViewHolder(holder, i);
            return holder.itemView;
        } catch (Exception e) {
            AfExceptionHandler.handle(e, "AfHeaderFooterAdapter.getView");
            return new View(viewGroup.getContext());
        }
    }
    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position, List<Object> payloads) {
        try {
            onBindViewHolder(holder, position);
        } catch (Exception e) {
            AfExceptionHandler.handle(e, "AfHeaderFooterAdapter.onBindViewHolder");
        }
    }
    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position) {
        bindingItem(holder.itemView, holder.getItem(), position);
    }

    @Override
    public ViewHolderItem<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            ItemViewer<T> item = newItemViewer(viewType);
            View view = inflateItem(item, parent);
            if (view == null) {
                view = new View(parent.getContext());
                AfExceptionHandler.handle("inflateItem 返回 null", "AfListAdapter.onCreateViewHolder.inflateItem");
            }
            return new ViewHolderItem<>(item, view);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfListAdapter.onCreateViewHolder");
            return new ViewHolderItem<>(null, new View(parent.getContext()));
        }
    }
    //</editor-fold>

    //<editor-fold desc="功能实现">


    @Override
    public T get(int position) {
        if (position < mHeaders.size()) {
            return null;
        } else if (position - mHeaders.size() - super.getItemCount() >= 0){
            return null;
        }
        return super.get(position - mHeaders.size());
    }

    @Override
    public Object getItem(int i) {
        return get(i);
    }

    @Override
    public int getCount() {
        return this.getItemCount();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mHeaders.size() + mFooters.size();
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + mHeaders.size() + mFooters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaders.size()) {
            return super.getViewTypeCount() + position;
        } else if (position - mHeaders.size() - super.getItemCount() >= 0){
            return super.getViewTypeCount() + mHeaders.size() + (position - mHeaders.size() - super.getItemCount());
        }
        return super.getItemViewType(position - mHeaders.size());
    }

    @NonNull
    @Override
    public ItemViewer<T> newItemViewer(int viewType) {
        int type = viewType - super.getViewTypeCount();
        if (type >= 0) {
            if (type < mHeaders.size()) {
                return mHeaders.get(type);
            }
            type -= mHeaders.size();
            if (type >= 0 && type < mFooters.size()) {
                return mFooters.get(type);
            }
        }
        return super.newItemViewer(viewType);
    }

    public void bindingItem(View view, ItemViewer<T> item, int index) {
        if (index < mHeaders.size()) {
            item.onBinding(view, get(index), index);
        } else if (index - mHeaders.size() - super.getItemCount() >= 0) {
            item.onBinding(view, get(index), index);
        } else {
            super.bindingItem(view, item, index - mHeaders.size());
        }
    }

    //</editor-fold>

    //<editor-fold desc="功能方法">

    public boolean addHeader(ItemViewer<T> item) {
        if (item != null && !mHeaders.contains(item)) {
            mHeaders.add(item);
            notifyDataSetChanged();
        }
        return item != null;
    }

    public boolean addHeaderLayout(int layoutId) {
        return addHeader(new AfItemViewer<T>(layoutId) {
            @Override
            public void onBinding(T model, int index) {
            }
        });
    }

    public boolean addHeaderView(View view) {
        return addHeader(new AfItemViewer<T>() {
            View mView = view;
            @Override
            public void onBinding(T model, int index) {
            }
            @Override
            public View onCreateView(Context context, ViewGroup parent) {
                return mView;
            }
        });
    }

    public boolean addFooter(ItemViewer<T> item) {
        if (item != null && !mFooters.contains(item)) {
            mFooters.add(item);
            notifyDataSetChanged();
        }
        return item != null;
    }

    public boolean addFooterLayout(int layoutId) {
        return addFooter(new AfItemViewer<T>(layoutId) {
            @Override
            public void onBinding(T model, int index) {
            }
        });
    }

    public boolean addFooterView(View view) {
        return addFooter(new AfItemViewer<T>() {
            {mLayout = view;}
            @Override
            public void onBinding(T model, int index) {
            }
            @Override
            public View onCreateView(Context context, ViewGroup parent) {
                return mLayout;
            }
        });
    }

    public boolean removeHeader(ItemViewer<T> item) {
        if (mHeaders.remove(item)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean removeFooter(ItemViewer<T> item) {
        if (mFooters.remove(item)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean removeHeaderView(View view) {
        for (int i = 0; i < mHeaders.size(); i++) {
            ItemViewer<T> item = mHeaders.get(i);
            if (item instanceof Viewer && ((Viewer) item).getView() == view) {
                mHeaders.remove(i);
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public boolean removeFooterView(View view) {
        for (int i = 0; i < mFooters.size(); i++) {
            ItemViewer<T> item = mFooters.get(i);
            if (item instanceof Viewer && ((Viewer) item).getView() == view) {
                mFooters.remove(i);
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public void clearHeader() {
        mHeaders.clear();
    }

    public void clearFooter() {
        mFooters.clear();
    }

    public boolean hasHeaderView(View view) {
        for (int i = 0; i < mHeaders.size(); i++) {
            ItemViewer<T> item = mHeaders.get(i);
            if (item instanceof Viewer && ((Viewer) item).getView() == view) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFooterView(View view) {
        for (int i = 0; i < mFooters.size(); i++) {
            ItemViewer<T> item = mFooters.get(i);
            if (item instanceof Viewer && ((Viewer) item).getView() == view) {
                return true;
            }
        }
        return false;
    }

    //</editor-fold>

    //<editor-fold desc="子类重写">

    //</editor-fold>

}
