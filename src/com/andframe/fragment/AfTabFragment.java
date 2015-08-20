package com.andframe.fragment;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfApplication;
import com.andframe.feature.AfBundle;

public abstract class AfTabFragment extends AfFragment{

	// 切换到Fragment页面 的次数统计
	private int mSwitchCount = 0;
	// 标识是否创建视图
	private Boolean mIsCreateView = false;
	// 标识创建视图的时候是否需要Switch
	private Boolean mIsNeedSwitch = false;

	/**
	 * 自定义 View onCreate(Bundle)
	 */
	protected void onCreated(AfBundle bundle,AfView view)throws Exception{
		
	}

	@Override
	protected final void onCreated(AfView rootView, AfBundle bundle)throws Exception {
		mIsCreateView = true;
		onCreated(bundle,rootView);
		if (mIsNeedSwitch == true) {
			mIsNeedSwitch = false;
			AfApplication.getApp().setCurFragment(this, this);
			if (mSwitchCount == 0) {
				this.onFirstSwitchOver();
			}
			this.onSwitchOver(mSwitchCount++);
			this.onQueryChanged();
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
				AfApplication.getApp().setCurFragment(this, this);
				this.onSwitchOver(mSwitchCount++);
				this.onQueryChanged();
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

	}

	/**
	 * 每次切换到本页面
	 * @param count
	 *            切换序号
	 */
	protected void onSwitchOver(int count) {

	}

	/**
	 * 离开本页面
	 */
	protected void onSwitchLeave() {

	}

	/**
	 * 查询系统数据变动
	 */
	public void onQueryChanged() {
		
	}
}
