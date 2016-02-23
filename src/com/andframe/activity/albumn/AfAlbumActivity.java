package com.andframe.activity.albumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfViewable;
import com.andframe.bean.Page;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.model.Photo;
import com.andframe.thread.AfHandlerTask;
import com.andframe.thread.AfHandlerTimerTask;
import com.andframe.util.UUIDUtil;
import com.andframe.util.java.AfStringUtil;

/**
 * 框架相册浏览页面
 * @author 树朾
 * 支持 	单张照片浏览
 * 	多张浏览
 * 	动态加载相册浏览
 *
 * 继承之后主要实现
 * 	
	 * 	获取相册的总布局
	protected abstract int getAlbumLayoutId();
	 * 获取 显示相册名称 TextView
	protected abstract TextView getTextViewName(AfViewable view);
	 * 获取 显示相册总量和当前量 TextView
	protected abstract TextView getTextViewSize(AfViewable view);
	 * 获取 显示相册照片详细信息 TextView
	protected abstract TextView getTextViewDetail(AfViewable view);
	 * 获取相册滑动 ViewPager
	protected abstract ViewPager getViewPager(AfViewable view);
	
	
	 * 重写 这个函数 可以实现加载网络相册
	public List<Photo> onRequestAlbum(UUID albumID, Page page)
 */
public abstract class AfAlbumActivity extends AfActivity 
		implements OnPageChangeListener, OnTouchListener {

	// 通用信息
	public static final String EXTRA_STRING_NAME = "EXTRA_NAME";
	public static final String EXTRA_STRING_DESCRIBE = "EXTRA_DESCRIBE";
	
	// 发送一个已存在的相册列表（不会再网络加载列表）
	public static final String EXTRA_PHOTO_LIST = "EXTRA_LIST"; // 列表
	public static final String EXTRA_INT_INDEX = "EXTRA_INDEX";// 默认查看
	// 发送一个相册的封面（网络加载相册内容）
	public static final String EXTRA_UUID_ALBUMID = "EXTRA_ALBUMID";
	public static final String EXTRA_HEADURL = "EXTRA_HEADURL";
	// 发送一张单独的图片（网络加载高清图）
	// 图片URL（用于加载高清图）
	public static final String EXTRA_PHOTO_HEAD = "EXTRA_SINGLE_URL";

	public TextView mTvDetail = null;
	public TextView mTvName = null;
	public TextView mTvSize = null;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;

	protected UUID AlbumID = null;
	protected String mPhotoName = "相册名称";
	protected String mDescribe = "相册的基本描述信息和相关资料";
	protected String mHeadUrl = "";

	protected Photo mHeader = null;
	protected List<Photo> mltPhoto = new ArrayList<Photo>();

	protected AfAlbumPagerAdapter mAdapter = null;

	/**
	 * 以下是 相册必备的 View
	 * 	获取相册的总布局
	 */
	protected int getAlbumLayoutId() {
		return 0;
	}
	/**
	 * 获取 显示相册名称 TextView
	 */
	protected abstract TextView getTextViewName(AfViewable view);
	/**
	 * 获取 显示相册总量和当前量 TextView
	 */
	protected abstract TextView getTextViewSize(AfViewable view);
	/**
	 * 获取 显示相册照片详细信息 TextView
	 */
	protected abstract TextView getTextViewDetail(AfViewable view);
	/**
	 * 获取相册滑动 ViewPager
	 */
	protected abstract ViewPager getViewPager(AfViewable view);

	@Override
	protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);

		if (mRoot == null) {
			setContentView(getAlbumLayoutId());
		}

		mViewPager = getViewPager(this);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(this);

		mTvSize = getTextViewSize(this);
		mTvDetail = getTextViewDetail(this);
		mTvName = getTextViewName(this);

		int index = 0;
		mHeader = intent.get(EXTRA_PHOTO_HEAD, Photo.class);
		AlbumID = intent.get(EXTRA_UUID_ALBUMID, UUIDUtil.Empty,UUID.class);
		mDescribe = intent.getString(EXTRA_STRING_DESCRIBE, "");
		mPhotoName = intent.getString(EXTRA_STRING_NAME, "");
		mHeadUrl = intent.getString(EXTRA_HEADURL, "");
		List<Photo> list = intent.getList(EXTRA_PHOTO_LIST, Photo.class);
		//单张相册或者封面
		if(mHeader != null){
			mltPhoto.add(mHeader);
			if(mDescribe.equals("")){
				mDescribe = mHeader.Describe;
			}
			if(mPhotoName.equals("")){
				mPhotoName = mHeader.Name;
			}
		} else if (!AfStringUtil.isEmpty(mPhotoName)
				&& !AfStringUtil.isEmpty(mDescribe)
				&& !AfStringUtil.isEmpty(mHeadUrl)
				&& !UUIDUtil.Empty.equals(AlbumID)) {
			mHeader = new Photo(mPhotoName, mHeadUrl, mDescribe);
			mltPhoto.add(mHeader);
		}
		//预加载相册内容
		if(list != null && list.size() > 0){
			index = intent.getInt(EXTRA_INT_INDEX, 0);
			index = index >= list.size()?0:(index+mltPhoto.size());
			mltPhoto.addAll(list);
		}
		if(mltPhoto.size() == 0 && UUIDUtil.Empty.equals(AlbumID)){
			throw new AfToastException("相册列表为空!");
		}
		if(!AlbumID.equals(UUIDUtil.Empty)){
			postTask(new LoadAlbumTask());
		}
		
		onPageSelected(0);//用第零个元素初始化界面
		mAdapter = new AfAlbumPagerAdapter(this, mltPhoto);
		mAdapter.setOnTouchListener(this);
		mViewPager.setAdapter(mAdapter);
		mAdapter.setViewPager(mViewPager);
		mViewPager.setCurrentItem(index, false);
	}

	/**
	 * onTouchEvent 实现双击返回 上一次点击的时间
	 */
	private long mLastTouch = 0;
	private float mLastPosX = 0;
	private float mLastPosY = 0;
	private boolean mHandleing= false;

	@Override
	@SuppressLint("HandlerLeak")
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			mLastPosX = event.getRawX();
			mLastPosY = event.getRawY();
			mLastTouch = System.currentTimeMillis();
		} else if (action == MotionEvent.ACTION_MOVE) {
			float PosX = event.getRawX();
			float PosY = event.getRawY();
			float dPosX = PosX - mLastPosX;
			float dPosY = PosY - mLastPosY;
			double distance = Math.sqrt(dPosX*dPosX+dPosY*dPosY);
			if(distance > 20){
				mLastTouch = 0;
			}
		} else if (action == MotionEvent.ACTION_UP) {
			long current = System.currentTimeMillis();
			long dvalue = current - mLastTouch;
			if (dvalue < 200) {
				if(!mHandleing){
					mHandleing = true;
					Timer timer = new Timer();
					timer.schedule(new AfHandlerTimerTask() {
						long last = mLastTouch;
						@Override
						protected boolean onHandleTimer(Message msg) {
							mHandleing = false;
							if(last == mLastTouch){                                                                                                                                                                                                                           
								int index = mViewPager.getCurrentItem();
								onPageClick(index,mAdapter.getItemAt(index));
							}
							return true;
						}
					}, 300);
				}
			}
		}
		return false;
	}

	protected void onPageClick(int index, Photo photo) {
		this.finish();
	}

	private class LoadAlbumTask extends AfHandlerTask{
		
		public List<Photo> mphotos = null;

		@Override
		protected void onWorking(Message tMessage) throws Exception {
			mphotos = onRequestAlbum(AlbumID, new Page(100, mltPhoto.size()));
		}

		@Override
		protected boolean onHandle(Message msg) {
			if(mResult == RESULT_FINISH){
				mAdapter.AddData(mphotos);
				onPageSelected(mViewPager.getCurrentItem());
			}else{
				makeToastShort(makeErrorToast("相册加载失败"));
			}
			return true;
		}

	}

	/**
	 * 重写 这个函数 可以实现加载网络相册
	 * @param albumID	相册ID
	 * @param page 分页对象
	 * @return
	 */
	public List<Photo> onRequestAlbum(UUID albumID, Page page) throws Exception{
		return new ArrayList<Photo>();
	}

	// arg0==1的时候表示正在滑动，
	// arg0==2的时候表示滑动完毕了，
	// arg0==0的时候表示什么都没做，就是停在那。
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	
	// 默示在前一个页面滑动到后一个页面的时辰，在前一个页面滑动前调用的办法。
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// currPage是默示你当前选中的页面，这事务是在你页面跳转完毕的时辰调用的。
	@Override
	public void onPageSelected(int currPage) {
		if (mltPhoto.size() > currPage) {
			Photo photo = mltPhoto.get(currPage);
			if (photo.Describe != null && photo.Describe.length() > 0) {
				mTvDetail.setText(photo.Describe);
			}else{
				mTvDetail.setText(mDescribe);
			}
			if (photo.Name != null && photo.Name.length() > 0) {
				mTvName.setText(photo.Name);
			}else{
				mTvName.setText(mPhotoName);
			}
			mTvSize.setText((1 + currPage) + "/" + mltPhoto.size());
		}
	}

}
