package com.andframe.adapter.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.andframe.$;

/**
 * AfPagerFragmentTabAdapter
 * Created by Administrator on 2016/2/24 0024.
 */
@SuppressWarnings("unused")
public class PagerFragmentTabAdapter extends PagerFragmentAdapter {

    public interface TabItem {
        String name();
        Class<? extends Fragment> fragment();
    }

    protected final TabItem[] items;

    @SuppressWarnings("unchecked")
    public PagerFragmentTabAdapter(FragmentManager manager, TabItem... items) {
        super(manager, $.query(items).map(TabItem::fragment).toArrays());
        this.items = items;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items[position].name();
    }
}