package com.andframe.view;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.andframe.feature.AfDensity;
/**
 * 手势拖动布局
 * @author 树朾
 */
public class AfPullDownLayout extends LinearLayout {

	/**
	 *  手势监听器
	 * @author 树朾
	 */
	public interface OnPullDownListener {
		/**
		 *  手势操作进行时监听
		 * @param value 百分比
		 * @param height 高度差
		 * @return 暂无定义
		 */
		boolean onPulling(float value,int height,int max);
		/**
		 *  手势操作释放时监听
		 * @param value 百分比
		 * @param height 高度差
		 * @return 暂无定义
		 */
		boolean onRelease(float value,int height,int max);
	}
	// ===========================================================
	// Constants
	// ===========================================================

	// 公式1 y = H-A/(x+A/H)
	// 公式2 y = H-H*H/(x+H)
	private double H = 100;

	// 状态
	private static final int PULL_TO_DOWN = 0x0;
	private static final int RELEASE_TO_UP = 0x1;

	// ===========================================================
	// Fields
	// ===========================================================

	private int touchSlop;

	private float initMotionY;
	private float lastMotionX;
	private float lastMotionY;
	private boolean isBeingDragged = false;

	private int state = PULL_TO_DOWN;

	private boolean isPullToRefreshEnabled = true;

	private SmoothRunnable smoothrunnable;
	private OnPullDownListener onOnPullDownListener;

	private ReadyForPullDownable mPullDownable = new ReadyForPullDownable() {
		@Override
		public boolean isReadyForPullDown() {
			return true;
		}
	};

	private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

	public AfPullDownLayout(Context context) {
		super(context);
		this.initialized(context, null);
	}

	public AfPullDownLayout(Context context, int mode) {
		super(context);
		this.initialized(context, null);
	}

	public AfPullDownLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialized(context, attrs);
	}

	private void initialized(Context context, AttributeSet attrs) {
		this.H = AfDensity.dip2px(context, (float)H);
		setOrientation(LinearLayout.VERTICAL);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Whether Pull-to-Refresh is enabled
	 * @return enabled
	 */
	public final boolean isPullToRefreshEnabled() {
		return isPullToRefreshEnabled;
	}

	/**
	 * 设置加速器
	 * @param interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		if (interpolator != null) {
			this.mInterpolator = interpolator;
		}
	}

	/**
	 * 设置释放监听器
	 * @param listener
	 */
	public void setOnPullDownListener(OnPullDownListener listener) {
		this.onOnPullDownListener = listener;
	}

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		this.isPullToRefreshEnabled = enable;
	}

	public void setPullDownable(ReadyForPullDownable mPullDownable) {
		this.mPullDownable = mPullDownable;
	}

	public void setPullDownable(final ScrollView scrollview){
		if (scrollview != null) {
			this.mPullDownable = new ReadyForPullDownable() {
				@Override
				public boolean isReadyForPullDown() {
					return scrollview.getScrollY() <= 0;
				}
			};
		}
	}
	public void setPullDownable(final AbsListView listview){
		if (listview != null) {
			this.mPullDownable = new ReadyForPullDownable() {
				@Override
				public boolean isReadyForPullDown() {
					return 5 >= Math.abs(getLastPositionDistanceGuess(listview)
							- listview.getBottom());
				}

				private int getLastPositionDistanceGuess(AbsListView listview) {
					Field field;
					// 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
					try {
						field = AbsListView.class
								.getDeclaredField("mFirstPositionDistanceGuess");
						field.setAccessible(true);
						return (Integer) field.get(listview);
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return -1;
				}
			};
		}
	}

	public void setMaxScorllHeight(int height) {
		this.H = height;
	}

	public void setMaxScorllHeightDp(int height) {
		this.H = AfDensity.dip2px(getContext(), height);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public final boolean onTouchEvent(MotionEvent event) {

		if (!isPullToRefreshEnabled) {
			return false;
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
					smoothScrollTo(0);
					if (onOnPullDownListener != null) {
						int newHeight = Math.round(Math.max(lastMotionY - initMotionY, 0));
						int x = newHeight;
						int y = (int)(H-H*H/(x+H));
						onOnPullDownListener.onRelease((float)(y/H),y,(int) H);
					}
					return true;
				}
				break;
			}
		}

		return false;
	}

	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {

		final int action = event.getAction();

		if (!isPullToRefreshEnabled || action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			isBeingDragged = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && isBeingDragged) {
			return true;
		}

		switch (action) {
			case MotionEvent.ACTION_MOVE: {
				if (isReadyForPullDown()) {
					final float y = event.getY();
					final float dy = y - lastMotionY;
					final float yDiff = Math.abs(dy);
					final float xDiff = Math.abs(event.getX() - lastMotionX);

					if (yDiff > touchSlop && yDiff > xDiff && dy >= 0.0001f) {
						lastMotionY = y;
						isBeingDragged = true;
					}
				}
				break;
			}
			case MotionEvent.ACTION_DOWN: {
				if (isReadyForPullDown()) {
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
	 * @return true if the Event has been handled, false if there has been no
	 *         change
	 */
	private boolean pullEvent() {

		int oldHeight = this.getScrollY();
		int newHeight = Math.round(Math.max(lastMotionY - initMotionY, 0));

		// 公式2 y = H-H*H/(x+H)
		int x = newHeight;
		int y = (int)(H-H*H/(x+H));

		setHeaderScroll(-y);

		if (onOnPullDownListener != null) {
			onOnPullDownListener.onPulling((float)(y/H),y,(int) H);
		}

		if (newHeight != 0) {
			if (state == PULL_TO_DOWN) {
				state = RELEASE_TO_UP;
				return true;

			} else if (state == RELEASE_TO_UP) {
				// state = PULL_TO_DOWN;
				return true;
			}
		}
		return oldHeight != newHeight;
	}

	// ===========================================================
	// Interfaces
	// ===========================================================

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling down.
	 * @return true if the View is currently the correct state (for example, top
	 *         of a ListView)
	 */
	protected boolean isReadyForPullDown() {
		if(this.mPullDownable != null){
			return mPullDownable.isReadyForPullDown();
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

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

	// ===========================================================
	// Inner interface
	// ===========================================================

	public static interface ReadyForPullDownable {
		public boolean isReadyForPullDown();
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
			scrollTo(0, value);
			if (onOnPullDownListener != null) {
				value = -value;
				onOnPullDownListener.onPulling((float)(value/H),value,(int) H);
			}
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
			this.interpolator = mInterpolator;
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
