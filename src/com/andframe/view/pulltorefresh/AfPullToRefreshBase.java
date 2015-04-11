package com.andframe.view.pulltorefresh;

import java.util.Date;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

public abstract class AfPullToRefreshBase<Target extends View> extends
		LinearLayout {
	// ===========================================================
	// Constants
	// ===========================================================

	// 摩擦力
	private static final float FRICTION = 2.0f;

	// 状态
	private static final int PULL_TO_REFRESH = 0x0;
	private static final int RELEASE_TO_REFRESH = 0x1;
	private static final int REFRESHING = 0x2;
	private static final int MANUAL_REFRESHING = 0x3;
	// 模式
	public static final int MODE_NONE = 0x0;
	public static final int MODE_PULL_DOWN = 0x1;
	public static final int MODE_PULL_UP = 0x2;
	public static final int MODE_BOTH = 0x3;

	// ===========================================================
	// Fields
	// ===========================================================

	private int touchSlop;

	private float initMotionY;
	private float lastMotionX;
	private float lastMotionY;
	private boolean isBeingDragged = false;

	private int state = PULL_TO_REFRESH;
	private int mode = MODE_NONE;
	private int curmode = MODE_NONE;

	private boolean isPullToRefreshEnabled = true;
	private boolean disableScrollingWhileRefreshing = true;

	private int headerHeight;
	private int footerHeight;
	protected Target mTargetView;
	protected AfPullHeaderLayout mHeaderLayout;
	protected AfPullFooterLayout mFooterLayout;

	protected OnTouchListener mTouchListener;
	protected OnRefreshListener onRefreshListener;

	protected SmoothRunnable smoothrunnable;

	public AfPullToRefreshBase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.initialized(context, null);
	}

	public AfPullToRefreshBase(Context context, int mode) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mode = mode;
		this.initialized(context, null);
	}

	public AfPullToRefreshBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.initialized(context, attrs);
	}

	public AfPullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.initialized(context, attrs);
	}

	private void initialized(Context context, AttributeSet attrs) {

		setOrientation(LinearLayout.VERTICAL);

		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		// Styleables from XML
		// TypedArray array = context.obtainStyledAttributes(attrs,
		// R.styleable.PullToRefresh);
		// if (array.hasValue(R.styleable.PullToRefresh_mode)) {
		// mode = array.getInteger(R.styleable.PullToRefresh_mode,
		// MODE_PULL_DOWN);
		// }

		// Refreshable View
		// By passing the attrs, we can add ListView/GridView params via XML
		mTargetView = this.onCreateRefreshableView(context, attrs);
		LayoutParams lpTarget = new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f);
		super.addView(mTargetView, lpTarget);

		// Add Loading Views
		// LayoutParams lpHeader = new
		// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		// if (mode == MODE_PULL_DOWN || mode == MODE_BOTH) {
		// mHeaderLayout = new PullHeaderLayout(context,attrs);
		// super.addView(mHeaderLayout, 0, lpHeader);
		// measureView(mHeaderLayout);
		// headerHeight = mHeaderLayout.getMeasuredHeight();
		// }
		// if (mode == MODE_PULL_UP || mode == MODE_BOTH) {
		// mFooterLayout = new PullFooterLayout(context,attrs);
		// super.addView(mFooterLayout,lpHeader);
		// measureView(mFooterLayout);
		// footerHeight = mFooterLayout.getMeasuredHeight();
		// }

		// Styleables from XML
		// if (array.hasValue(R.styleable.PullToRefresh_headerTextColor)) {
		// final int color =
		// array.getColor(R.styleable.PullToRefresh_headerTextColor,
		// Color.BLACK);
		// if (null != mHeaderLayout) {
		// mHeaderLayout.setTextColor(color);
		// }
		// if (null != mFooterLayout) {
		// mFooterLayout.setTextColor(color);
		// }
		// }
		// if (array.hasValue(R.styleable.PullToRefresh_headerBackground)) {
		// this.setBackgroundResource(array.getResourceId(R.styleable.PullToRefresh_headerBackground,
		// Color.WHITE));
		// }
		// if (array.hasValue(R.styleable.PullToRefresh_adapterViewBackground))
		// {
		// mTargetView.setBackgroundResource(array.getResourceId(R.styleable.PullToRefresh_adapterViewBackground,
		// Color.WHITE));
		// }
		// array.recycle();

		// Hide Loading Views
		// switch (mode) {
		// case MODE_BOTH:
		// setPadding(0, -headerHeight, 0, -headerHeight);
		// break;
		// case MODE_PULL_UP:
		// setPadding(0, 0, 0, -headerHeight);
		// break;
		// case MODE_PULL_DOWN:
		// default:
		// setPadding(0, -headerHeight, 0, 0);
		// break;
		// }

		// If we're not using MODE_BOTH, then just set currentMode to current
		// mode
		// if (mode != MODE_BOTH) {
		// curmode = mode;
		// }

	}

	public void setPullHeaderLayout(AfPullHeaderLayout layout) {
		if (mHeaderLayout == null && layout != null) {
			mHeaderLayout = layout;
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			super.addView(mHeaderLayout, 0, lp);
			measureView(mHeaderLayout);
			headerHeight = mHeaderLayout.getMeasuredHeight();
			setPadding(0, -headerHeight, 0, -headerHeight);
			mode = mFooterLayout != null ? MODE_BOTH : MODE_PULL_DOWN;
		}else if(mHeaderLayout != null && layout == null){
			mHeaderLayout = null;
			mode = mFooterLayout != null ? MODE_PULL_UP : MODE_NONE;
		}
	}

	public void setPullFooterLayout(AfPullFooterLayout layout) {
		if (mFooterLayout == null && layout != null) {
			mFooterLayout = layout;
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			super.addView(mFooterLayout, lp);
			measureView(mFooterLayout);
			footerHeight = mFooterLayout.getMeasuredHeight();
			setPadding(0, -headerHeight, 0, -headerHeight);
			mode = mHeaderLayout != null ? MODE_BOTH : MODE_PULL_UP;
		}else if(mFooterLayout != null && layout == null){
			mFooterLayout = null;
			mode = mHeaderLayout != null ? MODE_PULL_DOWN : MODE_NONE;
		}
	}

	private Point measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		}
		int childHeightSpec;
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		if (p.height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(p.height,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		view.measure(childWidthSpec, childHeightSpec);
		return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
	}

	// ===========================================================
	// Function
	// ===========================================================

	/**
	 * 提交刷新完成 更新时间
	 */
	public final void finishRefresh() {
		// TODO Auto-generated method stub
		onRefreshComplete();
		setLastUpdateTime(new Date());
	}

	/**
	 * 提交刷新完成 但是失败 不更新时间
	 */
	public final void finishRefreshFail() {
		// TODO Auto-generated method stub
		onRefreshComplete();
	}

	public final void finishLoadMore() {
		// TODO Auto-generated method stub
		onRefreshComplete();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final void setLastUpdateTime(Date date) {
		// TODO Auto-generated method stub
		if (mHeaderLayout != null) {
			mHeaderLayout.setLastUpdateTime(date);
		}
	}

	/**
	 * Deprecated. Use {@link #getRefreshableView()} from now on.
	 * 
	 * @deprecated
	 * @return The Refreshable View which is currently wrapped
	 */
	public final Target getAdapterView() {
		return mTargetView;
	}

	/**
	 * Get the Wrapped Refreshable View. Anything returned here has already been
	 * added to the content view.
	 * 
	 * @return The View which is currently wrapped
	 */
	public final Target getRefreshableView() {
		return mTargetView;
	}

	/**
	 * Whether Pull-to-Refresh is enabled
	 * 
	 * @return enabled
	 */
	public final boolean isPullToRefreshEnabled() {
		return isPullToRefreshEnabled;
	}

	/**
	 * Returns whether the widget has disabled scrolling on the Refreshable View
	 * while refreshing.
	 * 
	 * @param true if the widget has disabled scrolling while refreshing
	 */
	public final boolean isDisableScrollingWhileRefreshing() {
		return disableScrollingWhileRefreshing;
	}

	/**
	 * Returns whether the Widget is currently in the Refreshing state
	 * 
	 * @return true if the Widget is currently refreshing
	 */
	public final boolean isRefreshing() {
		return state == REFRESHING || state == MANUAL_REFRESHING;
	}

	/**
	 * By default the Widget disabled scrolling on the Refreshable View while
	 * refreshing. This method can change this behaviour.
	 * 
	 * @param disableScrollingWhileRefreshing
	 *            - true if you want to disable scrolling while refreshing
	 */
	public final void setDisableScrollingWhileRefreshing(
			boolean disableScrollingWhileRefreshing) {
		this.disableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
	}

	/**
	 * Mark the current Refresh as complete. Will Reset the UI and hide the
	 * Refreshing View
	 */
	public final void onRefreshComplete() {
		if (state != PULL_TO_REFRESH) {
			resetLayout();
		}
	}

	/**
	 * Set OnRefreshListener for the Widget
	 * 
	 * @param listener
	 *            - Listener to be used when the Widget is set to Refresh
	 */
	public final void setOnRefreshListener(OnRefreshListener listener) {
		this.onRefreshListener = listener;
	}

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * 
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		this.isPullToRefreshEnabled = enable;
	}

	/**
	 * Set Text to show when the Widget is being pulled, and will refresh when
	 * released
	 * 
	 * @param releaseLabel
	 *            - String to display
	 */
	public void setReleaseLabel(String releaseLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setLabelRelease(releaseLabel);
		}
		if (null != mFooterLayout) {
			mFooterLayout.setLabelRelease(releaseLabel);
		}
	}

	/**
	 * Set Text to show when the Widget is being Pulled
	 * 
	 * @param pullLabel
	 *            - String to display
	 */
	public void setPullLabel(String pullLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setLabelPull(pullLabel);
		}
		if (null != mFooterLayout) {
			mFooterLayout.setLabelPull(pullLabel);
		}
	}

	/**
	 * Set Text to show when the Widget is refreshing
	 * 
	 * @param refreshingLabel
	 *            - String to display
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setLabelRefreshing(refreshingLabel);
		}
		if (null != mFooterLayout) {
			mFooterLayout.setLabelRefreshing(refreshingLabel);
		}
	}

	public final void setRefreshing() {
		this.setRefreshing(true);
	}

	/**
	 * Sets the Widget to be in the refresh state. The UI will be updated to
	 * show the 'Refreshing' view.
	 * 
	 * @param doScroll
	 *            - true if you want to force a scroll to the Refreshing view.
	 */
	public final void setRefreshing(boolean doScroll) {
		if (!isRefreshing()) {
			setRefreshingInternal(doScroll);
			state = MANUAL_REFRESHING;
		}
	}

	public final boolean hasPullFromTop() {
		return curmode != MODE_PULL_UP;
	}

	@Override
	public void setLongClickable(boolean longClickable) {
		mTargetView.setLongClickable(longClickable);
	}

	protected final int getCurrentMode() {
		return curmode;
	}

	public final AfPullFooterLayout getFooterLayout() {
		return mFooterLayout;
	}

	public final AfPullHeaderLayout getHeaderLayout() {
		return mHeaderLayout;
	}

	protected final int getHeaderHeight() {
		return headerHeight;
	}

	protected final int getMode() {
		return mode;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isPullToRefreshEnabled) {
			return false;
		}

		if (isRefreshing() && disableScrollingWhileRefreshing) {
			return true;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getEdgeFlags() != 0) {
			return false;
		}

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE: {
			if (isBeingDragged) {
				lastMotionY = event.getY();
				this.pullEvent();
				return true;
			}
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if (isBeingDragged) {
				isBeingDragged = false;
				if (state == RELEASE_TO_REFRESH && null != onRefreshListener) {
					boolean acceptance = false;
					if (curmode == MODE_PULL_DOWN) {
						acceptance = onRefreshListener.onRefresh();
					} else {
						acceptance = onRefreshListener.onMore();
					}
					if (acceptance) {
						setRefreshingInternal(true);
					} else {
						smoothScrollTo(0);
					}
				} else {
					smoothScrollTo(0);
				}
				return true;
			}
			break;
		}
		}

		return false;
	}

	@Override
	public void setOnTouchListener(OnTouchListener listener) {
		// TODO Auto-generated method stub
		mTouchListener = listener;
	}

	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {

		final int action = event.getAction();

		if (!isPullToRefreshEnabled || action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			isBeingDragged = false;
			if (mTouchListener != null) {
				return mTouchListener.onTouch(this, event);
			}
			return false;
		}

		if (isRefreshing() && disableScrollingWhileRefreshing) {
			return true;
		}

		if (action != MotionEvent.ACTION_DOWN && isBeingDragged) {
			return true;
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			if (isReadyForPull()) {
				final float y = event.getY();
				final float dy = y - lastMotionY;
				final float yDiff = Math.abs(dy);
				final float xDiff = Math.abs(event.getX() - lastMotionX);

				if (yDiff > touchSlop && yDiff > xDiff) {
					if ((mode & MODE_PULL_DOWN) == MODE_PULL_DOWN
							&& dy >= 0.0001f && isReadyForPullDown()) {
						lastMotionY = y;
						isBeingDragged = true;
						if (mode == MODE_BOTH) {
							curmode = MODE_PULL_DOWN;
						}
					} else if ((mode & MODE_PULL_UP) == MODE_PULL_UP
							&& dy <= 0.0001f && isReadyForPullUp()) {
						lastMotionY = y;
						isBeingDragged = true;
						if (mode == MODE_BOTH) {
							curmode = MODE_PULL_UP;
						}
					}
				}
			}
			break;
		}
		case MotionEvent.ACTION_DOWN: {
			if (isReadyForPull()) {
				lastMotionY = initMotionY = event.getY();
				lastMotionX = event.getX();
				isBeingDragged = false;
			}
			break;
		}
		}
		return isBeingDragged;
	}

	/**
	 * Actions a Pull Event
	 * 
	 * @return true if the Event has been handled, false if there has been no
	 *         change
	 */
	private boolean pullEvent() {

		final int newHeight;
		final int oldHeight = this.getScrollY();

		switch (curmode) {
		case MODE_PULL_UP:
			newHeight = Math.round(Math.max(initMotionY - lastMotionY, 0)
					/ FRICTION);
			break;
		case MODE_PULL_DOWN:
		default:
			newHeight = Math.round(Math.min(initMotionY - lastMotionY, 0)
					/ FRICTION);
			break;
		}

		setHeaderScroll(newHeight);

		if (newHeight != 0) {
			if (state == PULL_TO_REFRESH && headerHeight < Math.abs(newHeight)) {
				state = RELEASE_TO_REFRESH;

				switch (curmode) {
				case MODE_PULL_UP:
					mFooterLayout.releaseToRefresh();
					break;
				case MODE_PULL_DOWN:
					mHeaderLayout.releaseToRefresh();
					break;
				}
				return true;

			} else if (state == RELEASE_TO_REFRESH
					&& headerHeight >= Math.abs(newHeight)) {
				state = PULL_TO_REFRESH;

				switch (curmode) {
				case MODE_PULL_UP:
					mFooterLayout.pullToRefresh();
					break;
				case MODE_PULL_DOWN:
					mHeaderLayout.pullToRefresh();
					break;
				}

				return true;
			}
		}

		return oldHeight != newHeight;
	}

	// ===========================================================
	// Interfaces
	// ===========================================================

	/**
	 * This is implemented by derived classes to return the created View. If you
	 * need to use a custom View (such as a custom ListView), override this
	 * method and return an instance of your custom class.
	 * 
	 * Be sure to set the ID of the view in this method, especially if you're
	 * using a ListActivity or ListFragment.
	 * 
	 * @param context
	 * @param attrs
	 *            AttributeSet from wrapped class. Means that anything you
	 *            include in the XML layout declaration will be routed to the
	 *            created View
	 * @return New instance of the Refreshable View
	 */
	protected abstract Target onCreateRefreshableView(Context context,
			AttributeSet attrs);

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling down.
	 * 
	 * @return true if the View is currently the correct state (for example, top
	 *         of a ListView)
	 */
	protected abstract boolean isReadyForPullDown();

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling up.
	 * 
	 * @return true if the View is currently in the correct state (for example,
	 *         bottom of a ListView)
	 */
	protected abstract boolean isReadyForPullUp();

	// ===========================================================
	// Methods
	// ===========================================================

	protected void resetLayout() {
		state = PULL_TO_REFRESH;
		isBeingDragged = false;
		if (null != mHeaderLayout) {
			mHeaderLayout.reset();
		}
		if (null != mFooterLayout) {
			mFooterLayout.reset();
		}
		smoothScrollTo(0);
	}

	protected void setRefreshingInternal(boolean doScroll) {
		state = REFRESHING;
		if (null != mHeaderLayout) {
			mHeaderLayout.refreshing();
		}
		if (null != mFooterLayout) {
			mFooterLayout.refreshing();
		}
		if (doScroll) {
			if (curmode == MODE_PULL_DOWN) {
				smoothScrollTo(-headerHeight);
			} else {
				smoothScrollTo(+footerHeight);
			}
		}
	}

	protected final void setHeaderScroll(int y) {
		scrollTo(0, y);
	}

	protected final void smoothScrollTo(int y) {
		if (null != smoothrunnable) {
			smoothrunnable.stop();
		}
		if (this.getScrollY() != y) {
			this.smoothrunnable = new SmoothRunnable(Looper.myLooper(),
					mSmooth, getScrollY(), y);
			this.smoothrunnable.start();
		}
	}

	private boolean isReadyForPull() {
		switch (mode) {
		case MODE_PULL_DOWN:
			return isReadyForPullDown();
		case MODE_PULL_UP:
			return isReadyForPullUp();
		case MODE_BOTH:
			return isReadyForPullUp() || isReadyForPullDown();
		}
		return false;
	}

	// ===========================================================
	// Inner interface
	// ===========================================================

	public static interface OnRefreshListener {
		public boolean onMore();

		public boolean onRefresh();
	}

	public static interface OnLastItemVisibleListener {
		public void onLastItemVisible();
	}

	private static interface Smoothable {
		public boolean onSmooth(int value, int form, int to);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private Smoothable mSmooth = new Smoothable() {
		@Override
		public boolean onSmooth(int value, int form, int to) {
			// TODO Auto-generated method stub
			scrollTo(0, value);
			return true;
		}
	};

	private final class SmoothRunnable implements Runnable {

		public static final int ANIMATION_FPS = 1000 / 60;
		public static final int ANIMATION_DURATION_MS = 190;

		private final int valueto;
		private final int valuefrom;
		private final Handler handler;
		private final Smoothable smoothable;
		private final Interpolator interpolator;

		private int value = -1;
		private long startTime = -1;
		private boolean running = true;

		public SmoothRunnable(Looper looper, Smoothable smoothable, int from,
				int to) {
			this.valueto = to;
			this.valuefrom = from;
			this.smoothable = smoothable;
			this.handler = new Handler(looper);
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		public void run() {
			/**
			 * Only set startTime if this is the first time we're starting, else
			 * actually calculate the Y delta
			 */
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = System.currentTimeMillis();
				normalizedTime = 1000 * (normalizedTime - startTime);
				normalizedTime = normalizedTime / ANIMATION_DURATION_MS;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				float interpolation = interpolator
						.getInterpolation(normalizedTime / 1000f);
				final int delta = Math.round((valuefrom - valueto)
						* interpolation);
				this.value = valuefrom - delta;
				if (smoothable.onSmooth(value, valuefrom, valueto) == false) {
					running = false;
					this.handler.removeCallbacks(this);
				}
			}

			// If we're not at the target Y, keep going...
			if (running && valueto != value) {
				handler.postDelayed(this, ANIMATION_FPS);
			}
		}

		public void stop() {
			this.running = false;
			this.handler.removeCallbacks(this);
		}

		public void start() {
			this.running = true;
			this.handler.post(this);
		}
	};
}
