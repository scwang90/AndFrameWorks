package com.andframe.fragment;

import android.support.v4.app.Fragment;

import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;

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
    protected void onCreated(AfBundle bundle, AfView view) throws Exception {

    }

    @Override
    protected final void onCreated(AfView rootView, AfBundle bundle) throws Exception {
        mIsCreateView = true;
        onCreated(bundle, rootView);
        if (mIsNeedSwitch) {
            mIsNeedSwitch = false;
//            AfApp.get().setCurFragment(this, this);
            if (mSwitchCount == 0) {
                this.onFirstSwitchOver();
            }
            this.onSwitchOver(mSwitchCount++);
//            this.onQueryChanged();
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
//                AfApp.get().setCurFragment(this, this);
                this.onSwitchOver(mSwitchCount++);
//                this.onQueryChanged();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (AfApp.get().getCurFragment() == this) {
//            AfApp.get().setCurFragment(this, null);
//        }
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

//    /**
//     * 查询系统数据变动
//     */
//    public void onQueryChanged() {
//        super.onQueryChanged();
//    }


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
