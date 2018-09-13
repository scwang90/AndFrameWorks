package com.andframe.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPagerAdapter
 * Created by Administrator on 2016/2/24 0024.
 */
@SuppressWarnings("unused")
public class AfPagerFragmentAdapter extends FragmentStatePagerAdapter {

    protected final Class<? extends Fragment>[] clazzs;
    protected final Fragment[] fragments;

    @SafeVarargs
    public AfPagerFragmentAdapter(FragmentManager manager, Class<? extends Fragment>... fragments) {
        super(manager);
        this.clazzs = fragments;
        this.fragments = new Fragment[fragments.length];
    }

    @Override
    public Fragment getItem(int position) {
        try {
            if (fragments[position] == null) {
                fragments[position] = newFragment(position);
            }
            return fragments[position];
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Fragment newFragment(int position) throws InstantiationException, IllegalAccessException {
        return clazzs[position].newInstance();
    }

    @Deprecated
    @SuppressWarnings("deprecation")
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
//        fragments[position] = null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
//        fragments[position] = null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return clazzs[position].getSimpleName();
    }
}