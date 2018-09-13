package com.andframe.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * AfPagerFragmentTabAdapter
 * Created by Administrator on 2016/2/24 0024.
 */
@SuppressWarnings("unused")
public class AfPagerFragmentTabAdapter extends FragmentStatePagerAdapter {

    public interface TabItem {
        String name();
        Class<? extends Fragment> fragment();
    }

    protected final TabItem[] items;
    protected final Fragment[] fragments;

    public AfPagerFragmentTabAdapter(FragmentManager manager, TabItem... items) {
        super(manager);
        this.items = items;
        this.fragments = new Fragment[items.length];
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
        return items[position].fragment().newInstance();
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
        return items[position].name();
    }
}