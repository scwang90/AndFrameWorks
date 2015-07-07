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
import android.os.Build;
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
import android.view.ViewGroup.LayoutParams;
import android.view.ViewOverlay;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.WindowId;
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
		if (this.target == null) {
			this.target = this;
		}else {
			this.target = view;
		}
	}

	public void addChildrenForAccessibility(ArrayList<View> children) {
		target.addChildrenForAccessibility(children);
	}

	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		target.addFocusables(views, direction, focusableMode);
	}

	public void addFocusables(ArrayList<View> views, int direction) {
		target.addFocusables(views, direction);
	}

	public void addOnAttachStateChangeListener(
			OnAttachStateChangeListener listener) {
		target.addOnAttachStateChangeListener(listener);
	}

	public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
		target.addOnLayoutChangeListener(listener);
	}

	public void addTouchables(ArrayList<View> views) {
		target.addTouchables(views);
	}

	public ViewPropertyAnimator animate() {
		return target.animate();
	}

	public void announceForAccessibility(CharSequence text) {
		target.announceForAccessibility(text);
	}

	public void bringToFront() {
		target.bringToFront();
	}

	public void buildDrawingCache() {
		target.buildDrawingCache();
	}

	public void buildDrawingCache(boolean autoScale) {
		target.buildDrawingCache(autoScale);
	}

	public void buildLayer() {
		target.buildLayer();
	}

	public boolean callOnClick() {
		return target.callOnClick();
	}

	public boolean canScrollHorizontally(int direction) {
		return target.canScrollHorizontally(direction);
	}

	public boolean canScrollVertically(int direction) {
		return target.canScrollVertically(direction);
	}

	public void cancelLongPress() {
		target.cancelLongPress();
	}

	public boolean checkInputConnectionProxy(View view) {
		return target.checkInputConnectionProxy(view);
	}

	public void clearAnimation() {
		target.clearAnimation();
	}

	public void clearFocus() {
		target.clearFocus();
	}

	public void computeScroll() {
		target.computeScroll();
	}

	public AccessibilityNodeInfo createAccessibilityNodeInfo() {
		return target.createAccessibilityNodeInfo();
	}

	public void createContextMenu(ContextMenu menu) {
		target.createContextMenu(menu);
	}

	public void destroyDrawingCache() {
		target.destroyDrawingCache();
	}

	public void dispatchConfigurationChanged(Configuration newConfig) {
		target.dispatchConfigurationChanged(newConfig);
	}

	public void dispatchDisplayHint(int hint) {
		target.dispatchDisplayHint(hint);
	}

	public boolean dispatchDragEvent(DragEvent event) {
		return target.dispatchDragEvent(event);
	}

	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		return target.dispatchGenericMotionEvent(event);
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		return target.dispatchKeyEvent(event);
	}

	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		return target.dispatchKeyEventPreIme(event);
	}

	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		return target.dispatchKeyShortcutEvent(event);
	}

	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		return target.dispatchPopulateAccessibilityEvent(event);
	}

	public void dispatchSystemUiVisibilityChanged(int visibility) {
		target.dispatchSystemUiVisibilityChanged(visibility);
	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		return target.dispatchTouchEvent(event);
	}

	public boolean dispatchTrackballEvent(MotionEvent event) {
		return target.dispatchTrackballEvent(event);
	}

	public boolean dispatchUnhandledMove(View focused, int direction) {
		return target.dispatchUnhandledMove(focused, direction);
	}

	public void dispatchWindowFocusChanged(boolean hasFocus) {
		target.dispatchWindowFocusChanged(hasFocus);
	}

	public void dispatchWindowSystemUiVisiblityChanged(int visible) {
		target.dispatchWindowSystemUiVisiblityChanged(visible);
	}

	public void dispatchWindowVisibilityChanged(int visibility) {
		target.dispatchWindowVisibilityChanged(visibility);
	}

	public void draw(Canvas canvas) {
		target.draw(canvas);
	}

	public boolean equals(Object o) {
		return target.equals(o);
	}

	public View findFocus() {
		return target.findFocus();
	}

	public void findViewsWithText(ArrayList<View> outViews,
			CharSequence searched, int flags) {
		target.findViewsWithText(outViews, searched, flags);
	}

	public View focusSearch(int direction) {
		return target.focusSearch(direction);
	}

	public void forceLayout() {
		target.forceLayout();
	}

	public AccessibilityNodeProvider getAccessibilityNodeProvider() {
		return target.getAccessibilityNodeProvider();
	}

	public float getAlpha() {
		return target.getAlpha();
	}

	public Animation getAnimation() {
		return target.getAnimation();
	}

	public IBinder getApplicationWindowToken() {
		return target.getApplicationWindowToken();
	}

	public Drawable getBackground() {
		return target.getBackground();
	}

	public int getBaseline() {
		return target.getBaseline();
	}

	public float getCameraDistance() {
		return target.getCameraDistance();
	}

	public Rect getClipBounds() {
		return target.getClipBounds();
	}

	public CharSequence getContentDescription() {
		return target.getContentDescription();
	}

	public Display getDisplay() {
		return target.getDisplay();
	}

	public Bitmap getDrawingCache() {
		return target.getDrawingCache();
	}

	public Bitmap getDrawingCache(boolean autoScale) {
		return target.getDrawingCache(autoScale);
	}

	public int getDrawingCacheBackgroundColor() {
		return target.getDrawingCacheBackgroundColor();
	}

	public int getDrawingCacheQuality() {
		return target.getDrawingCacheQuality();
	}

	public void getDrawingRect(Rect outRect) {
		target.getDrawingRect(outRect);
	}

	public long getDrawingTime() {
		return target.getDrawingTime();
	}

	public boolean getFilterTouchesWhenObscured() {
		return target.getFilterTouchesWhenObscured();
	}

	public boolean getFitsSystemWindows() {
		return target.getFitsSystemWindows();
	}

	public ArrayList<View> getFocusables(int direction) {
		return target.getFocusables(direction);
	}

	public void getFocusedRect(Rect r) {
		target.getFocusedRect(r);
	}

	public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
		return target.getGlobalVisibleRect(r, globalOffset);
	}

	public Handler getHandler() {
		return target.getHandler();
	}

	public void getHitRect(Rect outRect) {
		target.getHitRect(outRect);
	}

	public int getHorizontalFadingEdgeLength() {
		return target.getHorizontalFadingEdgeLength();
	}

	public int getId() {
		return target.getId();
	}

	public int getImportantForAccessibility() {
		return target.getImportantForAccessibility();
	}

	public boolean getKeepScreenOn() {
		return target.getKeepScreenOn();
	}

	public DispatcherState getKeyDispatcherState() {
		return target.getKeyDispatcherState();
	}

	public int getLabelFor() {
		return target.getLabelFor();
	}

	public int getLayerType() {
		return target.getLayerType();
	}

	public int getLayoutDirection() {
		return target.getLayoutDirection();
	}

	public LayoutParams getLayoutParams() {
		return target.getLayoutParams();
	}

	public void getLocationInWindow(int[] location) {
		target.getLocationInWindow(location);
	}

	public void getLocationOnScreen(int[] location) {
		target.getLocationOnScreen(location);
	}

	public Matrix getMatrix() {
		return target.getMatrix();
	}

	public int getMinimumHeight() {
		return target.getMinimumHeight();
	}

	public int getMinimumWidth() {
		return target.getMinimumWidth();
	}

	public int getNextFocusDownId() {
		return target.getNextFocusDownId();
	}

	public int getNextFocusForwardId() {
		return target.getNextFocusForwardId();
	}

	public int getNextFocusLeftId() {
		return target.getNextFocusLeftId();
	}

	public int getNextFocusRightId() {
		return target.getNextFocusRightId();
	}

	public int getNextFocusUpId() {
		return target.getNextFocusUpId();
	}

	public OnFocusChangeListener getOnFocusChangeListener() {
		return target.getOnFocusChangeListener();
	}

	public int getOverScrollMode() {
		return target.getOverScrollMode();
	}

	public ViewOverlay getOverlay() {
		return target.getOverlay();
	}

	public int getPaddingBottom() {
		return target.getPaddingBottom();
	}

	public int getPaddingEnd() {
		return target.getPaddingEnd();
	}

	public int getPaddingLeft() {
		return target.getPaddingLeft();
	}

	public int getPaddingRight() {
		return target.getPaddingRight();
	}

	public int getPaddingStart() {
		return target.getPaddingStart();
	}

	public int getPaddingTop() {
		return target.getPaddingTop();
	}

	public ViewParent getParentForAccessibility() {
		return target.getParentForAccessibility();
	}

	public float getPivotX() {
		return target.getPivotX();
	}

	public float getPivotY() {
		return target.getPivotY();
	}

	public Resources getResources() {
		return target.getResources();
	}

	public View getRootView() {
		return target.getRootView();
	}

	public float getRotation() {
		return target.getRotation();
	}

	public float getRotationX() {
		return target.getRotationX();
	}

	public float getRotationY() {
		return target.getRotationY();
	}

	public float getScaleX() {
		return target.getScaleX();
	}

	public float getScaleY() {
		return target.getScaleY();
	}

	public int getScrollBarDefaultDelayBeforeFade() {
		return target.getScrollBarDefaultDelayBeforeFade();
	}

	public int getScrollBarFadeDuration() {
		return target.getScrollBarFadeDuration();
	}

	public int getScrollBarSize() {
		return target.getScrollBarSize();
	}

	public int getScrollBarStyle() {
		return target.getScrollBarStyle();
	}

	public int getSolidColor() {
		return target.getSolidColor();
	}

	public int getSystemUiVisibility() {
		return target.getSystemUiVisibility();
	}

	public Object getTag() {
		return target.getTag();
	}

	public Object getTag(int key) {
		return target.getTag(key);
	}

	public int getTextAlignment() {
		return target.getTextAlignment();
	}

	public int getTextDirection() {
		return target.getTextDirection();
	}

	public TouchDelegate getTouchDelegate() {
		return target.getTouchDelegate();
	}

	public ArrayList<View> getTouchables() {
		return target.getTouchables();
	}

	public float getTranslationX() {
		return target.getTranslationX();
	}

	public float getTranslationY() {
		return target.getTranslationY();
	}

	public int getVerticalFadingEdgeLength() {
		return target.getVerticalFadingEdgeLength();
	}

	public int getVerticalScrollbarPosition() {
		return target.getVerticalScrollbarPosition();
	}

	public int getVerticalScrollbarWidth() {
		return target.getVerticalScrollbarWidth();
	}

	public ViewTreeObserver getViewTreeObserver() {
		return target.getViewTreeObserver();
	}

	public int getVisibility() {
		return target.getVisibility();
	}

	public WindowId getWindowId() {
		return target.getWindowId();
	}

	public int getWindowSystemUiVisibility() {
		return target.getWindowSystemUiVisibility();
	}

	public IBinder getWindowToken() {
		return target.getWindowToken();
	}

	public int getWindowVisibility() {
		return target.getWindowVisibility();
	}

	public void getWindowVisibleDisplayFrame(Rect outRect) {
		target.getWindowVisibleDisplayFrame(outRect);
	}

	public float getX() {
		return target.getX();
	}

	public float getY() {
		return target.getY();
	}

	public boolean hasFocus() {
		return target.hasFocus();
	}

	public boolean hasFocusable() {
		return target.hasFocusable();
	}

	public boolean hasOnClickListeners() {
		return target.hasOnClickListeners();
	}

	public boolean hasOverlappingRendering() {
		return target.hasOverlappingRendering();
	}

	public boolean hasTransientState() {
		return target.hasTransientState();
	}

	public boolean hasWindowFocus() {
		return target.hasWindowFocus();
	}

	public int hashCode() {
		return target.hashCode();
	}

	public void invalidate() {
		target.invalidate();
	}

	public void invalidate(int l, int t, int r, int b) {
		target.invalidate(l, t, r, b);
	}

	public void invalidate(Rect dirty) {
		target.invalidate(dirty);
	}

	public void invalidateDrawable(Drawable drawable) {
		target.invalidateDrawable(drawable);
	}

	public boolean isActivated() {
		return target.isActivated();
	}

	public boolean isClickable() {
		return target.isClickable();
	}

	public boolean isDirty() {
		return target.isDirty();
	}

	public boolean isDrawingCacheEnabled() {
		return target.isDrawingCacheEnabled();
	}

	public boolean isDuplicateParentStateEnabled() {
		return target.isDuplicateParentStateEnabled();
	}

	public boolean isEnabled() {
		return target.isEnabled();
	}

	public boolean isFocused() {
		return target.isFocused();
	}

	public boolean isHapticFeedbackEnabled() {
		return target.isHapticFeedbackEnabled();
	}

	public boolean isHardwareAccelerated() {
		return target.isHardwareAccelerated();
	}

	public boolean isHorizontalFadingEdgeEnabled() {
		return target.isHorizontalFadingEdgeEnabled();
	}

	public boolean isHorizontalScrollBarEnabled() {
		return target.isHorizontalScrollBarEnabled();
	}

	public boolean isHovered() {
		return target.isHovered();
	}

	public boolean isInEditMode() {
		return target.isInEditMode();
	}

	public boolean isInLayout() {
		return target.isInLayout();
	}

	public boolean isInTouchMode() {
		return target.isInTouchMode();
	}

	public boolean isLayoutRequested() {
		return target.isLayoutRequested();
	}

	public boolean isLongClickable() {
		return target.isLongClickable();
	}

	public boolean isOpaque() {
		return target.isOpaque();
	}

	public boolean isPaddingRelative() {
		return target.isPaddingRelative();
	}

	public boolean isPressed() {
		return target.isPressed();
	}

	public boolean isSaveEnabled() {
		return target.isSaveEnabled();
	}

	public boolean isSaveFromParentEnabled() {
		return target.isSaveFromParentEnabled();
	}

	public boolean isScrollContainer() {
		return target.isScrollContainer();
	}

	public boolean isScrollbarFadingEnabled() {
		return target.isScrollbarFadingEnabled();
	}

	public boolean isSelected() {
		return target.isSelected();
	}

	public boolean isShown() {
		return target.isShown();
	}

	public boolean isSoundEffectsEnabled() {
		return target.isSoundEffectsEnabled();
	}

	public boolean isVerticalFadingEdgeEnabled() {
		return target.isVerticalFadingEdgeEnabled();
	}

	public boolean isVerticalScrollBarEnabled() {
		return target.isVerticalScrollBarEnabled();
	}

	public void jumpDrawablesToCurrentState() {
		target.jumpDrawablesToCurrentState();
	}

	public void layouty(int l, int t, int r, int b) {
		target.layout(l, t, r, b);
	}

	public void offsetLeftAndRight(int offset) {
		target.offsetLeftAndRight(offset);
	}

	public void offsetTopAndBottom(int offset) {
		target.offsetTopAndBottom(offset);
	}

	public boolean onCheckIsTextEditor() {
		return target.onCheckIsTextEditor();
	}

	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		return target.onCreateInputConnection(outAttrs);
	}

	public boolean onDragEvent(DragEvent event) {
		return target.onDragEvent(event);
	}

	public boolean onFilterTouchEventForSecurity(MotionEvent event) {
		return target.onFilterTouchEventForSecurity(event);
	}

	public void onFinishTemporaryDetach() {
		target.onFinishTemporaryDetach();
	}

	public boolean onGenericMotionEvent(MotionEvent event) {
		return target.onGenericMotionEvent(event);
	}

	public void onHoverChanged(boolean hovered) {
		target.onHoverChanged(hovered);
	}

	public boolean onHoverEvent(MotionEvent event) {
		return target.onHoverEvent(event);
	}

	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		target.onInitializeAccessibilityEvent(event);
	}

	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		target.onInitializeAccessibilityNodeInfo(info);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return target.onKeyDown(keyCode, event);
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return target.onKeyLongPress(keyCode, event);
	}

	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return target.onKeyMultiple(keyCode, repeatCount, event);
	}

	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		return target.onKeyPreIme(keyCode, event);
	}

	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		return target.onKeyShortcut(keyCode, event);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return target.onKeyUp(keyCode, event);
	}

	public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
		target.onPopulateAccessibilityEvent(event);
	}

	public void onRtlPropertiesChanged(int layoutDirection) {
		target.onRtlPropertiesChanged(layoutDirection);
	}

	public void onScreenStateChanged(int screenState) {
		target.onScreenStateChanged(screenState);
	}

	public void onStartTemporaryDetach() {
		target.onStartTemporaryDetach();
	}

	public boolean onTouchEvent(MotionEvent event) {
		return target.onTouchEvent(event);
	}

	public boolean onTrackballEvent(MotionEvent event) {
		return target.onTrackballEvent(event);
	}

	public void onWindowFocusChanged(boolean hasWindowFocus) {
		target.onWindowFocusChanged(hasWindowFocus);
	}

	public void onWindowSystemUiVisibilityChanged(int visible) {
		target.onWindowSystemUiVisibilityChanged(visible);
	}

	public boolean performAccessibilityAction(int action, Bundle arguments) {
		return target.performAccessibilityAction(action, arguments);
	}

	public boolean performClick() {
		return target.performClick();
	}

	public boolean performHapticFeedback(int feedbackConstant, int flags) {
		return target.performHapticFeedback(feedbackConstant, flags);
	}

	public boolean performHapticFeedback(int feedbackConstant) {
		return target.performHapticFeedback(feedbackConstant);
	}

	public boolean performLongClick() {
		return target.performLongClick();
	}

	public void playSoundEffect(int soundConstant) {
		target.playSoundEffect(soundConstant);
	}

	public boolean post(Runnable action) {
		return target.post(action);
	}

	public boolean postDelayed(Runnable action, long delayMillis) {
		return target.postDelayed(action, delayMillis);
	}

	public void postInvalidate() {
		target.postInvalidate();
	}

	public void postInvalidate(int left, int top, int right, int bottom) {
		target.postInvalidate(left, top, right, bottom);
	}

	public void postInvalidateDelayed(long delayMilliseconds, int left,
			int top, int right, int bottom) {
		target.postInvalidateDelayed(delayMilliseconds, left, top, right,
				bottom);
	}

	public void postInvalidateDelayed(long delayMilliseconds) {
		target.postInvalidateDelayed(delayMilliseconds);
	}

	public void postInvalidateOnAnimation() {
		target.postInvalidateOnAnimation();
	}

	public void postInvalidateOnAnimation(int left, int top, int right,
			int bottom) {
		target.postInvalidateOnAnimation(left, top, right, bottom);
	}

	public void postOnAnimation(Runnable action) {
		target.postOnAnimation(action);
	}

	public void postOnAnimationDelayed(Runnable action, long delayMillis) {
		target.postOnAnimationDelayed(action, delayMillis);
	}

	public void refreshDrawableState() {
		target.refreshDrawableState();
	}

	public boolean removeCallbacks(Runnable action) {
		return target.removeCallbacks(action);
	}

	public void removeOnAttachStateChangeListener(
			OnAttachStateChangeListener listener) {
		target.removeOnAttachStateChangeListener(listener);
	}

	public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
		target.removeOnLayoutChangeListener(listener);
	}

	@Deprecated
	public void requestFitSystemWindows() {
		target.requestFitSystemWindows();
	}

	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		return target.requestFocus(direction, previouslyFocusedRect);
	}

	public void requestLayout() {
		target.requestLayout();
	}

	public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
		return target.requestRectangleOnScreen(rectangle, immediate);
	}

	public boolean requestRectangleOnScreen(Rect rectangle) {
		return target.requestRectangleOnScreen(rectangle);
	}

	public void restoreHierarchyState(SparseArray<Parcelable> container) {
		target.restoreHierarchyState(container);
	}

	public void saveHierarchyState(SparseArray<Parcelable> container) {
		target.saveHierarchyState(container);
	}

	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		target.scheduleDrawable(who, what, when);
	}

	public void scrollBy(int x, int y) {
		target.scrollBy(x, y);
	}

	public void scrollTo(int x, int y) {
		target.scrollTo(x, y);
	}

	public void sendAccessibilityEvent(int eventType) {
		target.sendAccessibilityEvent(eventType);
	}

	public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
		target.sendAccessibilityEventUnchecked(event);
	}

	public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
		target.setAccessibilityDelegate(delegate);
	}

	public void setActivated(boolean activated) {
		target.setActivated(activated);
	}

	public void setAlpha(float alpha) {
		target.setAlpha(alpha);
	}

	public void setAnimation(Animation animation) {
		target.setAnimation(animation);
	}

	@SuppressWarnings("deprecation")
	public void setBackground(Drawable background) {
		if (Build.VERSION.SDK_INT > 16) {
			target.setBackground(background);
		}else {
			target.setBackgroundDrawable(background);
		}
	}

	public void setBackgroundColor(int color) {
		target.setBackgroundColor(color);
	}

	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		target.setBackgroundDrawable(background);
	}

	public void setBackgroundResource(int resid) {
		target.setBackgroundResource(resid);
	}

	public void setCameraDistance(float distance) {
		target.setCameraDistance(distance);
	}

	public void setClickable(boolean clickable) {
		target.setClickable(clickable);
	}

	public void setClipBounds(Rect clipBounds) {
		target.setClipBounds(clipBounds);
	}

	public void setContentDescription(CharSequence contentDescription) {
		target.setContentDescription(contentDescription);
	}

	public void setDrawingCacheBackgroundColor(int color) {
		target.setDrawingCacheBackgroundColor(color);
	}

	public void setDrawingCacheEnabled(boolean enabled) {
		target.setDrawingCacheEnabled(enabled);
	}

	public void setDrawingCacheQuality(int quality) {
		target.setDrawingCacheQuality(quality);
	}

	public void setDuplicateParentStateEnabled(boolean enabled) {
		target.setDuplicateParentStateEnabled(enabled);
	}

	public void setEnabled(boolean enabled) {
		target.setEnabled(enabled);
	}

	public void setFadingEdgeLength(int length) {
		target.setFadingEdgeLength(length);
	}

	public void setFilterTouchesWhenObscured(boolean enabled) {
		target.setFilterTouchesWhenObscured(enabled);
	}

	public void setFitsSystemWindows(boolean fitSystemWindows) {
		target.setFitsSystemWindows(fitSystemWindows);
	}

	public void setFocusable(boolean focusable) {
		target.setFocusable(focusable);
	}

	public void setFocusableInTouchMode(boolean focusableInTouchMode) {
		target.setFocusableInTouchMode(focusableInTouchMode);
	}

	public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
		target.setHapticFeedbackEnabled(hapticFeedbackEnabled);
	}

	public void setHasTransientState(boolean hasTransientState) {
		target.setHasTransientState(hasTransientState);
	}

	public void setHorizontalFadingEdgeEnabled(
			boolean horizontalFadingEdgeEnabled) {
		target.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
	}

	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		target.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}

	public void setHovered(boolean hovered) {
		target.setHovered(hovered);
	}

	public void setId(int id) {
		target.setId(id);
	}

	public void setImportantForAccessibility(int mode) {
		target.setImportantForAccessibility(mode);
	}

	public void setKeepScreenOn(boolean keepScreenOn) {
		target.setKeepScreenOn(keepScreenOn);
	}

	public void setLabelFor(int id) {
		target.setLabelFor(id);
	}

	public void setLayerPaint(Paint paint) {
		target.setLayerPaint(paint);
	}

	public void setLayerType(int layerType, Paint paint) {
		target.setLayerType(layerType, paint);
	}

	public void setLayoutDirection(int layoutDirection) {
		target.setLayoutDirection(layoutDirection);
	}

	public void setLayoutParams(LayoutParams params) {
		target.setLayoutParams(params);
	}

	public void setLongClickable(boolean longClickable) {
		target.setLongClickable(longClickable);
	}

	public void setMinimumHeight(int minHeight) {
		target.setMinimumHeight(minHeight);
	}

	public void setMinimumWidth(int minWidth) {
		target.setMinimumWidth(minWidth);
	}

	public void setNextFocusDownId(int nextFocusDownId) {
		target.setNextFocusDownId(nextFocusDownId);
	}

	public void setNextFocusForwardId(int nextFocusForwardId) {
		target.setNextFocusForwardId(nextFocusForwardId);
	}

	public void setNextFocusLeftId(int nextFocusLeftId) {
		target.setNextFocusLeftId(nextFocusLeftId);
	}

	public void setNextFocusRightId(int nextFocusRightId) {
		target.setNextFocusRightId(nextFocusRightId);
	}

	public void setNextFocusUpId(int nextFocusUpId) {
		target.setNextFocusUpId(nextFocusUpId);
	}

	public void setOnClickListener(OnClickListener l) {
		target.setOnClickListener(l);
	}

	public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
		target.setOnCreateContextMenuListener(l);
	}

	public void setOnDragListener(OnDragListener l) {
		target.setOnDragListener(l);
	}

	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		target.setOnFocusChangeListener(l);
	}

	public void setOnGenericMotionListener(OnGenericMotionListener l) {
		target.setOnGenericMotionListener(l);
	}

	public void setOnHoverListener(OnHoverListener l) {
		target.setOnHoverListener(l);
	}

	public void setOnKeyListener(OnKeyListener l) {
		target.setOnKeyListener(l);
	}

	public void setOnLongClickListener(OnLongClickListener l) {
		target.setOnLongClickListener(l);
	}

	public void setOnSystemUiVisibilityChangeListener(
			OnSystemUiVisibilityChangeListener l) {
		target.setOnSystemUiVisibilityChangeListener(l);
	}

	public void setOnTouchListener(OnTouchListener l) {
		target.setOnTouchListener(l);
	}

	public void setOverScrollMode(int overScrollMode) {
		target.setOverScrollMode(overScrollMode);
	}

	public void setPadding(int left, int top, int right, int bottom) {
		target.setPadding(left, top, right, bottom);
	}

	public void setPaddingRelative(int start, int top, int end, int bottom) {
		target.setPaddingRelative(start, top, end, bottom);
	}

	public void setPivotX(float pivotX) {
		target.setPivotX(pivotX);
	}

	public void setPivotY(float pivotY) {
		target.setPivotY(pivotY);
	}

	public void setPressed(boolean pressed) {
		target.setPressed(pressed);
	}

	public void setRotation(float rotation) {
		target.setRotation(rotation);
	}

	public void setRotationX(float rotationX) {
		target.setRotationX(rotationX);
	}

	public void setRotationY(float rotationY) {
		target.setRotationY(rotationY);
	}

	public void setSaveEnabled(boolean enabled) {
		target.setSaveEnabled(enabled);
	}

	public void setSaveFromParentEnabled(boolean enabled) {
		target.setSaveFromParentEnabled(enabled);
	}

	public void setScaleX(float scaleX) {
		target.setScaleX(scaleX);
	}

	public void setScaleY(float scaleY) {
		target.setScaleY(scaleY);
	}

	public void setScrollBarDefaultDelayBeforeFade(
			int scrollBarDefaultDelayBeforeFade) {
		target.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
	}

	public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
		target.setScrollBarFadeDuration(scrollBarFadeDuration);
	}

	public void setScrollBarSize(int scrollBarSize) {
		target.setScrollBarSize(scrollBarSize);
	}

	public void setScrollBarStyle(int style) {
		target.setScrollBarStyle(style);
	}

	public void setScrollContainer(boolean isScrollContainer) {
		target.setScrollContainer(isScrollContainer);
	}

	public void setScrollX(int value) {
		target.setScrollX(value);
	}

	public void setScrollY(int value) {
		target.setScrollY(value);
	}

	public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
		target.setScrollbarFadingEnabled(fadeScrollbars);
	}

	public void setSelected(boolean selected) {
		target.setSelected(selected);
	}

	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		target.setSoundEffectsEnabled(soundEffectsEnabled);
	}

	public void setSystemUiVisibility(int visibility) {
		target.setSystemUiVisibility(visibility);
	}

	public void setTag(int key, Object tag) {
		target.setTag(key, tag);
	}

	public void setTag(Object tag) {
		target.setTag(tag);
	}

	public void setTextAlignment(int textAlignment) {
		target.setTextAlignment(textAlignment);
	}

	public void setTextDirection(int textDirection) {
		target.setTextDirection(textDirection);
	}

	public void setTouchDelegate(TouchDelegate delegate) {
		target.setTouchDelegate(delegate);
	}

	public void setTranslationX(float translationX) {
		target.setTranslationX(translationX);
	}

	public void setTranslationY(float translationY) {
		target.setTranslationY(translationY);
	}

	public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
		target.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
	}

	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		target.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}

	public void setVerticalScrollbarPosition(int position) {
		target.setVerticalScrollbarPosition(position);
	}

	public void setVisibility(int visibility) {
		target.setVisibility(visibility);
	}

	public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
		target.setWillNotCacheDrawing(willNotCacheDrawing);
	}

	public void setWillNotDraw(boolean willNotDraw) {
		target.setWillNotDraw(willNotDraw);
	}

	public void setX(float x) {
		target.setX(x);
	}

	public void setY(float y) {
		target.setY(y);
	}

	public boolean showContextMenu() {
		return target.showContextMenu();
	}

	public ActionMode startActionMode(Callback callback) {
		return target.startActionMode(callback);
	}

	public void startAnimation(Animation animation) {
		target.startAnimation(animation);
	}

	public String toString() {
		return target.toString();
	}

	public void unscheduleDrawable(Drawable who, Runnable what) {
		target.unscheduleDrawable(who, what);
	}

	public void unscheduleDrawable(Drawable who) {
		target.unscheduleDrawable(who);
	}

	public boolean willNotCacheDrawing() {
		return target.willNotCacheDrawing();
	}

	public boolean willNotDraw() {
		return target.willNotDraw();
	}


}
