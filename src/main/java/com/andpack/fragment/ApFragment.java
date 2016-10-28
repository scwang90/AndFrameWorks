package com.andpack.fragment;

import android.support.v4.app.Fragment;

import com.andframe.annotation.view.BindViewCreated;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfTabFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public class ApFragment extends AfTabFragment implements ApPager {

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    @Override
    protected void onCreate(AfBundle bundle, AfView view) throws Exception {
        mHelper.onCreate();
        super.onCreate(bundle, view);
    }

    @BindViewCreated
    protected void onViewCreated() throws Exception {
        mHelper.onViewCreated();
    }


    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(this, clazz, request, args);
    }
}
