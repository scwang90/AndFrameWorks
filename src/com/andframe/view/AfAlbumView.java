package com.andframe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.andframe.activity.albumn.AfAlbumViewPager;
import com.andframe.feature.AfDensity;

@SuppressWarnings("deprecation")
public class AfAlbumView extends ImageView implements OnClickListener {

	private float MAX_SCALE = 2.0f;

	private static final int MODE_NONE = 0;// 初始状态
	private static final int MODE_DRAG = 1;// 拖动
	private static final int MODE_ZOOM = 2;// 缩放
	private static final int MODE_ROTATE = 3;// 旋转
	private static final int ZOOM_OR_ROTATE = 4; // 缩放或旋转

	private int mode = MODE_NONE;

	private float imageW;
	private float imageH;
	private float rotatedImageW;
	private float rotatedImageH;
	private float viewW;
	private float viewH;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();

	private PointF pMain = new PointF(); // 主点按下坐标
	private PointF pDeputy = new PointF(); // 副点按下坐标
	private PointF pMiddle = new PointF(); // 计算中间点
	private PointF pLast = new PointF(); // 上一点
	private long lastClickTime = 0; // 上一点时间
	private double rotation = 0.0;
	private float dist = 1f;

	private OnTouchListener mTouchListener;

	private boolean mHasScale;
	
	public boolean isHasScale() {
		return mHasScale;
	}

	public AfAlbumView(Context context) {
		super(context);
		init();
	}

	public AfAlbumView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AfAlbumView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setScaleType(ImageView.ScaleType.MATRIX);
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getContext(), "点击事件", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setOnTouchListener(OnTouchListener listener) {
		mTouchListener = listener;
	}

	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		setImageWidthHeight();
	}

	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		setImageWidthHeight();
	}

	public void setImageResource(int resId) {
		super.setImageResource(resId);
		setImageWidthHeight();
	}

	/**
	 * 获取图片原尺寸
	 */
	private void setImageWidthHeight() {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		imageW = rotatedImageW = drawable.getIntrinsicWidth();
		imageH = rotatedImageH = drawable.getIntrinsicHeight();
		AfDensity density = new AfDensity(getContext());
		if (1.0f*imageW/imageH> 1.0f*density.getWidthPixels()/density.getHeightPixels()) {
			//if(MAX_SCALE * imageW < density.getWidthPixels()){
				MAX_SCALE = 2f*density.getWidthPixels()/imageW;
			//}
		}else /*if (MAX_SCALE * imageH < density.getHeightPixels()) */{
			MAX_SCALE = 2f*density.getHeightPixels()/imageH;
		}
		initImage();
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewW = w;
		viewH = h;
		if (oldw == 0) {
			initImage();
		} else {
			fixScale();
			fixTranslation();
			setImageMatrix(matrix);
		}
	}

	/**
	 * 恢复原始比例，原始状态
	 */
	private void initImage() {
		if (viewW <= 0 || viewH <= 0 || imageW <= 0 || imageH <= 0) {
			return;
		}
		mode = MODE_NONE;
		matrix.setScale(0, 0);
		fixScale();
		fixTranslation();
		setImageMatrix(matrix);
	}

	/**
	 * 自适应比例
	 */
	private void fixScale() {
		// 3*3 矩阵
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW,
				(float) viewH / (float) rotatedImageH);

		if (curScale < minScale) {
			if (curScale > 0) {
				double scale = minScale / curScale;
				p[0] = (float) (p[0] * scale);
				p[1] = (float) (p[1] * scale);
				p[3] = (float) (p[3] * scale);
				p[4] = (float) (p[4] * scale);
				matrix.setValues(p);
			} else {
				matrix.setScale(minScale, minScale);
			}
			this.mHasScale = false;
		}
	}

	/**
	 * 计算最大比例
	 * @return
	 */
	private float maxPostScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW,
				(float) viewH / (float) rotatedImageH);
		float maxScale = Math.max(minScale, MAX_SCALE);
		return maxScale / curScale;
	}

	/**
	 * 自适应位移转换
	 */
	private void fixTranslation() {
		RectF rect = new RectF(0, 0, imageW, imageH);
		matrix.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (width < viewW) {
			deltaX = (viewW - width) / 2 - rect.left;
		} else if (rect.left > 0) {
			deltaX = -rect.left;
		} else if (rect.right < viewW) {
			deltaX = viewW - rect.right;
		}

		if (height < viewH) {
			deltaY = (viewH - height) / 2 - rect.top;
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewH) {
			deltaY = viewH - rect.bottom;
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	@Override
	@SuppressLint("NewApi")
	public boolean onTouchEvent(MotionEvent event) {
		if (mTouchListener != null) {
			mTouchListener.onTouch(this, event);
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			pMain.set(event.getX(), event.getY());
			pDeputy.set(event.getX(), event.getY());
			mode = MODE_DRAG;
			break;
		// 副点按下
		case MotionEvent.ACTION_POINTER_DOWN:
			if (VERSION.SDK_INT < 8 || event.getActionIndex() > 1)
				break;
			dist = distance(event.getX(0), event.getY(0), event.getX(1),
					event.getY(1));
			// 如果连续两点距离大于10，则判定为多点模式
			if (dist > 10f) {
				savedMatrix.set(matrix);
				pMain.set(event.getX(0), event.getY(0));
				pDeputy.set(event.getX(1), event.getY(1));
				pMiddle.set((event.getX(0) + event.getX(1)) / 2,
						(event.getY(0) + event.getY(1)) / 2);
				mode = ZOOM_OR_ROTATE;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if (mode == MODE_DRAG) {
				if (distance(pMain.x, pMain.y, pDeputy.x, pDeputy.y) < 50) {
					long now = System.currentTimeMillis();
					if (now - lastClickTime < 500
							&& distance(pMain.x, pMain.y, pLast.x, pLast.y) < 50) {
						now = 0;
						doubleClick(pMain.x, pMain.y);
					}
					pLast.set(pMain);
					lastClickTime = now;
				}
			} else if (mode == MODE_ROTATE) {
				int level = (int) Math.floor((rotation + Math.PI / 4)
						/ (Math.PI / 2));
				if (level == 4)
					level = 0;
				matrix.set(savedMatrix);
				matrix.postRotate(90 * level, pMiddle.x, pMiddle.y);
				if (level == 1 || level == 3) {
					float tmp = rotatedImageW;
					rotatedImageW = rotatedImageH;
					rotatedImageH = tmp;
					fixScale();
				}
				fixTranslation();
				setImageMatrix(matrix);
			}
			notifyFocusables();
			mode = MODE_NONE;
//			new Handler(AfApplication.getLooper()).postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					notifyFocusables();
//				}
//			}, 500);
			break;
		case MotionEvent.ACTION_MOVE:

			if (mode == ZOOM_OR_ROTATE) {
				PointF pC = new PointF(event.getX(1) - event.getX(0) + pMain.x,
						event.getY(1) - event.getY(0) + pMain.y);
				double a = distance(pDeputy.x, pDeputy.y, pC.x, pC.y);
				double b = distance(pMain.x, pMain.y, pC.x, pC.y);
				double c = distance(pMain.x, pMain.y, pDeputy.x, pDeputy.y);
				if (a >= 10) {
					double cosB = (a * a + c * c - b * b) / (2 * a * c);
					double angleB = Math.acos(cosB);
					double PID4 = Math.PI / 4;
					if (angleB > PID4 && angleB < 3 * PID4) {
						mode = MODE_ROTATE;
						rotation = 0;
						this.notifyFocusables();
					} else {
						mode = MODE_ZOOM;
						this.notifyFocusables();
					}
				}
			}

			if (mode == MODE_DRAG) {
				matrix.set(savedMatrix);
				pDeputy.set(event.getX(), event.getY());
				matrix.postTranslate(event.getX() - pMain.x, event.getY()
						- pMain.y);
				fixTranslation();
				setImageMatrix(matrix);
			} else if (mode == MODE_ZOOM) {
				float newDist = distance(event.getX(0), event.getY(0),
						event.getX(1), event.getY(1));
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = Math.min(newDist / dist, maxPostScale());
					matrix.postScale(tScale, tScale, pMiddle.x, pMiddle.y);
					this.mHasScale = true;
					fixScale();
					fixTranslation();
					setImageMatrix(matrix);
				}
			} else if (mode == MODE_ROTATE) {
				PointF pC = new PointF(event.getX(1) - event.getX(0) + pMain.x,
						event.getY(1) - event.getY(0) + pMain.y);
				double a = distance(pDeputy.x, pDeputy.y, pC.x, pC.y);
				double b = distance(pMain.x, pMain.y, pC.x, pC.y);
				double c = distance(pMain.x, pMain.y, pDeputy.x, pDeputy.y);
				if (b > 10) {
					double cosA = (b * b + c * c - a * a) / (2 * b * c);
					double angleA = Math.acos(cosA);
					double ta = pDeputy.y - pMain.y;
					double tb = pMain.x - pDeputy.x;
					double tc = pDeputy.x * pMain.y - pMain.x * pDeputy.y;
					double td = ta * pC.x + tb * pC.y + tc;
					if (td > 0) {
						angleA = 2 * Math.PI - angleA;
					}
					rotation = angleA;
					matrix.set(savedMatrix);
					matrix.postRotate((float) (rotation * 180 / Math.PI),
							pMiddle.x, pMiddle.y);
					setImageMatrix(matrix);
				}
			}
			break;
		}
		return true;
	}
	
	

	/**
	 * 两点的距离
	 */
	private float distance(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;
		return (float)Math.sqrt(x * x + y * y);
	}

	private void doubleClick(float x, float y) {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW,
				(float) viewH / (float) rotatedImageH);
		float toScale = curScale;
		if (curScale <= minScale + 0.01) { // 放大
			toScale = Math.max(minScale, MAX_SCALE) / curScale;
			// matrix.postScale(toScale, toScale, x, y);
			this.mHasScale = true;
			this.notifyFocusables();
		} else { // 缩小
			toScale = minScale / curScale;
			this.mHasScale = false;
			this.notifyFocusables();
			// matrix.postScale(toScale, toScale, x, y);
			// fixTranslation();
		}
		if (mSmoothRunnable != null) {
			mSmoothRunnable.stop();
			mSmoothRunnable = null;
		}
		int from = (int) (1 * 100000);
		int to = (int) (toScale * 100000);
		Smooth smoothable = new Smooth(x, y, p);
		mSmoothRunnable = new SmoothRunnable(smoothable, from, to, 300);
		mSmoothRunnable.start();
		// setImageMatrix(matrix);
	}

	private void notifyFocusables() {
		if (mViewPager instanceof AfAlbumViewPager) {
			mViewPager.setHorizontalScrollBarEnabled(!(mHasScale 
					|| mode == MODE_ROTATE 
					|| mode == MODE_ZOOM
					/*|| mode == ZOOM_OR_ROTATE*/));
		}
	}

	private ViewPager mViewPager;
	private SmoothRunnable mSmoothRunnable = null;

	private class Smooth implements Smoothable {

		public float x = 0;
		public float y = 0;
		public float[] p = null;

		public Smooth(float x, float y, float[] p) {
			this.x = x;
			this.x = y;
			this.p = p;
		}

		@Override
		public void onStart(int from, int to) {

		}

		@Override
		public boolean onSmooth(int value, float percent, int from, int to) {
			float scale = 1f * value / 100000;
			matrix.setValues(p);
			matrix.postScale(scale, scale, x, y);
			fixTranslation();
			setImageMatrix(matrix);
			return true;
		}

		@Override
		public void onFinish(int from, int to) {

		}
	};

	public static interface Smoothable {
		public void onStart(int from, int to);

		public void onFinish(int from, int to);

		public boolean onSmooth(int value, float percent, int from, int to);
	}

	public class SmoothRunnable implements Runnable {
		public static final int ANIMATION_FPS = 1000 / 60;
		public static final int ANIMATION_DURATION_MS = 190;

		private final int valueto;
		private final int valuefrom;
		private final int duratioin;
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
			this.duratioin = ANIMATION_DURATION_MS;
			this.handler = new Handler(looper);
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		public SmoothRunnable(Looper looper, Smoothable smoothable, int from,
				int to, int duratioin) {
			this.valueto = to;
			this.valuefrom = from;
			this.duratioin = duratioin;
			this.smoothable = smoothable;
			this.handler = new Handler(looper);
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		public SmoothRunnable(Smoothable smoothable, int from, int to,
				int duratioin) {
			this.valueto = to;
			this.valuefrom = from;
			this.duratioin = duratioin;
			this.smoothable = smoothable;
			this.handler = new Handler(Looper.myLooper());
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
				smoothable.onStart(valuefrom, valueto);
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = System.currentTimeMillis();
				normalizedTime = 1000 * (normalizedTime - startTime);
				normalizedTime = normalizedTime / duratioin;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				float interpolation = interpolator
						.getInterpolation(normalizedTime / 1000f);
				final int delta = Math.round((valuefrom - valueto)
						* interpolation);
				this.value = valuefrom - delta;
				if (smoothable.onSmooth(value, interpolation, valuefrom,
						valueto) == false) {
					running = false;
					this.handler.removeCallbacks(this);
				}
			}

			// If we're not at the target Y, keep going...
			if (running && valueto != value) {
				handler.postDelayed(this, ANIMATION_FPS);
			} else {
				smoothable.onFinish(valuefrom, valueto);
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

	}

	public void setViewPager(ViewPager pager) {
		mViewPager = pager;
	}
}
