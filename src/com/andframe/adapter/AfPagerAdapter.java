package com.andframe.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnTouchListener;

import com.andframe.application.AfExceptionHandler;

/**
 * AfAlbumActivity 专用适配器
 * @author SCWANG 相册的适配器
 */
public abstract class AfPagerAdapter<T> extends PagerAdapter {
	
	protected List<T> mltData = null;
	protected OnTouchListener mTouchListener;
	protected HashMap<String, AfPagerItem<T>> mHashMap = null;
	protected ViewPager mViewPager;

	public AfPagerAdapter(Context context, List<T> ltData) {
		// TODO Auto-generated constructor stub
		mltData = ltData;
		mHashMap = new HashMap<String, AfPagerItem<T>>();
	}

	public void setOnTouchListener(OnTouchListener listener) {
		// TODO Auto-generated method stub
		mTouchListener = listener;
	}


	public T getItemAt(int index) {
		// TODO Auto-generated method stub
		return mltData.get(index);
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 * 
	 * @param ltNews
	 */
	public void AddData(List<T> ltData) {
		// TODO Auto-generated method stub
		mltData.addAll(ltData);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 数据刷新 接口
	 * 
	 * @param ltNews
	 */
	public void setData(List<T> ltData) {
		// TODO Auto-generated method stub
		mltData = ltData;
		notifyDataSetChanged();
	}

	// 这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated constructor stub
		View view = mHashMap.get(String.valueOf(position)).getLayout();
		((ViewPager) container).removeView(view);
		mHashMap.remove(String.valueOf(position));
	}

	@Override
	public void finishUpdate(View view) {
		// TODO Auto-generated constructor stub

	}

	// 这里返回相册有多少条,和BaseAdapter一样.
	@Override
	public int getCount() {
		// TODO Auto-generated constructor stub
		return mltData.size();
	}

	// 这里就是初始化ViewPagerItemView.如果ViewPagerItemView已经存在,
	// 重新reload，不存在new一个并且填充数据.
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated constructor stub
		View view = null;
		String key = String.valueOf(position);
		if (mHashMap.containsKey(key)) {
			view = mHashMap.get(key).getLayout();
		} else {
			try {
				T model = mltData.get(position);
				AfPagerItem<T> item = onNewPagerItem(model);
				view = item.onHandler(mViewPager,mTouchListener);
				item.onBinding(model,position);
				mHashMap.put(key, item);
				((ViewPager) container).addView(item.mLayout = view);
			} catch (Exception e) {
				// TODO: handle exception
				AfExceptionHandler.handleAttach(e, "AfPagerAdapter.instantiateItem Exception\r\nclass:"+getClass().getName());
			}
		}
		return view;
	}

	protected abstract AfPagerItem<T> onNewPagerItem(T photo);

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {

	}

	public static abstract class AfPagerItem<IT> {
		
		public View mLayout = null;

		/**
		 * 将数据绑定到控件显示
		 * @param review
		 */
		public abstract void onBinding(IT model, int position);
		public abstract View onHandler(ViewPager pager, OnTouchListener listener);

		public View getLayout() {
			// TODO Auto-generated method stub
			return mLayout;
		}

	}

	public void setViewPager(ViewPager pager) {
		// TODO Auto-generated method stub
		mViewPager = pager;
	}

}
