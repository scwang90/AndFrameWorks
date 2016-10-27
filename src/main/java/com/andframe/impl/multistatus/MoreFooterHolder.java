package com.andframe.impl.multistatus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.ListItem;
import com.andframe.api.multistatus.MoreFooter;

/**
 *
 * Created by SCWANG on 2016/10/27.
 */

public class MoreFooterHolder<T> implements ListItem<T> {

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
}
