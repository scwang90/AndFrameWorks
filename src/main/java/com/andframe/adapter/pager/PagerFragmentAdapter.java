package com.andframe.adapter.pager;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.$;
import com.andframe.R;

/**
 * ViewPagerAdapter
 * Created by Administrator on 2016/2/24 0024.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PagerFragmentAdapter extends FragmentPagerAdapter {

    protected final Fragment[] fragments;
    protected final Class<? extends Fragment>[] classes;

    @SafeVarargs
    public PagerFragmentAdapter(FragmentManager manager, Class<? extends Fragment>... fragments) {
        super(manager);
        this.classes = fragments;
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
            $.error().handle(e, "AfPagerFragmentAdapter.getItem");
        }
        return new EmptyFragment();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        try {
            return super.instantiateItem(container, position);
        } catch (Exception e) {
            $.error().handle(e, "AfPagerFragmentAdapter.instantiateItem");
        }
        return new EmptyFragment();
    }

    protected Fragment newFragment(int position) throws InstantiationException, IllegalAccessException {
        return classes[position].newInstance();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
//        fragments[position] = null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return classes[position].getSimpleName();
    }

    public static class EmptyFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.af_status_empty, container, false);
        }
    }
}