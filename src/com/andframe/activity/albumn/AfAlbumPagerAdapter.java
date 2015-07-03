package com.andframe.activity.albumn;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnTouchListener;

import com.andframe.model.Photo;
import com.andframe.network.AfImageService;
import com.andframe.view.AfAlbumView;

/**
 * AfAlbumActivity 专用适配器
 * @author 树朾 相册的适配器
 */
public class AfAlbumPagerAdapter extends PagerAdapter {
	
	protected List<Photo> mltData = null;
	protected OnTouchListener mTouchListener;
	protected HashMap<String, AlbumPagerItem> mHashMap = null;
	protected ViewPager mViewPager;

	public AfAlbumPagerAdapter(Context context, List<Photo> ltData) {
		// TODO Auto-generated constructor stub
		mltData = ltData;
		mHashMap = new HashMap<String, AlbumPagerItem>();
	}

	public void setOnTouchListener(OnTouchListener listener) {
		// TODO Auto-generated method stub
		mTouchListener = listener;
	}

	public Photo getItemAt(int index){
		return mltData.get(index);
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 * @param ltNews
	 */
	public void AddData(List<Photo> ltData) {
		// TODO Auto-generated method stub
		mltData.addAll(ltData);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 数据刷新 接口
	 * @param ltNews
	 */
	public void setData(List<Photo> ltData) {
		// TODO Auto-generated method stub
		mltData = ltData;
		notifyDataSetChanged();
	}

	// 这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated constructor stub
		View view = mHashMap.get(String.valueOf(position)).mTvImage;
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
		AfAlbumView view = null;
		String key = String.valueOf(position);
		if (mHashMap.containsKey(key)) {
			view = mHashMap.get(key).mTvImage;
		} else {
			Photo photo = mltData.get(position);
			view = new AfAlbumView(container.getContext());
			view.setViewPager(mViewPager);
			AlbumPagerItem tItem = new AlbumPagerItem(view,photo);
			tItem.setOnTouchListener(mTouchListener);
			mHashMap.put(key, tItem);
			((ViewPager) container).addView(view);
		}
		return view;
	}
	
	

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

	private class AlbumPagerItem {
		
		public AfAlbumView mTvImage = null;

		public AlbumPagerItem(AfAlbumView view, Photo photo) {
			Handle(view);
			Binding(photo);
		}

		public void setOnTouchListener(OnTouchListener listener) {
			// TODO Auto-generated method stub
			mTouchListener = listener;
			if (mTouchListener != null) {
				mTvImage.setOnTouchListener(mTouchListener);
			}
		}

		@SuppressLint("ClickableViewAccessibility")
		private void Handle(AfAlbumView view) {
			mTvImage = view;
			if (mTouchListener != null) {
				mTvImage.setOnTouchListener(mTouchListener);
			}
		}

		/**
		 * 将数据绑定到控件显示
		 * @param review
		 */
		public void Binding(Photo photo) {
			AfImageService.bindImage(photo.Url, mTvImage, photo.Remark);
		}
	}

	public void setViewPager(ViewPager pager) {
		// TODO Auto-generated method stub
		mViewPager = pager;
	}

}
