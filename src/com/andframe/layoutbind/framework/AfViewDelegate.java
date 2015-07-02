package com.andframe.layoutbind.framework;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.Display;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewOverlay;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.WindowId;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class AfViewDelegate extends View{

	protected View target;

	public AfViewDelegate(View view) {
		super(view.getContext());
		// TODO Auto-generated constructor stub
		this.target = view;
	}

	@Override
	public void addChildrenForAccessibility(ArrayList<View> children) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			super.addChildrenForAccessibility(children);
		}
		this.target.addChildrenForAccessibility(children);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.addFocusables(views, direction, focusableMode);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.addFocusables(views, direction);
	}

	@Override
	public void addOnAttachStateChangeListener(
			OnAttachStateChangeListener listener) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.addOnAttachStateChangeListener(listener);
	}

	@Override
	public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.addOnLayoutChangeListener(listener);
	}

	@Override
	public void addTouchables(ArrayList<View> views) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.addTouchables(views);
	}

	@Override
	public ViewPropertyAnimator animate() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.animate();
		}
		return this.target.animate();
	}

	@Override
	public void announceForAccessibility(CharSequence text) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.announceForAccessibility(text);
	}

	@Override
	public void bringToFront() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.bringToFront();
	}

	@Override
	public void buildDrawingCache() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.buildDrawingCache();
	}

	@Override
	public void buildDrawingCache(boolean autoScale) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.buildDrawingCache(autoScale);
	}

	@Override
	public void buildLayer() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.buildLayer();
	}

	@Override
	public boolean callOnClick() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.callOnClick();
		}
		return this.target.callOnClick();
	}

	@Override
	public boolean canResolveLayoutDirection() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.canResolveLayoutDirection();
		}
		return this.target.canResolveLayoutDirection();
	}

	@Override
	public boolean canResolveTextAlignment() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.canResolveTextAlignment();
		}
		return this.target.canResolveTextAlignment();
	}

	@Override
	public boolean canResolveTextDirection() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.canResolveTextDirection();
		}
		return this.target.canResolveTextDirection();
	}

	@Override
	public boolean canScrollHorizontally(int direction) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.canScrollHorizontally(direction);
		}
		return this.target.canScrollHorizontally(direction);
	}

	@Override
	public boolean canScrollVertically(int direction) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.canScrollVertically(direction);
		}
		return this.target.canScrollVertically(direction);
	}

	@Override
	public void cancelLongPress() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.cancelLongPress();
	}

	@Override
	public boolean checkInputConnectionProxy(View view) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.checkInputConnectionProxy(view);
		}
		return this.target.checkInputConnectionProxy(view);
	}

	@Override
	public void clearAnimation() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.clearAnimation();
	}

	@Override
	public void clearFocus() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.clearFocus();
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.computeScroll();
	}

	@Override
	public AccessibilityNodeInfo createAccessibilityNodeInfo() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.createAccessibilityNodeInfo();
		}
		return this.target.createAccessibilityNodeInfo();
	}

	@Override
	public void createContextMenu(ContextMenu menu) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.createContextMenu(menu);
	}

	@Override
	public void destroyDrawingCache() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.destroyDrawingCache();
	}

	@Override
	public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchApplyWindowInsets(insets);
		}
		return this.target.dispatchApplyWindowInsets(insets);
	}

	@Override
	public void dispatchConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.dispatchConfigurationChanged(newConfig);
	}

	@Override
	public void dispatchDisplayHint(int hint) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.dispatchDisplayHint(hint);
	}

	@Override
	public boolean dispatchDragEvent(DragEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchDragEvent(event);
		}
		return this.target.dispatchDragEvent(event);
	}

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchGenericMotionEvent(event);
		}
		return this.target.dispatchGenericMotionEvent(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchKeyEvent(event);
		}
		return this.target.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchKeyEventPreIme(event);
		}
		return this.target.dispatchKeyEventPreIme(event);
	}

	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchKeyShortcutEvent(event);
		}
		return this.target.dispatchKeyShortcutEvent(event);
	}

	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchPopulateAccessibilityEvent(event);
		}
		return this.target.dispatchPopulateAccessibilityEvent(event);
	}

	@Override
	public void dispatchSystemUiVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.dispatchSystemUiVisibilityChanged(visibility);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchTrackballEvent(event);
		}
		return this.target.dispatchTouchEvent(event);
	}

	@Override
	public boolean dispatchTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchTrackballEvent(event);
		}
		return this.target.dispatchTrackballEvent(event);
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.dispatchUnhandledMove(focused, direction);
		}
		return this.target.dispatchUnhandledMove(focused, direction);
	}

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.dispatchWindowFocusChanged(hasFocus);
	}

	@Override
	public void dispatchWindowSystemUiVisiblityChanged(int visible) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.dispatchWindowSystemUiVisiblityChanged(visible);
	}

	@Override
	public void dispatchWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.dispatchWindowVisibilityChanged(visibility);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.draw(canvas);
	}

	@Override
	public View findFocus() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.findFocus();
		}
		return this.target.findFocus();
	}

	
	
	@Override
	public void findViewsWithText(ArrayList<View> outViews,
			CharSequence searched, int flags) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.findViewsWithText(outViews, searched, flags);
	}

	@Override
	public View focusSearch(int direction) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.focusSearch(direction);
		}
		return this.target.focusSearch(direction);
	}

	@Override
	public void forceLayout() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.forceLayout();
	}

	@Override
	public int getAccessibilityLiveRegion() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getAccessibilityLiveRegion();
		}
		return this.target.getAccessibilityLiveRegion();
	}

	@Override
	public AccessibilityNodeProvider getAccessibilityNodeProvider() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getAccessibilityNodeProvider();
		}
		return this.target.getAccessibilityNodeProvider();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getAlpha() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getAlpha();
		}
		return this.target.getAlpha();
	}

	@Override
	public Animation getAnimation() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getAnimation();
		}
		return this.target.getAnimation();
	}

	@Override
	public IBinder getApplicationWindowToken() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getApplicationWindowToken();
		}
		return this.target.getApplicationWindowToken();
	}

	@Override
	public Drawable getBackground() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getBackground();
		}
		return this.target.getBackground();
	}

	@Override
	@ExportedProperty(category = "layout")
	public int getBaseline() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getBaseline();
		}
		return this.target.getBaseline();
	}

	@Override
	public float getCameraDistance() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getCameraDistance();
		}
		return this.target.getCameraDistance();
	}

	@Override
	public Rect getClipBounds() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getClipBounds();
		}
		return this.target.getClipBounds();
	}

	@Override
	@ExportedProperty(category = "accessibility")
	public CharSequence getContentDescription() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getContentDescription();
		}
		return this.target.getContentDescription();
	}

	@Override
	public Display getDisplay() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getDisplay();
		}
		return this.target.getDisplay();
	}

	@Override
	public Bitmap getDrawingCache() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getDrawingCache();
		}
		return this.target.getDrawingCache();
	}

	@Override
	public Bitmap getDrawingCache(boolean autoScale) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getDrawingCache(autoScale);
		}
		return this.target.getDrawingCache(autoScale);
	}

	@Override
	public int getDrawingCacheBackgroundColor() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getDrawingCacheBackgroundColor();
		}
		return this.target.getDrawingCacheBackgroundColor();
	}

	@Override
	public int getDrawingCacheQuality() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getDrawingCacheQuality();
		}
		return this.target.getDrawingCacheQuality();
	}

	@Override
	public void getDrawingRect(Rect outRect) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.getDrawingRect(outRect);
	}

	@Override
	public long getDrawingTime() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getDrawingTime();
		}
		return this.target.getDrawingTime();
	}

	@Override
	@ExportedProperty
	public boolean getFilterTouchesWhenObscured() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getFilterTouchesWhenObscured();
		}
		return this.target.getFilterTouchesWhenObscured();
	}

	@Override
	public boolean getFitsSystemWindows() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getFitsSystemWindows();
		}
		return this.target.getFitsSystemWindows();
	}

	@Override
	public ArrayList<View> getFocusables(int direction) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getFocusables(direction);
		}
		return this.target.getFocusables(direction);
	}

	@Override
	public void getFocusedRect(Rect r) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.getFocusedRect(r);
	}

	@Override
	public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getGlobalVisibleRect(r, globalOffset);
		}
		return this.target.getGlobalVisibleRect(r, globalOffset);
	}

	@Override
	public Handler getHandler() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getHandler();
		}
		return this.target.getHandler();
	}

	@Override
	public void getHitRect(Rect outRect) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.getHitRect(outRect);
	}

	@Override
	public int getHorizontalFadingEdgeLength() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getHorizontalFadingEdgeLength();
		}
		return this.target.getHorizontalFadingEdgeLength();
	}

	@Override
	@CapturedViewProperty
	public int getId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getId();
		}
		return this.target.getId();
	}

	@Override
	@ExportedProperty(category = "accessibility", mapping = {
			@IntToString(from = 0, to = "auto"),
			@IntToString(from = 1, to = "yes"),
			@IntToString(from = 2, to = "no"),
			@IntToString(from = 4, to = "noHideDescendants") })
	public int getImportantForAccessibility() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getImportantForAccessibility();
		}
		return this.target.getImportantForAccessibility();
	}

	@Override
	public boolean getKeepScreenOn() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getKeepScreenOn();
		}
		return this.target.getKeepScreenOn();
	}

	@Override
	public DispatcherState getKeyDispatcherState() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getKeyDispatcherState();
		}
		return this.target.getKeyDispatcherState();
	}

	@Override
	@ExportedProperty(category = "accessibility")
	public int getLabelFor() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getLabelFor();
		}
		return this.target.getLabelFor();
	}

	@Override
	public int getLayerType() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getLayerType();
		}
		return this.target.getLayerType();
	}

	@Override
	@ExportedProperty(category = "layout", mapping = {
			@IntToString(from = 0, to = "RESOLVED_DIRECTION_LTR"),
			@IntToString(from = 1, to = "RESOLVED_DIRECTION_RTL") })
	public int getLayoutDirection() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getLayoutDirection();
		}
		return this.target.getLayoutDirection();
	}

	@Override
	@ExportedProperty(deepExport = true, prefix = "layout_")
	public LayoutParams getLayoutParams() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getLayoutParams();
		}
		return this.target.getLayoutParams();
	}

	@Override
	public void getLocationInWindow(int[] location) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.getLocationInWindow(location);
	}

	@Override
	public void getLocationOnScreen(int[] location) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.getLocationOnScreen(location);
	}

	@Override
	public Matrix getMatrix() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getMatrix();
		}
		return this.target.getMatrix();
	}

	@Override
	public int getMinimumHeight() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getMinimumHeight();
		}
		return this.target.getMinimumHeight();
	}

	@Override
	public int getMinimumWidth() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getMinimumWidth();
		}
		return this.target.getMinimumWidth();
	}

	@Override
	public int getNextFocusDownId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getNextFocusDownId();
		}
		return this.target.getNextFocusDownId();
	}

	@Override
	public int getNextFocusForwardId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getNextFocusForwardId();
		}
		return this.target.getNextFocusForwardId();
	}

	@Override
	public int getNextFocusLeftId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getNextFocusLeftId();
		}
		return this.target.getNextFocusLeftId();
	}

	@Override
	public int getNextFocusRightId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getNextFocusRightId();
		}
		return this.target.getNextFocusRightId();
	}

	@Override
	public int getNextFocusUpId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getNextFocusUpId();
		}
		return this.target.getNextFocusUpId();
	}

	@Override
	public OnFocusChangeListener getOnFocusChangeListener() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getOnFocusChangeListener();
		}
		return this.target.getOnFocusChangeListener();
	}

	@Override
	public int getOverScrollMode() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getOverScrollMode();
		}
		return this.target.getOverScrollMode();
	}

	@Override
	public ViewOverlay getOverlay() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getOverlay();
		}
		return this.target.getOverlay();
	}

	@Override
	public int getPaddingBottom() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPaddingBottom();
		}
		return this.target.getPaddingBottom();
	}

	@Override
	public int getPaddingEnd() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPaddingEnd();
		}
		return this.target.getPaddingEnd();
	}

	@Override
	public int getPaddingLeft() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPaddingLeft();
		}
		return this.target.getPaddingLeft();
	}

	@Override
	public int getPaddingRight() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPaddingRight();
		}
		return this.target.getPaddingRight();
	}

	@Override
	public int getPaddingStart() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPaddingStart();
		}
		return this.target.getPaddingStart();
	}

	@Override
	public int getPaddingTop() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPaddingTop();
		}
		return this.target.getPaddingTop();
	}

	@Override
	public ViewParent getParentForAccessibility() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getParentForAccessibility();
		}
		return this.target.getParentForAccessibility();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getPivotX() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPivotX();
		}
		return this.target.getPivotX();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getPivotY() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getPivotY();
		}
		return this.target.getPivotY();
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getResources();
		}
		return this.target.getResources();
	}

	@Override
	public View getRootView() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getRootView();
		}
		return this.target.getRootView();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getRotation() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getRotation();
		}
		return this.target.getRotation();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getRotationX() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getRotationX();
		}
		return this.target.getRotationX();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getRotationY() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getRotationY();
		}
		return this.target.getRotationY();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getScaleX() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getScaleY();
		}
		return this.target.getScaleX();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getScaleY() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getScaleY();
		}
		return this.target.getScaleY();
	}

	@Override
	public int getScrollBarDefaultDelayBeforeFade() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getScrollBarDefaultDelayBeforeFade();
		}
		return this.target.getScrollBarDefaultDelayBeforeFade();
	}

	@Override
	public int getScrollBarFadeDuration() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getScrollBarFadeDuration();
		}
		return this.target.getScrollBarFadeDuration();
	}

	@Override
	public int getScrollBarSize() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getScrollBarSize();
		}
		return this.target.getScrollBarSize();
	}

	@Override
	@ExportedProperty(mapping = {
			@IntToString(from = 0, to = "INSIDE_OVERLAY"),
			@IntToString(from = 16777216, to = "INSIDE_INSET"),
			@IntToString(from = 33554432, to = "OUTSIDE_OVERLAY"),
			@IntToString(from = 50331648, to = "OUTSIDE_INSET") })
	public int getScrollBarStyle() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getScrollBarStyle();
		}
		return this.target.getScrollBarStyle();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public int getSolidColor() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getSolidColor();
		}
		return this.target.getSolidColor();
	}

	@Override
	public int getSystemUiVisibility() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getSystemUiVisibility();
		}
		return this.target.getSystemUiVisibility();
	}

	@Override
	@ExportedProperty
	public Object getTag() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTag();
		}
		return this.target.getTag();
	}

	@Override
	public Object getTag(int key) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTag(key);
		}
		return this.target.getTag(key);
	}

	@Override
	@ExportedProperty(category = "text", mapping = {
			@IntToString(from = 0, to = "INHERIT"),
			@IntToString(from = 1, to = "GRAVITY"),
			@IntToString(from = 2, to = "TEXT_START"),
			@IntToString(from = 3, to = "TEXT_END"),
			@IntToString(from = 4, to = "CENTER"),
			@IntToString(from = 5, to = "VIEW_START"),
			@IntToString(from = 6, to = "VIEW_END") })
	public int getTextAlignment() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTextAlignment();
		}
		return this.target.getTextAlignment();
	}

	@Override
	@ExportedProperty(category = "text", mapping = {
			@IntToString(from = 0, to = "INHERIT"),
			@IntToString(from = 1, to = "FIRST_STRONG"),
			@IntToString(from = 2, to = "ANY_RTL"),
			@IntToString(from = 3, to = "LTR"),
			@IntToString(from = 4, to = "RTL"),
			@IntToString(from = 5, to = "LOCALE") })
	public int getTextDirection() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTextDirection();
		}
		return this.target.getTextDirection();
	}

	@Override
	public TouchDelegate getTouchDelegate() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTouchDelegate();
		}
		return this.target.getTouchDelegate();
	}

	@Override
	public ArrayList<View> getTouchables() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTouchables();
		}
		return this.target.getTouchables();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getTranslationX() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTranslationX();
		}
		return this.target.getTranslationX();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getTranslationY() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getTranslationY();
		}
		return this.target.getTranslationY();
	}

	@Override
	public int getVerticalFadingEdgeLength() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getVerticalFadingEdgeLength();
		}
		return this.target.getVerticalFadingEdgeLength();
	}

	@Override
	public int getVerticalScrollbarPosition() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getVerticalScrollbarPosition();
		}
		return this.target.getVerticalScrollbarPosition();
	}

	@Override
	public int getVerticalScrollbarWidth() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getVerticalScrollbarWidth();
		}
		return this.target.getVerticalScrollbarWidth();
	}

	@Override
	public ViewTreeObserver getViewTreeObserver() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getViewTreeObserver();
		}
		return this.target.getViewTreeObserver();
	}

	@Override
	@ExportedProperty(mapping = { @IntToString(from = 0, to = "VISIBLE"),
			@IntToString(from = 4, to = "INVISIBLE"),
			@IntToString(from = 8, to = "GONE") })
	public int getVisibility() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getVisibility();
		}
		return this.target.getVisibility();
	}

	@Override
	public WindowId getWindowId() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getWindowId();
		}
		return this.target.getWindowId();
	}

	@Override
	public int getWindowSystemUiVisibility() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getWindowSystemUiVisibility();
		}
		return this.target.getWindowSystemUiVisibility();
	}

	@Override
	public IBinder getWindowToken() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getWindowToken();
		}
		return this.target.getWindowToken();
	}

	@Override
	public int getWindowVisibility() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getWindowVisibility();
		}
		return this.target.getWindowVisibility();
	}

	@Override
	public void getWindowVisibleDisplayFrame(Rect outRect) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.getWindowVisibleDisplayFrame(outRect);
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getX() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getX();
		}
		return this.target.getX();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public float getY() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.getY();
		}
		return this.target.getY();
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean hasFocus() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.hasFocus();
		}
		return this.target.hasFocus();
	}

	@Override
	public boolean hasFocusable() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.hasFocusable();
		}
		return this.target.hasFocusable();
	}

	@Override
	public boolean hasOnClickListeners() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.hasOnClickListeners();
		}
		return this.target.hasOnClickListeners();
	}

	@Override
	public boolean hasOverlappingRendering() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.hasOverlappingRendering();
		}
		return this.target.hasOverlappingRendering();
	}

	@Override
	@ExportedProperty(category = "layout")
	public boolean hasTransientState() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.hasTransientState();
		}
		return this.target.hasTransientState();
	}

	@Override
	public boolean hasWindowFocus() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.hasWindowFocus();
		}
		return this.target.hasWindowFocus();
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.invalidate();
	}

	@Override
	public void invalidate(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.invalidate(l, t, r, b);
	}

	@Override
	public void invalidate(Rect dirty) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.invalidate(dirty);
	}

	@Override
	public void invalidateDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.invalidateDrawable(drawable);
	}

	@Override
	@ExportedProperty
	public boolean isActivated() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isActivated();
		}
		return this.target.isActivated();
	}

	@Override
	public boolean isAttachedToWindow() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isAttachedToWindow();
		}
		return this.target.isAttachedToWindow();
	}

	@Override
	@ExportedProperty
	public boolean isClickable() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isClickable();
		}
		return this.target.isClickable();
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isDirty();
		}
		return this.target.isDirty();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public boolean isDrawingCacheEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isDrawingCacheEnabled();
		}
		return this.target.isDrawingCacheEnabled();
	}

	@Override
	public boolean isDuplicateParentStateEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isDuplicateParentStateEnabled();
		}
		return this.target.isDuplicateParentStateEnabled();
	}

	@Override
	@ExportedProperty
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isEnabled();
		}
		return this.target.isEnabled();
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isFocused();
		}
		return this.target.isFocused();
	}

	@Override
	@ExportedProperty
	public boolean isHapticFeedbackEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isHapticFeedbackEnabled();
		}
		return this.target.isHapticFeedbackEnabled();
	}

	@Override
	public boolean isHardwareAccelerated() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isHardwareAccelerated();
		}
		return this.target.isHardwareAccelerated();
	}

	@Override
	public boolean isHorizontalFadingEdgeEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isHorizontalFadingEdgeEnabled();
		}
		return this.target.isHorizontalFadingEdgeEnabled();
	}

	@Override
	public boolean isHorizontalScrollBarEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isHorizontalScrollBarEnabled();
		}
		return this.target.isHorizontalScrollBarEnabled();
	}

	@Override
	@ExportedProperty
	public boolean isHovered() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isHovered();
		}
		return this.target.isHovered();
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isInLayout();
		}
		return this.target.isInEditMode();
	}

	@Override
	public boolean isInLayout() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isInLayout();
		}
		return this.target.isInLayout();
	}

	@Override
	@ExportedProperty
	public boolean isInTouchMode() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isInTouchMode();
		}
		return this.target.isInTouchMode();
	}

	@Override
	public boolean isLaidOut() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isLaidOut();
		}
		return this.target.isLaidOut();
	}

	@Override
	public boolean isLayoutDirectionResolved() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isLayoutDirectionResolved();
		}
		return this.target.isLayoutDirectionResolved();
	}

	@Override
	public boolean isLayoutRequested() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isLayoutRequested();
		}
		return this.target.isLayoutRequested();
	}

	@Override
	public boolean isLongClickable() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isLongClickable();
		}
		return this.target.isLongClickable();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public boolean isOpaque() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isOpaque();
		}
		return this.target.isOpaque();
	}

	@Override
	public boolean isPaddingRelative() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isPaddingRelative();
		}
		return this.target.isPaddingRelative();
	}

	@Override
	public boolean isPressed() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isPressed();
		}
		return this.target.isPressed();
	}

	@Override
	public boolean isSaveEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isSaveEnabled();
		}
		return this.target.isSaveEnabled();
	}

	@Override
	public boolean isSaveFromParentEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isSaveFromParentEnabled();
		}
		return this.target.isSaveFromParentEnabled();
	}

	@Override
	public boolean isScrollContainer() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isScrollContainer();
		}
		return this.target.isScrollContainer();
	}

	@Override
	public boolean isScrollbarFadingEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isScrollbarFadingEnabled();
		}
		return this.target.isScrollbarFadingEnabled();
	}

	@Override
	@ExportedProperty
	public boolean isSelected() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isSelected();
		}
		return this.target.isSelected();
	}

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isShown();
		}
		return this.target.isShown();
	}

	@Override
	@ExportedProperty
	public boolean isSoundEffectsEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isSoundEffectsEnabled();
		}
		return this.target.isSoundEffectsEnabled();
	}

	@Override
	public boolean isTextAlignmentResolved() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isTextAlignmentResolved();
		}
		return this.target.isTextAlignmentResolved();
	}

	@Override
	public boolean isTextDirectionResolved() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isTextDirectionResolved();
		}
		return this.target.isTextDirectionResolved();
	}

	@Override
	public boolean isVerticalFadingEdgeEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isVerticalFadingEdgeEnabled();
		}
		return this.target.isVerticalFadingEdgeEnabled();
	}

	@Override
	public boolean isVerticalScrollBarEnabled() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.isVerticalScrollBarEnabled();
		}
		return this.target.isVerticalScrollBarEnabled();
	}

	@Override
	public void jumpDrawablesToCurrentState() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.jumpDrawablesToCurrentState();
	}

	@Override
	public void layout(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.layout(l, t, r, b);
	}

	@Override
	public void offsetLeftAndRight(int offset) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.offsetLeftAndRight(offset);
	}

	@Override
	public void offsetTopAndBottom(int offset) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.offsetTopAndBottom(offset);
	}

	@Override
	public WindowInsets onApplyWindowInsets(WindowInsets insets) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onApplyWindowInsets(insets);
		}
		return this.target.onApplyWindowInsets(insets);
	}

	@Override
	public void onCancelPendingInputEvents() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onCancelPendingInputEvents();
	}

	@Override
	public boolean onCheckIsTextEditor() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onCheckIsTextEditor();
		}
		return this.target.onCheckIsTextEditor();
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onCreateInputConnection(outAttrs);
		}
		return this.target.onCreateInputConnection(outAttrs);
	}

	@Override
	public boolean onDragEvent(DragEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onDragEvent(event);
		}
		return this.target.onDragEvent(event);
	}

	@Override
	public boolean onFilterTouchEventForSecurity(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onFilterTouchEventForSecurity(event);
		}
		return this.target.onFilterTouchEventForSecurity(event);
	}

	@Override
	public void onFinishTemporaryDetach() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onFinishTemporaryDetach();
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onGenericMotionEvent(event);
		}
		return this.target.onGenericMotionEvent(event);
	}

	@Override
	public void onHoverChanged(boolean hovered) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onHoverChanged(hovered);
	}

	@Override
	public boolean onHoverEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onHoverEvent(event);
		}
		return this.target.onHoverEvent(event);
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onInitializeAccessibilityEvent(event);
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onInitializeAccessibilityNodeInfo(info);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onKeyDown(keyCode, event);
		}
		return this.target.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onKeyLongPress(keyCode, event);
		}
		return this.target.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onKeyMultiple(keyCode, repeatCount, event);
		}
		return this.target.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onKeyPreIme(keyCode, event);
		}
		return this.target.onKeyPreIme(keyCode, event);
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onKeyShortcut(keyCode, event);
		}
		return this.target.onKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onKeyUp(keyCode, event);
		}
		return this.target.onKeyUp(keyCode, event);
	}

	@Override
	public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onPopulateAccessibilityEvent(event);
	}

	@Override
	public void onRtlPropertiesChanged(int layoutDirection) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onRtlPropertiesChanged(layoutDirection);
	}

	@Override
	public void onScreenStateChanged(int screenState) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onScreenStateChanged(screenState);
	}

	@Override
	public void onStartTemporaryDetach() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onStartTemporaryDetach();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onTouchEvent(event);
		}
		return this.target.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.onTrackballEvent(event);
		}
		return this.target.onTrackballEvent(event);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	public void onWindowSystemUiVisibilityChanged(int visible) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.onWindowSystemUiVisibilityChanged(visible);
	}

	@Override
	public boolean performAccessibilityAction(int action, Bundle arguments) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.performAccessibilityAction(action, arguments);
		}
		return this.target.performAccessibilityAction(action, arguments);
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.performClick();
		}
		return this.target.performClick();
	}

	@Override
	public boolean performHapticFeedback(int feedbackConstant, int flags) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.performHapticFeedback(feedbackConstant, flags);
		}
		return this.target.performHapticFeedback(feedbackConstant, flags);
	}

	@Override
	public boolean performHapticFeedback(int feedbackConstant) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.performHapticFeedback(feedbackConstant);
		}
		return this.target.performHapticFeedback(feedbackConstant);
	}

	@Override
	public boolean performLongClick() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.performLongClick();
		}
		return this.target.performLongClick();
	}

	@Override
	public void playSoundEffect(int soundConstant) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.playSoundEffect(soundConstant);
	}

	@Override
	public boolean post(Runnable action) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.post(action);
		}
		return this.target.post(action);
	}

	@Override
	public boolean postDelayed(Runnable action, long delayMillis) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.postDelayed(action, delayMillis);
		}
		return this.target.postDelayed(action, delayMillis);
	}

	@Override
	public void postInvalidate() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postInvalidate();
	}

	@Override
	public void postInvalidate(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postInvalidate(left, top, right, bottom);
	}

	@Override
	public void postInvalidateDelayed(long delayMilliseconds, int left,
			int top, int right, int bottom) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
	}

	@Override
	public void postInvalidateDelayed(long delayMilliseconds) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postInvalidateDelayed(delayMilliseconds);
	}

	@Override
	public void postInvalidateOnAnimation() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postInvalidateOnAnimation();
	}

	@Override
	public void postInvalidateOnAnimation(int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postInvalidateOnAnimation(left, top, right, bottom);
	}

	@Override
	public void postOnAnimation(Runnable action) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postOnAnimation(action);
	}

	@Override
	public void postOnAnimationDelayed(Runnable action, long delayMillis) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.postOnAnimationDelayed(action, delayMillis);
	}

	@Override
	public void refreshDrawableState() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.refreshDrawableState();
	}

	@Override
	public boolean removeCallbacks(Runnable action) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.removeCallbacks(action);
		}
		return this.target.removeCallbacks(action);
	}

	@Override
	public void removeOnAttachStateChangeListener(
			OnAttachStateChangeListener listener) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.removeOnAttachStateChangeListener(listener);
	}

	@Override
	public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.removeOnLayoutChangeListener(listener);
	}

	@Override
	public void requestApplyInsets() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.requestApplyInsets();
	}

	@Override
	@Deprecated
	public void requestFitSystemWindows() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.requestFitSystemWindows();
	}

	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.requestFocus(direction, previouslyFocusedRect);
		}
		return this.target.requestFocus(direction, previouslyFocusedRect);
	}

	@Override
	public void requestLayout() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.requestLayout();
	}

	@Override
	public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.requestRectangleOnScreen(rectangle, immediate);
		}
		return this.target.requestRectangleOnScreen(rectangle, immediate);
	}

	@Override
	public boolean requestRectangleOnScreen(Rect rectangle) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.requestRectangleOnScreen(rectangle);
		}
		return this.target.requestRectangleOnScreen(rectangle);
	}

	@Override
	public void restoreHierarchyState(SparseArray<Parcelable> container) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.restoreHierarchyState(container);
	}

	@Override
	public void saveHierarchyState(SparseArray<Parcelable> container) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.saveHierarchyState(container);
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.scheduleDrawable(who, what, when);
	}

	@Override
	public void scrollBy(int x, int y) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.scrollBy(x, y);
	}

	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.scrollTo(x, y);
	}

	@Override
	public void sendAccessibilityEvent(int eventType) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.sendAccessibilityEvent(eventType);
	}

	@Override
	public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.sendAccessibilityEventUnchecked(event);
	}

	@Override
	public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setAccessibilityDelegate(delegate);
	}

	@Override
	public void setAccessibilityLiveRegion(int mode) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setAccessibilityLiveRegion(mode);
	}

	@Override
	public void setActivated(boolean activated) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setActivated(activated);
	}

	@Override
	public void setAlpha(float alpha) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setAlpha(alpha);
	}

	@Override
	public void setAnimation(Animation animation) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setAnimation(animation);
	}

	@Override
	public void setBackground(Drawable background) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setBackground(background);
	}

	@Override
	public void setBackgroundColor(int color) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setBackgroundColor(color);
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setBackgroundDrawable(background);
	}

	@Override
	public void setBackgroundResource(int resid) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setBackgroundResource(resid);
	}

	@Override
	public void setCameraDistance(float distance) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setCameraDistance(distance);
	}

	@Override
	public void setClickable(boolean clickable) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setClickable(clickable);
	}

	@Override
	public void setClipBounds(Rect clipBounds) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setClipBounds(clipBounds);
	}

	@Override
	public void setContentDescription(CharSequence contentDescription) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setContentDescription(contentDescription);
	}

	@Override
	public void setDrawingCacheBackgroundColor(int color) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setDrawingCacheBackgroundColor(color);
	}

	@Override
	public void setDrawingCacheEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setDrawingCacheEnabled(enabled);
	}

	@Override
	public void setDrawingCacheQuality(int quality) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setDrawingCacheQuality(quality);
	}

	@Override
	public void setDuplicateParentStateEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setDuplicateParentStateEnabled(enabled);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setEnabled(enabled);
	}

	@Override
	public void setFadingEdgeLength(int length) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setFadingEdgeLength(length);
	}

	@Override
	public void setFilterTouchesWhenObscured(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setFilterTouchesWhenObscured(enabled);
	}

	@Override
	public void setFitsSystemWindows(boolean fitSystemWindows) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setFitsSystemWindows(fitSystemWindows);
	}

	@Override
	public void setFocusable(boolean focusable) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setFocusable(focusable);
	}

	@Override
	public void setFocusableInTouchMode(boolean focusableInTouchMode) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setFocusableInTouchMode(focusableInTouchMode);
	}

	@Override
	public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setHapticFeedbackEnabled(hapticFeedbackEnabled);
	}

	@Override
	public void setHasTransientState(boolean hasTransientState) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setHasTransientState(hasTransientState);
	}

	@Override
	public void setHorizontalFadingEdgeEnabled(
			boolean horizontalFadingEdgeEnabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
	}

	@Override
	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}

	@Override
	public void setHovered(boolean hovered) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setHovered(hovered);
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setId(id);
	}

	@Override
	public void setImportantForAccessibility(int mode) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setImportantForAccessibility(mode);
	}

	@Override
	public void setKeepScreenOn(boolean keepScreenOn) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setKeepScreenOn(keepScreenOn);
	}

	@Override
	public void setLabelFor(int id) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setLabelFor(id);
	}

	@Override
	public void setLayerPaint(Paint paint) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setLayerPaint(paint);
	}

	@Override
	public void setLayerType(int layerType, Paint paint) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setLayerType(layerType, paint);
	}

	@Override
	public void setLayoutDirection(int layoutDirection) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setLayoutDirection(layoutDirection);
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setLayoutParams(params);
	}

	@Override
	public void setLongClickable(boolean longClickable) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setLongClickable(longClickable);
	}

	@Override
	public void setMinimumHeight(int minHeight) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setMinimumHeight(minHeight);
	}

	@Override
	public void setMinimumWidth(int minWidth) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setMinimumWidth(minWidth);
	}

	@Override
	public void setNextFocusDownId(int nextFocusDownId) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setNextFocusDownId(nextFocusDownId);
	}

	@Override
	public void setNextFocusForwardId(int nextFocusForwardId) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setNextFocusForwardId(nextFocusForwardId);
	}

	@Override
	public void setNextFocusLeftId(int nextFocusLeftId) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setNextFocusLeftId(nextFocusLeftId);
	}

	@Override
	public void setNextFocusRightId(int nextFocusRightId) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setNextFocusRightId(nextFocusRightId);
	}

	@Override
	public void setNextFocusUpId(int nextFocusUpId) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setNextFocusUpId(nextFocusUpId);
	}

	@Override
	public void setOnApplyWindowInsetsListener(
			OnApplyWindowInsetsListener listener) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnApplyWindowInsetsListener(listener);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnClickListener(l);
	}

	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnCreateContextMenuListener(l);
	}

	@Override
	public void setOnDragListener(OnDragListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnDragListener(l);
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnFocusChangeListener(l);
	}

	@Override
	public void setOnGenericMotionListener(OnGenericMotionListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnGenericMotionListener(l);
	}

	@Override
	public void setOnHoverListener(OnHoverListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnHoverListener(l);
	}

	@Override
	public void setOnKeyListener(OnKeyListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnKeyListener(l);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnLongClickListener(l);
	}

	@Override
	public void setOnSystemUiVisibilityChangeListener(
			OnSystemUiVisibilityChangeListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnSystemUiVisibilityChangeListener(l);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOnTouchListener(l);
	}

	@Override
	public void setOverScrollMode(int overScrollMode) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setOverScrollMode(overScrollMode);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setPadding(left, top, right, bottom);
	}

	@Override
	public void setPaddingRelative(int start, int top, int end, int bottom) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setPaddingRelative(start, top, end, bottom);
	}

	@Override
	public void setPivotX(float pivotX) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setPivotX(pivotX);
	}

	@Override
	public void setPivotY(float pivotY) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setPivotY(pivotY);
	}

	@Override
	public void setPressed(boolean pressed) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setPressed(pressed);
	}

	@Override
	public void setRotation(float rotation) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setRotation(rotation);
	}

	@Override
	public void setRotationX(float rotationX) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setRotationX(rotationX);
	}

	@Override
	public void setRotationY(float rotationY) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setRotationY(rotationY);
	}

	@Override
	public void setSaveEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setSaveEnabled(enabled);
	}

	@Override
	public void setSaveFromParentEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setSaveFromParentEnabled(enabled);
	}

	@Override
	public void setScaleX(float scaleX) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScaleX(scaleX);
	}

	@Override
	public void setScaleY(float scaleY) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScaleY(scaleY);
	}

	@Override
	public void setScrollBarDefaultDelayBeforeFade(
			int scrollBarDefaultDelayBeforeFade) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
	}

	@Override
	public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollBarFadeDuration(scrollBarFadeDuration);
	}

	@Override
	public void setScrollBarSize(int scrollBarSize) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollBarSize(scrollBarSize);
	}

	@Override
	public void setScrollBarStyle(int style) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollBarStyle(style);
	}

	@Override
	public void setScrollContainer(boolean isScrollContainer) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollContainer(isScrollContainer);
	}

	@Override
	public void setScrollX(int value) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollX(value);
	}

	@Override
	public void setScrollY(int value) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollY(value);
	}

	@Override
	public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setScrollbarFadingEnabled(fadeScrollbars);
	}

	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setSelected(selected);
	}

	@Override
	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setSoundEffectsEnabled(soundEffectsEnabled);
	}

	@Override
	public void setSystemUiVisibility(int visibility) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setSystemUiVisibility(visibility);
	}

	@Override
	public void setTag(int key, Object tag) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTag(key, tag);
	}

	@Override
	public void setTag(Object tag) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTag(tag);
	}

	@Override
	public void setTextAlignment(int textAlignment) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTextAlignment(textAlignment);
	}

	@Override
	public void setTextDirection(int textDirection) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTextDirection(textDirection);
	}

	@Override
	public void setTouchDelegate(TouchDelegate delegate) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTouchDelegate(delegate);
	}

	@Override
	public void setTranslationX(float translationX) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTranslationX(translationX);
	}

	@Override
	public void setTranslationY(float translationY) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setTranslationY(translationY);
	}

	@Override
	public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
	}

	@Override
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}

	@Override
	public void setVerticalScrollbarPosition(int position) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setVerticalScrollbarPosition(position);
	}

	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setVisibility(visibility);
	}

	@Override
	public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setWillNotCacheDrawing(willNotCacheDrawing);
	}

	@Override
	public void setWillNotDraw(boolean willNotDraw) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setWillNotDraw(willNotDraw);
	}

	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setX(x);
	}

	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.setY(y);
	}

	@Override
	public boolean showContextMenu() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.showContextMenu();
		}
		return this.target.showContextMenu();
	}

	@Override
	public ActionMode startActionMode(Callback callback) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.startActionMode(callback);
		}
		return this.target.startActionMode(callback);
	}

	@Override
	public void startAnimation(Animation animation) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.startAnimation(animation);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.toString();
		}
		return this.target.toString();
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.unscheduleDrawable(who, what);
	}

	@Override
	public void unscheduleDrawable(Drawable who) {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return;
		}
		this.target.unscheduleDrawable(who);
	}

	@Override
	@ExportedProperty(category = "drawing")
	public boolean willNotCacheDrawing() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.willNotCacheDrawing();
		}
		return this.target.willNotCacheDrawing();
	}

	@Override
	@ExportedProperty(category = "drawing")
	public boolean willNotDraw() {
		// TODO Auto-generated method stub
		if (this.target == null) {
			return super.willNotDraw();
		}
		return this.target.willNotDraw();
	}

}
