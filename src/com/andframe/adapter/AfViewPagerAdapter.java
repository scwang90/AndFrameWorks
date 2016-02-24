package com.andframe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ViewPagerAdapter
 * Created by Administrator on 2016/2/24 0024.
 */
public class AfViewPagerAdapter extends FragmentPagerAdapter {

    private final Class<? extends Fragment>[] fragments;

    public AfViewPagerAdapter(FragmentActivity activity, Class<? extends Fragment>... fragments) {
        super(activity.getSupportFragmentManager());
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return fragments[position].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position].getSimpleName();
    }
}
