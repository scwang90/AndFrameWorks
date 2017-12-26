package com.andframe.adapter.pager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.query.hindler.Where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AfPagerViewAdapter<T> extends PagerAdapter {

    //<editor-fold desc="PagerAdapter">
    protected static final int KEY_VIEW_TAG = "AfPagerViewAdapter".hashCode();
    protected static final int KEY_VIEW_INDEX = "AfPagerViewAdapter.INDEX".hashCode();

    protected List<T> list;
    protected View[] views = new View[3];

    public AfPagerViewAdapter(Collection<T> collection) {
        this.list = new ArrayList<>(collection);
    }

    public void refresh(Collection<T> collection) {
        list = new ArrayList<>(collection);
        notifyDataSetChanged();
        for (View view : views) {
            if (view != null) {
                //noinspection unchecked
                ItemViewer<T> viewer = (ItemViewer<T>) view.getTag(KEY_VIEW_TAG);
                Integer inedx = (Integer)view.getTag(KEY_VIEW_INDEX);
                if (inedx < list.size()) {
                    viewer.onBinding(view, list.get(inedx), inedx);
                }
            }
        }
    }

    public void set(int index, T t) {
        list.set(index, t);
        notifyDataSetChanged();
        View view = views[index % views.length];
        if (view != null) {
            //noinspection unchecked
            ItemViewer<T> viewer = (ItemViewer<T>) view.getTag(KEY_VIEW_TAG);
            Integer viewinedx = (Integer)view.getTag(KEY_VIEW_INDEX);
            if (viewinedx == index) {
                viewer.onBinding(view, t, index);
            }
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views[position % views.length];
        ItemViewer<T> viewer;
        if (view == null) {
            viewer = newItemViewer();
            view = views[position % views.length] = viewer.onCreateView(container.getContext(), container);
            view.setTag(KEY_VIEW_TAG, viewer);
            container.addView(view);
        } else {
            //noinspection unchecked
            viewer = (ItemViewer<T>) view.getTag(KEY_VIEW_TAG);
        }
        view.setTag(KEY_VIEW_INDEX, position);
        viewer.onBinding(view, list.get(position), position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public abstract ItemViewer<T> newItemViewer();

    public void remove(Where<T> where) {
        for (int i = 0; i < list.size(); i++) {
            if (where.where(list.get(i))) {
                list.remove(i--);
            }
        }
        notifyDataSetChanged();
    }
    //</editor-fold>

}