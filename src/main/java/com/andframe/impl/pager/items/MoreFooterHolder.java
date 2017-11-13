package com.andframe.impl.pager.items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.pager.items.MoreFooter;
import com.andframe.api.viewer.Viewer;

/**
 *
 * Created by SCWANG on 2016/10/27.
 */

public class MoreFooterHolder<T> implements ItemViewer<T>, Viewer {

    MoreFooter footer;

    public MoreFooterHolder(MoreFooter footer) {
        this.footer = footer;
    }

    @Override
    public void onBinding(View view, T model, int index) {
        footer.onUpdateStatus(view, index);
    }

    @Override
    public View onCreateView(Context context, ViewGroup parent) {
        return footer.onCreateView(context, parent);
    }

    @Override
    public Context getContext() {
        return footer.getView().getContext();
    }

    @Override
    public View getView() {
        return footer.getView();
    }

    @Override
    public <TT extends View> TT  findViewById(int id) {
        return footer.getView().findViewById(id);
    }

    @Override
    public <TT extends View> TT findViewById(int id, Class<TT> clazz) {
        View view = findViewById(id);
        if (clazz.isInstance(view)) {
            return clazz.cast(view);
        }
        return null;
    }
}
