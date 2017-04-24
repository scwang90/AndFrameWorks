package com.andframe.fragment;

import android.support.v4.app.Fragment;

import com.andframe.feature.AfBundle;
import com.andframe.impl.viewer.AfView;

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

    /**
     * 自定义 View onCreate(Bundle)
     */
    protected void onCreate(AfBundle bundle, AfView view) throws Exception {

    }

    @Override
    protected final void onCreate(AfView rootView, AfBundle bundle) throws Exception {
        mIsCreateView = true;
        onCreate(bundle, rootView);
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
        } else {
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
