package com.andpack.fragment;

import android.content.Context;

import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiItemsFragment;
import com.andpack.api.ApPager;
import com.andpack.impl.ApListHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiItemsFragment<T> extends AfMultiItemsFragment<T> implements ApPager {

    protected ApListHelper mHelper = new ApListHelper(this);

    @Override
    protected void onCreated(AfBundle bundle, AfView view) throws Exception {
        mHelper.onCreate();
        super.onCreated(bundle, view);
    }

    @Override
    public void onViewCreated() throws Exception {
        mHelper.onViewCreated(null);
        super.onViewCreated();
    }

    @Override
    public RefreshLayouter createRefreshLayouter(Context context) {
        RefreshLayouter layouter = mHelper.createRefreshLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.createRefreshLayouter(context);
    }

}
