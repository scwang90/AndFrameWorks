package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;

import com.andframe.activity.AfMultiItemsActivity;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApPager;
import com.andpack.impl.ApListHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiItemsActivity<T> extends AfMultiItemsActivity<T> implements ApPager {

    protected ApListHelper mApHelper = new ApListHelper(this);

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        mApHelper.onCreate();
        super.onCreate(bundle, intent);
    }

    @Override
    public void onViewCreated() throws Exception {
        mApHelper.onViewCreated(null);
        super.onViewCreated();
    }

    @Override
    public RefreshLayouter createRefreshLayouter(Context context) {
        RefreshLayouter layouter = mApHelper.createRefreshLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.createRefreshLayouter(context);
    }

}
