package com.andframe.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
/**
 * ViewPager 框架主页面
 * @author SCWANG
 *	如果App 主页是一个 ViewPager 的话，可以继承 
 *		AfMainViewPagerActivity
 */
public abstract class AfMainViewPagerActivity extends AfMainActivity
	implements OnPageChangeListener{

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	protected PagerAdapter mPagerAdapter;

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
