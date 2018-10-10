package com.andframe.impl.wrapper;

import android.annotation.SuppressLint;
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
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

@SuppressLint({"NewApi", "deprecation", "ViewConstructor"})
public class ViewWrapper extends View {

	protected View wrapped;

	public ViewWrapper(View view) {
		super(view.getContext());
		this.wrapped = view;
	}

	public View getView() {
		return wrapped;
	}

	@Override
	public void addChildrenForAccessibility(ArrayList<View> children) {
		if (this.wrapped == null) {
			super.addChildrenForAccessibility(children);
		}
		this.wrapped.addChildrenForAccessibility(children);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.addFocusables(views, direction, focusableMode);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.addFocusables(views, direction);
	}

	@Override
	public void addOnAttachStateChangeListener(
			OnAttachStateChangeListener listener) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.addOnAttachStateChangeListener(listener);
	}

	@Override
	public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.addOnLayoutChangeListener(listener);
	}

	@Override
	public void addTouchables(ArrayList<View> views) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.addTouchables(views);
	}

	@Override
	public ViewPropertyAnimator animate() {
		if (this.wrapped == null) {
			return super.animate();
		}
		return this.wrapped.animate();
	}

	@Override
	public void announceForAccessibility(CharSequence text) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.announceForAccessibility(text);
	}

	@Override
	public void bringToFront() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.bringToFront();
	}

	@Override
	public void buildDrawingCache() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.buildDrawingCache();
	}

	@Override
	public void buildDrawingCache(boolean autoScale) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.buildDrawingCache(autoScale);
	}

	@Override
	public void buildLayer() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.buildLayer();
	}

	@Override
	public boolean callOnClick() {
		if (this.wrapped == null) {
			return super.callOnClick();
		}
		return this.wrapped.callOnClick();
	}

	@Override
	public boolean canResolveLayoutDirection() {
		if (this.wrapped == null) {
			return super.canResolveLayoutDirection();
		}
		return this.wrapped.canResolveLayoutDirection();
	}

	@Override
	public boolean canResolveTextAlignment() {
		if (this.wrapped == null) {
			return super.canResolveTextAlignment();
		}
		return this.wrapped.canResolveTextAlignment();
	}

	@Override
	public boolean canResolveTextDirection() {
		if (this.wrapped == null) {
			return super.canResolveTextDirection();
		}
		return this.wrapped.canResolveTextDirection();
	}

	@Override
	public boolean canScrollHorizontally(int direction) {
		if (this.wrapped == null) {
			return super.canScrollHorizontally(direction);
		}
		return this.wrapped.canScrollHorizontally(direction);
	}

	@Override
	public boolean canScrollVertically(int direction) {
		if (this.wrapped == null) {
			return super.canScrollVertically(direction);
		}
		return this.wrapped.canScrollVertically(direction);
	}

	@Override
	public void cancelLongPress() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.cancelLongPress();
	}

	@Override
	public boolean checkInputConnectionProxy(View view) {
		if (this.wrapped == null) {
			return super.checkInputConnectionProxy(view);
		}
		return this.wrapped.checkInputConnectionProxy(view);
	}

	@Override
	public void clearAnimation() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.clearAnimation();
	}

	@Override
	public void clearFocus() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.clearFocus();
	}

	@Override
	public void computeScroll() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.computeScroll();
	}

	@Override
	public AccessibilityNodeInfo createAccessibilityNodeInfo() {
		if (this.wrapped == null) {
			return super.createAccessibilityNodeInfo();
		}
		return this.wrapped.createAccessibilityNodeInfo();
	}

	@Override
	public void createContextMenu(ContextMenu menu) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.createContextMenu(menu);
	}

	@Override
	public void destroyDrawingCache() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.destroyDrawingCache();
	}

	@Override
	public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
		if (this.wrapped == null) {
			return super.dispatchApplyWindowInsets(insets);
		}
		return this.wrapped.dispatchApplyWindowInsets(insets);
	}

	@Override
	public void dispatchConfigurationChanged(Configuration newConfig) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.dispatchConfigurationChanged(newConfig);
	}

	@Override
	public void dispatchDisplayHint(int hint) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.dispatchDisplayHint(hint);
	}

	@Override
	public boolean dispatchDragEvent(DragEvent event) {
		if (this.wrapped == null) {
			return super.dispatchDragEvent(event);
		}
		return this.wrapped.dispatchDragEvent(event);
	}

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.dispatchGenericMotionEvent(event);
		}
		return this.wrapped.dispatchGenericMotionEvent(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (this.wrapped == null) {
			return super.dispatchKeyEvent(event);
		}
		return this.wrapped.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if (this.wrapped == null) {
			return super.dispatchKeyEventPreIme(event);
		}
		return this.wrapped.dispatchKeyEventPreIme(event);
	}

	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		if (this.wrapped == null) {
			return super.dispatchKeyShortcutEvent(event);
		}
		return this.wrapped.dispatchKeyShortcutEvent(event);
	}

	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		if (this.wrapped == null) {
			return super.dispatchPopulateAccessibilityEvent(event);
		}
		return this.wrapped.dispatchPopulateAccessibilityEvent(event);
	}

	@Override
	public void dispatchSystemUiVisibilityChanged(int visibility) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.dispatchSystemUiVisibilityChanged(visibility);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.dispatchTrackballEvent(event);
		}
		return this.wrapped.dispatchTouchEvent(event);
	}

	@Override
	public boolean dispatchTrackballEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.dispatchTrackballEvent(event);
		}
		return this.wrapped.dispatchTrackballEvent(event);
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (this.wrapped == null) {
			return super.dispatchUnhandledMove(focused, direction);
		}
		return this.wrapped.dispatchUnhandledMove(focused, direction);
	}

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.dispatchWindowFocusChanged(hasFocus);
	}

	@Override
	public void dispatchWindowSystemUiVisiblityChanged(int visible) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.dispatchWindowSystemUiVisiblityChanged(visible);
	}

	@Override
	public void dispatchWindowVisibilityChanged(int visibility) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.dispatchWindowVisibilityChanged(visibility);
	}

	@Override
	public void draw(Canvas canvas) {
		if (this.wrapped == null) {
			super.draw(canvas);
			return;
		}
		this.wrapped.draw(canvas);
	}

	@Override
	public View findFocus() {
		if (this.wrapped == null) {
			return super.findFocus();
		}
		return this.wrapped.findFocus();
	}

	
	
	@Override
	public void findViewsWithText(ArrayList<View> outViews,
			CharSequence searched, int flags) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.findViewsWithText(outViews, searched, flags);
	}

	@Override
	public View focusSearch(int direction) {
		if (this.wrapped == null) {
			return super.focusSearch(direction);
		}
		return this.wrapped.focusSearch(direction);
	}

	@Override
	public void forceLayout() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.forceLayout();
	}

	@Override
	public int getAccessibilityLiveRegion() {
		if (this.wrapped == null) {
			return super.getAccessibilityLiveRegion();
		}
		return this.wrapped.getAccessibilityLiveRegion();
	}

	@Override
	public AccessibilityNodeProvider getAccessibilityNodeProvider() {
		if (this.wrapped == null) {
			return super.getAccessibilityNodeProvider();
		}
		return this.wrapped.getAccessibilityNodeProvider();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getAlpha() {
		if (this.wrapped == null) {
			return super.getAlpha();
		}
		return this.wrapped.getAlpha();
	}

	@Override
	public Animation getAnimation() {
		if (this.wrapped == null) {
			return super.getAnimation();
		}
		return this.wrapped.getAnimation();
	}

	@Override
	public IBinder getApplicationWindowToken() {
		if (this.wrapped == null) {
			return super.getApplicationWindowToken();
		}
		return this.wrapped.getApplicationWindowToken();
	}

	@Override
	public Drawable getBackground() {
		if (this.wrapped == null) {
			return super.getBackground();
		}
		return this.wrapped.getBackground();
	}

	@Override
	//@ExportedProperty(category = "layout")
	public int getBaseline() {
		if (this.wrapped == null) {
			return super.getBaseline();
		}
		return this.wrapped.getBaseline();
	}

	@Override
	public float getCameraDistance() {
		if (this.wrapped == null) {
			return super.getCameraDistance();
		}
		return this.wrapped.getCameraDistance();
	}

	@Override
	public Rect getClipBounds() {
		if (this.wrapped == null) {
			return super.getClipBounds();
		}
		return this.wrapped.getClipBounds();
	}

	@Override
	//@ExportedProperty(category = "accessibility")
	public CharSequence getContentDescription() {
		if (this.wrapped == null) {
			return super.getContentDescription();
		}
		return this.wrapped.getContentDescription();
	}

	@Override
	public Display getDisplay() {
		if (this.wrapped == null) {
			return super.getDisplay();
		}
		return this.wrapped.getDisplay();
	}

	@Override
	public Bitmap getDrawingCache() {
		if (this.wrapped == null) {
			return super.getDrawingCache();
		}
		return this.wrapped.getDrawingCache();
	}

	@Override
	public Bitmap getDrawingCache(boolean autoScale) {
		if (this.wrapped == null) {
			return super.getDrawingCache(autoScale);
		}
		return this.wrapped.getDrawingCache(autoScale);
	}

	@Override
	public int getDrawingCacheBackgroundColor() {
		if (this.wrapped == null) {
			return super.getDrawingCacheBackgroundColor();
		}
		return this.wrapped.getDrawingCacheBackgroundColor();
	}

	@Override
	public int getDrawingCacheQuality() {
		if (this.wrapped == null) {
			return super.getDrawingCacheQuality();
		}
		return this.wrapped.getDrawingCacheQuality();
	}

	@Override
	public void getDrawingRect(Rect outRect) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.getDrawingRect(outRect);
	}

	@Override
	public long getDrawingTime() {
		if (this.wrapped == null) {
			return super.getDrawingTime();
		}
		return this.wrapped.getDrawingTime();
	}

	@Override
	//@ExportedProperty
	public boolean getFilterTouchesWhenObscured() {
		if (this.wrapped == null) {
			return super.getFilterTouchesWhenObscured();
		}
		return this.wrapped.getFilterTouchesWhenObscured();
	}

	@Override
	public boolean getFitsSystemWindows() {
		if (this.wrapped == null) {
			return super.getFitsSystemWindows();
		}
		return this.wrapped.getFitsSystemWindows();
	}

	@Override
	public ArrayList<View> getFocusables(int direction) {
		if (this.wrapped == null) {
			return super.getFocusables(direction);
		}
		return this.wrapped.getFocusables(direction);
	}

	@Override
	public void getFocusedRect(Rect r) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.getFocusedRect(r);
	}

	@Override
	public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
		if (this.wrapped == null) {
			return super.getGlobalVisibleRect(r, globalOffset);
		}
		return this.wrapped.getGlobalVisibleRect(r, globalOffset);
	}

	@Override
	public Handler getHandler() {
		if (this.wrapped == null) {
			return super.getHandler();
		}
		return this.wrapped.getHandler();
	}

	@Override
	public void getHitRect(Rect outRect) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.getHitRect(outRect);
	}

	@Override
	public int getHorizontalFadingEdgeLength() {
		if (this.wrapped == null) {
			return super.getHorizontalFadingEdgeLength();
		}
		return this.wrapped.getHorizontalFadingEdgeLength();
	}

	@Override
	//@CapturedViewProperty
	public int getId() {
		if (this.wrapped == null) {
			return super.getId();
		}
		return this.wrapped.getId();
	}

	@Override
	//@ExportedProperty(category = "accessibility", mapping = {
			//@IntToString(from = 0, to = "auto"),
			//@IntToString(from = 1, to = "yes"),
			//@IntToString(from = 2, to = "no"),
			//@IntToString(from = 4, to = "noHideDescendants") })
	public int getImportantForAccessibility() {
		if (this.wrapped == null) {
			return super.getImportantForAccessibility();
		}
		return this.wrapped.getImportantForAccessibility();
	}

	@Override
	public boolean getKeepScreenOn() {
		if (this.wrapped == null) {
			return super.getKeepScreenOn();
		}
		return this.wrapped.getKeepScreenOn();
	}

	@Override
	public DispatcherState getKeyDispatcherState() {
		if (this.wrapped == null) {
			return super.getKeyDispatcherState();
		}
		return this.wrapped.getKeyDispatcherState();
	}

	@Override
	//@ExportedProperty(category = "accessibility")
	public int getLabelFor() {
		if (this.wrapped == null) {
			return super.getLabelFor();
		}
		return this.wrapped.getLabelFor();
	}

	@Override
	public int getLayerType() {
		if (this.wrapped == null) {
			return super.getLayerType();
		}
		return this.wrapped.getLayerType();
	}

	@Override
	//@ExportedProperty(category = "layout", mapping = {
			//@IntToString(from = 0, to = "RESOLVED_DIRECTION_LTR"),
			//@IntToString(from = 1, to = "RESOLVED_DIRECTION_RTL") })
	public int getLayoutDirection() {
		if (this.wrapped == null) {
			return super.getLayoutDirection();
		}
		return this.wrapped.getLayoutDirection();
	}

	@Override
	//@ExportedProperty(deepExport = true, prefix = "layout_")
	public LayoutParams getLayoutParams() {
		if (this.wrapped == null) {
			return super.getLayoutParams();
		}
		return this.wrapped.getLayoutParams();
	}

	@Override
	public void getLocationInWindow(int[] location) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.getLocationInWindow(location);
	}

	@Override
	public void getLocationOnScreen(int[] location) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.getLocationOnScreen(location);
	}

	@Override
	public Matrix getMatrix() {
		if (this.wrapped == null) {
			return super.getMatrix();
		}
		return this.wrapped.getMatrix();
	}

	@Override
	public int getMinimumHeight() {
		if (this.wrapped == null) {
			return super.getMinimumHeight();
		}
		return this.wrapped.getMinimumHeight();
	}

	@Override
	public int getMinimumWidth() {
		if (this.wrapped == null) {
			return super.getMinimumWidth();
		}
		return this.wrapped.getMinimumWidth();
	}

	@Override
	public int getNextFocusDownId() {
		if (this.wrapped == null) {
			return super.getNextFocusDownId();
		}
		return this.wrapped.getNextFocusDownId();
	}

	@Override
	public int getNextFocusForwardId() {
		if (this.wrapped == null) {
			return super.getNextFocusForwardId();
		}
		return this.wrapped.getNextFocusForwardId();
	}

	@Override
	public int getNextFocusLeftId() {
		if (this.wrapped == null) {
			return super.getNextFocusLeftId();
		}
		return this.wrapped.getNextFocusLeftId();
	}

	@Override
	public int getNextFocusRightId() {
		if (this.wrapped == null) {
			return super.getNextFocusRightId();
		}
		return this.wrapped.getNextFocusRightId();
	}

	@Override
	public int getNextFocusUpId() {
		if (this.wrapped == null) {
			return super.getNextFocusUpId();
		}
		return this.wrapped.getNextFocusUpId();
	}

	@Override
	public OnFocusChangeListener getOnFocusChangeListener() {
		if (this.wrapped == null) {
			return super.getOnFocusChangeListener();
		}
		return this.wrapped.getOnFocusChangeListener();
	}

	@Override
	public int getOverScrollMode() {
		if (this.wrapped == null) {
			return super.getOverScrollMode();
		}
		return this.wrapped.getOverScrollMode();
	}

	@Override
	public ViewOverlay getOverlay() {
		if (this.wrapped == null) {
			return super.getOverlay();
		}
		return this.wrapped.getOverlay();
	}

	@Override
	public int getPaddingBottom() {
		if (this.wrapped == null) {
			return super.getPaddingBottom();
		}
		return this.wrapped.getPaddingBottom();
	}

	@Override
	public int getPaddingEnd() {
		if (this.wrapped == null) {
			return super.getPaddingEnd();
		}
		return this.wrapped.getPaddingEnd();
	}

	@Override
	public int getPaddingLeft() {
		if (this.wrapped == null) {
			return super.getPaddingLeft();
		}
		return this.wrapped.getPaddingLeft();
	}

	@Override
	public int getPaddingRight() {
		if (this.wrapped == null) {
			return super.getPaddingRight();
		}
		return this.wrapped.getPaddingRight();
	}

	@Override
	public int getPaddingStart() {
		if (this.wrapped == null) {
			return super.getPaddingStart();
		}
		return this.wrapped.getPaddingStart();
	}

	@Override
	public int getPaddingTop() {
		if (this.wrapped == null) {
			return super.getPaddingTop();
		}
		return this.wrapped.getPaddingTop();
	}

	@Override
	public ViewParent getParentForAccessibility() {
		if (this.wrapped == null) {
			return super.getParentForAccessibility();
		}
		return this.wrapped.getParentForAccessibility();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getPivotX() {
		if (this.wrapped == null) {
			return super.getPivotX();
		}
		return this.wrapped.getPivotX();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getPivotY() {
		if (this.wrapped == null) {
			return super.getPivotY();
		}
		return this.wrapped.getPivotY();
	}

	@Override
	public Resources getResources() {
		if (this.wrapped == null) {
			return super.getResources();
		}
		return this.wrapped.getResources();
	}

	@Override
	public View getRootView() {
		if (this.wrapped == null) {
			return super.getRootView();
		}
		return this.wrapped.getRootView();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getRotation() {
		if (this.wrapped == null) {
			return super.getRotation();
		}
		return this.wrapped.getRotation();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getRotationX() {
		if (this.wrapped == null) {
			return super.getRotationX();
		}
		return this.wrapped.getRotationX();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getRotationY() {
		if (this.wrapped == null) {
			return super.getRotationY();
		}
		return this.wrapped.getRotationY();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getScaleX() {
		if (this.wrapped == null) {
			return super.getScaleY();
		}
		return this.wrapped.getScaleX();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getScaleY() {
		if (this.wrapped == null) {
			return super.getScaleY();
		}
		return this.wrapped.getScaleY();
	}

	@Override
	public int getScrollBarDefaultDelayBeforeFade() {
		if (this.wrapped == null) {
			return super.getScrollBarDefaultDelayBeforeFade();
		}
		return this.wrapped.getScrollBarDefaultDelayBeforeFade();
	}

	@Override
	public int getScrollBarFadeDuration() {
		if (this.wrapped == null) {
			return super.getScrollBarFadeDuration();
		}
		return this.wrapped.getScrollBarFadeDuration();
	}

	@Override
	public int getScrollBarSize() {
		if (this.wrapped == null) {
			return super.getScrollBarSize();
		}
		return this.wrapped.getScrollBarSize();
	}

	@Override
	//@ExportedProperty(mapping = {
			//@IntToString(from = 0, to = "INSIDE_OVERLAY"),
			//@IntToString(from = 16777216, to = "INSIDE_INSET"),
			//@IntToString(from = 33554432, to = "OUTSIDE_OVERLAY"),
			//@IntToString(from = 50331648, to = "OUTSIDE_INSET") })
	public int getScrollBarStyle() {
		if (this.wrapped == null) {
			return super.getScrollBarStyle();
		}
		return this.wrapped.getScrollBarStyle();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public int getSolidColor() {
		if (this.wrapped == null) {
			return super.getSolidColor();
		}
		return this.wrapped.getSolidColor();
	}

	@Override
	public int getSystemUiVisibility() {
		if (this.wrapped == null) {
			return super.getSystemUiVisibility();
		}
		return this.wrapped.getSystemUiVisibility();
	}

	@Override
	//@ExportedProperty
	public Object getTag() {
		if (this.wrapped == null) {
			return super.getTag();
		}
		return this.wrapped.getTag();
	}

	@Override
	public Object getTag(int key) {
		if (this.wrapped == null) {
			return super.getTag(key);
		}
		return this.wrapped.getTag(key);
	}

	@Override
	//@ExportedProperty(category = "text", mapping = {
			//@IntToString(from = 0, to = "INHERIT"),
			//@IntToString(from = 1, to = "GRAVITY"),
			//@IntToString(from = 2, to = "TEXT_START"),
			//@IntToString(from = 3, to = "TEXT_END"),
			//@IntToString(from = 4, to = "CENTER"),
			//@IntToString(from = 5, to = "VIEW_START"),
			//@IntToString(from = 6, to = "VIEW_END") })
	public int getTextAlignment() {
		if (this.wrapped == null) {
			return super.getTextAlignment();
		}
		return this.wrapped.getTextAlignment();
	}

	@Override
	//@ExportedProperty(category = "text", mapping = {
			//@IntToString(from = 0, to = "INHERIT"),
			//@IntToString(from = 1, to = "FIRST_STRONG"),
			//@IntToString(from = 2, to = "ANY_RTL"),
			//@IntToString(from = 3, to = "LTR"),
			//@IntToString(from = 4, to = "RTL"),
			//@IntToString(from = 5, to = "LOCALE") })
	public int getTextDirection() {
		if (this.wrapped == null) {
			return super.getTextDirection();
		}
		return this.wrapped.getTextDirection();
	}

	@Override
	public TouchDelegate getTouchDelegate() {
		if (this.wrapped == null) {
			return super.getTouchDelegate();
		}
		return this.wrapped.getTouchDelegate();
	}

	@Override
	public ArrayList<View> getTouchables() {
		if (this.wrapped == null) {
			return super.getTouchables();
		}
		return this.wrapped.getTouchables();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getTranslationX() {
		if (this.wrapped == null) {
			return super.getTranslationX();
		}
		return this.wrapped.getTranslationX();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getTranslationY() {
		if (this.wrapped == null) {
			return super.getTranslationY();
		}
		return this.wrapped.getTranslationY();
	}

	@Override
	public int getVerticalFadingEdgeLength() {
		if (this.wrapped == null) {
			return super.getVerticalFadingEdgeLength();
		}
		return this.wrapped.getVerticalFadingEdgeLength();
	}

	@Override
	public int getVerticalScrollbarPosition() {
		if (this.wrapped == null) {
			return super.getVerticalScrollbarPosition();
		}
		return this.wrapped.getVerticalScrollbarPosition();
	}

	@Override
	public int getVerticalScrollbarWidth() {
		if (this.wrapped == null) {
			return super.getVerticalScrollbarWidth();
		}
		return this.wrapped.getVerticalScrollbarWidth();
	}

	@Override
	public ViewTreeObserver getViewTreeObserver() {
		if (this.wrapped == null) {
			return super.getViewTreeObserver();
		}
		return this.wrapped.getViewTreeObserver();
	}

	@Override
	//@ExportedProperty(mapping = { //@IntToString(from = 0, to = "VISIBLE"),
			//@IntToString(from = 4, to = "INVISIBLE"),
			//@IntToString(from = 8, to = "GONE") })
	public int getVisibility() {
		if (this.wrapped == null) {
			return super.getVisibility();
		}
		return this.wrapped.getVisibility();
	}

	@Override
	public WindowId getWindowId() {
		if (this.wrapped == null) {
			return super.getWindowId();
		}
		return this.wrapped.getWindowId();
	}

	@Override
	public int getWindowSystemUiVisibility() {
		if (this.wrapped == null) {
			return super.getWindowSystemUiVisibility();
		}
		return this.wrapped.getWindowSystemUiVisibility();
	}

	@Override
	public IBinder getWindowToken() {
		if (this.wrapped == null) {
			return super.getWindowToken();
		}
		return this.wrapped.getWindowToken();
	}

	@Override
	public int getWindowVisibility() {
		if (this.wrapped == null) {
			return super.getWindowVisibility();
		}
		return this.wrapped.getWindowVisibility();
	}

	@Override
	public void getWindowVisibleDisplayFrame(Rect outRect) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.getWindowVisibleDisplayFrame(outRect);
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getX() {
		if (this.wrapped == null) {
			return super.getX();
		}
		return this.wrapped.getX();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public float getY() {
		if (this.wrapped == null) {
			return super.getY();
		}
		return this.wrapped.getY();
	}

	@Override
	//@ExportedProperty(category = "focus")
	public boolean hasFocus() {
		if (this.wrapped == null) {
			return super.hasFocus();
		}
		return this.wrapped.hasFocus();
	}

	@Override
	public boolean hasFocusable() {
		if (this.wrapped == null) {
			return super.hasFocusable();
		}
		return this.wrapped.hasFocusable();
	}

	@Override
	public boolean hasOnClickListeners() {
		if (this.wrapped == null) {
			return super.hasOnClickListeners();
		}
		return this.wrapped.hasOnClickListeners();
	}

	@Override
	public boolean hasOverlappingRendering() {
		if (this.wrapped == null) {
			return super.hasOverlappingRendering();
		}
		return this.wrapped.hasOverlappingRendering();
	}

	@Override
	//@ExportedProperty(category = "layout")
	public boolean hasTransientState() {
		if (this.wrapped == null) {
			return super.hasTransientState();
		}
		return this.wrapped.hasTransientState();
	}

	@Override
	public boolean hasWindowFocus() {
		if (this.wrapped == null) {
			return super.hasWindowFocus();
		}
		return this.wrapped.hasWindowFocus();
	}

	@Override
	public void invalidate() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.invalidate();
	}

	@Override
	public void invalidate(int l, int t, int r, int b) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.invalidate(l, t, r, b);
	}

	@Override
	public void invalidate(Rect dirty) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.invalidate(dirty);
	}

	@Override
	public void invalidateDrawable(@NonNull Drawable drawable) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.invalidateDrawable(drawable);
	}

	@Override
	//@ExportedProperty
	public boolean isActivated() {
		if (this.wrapped == null) {
			return super.isActivated();
		}
		return this.wrapped.isActivated();
	}

	@Override
	public boolean isAttachedToWindow() {
		if (this.wrapped == null) {
			return super.isAttachedToWindow();
		}
		return this.wrapped.isAttachedToWindow();
	}

	@Override
	//@ExportedProperty
	public boolean isClickable() {
		if (this.wrapped == null) {
			return super.isClickable();
		}
		return this.wrapped.isClickable();
	}

	@Override
	public boolean isDirty() {
		if (this.wrapped == null) {
			return super.isDirty();
		}
		return this.wrapped.isDirty();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public boolean isDrawingCacheEnabled() {
		if (this.wrapped == null) {
			return super.isDrawingCacheEnabled();
		}
		return this.wrapped.isDrawingCacheEnabled();
	}

	@Override
	public boolean isDuplicateParentStateEnabled() {
		if (this.wrapped == null) {
			return super.isDuplicateParentStateEnabled();
		}
		return this.wrapped.isDuplicateParentStateEnabled();
	}

	@Override
	//@ExportedProperty
	public boolean isEnabled() {
		if (this.wrapped == null) {
			return super.isEnabled();
		}
		return this.wrapped.isEnabled();
	}

	@Override
	//@ExportedProperty(category = "focus")
	public boolean isFocused() {
		if (this.wrapped == null) {
			return super.isFocused();
		}
		return this.wrapped.isFocused();
	}

	@Override
	//@ExportedProperty
	public boolean isHapticFeedbackEnabled() {
		if (this.wrapped == null) {
			return super.isHapticFeedbackEnabled();
		}
		return this.wrapped.isHapticFeedbackEnabled();
	}

	@Override
	public boolean isHardwareAccelerated() {
		if (this.wrapped == null) {
			return super.isHardwareAccelerated();
		}
		return this.wrapped.isHardwareAccelerated();
	}

	@Override
	public boolean isHorizontalFadingEdgeEnabled() {
		if (this.wrapped == null) {
			return super.isHorizontalFadingEdgeEnabled();
		}
		return this.wrapped.isHorizontalFadingEdgeEnabled();
	}

	@Override
	public boolean isHorizontalScrollBarEnabled() {
		if (this.wrapped == null) {
			return super.isHorizontalScrollBarEnabled();
		}
		return this.wrapped.isHorizontalScrollBarEnabled();
	}

	@Override
	//@ExportedProperty
	public boolean isHovered() {
		if (this.wrapped == null) {
			return super.isHovered();
		}
		return this.wrapped.isHovered();
	}

	@Override
	public boolean isInEditMode() {
		if (this.wrapped == null) {
			return super.isInLayout();
		}
		return this.wrapped.isInEditMode();
	}

	@Override
	public boolean isInLayout() {
		if (this.wrapped == null) {
			return super.isInLayout();
		}
		return this.wrapped.isInLayout();
	}

	@Override
	//@ExportedProperty
	public boolean isInTouchMode() {
		if (this.wrapped == null) {
			return super.isInTouchMode();
		}
		return this.wrapped.isInTouchMode();
	}

	@Override
	public boolean isLaidOut() {
		if (this.wrapped == null) {
			return super.isLaidOut();
		}
		return this.wrapped.isLaidOut();
	}

	@Override
	public boolean isLayoutDirectionResolved() {
		if (this.wrapped == null) {
			return super.isLayoutDirectionResolved();
		}
		return this.wrapped.isLayoutDirectionResolved();
	}

	@Override
	public boolean isLayoutRequested() {
		if (this.wrapped == null) {
			return super.isLayoutRequested();
		}
		return this.wrapped.isLayoutRequested();
	}

	@Override
	public boolean isLongClickable() {
		if (this.wrapped == null) {
			return super.isLongClickable();
		}
		return this.wrapped.isLongClickable();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public boolean isOpaque() {
		if (this.wrapped == null) {
			return super.isOpaque();
		}
		return this.wrapped.isOpaque();
	}

	@Override
	public boolean isPaddingRelative() {
		if (this.wrapped == null) {
			return super.isPaddingRelative();
		}
		return this.wrapped.isPaddingRelative();
	}

	@Override
	public boolean isPressed() {
		if (this.wrapped == null) {
			return super.isPressed();
		}
		return this.wrapped.isPressed();
	}

	@Override
	public boolean isSaveEnabled() {
		if (this.wrapped == null) {
			return super.isSaveEnabled();
		}
		return this.wrapped.isSaveEnabled();
	}

	@Override
	public boolean isSaveFromParentEnabled() {
		if (this.wrapped == null) {
			return super.isSaveFromParentEnabled();
		}
		return this.wrapped.isSaveFromParentEnabled();
	}

	@Override
	public boolean isScrollContainer() {
		if (this.wrapped == null) {
			return super.isScrollContainer();
		}
		return this.wrapped.isScrollContainer();
	}

	@Override
	public boolean isScrollbarFadingEnabled() {
		if (this.wrapped == null) {
			return super.isScrollbarFadingEnabled();
		}
		return this.wrapped.isScrollbarFadingEnabled();
	}

	@Override
	//@ExportedProperty
	public boolean isSelected() {
		if (this.wrapped == null) {
			return super.isSelected();
		}
		return this.wrapped.isSelected();
	}

	@Override
	public boolean isShown() {
		if (this.wrapped == null) {
			return super.isShown();
		}
		return this.wrapped.isShown();
	}

	@Override
	//@ExportedProperty
	public boolean isSoundEffectsEnabled() {
		if (this.wrapped == null) {
			return super.isSoundEffectsEnabled();
		}
		return this.wrapped.isSoundEffectsEnabled();
	}

	@Override
	public boolean isTextAlignmentResolved() {
		if (this.wrapped == null) {
			return super.isTextAlignmentResolved();
		}
		return this.wrapped.isTextAlignmentResolved();
	}

	@Override
	public boolean isTextDirectionResolved() {
		if (this.wrapped == null) {
			return super.isTextDirectionResolved();
		}
		return this.wrapped.isTextDirectionResolved();
	}

	@Override
	public boolean isVerticalFadingEdgeEnabled() {
		if (this.wrapped == null) {
			return super.isVerticalFadingEdgeEnabled();
		}
		return this.wrapped.isVerticalFadingEdgeEnabled();
	}

	@Override
	public boolean isVerticalScrollBarEnabled() {
		if (this.wrapped == null) {
			return super.isVerticalScrollBarEnabled();
		}
		return this.wrapped.isVerticalScrollBarEnabled();
	}

	@Override
	public void jumpDrawablesToCurrentState() {
		if (this.wrapped == null) {
			super.jumpDrawablesToCurrentState();
			return;
		}
		this.wrapped.jumpDrawablesToCurrentState();
	}

	//@Override
//	public void layouty(int l, int t, int r, int b) {
//		if (this.target == null) {
//			return;
//		}
//		this.target.layout(l, t, r, b);
//	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (this.wrapped == null) {
			super.onLayout(changed, left, top, right, bottom);
		}
		this.wrapped.layout(left, top, right, bottom);
	}

	@Override
	public void offsetLeftAndRight(int offset) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.offsetLeftAndRight(offset);
	}

	@Override
	public void offsetTopAndBottom(int offset) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.offsetTopAndBottom(offset);
	}

	@Override
	public WindowInsets onApplyWindowInsets(WindowInsets insets) {
		if (this.wrapped == null) {
			return super.onApplyWindowInsets(insets);
		}
		return this.wrapped.onApplyWindowInsets(insets);
	}

	@Override
	public void onCancelPendingInputEvents() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onCancelPendingInputEvents();
	}

	@Override
	public boolean onCheckIsTextEditor() {
		if (this.wrapped == null) {
			return super.onCheckIsTextEditor();
		}
		return this.wrapped.onCheckIsTextEditor();
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		if (this.wrapped == null) {
			return super.onCreateInputConnection(outAttrs);
		}
		return this.wrapped.onCreateInputConnection(outAttrs);
	}

	@Override
	public boolean onDragEvent(DragEvent event) {
		if (this.wrapped == null) {
			return super.onDragEvent(event);
		}
		return this.wrapped.onDragEvent(event);
	}

	@Override
	public boolean onFilterTouchEventForSecurity(MotionEvent event) {
		if (this.wrapped == null) {
			return super.onFilterTouchEventForSecurity(event);
		}
		return this.wrapped.onFilterTouchEventForSecurity(event);
	}

	@Override
	public void onFinishTemporaryDetach() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onFinishTemporaryDetach();
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.onGenericMotionEvent(event);
		}
		return this.wrapped.onGenericMotionEvent(event);
	}

	@Override
	public void onHoverChanged(boolean hovered) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onHoverChanged(hovered);
	}

	@Override
	public boolean onHoverEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.onHoverEvent(event);
		}
		return this.wrapped.onHoverEvent(event);
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onInitializeAccessibilityEvent(event);
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onInitializeAccessibilityNodeInfo(info);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (this.wrapped == null) {
			return super.onKeyDown(keyCode, event);
		}
		return this.wrapped.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (this.wrapped == null) {
			return super.onKeyLongPress(keyCode, event);
		}
		return this.wrapped.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		if (this.wrapped == null) {
			return super.onKeyMultiple(keyCode, repeatCount, event);
		}
		return this.wrapped.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (this.wrapped == null) {
			return super.onKeyPreIme(keyCode, event);
		}
		return this.wrapped.onKeyPreIme(keyCode, event);
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		if (this.wrapped == null) {
			return super.onKeyShortcut(keyCode, event);
		}
		return this.wrapped.onKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (this.wrapped == null) {
			return super.onKeyUp(keyCode, event);
		}
		return this.wrapped.onKeyUp(keyCode, event);
	}

	@Override
	public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
		super.onPopulateAccessibilityEvent(event);
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onPopulateAccessibilityEvent(event);
	}

	@Override
	public void onRtlPropertiesChanged(int layoutDirection) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onRtlPropertiesChanged(layoutDirection);
	}

	@Override
	public void onScreenStateChanged(int screenState) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onScreenStateChanged(screenState);
	}

	@Override
	public void onStartTemporaryDetach() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onStartTemporaryDetach();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.onTouchEvent(event);
		}
		return this.wrapped.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if (this.wrapped == null) {
			return super.onTrackballEvent(event);
		}
		return this.wrapped.onTrackballEvent(event);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	public void onWindowSystemUiVisibilityChanged(int visible) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.onWindowSystemUiVisibilityChanged(visible);
	}

	@Override
	public boolean performAccessibilityAction(int action, Bundle arguments) {
		if (this.wrapped == null) {
			return super.performAccessibilityAction(action, arguments);
		}
		return this.wrapped.performAccessibilityAction(action, arguments);
	}

	@Override
	public boolean performClick() {
		if (this.wrapped == null) {
			return super.performClick();
		}
		return this.wrapped.performClick();
	}

	@Override
	public boolean performHapticFeedback(int feedbackConstant, int flags) {
		if (this.wrapped == null) {
			return super.performHapticFeedback(feedbackConstant, flags);
		}
		return this.wrapped.performHapticFeedback(feedbackConstant, flags);
	}

	@Override
	public boolean performHapticFeedback(int feedbackConstant) {
		if (this.wrapped == null) {
			return super.performHapticFeedback(feedbackConstant);
		}
		return this.wrapped.performHapticFeedback(feedbackConstant);
	}

	@Override
	public boolean performLongClick() {
		if (this.wrapped == null) {
			return super.performLongClick();
		}
		return this.wrapped.performLongClick();
	}

	@Override
	public void playSoundEffect(int soundConstant) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.playSoundEffect(soundConstant);
	}

	@Override
	public boolean post(Runnable action) {
		if (this.wrapped == null) {
			return super.post(action);
		}
		return this.wrapped.post(action);
	}

	@Override
	public boolean postDelayed(Runnable action, long delayMillis) {
		if (this.wrapped == null) {
			return super.postDelayed(action, delayMillis);
		}
		return this.wrapped.postDelayed(action, delayMillis);
	}

	@Override
	public void postInvalidate() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postInvalidate();
	}

	@Override
	public void postInvalidate(int left, int top, int right, int bottom) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postInvalidate(left, top, right, bottom);
	}

	@Override
	public void postInvalidateDelayed(long delayMilliseconds, int left,
			int top, int right, int bottom) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
	}

	@Override
	public void postInvalidateDelayed(long delayMilliseconds) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postInvalidateDelayed(delayMilliseconds);
	}

	@Override
	public void postInvalidateOnAnimation() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postInvalidateOnAnimation();
	}

	@Override
	public void postInvalidateOnAnimation(int left, int top, int right,
			int bottom) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postInvalidateOnAnimation(left, top, right, bottom);
	}

	@Override
	public void postOnAnimation(Runnable action) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postOnAnimation(action);
	}

	@Override
	public void postOnAnimationDelayed(Runnable action, long delayMillis) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.postOnAnimationDelayed(action, delayMillis);
	}

	@Override
	public void refreshDrawableState() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.refreshDrawableState();
	}

	@Override
	public boolean removeCallbacks(Runnable action) {
		if (this.wrapped == null) {
			return super.removeCallbacks(action);
		}
		return this.wrapped.removeCallbacks(action);
	}

	@Override
	public void removeOnAttachStateChangeListener(
			OnAttachStateChangeListener listener) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.removeOnAttachStateChangeListener(listener);
	}

	@Override
	public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.removeOnLayoutChangeListener(listener);
	}

	@Override
	public void requestApplyInsets() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.requestApplyInsets();
	}

	@Override
	@Deprecated
	public void requestFitSystemWindows() {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.requestFitSystemWindows();
	}

	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		if (this.wrapped == null) {
			return super.requestFocus(direction, previouslyFocusedRect);
		}
		return this.wrapped.requestFocus(direction, previouslyFocusedRect);
	}

	@Override
	public void requestLayout() {
		super.requestLayout();
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.requestLayout();
	}

	@Override
	public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
		if (this.wrapped == null) {
			return super.requestRectangleOnScreen(rectangle, immediate);
		}
		return this.wrapped.requestRectangleOnScreen(rectangle, immediate);
	}

	@Override
	public boolean requestRectangleOnScreen(Rect rectangle) {
		if (this.wrapped == null) {
			return super.requestRectangleOnScreen(rectangle);
		}
		return this.wrapped.requestRectangleOnScreen(rectangle);
	}

	@Override
	public void restoreHierarchyState(SparseArray<Parcelable> container) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.restoreHierarchyState(container);
	}

	@Override
	public void saveHierarchyState(SparseArray<Parcelable> container) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.saveHierarchyState(container);
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.scheduleDrawable(who, what, when);
	}

	@Override
	public void scrollBy(int x, int y) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.scrollBy(x, y);
	}

	@Override
	public void scrollTo(int x, int y) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.scrollTo(x, y);
	}

	@Override
	public void sendAccessibilityEvent(int eventType) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.sendAccessibilityEvent(eventType);
	}

	@Override
	public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.sendAccessibilityEventUnchecked(event);
	}

	@Override
	public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setAccessibilityDelegate(delegate);
	}

	@Override
	public void setAccessibilityLiveRegion(int mode) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setAccessibilityLiveRegion(mode);
	}

	@Override
	public void setActivated(boolean activated) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setActivated(activated);
	}

	@Override
	public void setAlpha(float alpha) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setAlpha(alpha);
	}

	@Override
	public void setAnimation(Animation animation) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setAnimation(animation);
	}

	@Override
	public void setBackground(Drawable background) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setBackground(background);
	}

	@Override
	public void setBackgroundColor(int color) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setBackgroundColor(color);
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setBackgroundDrawable(background);
	}

	@Override
	public void setBackgroundResource(int resId) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setBackgroundResource(resId);
	}

	@Override
	public void setCameraDistance(float distance) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setCameraDistance(distance);
	}

	@Override
	public void setClickable(boolean clickable) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setClickable(clickable);
	}

	@Override
	public void setClipBounds(Rect clipBounds) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setClipBounds(clipBounds);
	}

	@Override
	public void setContentDescription(CharSequence contentDescription) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setContentDescription(contentDescription);
	}

	@Override
	public void setDrawingCacheBackgroundColor(int color) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setDrawingCacheBackgroundColor(color);
	}

	@Override
	public void setDrawingCacheEnabled(boolean enabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setDrawingCacheEnabled(enabled);
	}

	@Override
	public void setDrawingCacheQuality(int quality) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setDrawingCacheQuality(quality);
	}

	@Override
	public void setDuplicateParentStateEnabled(boolean enabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setDuplicateParentStateEnabled(enabled);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setEnabled(enabled);
	}

	@Override
	public void setFadingEdgeLength(int length) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setFadingEdgeLength(length);
	}

	@Override
	public void setFilterTouchesWhenObscured(boolean enabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setFilterTouchesWhenObscured(enabled);
	}

	@Override
	public void setFitsSystemWindows(boolean fitSystemWindows) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setFitsSystemWindows(fitSystemWindows);
	}

	@Override
	public void setFocusable(boolean focusable) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setFocusable(focusable);
	}

	@Override
	public void setFocusableInTouchMode(boolean focusableInTouchMode) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setFocusableInTouchMode(focusableInTouchMode);
	}

	@Override
	public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setHapticFeedbackEnabled(hapticFeedbackEnabled);
	}

	@Override
	public void setHasTransientState(boolean hasTransientState) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setHasTransientState(hasTransientState);
	}

	@Override
	public void setHorizontalFadingEdgeEnabled(
			boolean horizontalFadingEdgeEnabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
	}

	@Override
	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}

	@Override
	public void setHovered(boolean hovered) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setHovered(hovered);
	}

	@Override
	public void setId(int id) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setId(id);
	}

	@Override
	public void setImportantForAccessibility(int mode) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setImportantForAccessibility(mode);
	}

	@Override
	public void setKeepScreenOn(boolean keepScreenOn) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setKeepScreenOn(keepScreenOn);
	}

	@Override
	public void setLabelFor(int id) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setLabelFor(id);
	}

	@Override
	public void setLayerPaint(Paint paint) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setLayerPaint(paint);
	}

	@Override
	public void setLayerType(int layerType, Paint paint) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setLayerType(layerType, paint);
	}

	@Override
	public void setLayoutDirection(int layoutDirection) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setLayoutDirection(layoutDirection);
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setLayoutParams(params);
	}

	@Override
	public void setLongClickable(boolean longClickable) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setLongClickable(longClickable);
	}

	@Override
	public void setMinimumHeight(int minHeight) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setMinimumHeight(minHeight);
	}

	@Override
	public void setMinimumWidth(int minWidth) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setMinimumWidth(minWidth);
	}

	@Override
	public void setNextFocusDownId(int nextFocusDownId) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setNextFocusDownId(nextFocusDownId);
	}

	@Override
	public void setNextFocusForwardId(int nextFocusForwardId) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setNextFocusForwardId(nextFocusForwardId);
	}

	@Override
	public void setNextFocusLeftId(int nextFocusLeftId) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setNextFocusLeftId(nextFocusLeftId);
	}

	@Override
	public void setNextFocusRightId(int nextFocusRightId) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setNextFocusRightId(nextFocusRightId);
	}

	@Override
	public void setNextFocusUpId(int nextFocusUpId) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setNextFocusUpId(nextFocusUpId);
	}

	@Override
	public void setOnApplyWindowInsetsListener(
			OnApplyWindowInsetsListener listener) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnApplyWindowInsetsListener(listener);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnClickListener(l);
	}

	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnCreateContextMenuListener(l);
	}

	@Override
	public void setOnDragListener(OnDragListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnDragListener(l);
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnFocusChangeListener(l);
	}

	@Override
	public void setOnGenericMotionListener(OnGenericMotionListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnGenericMotionListener(l);
	}

	@Override
	public void setOnHoverListener(OnHoverListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnHoverListener(l);
	}

	@Override
	public void setOnKeyListener(OnKeyListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnKeyListener(l);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnLongClickListener(l);
	}

	@Override
	public void setOnSystemUiVisibilityChangeListener(
			OnSystemUiVisibilityChangeListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnSystemUiVisibilityChangeListener(l);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOnTouchListener(l);
	}

	@Override
	public void setOverScrollMode(int overScrollMode) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setOverScrollMode(overScrollMode);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setPadding(left, top, right, bottom);
	}

	@Override
	public void setPaddingRelative(int start, int top, int end, int bottom) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setPaddingRelative(start, top, end, bottom);
	}

	@Override
	public void setPivotX(float pivotX) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setPivotX(pivotX);
	}

	@Override
	public void setPivotY(float pivotY) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setPivotY(pivotY);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setPressed(pressed);
	}

	@Override
	public void setRotation(float rotation) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setRotation(rotation);
	}

	@Override
	public void setRotationX(float rotationX) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setRotationX(rotationX);
	}

	@Override
	public void setRotationY(float rotationY) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setRotationY(rotationY);
	}

	@Override
	public void setSaveEnabled(boolean enabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setSaveEnabled(enabled);
	}

	@Override
	public void setSaveFromParentEnabled(boolean enabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setSaveFromParentEnabled(enabled);
	}

	@Override
	public void setScaleX(float scaleX) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScaleX(scaleX);
	}

	@Override
	public void setScaleY(float scaleY) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScaleY(scaleY);
	}

	@Override
	public void setScrollBarDefaultDelayBeforeFade(
			int scrollBarDefaultDelayBeforeFade) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
	}

	@Override
	public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollBarFadeDuration(scrollBarFadeDuration);
	}

	@Override
	public void setScrollBarSize(int scrollBarSize) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollBarSize(scrollBarSize);
	}

	@Override
	public void setScrollBarStyle(int style) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollBarStyle(style);
	}

	@Override
	public void setScrollContainer(boolean isScrollContainer) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollContainer(isScrollContainer);
	}

	@Override
	public void setScrollX(int value) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollX(value);
	}

	@Override
	public void setScrollY(int value) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollY(value);
	}

	@Override
	public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setScrollbarFadingEnabled(fadeScrollbars);
	}

	@Override
	public void setSelected(boolean selected) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setSelected(selected);
	}

	@Override
	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setSoundEffectsEnabled(soundEffectsEnabled);
	}

	@Override
	public void setSystemUiVisibility(int visibility) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setSystemUiVisibility(visibility);
	}

	@Override
	public void setTag(int key, Object tag) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTag(key, tag);
	}

	@Override
	public void setTag(Object tag) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTag(tag);
	}

	@Override
	public void setTextAlignment(int textAlignment) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTextAlignment(textAlignment);
	}

	@Override
	public void setTextDirection(int textDirection) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTextDirection(textDirection);
	}

	@Override
	public void setTouchDelegate(TouchDelegate delegate) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTouchDelegate(delegate);
	}

	@Override
	public void setTranslationX(float translationX) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTranslationX(translationX);
	}

	@Override
	public void setTranslationY(float translationY) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setTranslationY(translationY);
	}

	@Override
	public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
	}

	@Override
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}

	@Override
	public void setVerticalScrollbarPosition(int position) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setVerticalScrollbarPosition(position);
	}

	@Override
	public void setVisibility(int visibility) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setVisibility(visibility);
	}

	@Override
	public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setWillNotCacheDrawing(willNotCacheDrawing);
	}

	@Override
	public void setWillNotDraw(boolean willNotDraw) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setWillNotDraw(willNotDraw);
	}

	@Override
	public void setX(float x) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setX(x);
	}

	@Override
	public void setY(float y) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.setY(y);
	}

	@Override
	public boolean showContextMenu() {
		if (this.wrapped == null) {
			return super.showContextMenu();
		}
		return this.wrapped.showContextMenu();
	}

	@Override
	public ActionMode startActionMode(Callback callback) {
		if (this.wrapped == null) {
			return super.startActionMode(callback);
		}
		return this.wrapped.startActionMode(callback);
	}

	@Override
	public void startAnimation(Animation animation) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.startAnimation(animation);
	}

	@Override
	public String toString() {
		if (this.wrapped == null) {
			return super.toString();
		}
		return this.wrapped.toString();
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.unscheduleDrawable(who, what);
	}

	@Override
	public void unscheduleDrawable(Drawable who) {
		if (this.wrapped == null) {
			return;
		}
		this.wrapped.unscheduleDrawable(who);
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public boolean willNotCacheDrawing() {
		if (this.wrapped == null) {
			return super.willNotCacheDrawing();
		}
		return this.wrapped.willNotCacheDrawing();
	}

	@Override
	//@ExportedProperty(category = "drawing")
	public boolean willNotDraw() {
		if (this.wrapped == null) {
			return super.willNotDraw();
		}
		return this.wrapped.willNotDraw();
	}

}
