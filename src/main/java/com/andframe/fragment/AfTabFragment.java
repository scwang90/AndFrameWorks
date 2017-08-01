package com.andframe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 可切换的Fragment
 */
public abstract class AfTabFragment extends AfFragment {

    // 切换到Fragment页面 的次数统计
    private int mSwitchCount = 0;
    // 标识是否创建视图
    private Boolean mIsCreateView = false;
    // 标识创建视图的时候是否需要Switch
    private Boolean mIsNeedSwitch = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsCreateView = true;
        if (mIsNeedSwitch) {
            mIsNeedSwitch = false;
            if (mSwitchCount == 0) {
                this.onFirstSwitchOver();
            }
            this.onSwitchOver(mSwitchCount++);
        }
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        boolean userVisibleHint = getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsCreateView) {
                if (mSwitchCount == 0) {
                    this.onFirstSwitchOver();
                }
                this.onSwitchOver(mSwitchCount++);
            } else {
                mIsNeedSwitch = true;
            }
        } else if (mSwitchCount > 0 && userVisibleHint) {
            this.onSwitchLeave();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsCreateView = false;
        mIsNeedSwitch = false;
    }

    /**
     * 第一次切换到本页面
     */
    protected void onFirstSwitchOver() {
        super.onFirstSwitchOver();
    }

    /**
     * 每次切换到本页面
     *
     * @param count 切换序号
     */
    protected void onSwitchOver(int count) {
        super.onSwitchOver(count);
    }

    /**
     * 离开本页面
     */
    protected void onSwitchLeave() {
        super.onSwitchLeave();
    }

    @Override
    public boolean onBackPressed() {
        boolean isHandled = false;
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        fragments = fragments == null ? new ArrayList<>() : fragments;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof AfFragment) {
                AfFragment afment = (AfFragment) fragment;
                isHandled = afment.onBackPressed() || isHandled;
            }
        }
        return isHandled || super.onBackPressed();
    }

}
