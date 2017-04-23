package com.andframe.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * AfViewPagerFragmentAdapter
 * Created by Administrator on 2016/2/24 0024.
 */
@SuppressWarnings("unused")
public abstract class AfViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

    protected final Fragment[] fragments;

    public AfViewPagerFragmentAdapter(FragmentManager manager, int len) {
        super(manager);
        this.fragments = new Fragment[len];
    }

    @Override
    public Fragment getItem(int position) {
        try {
            if (fragments[position] == null) {
                fragments[position] = newFragment(position);
            }
            return fragments[position];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract Fragment newFragment(int position) throws Exception ;

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
        return String.format(Locale.CHINA, "Fragment-%d", position);
    }
}